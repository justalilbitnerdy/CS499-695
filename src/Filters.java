public class Filters{
  //// FastSin and FastCos are from
  //// https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/MathHelper.java

  // The problem is that to get decent looking sawtooth and square and triangle waves etc., you need to have all
  // your partials running at the same phase -- but making changes in frequency will change this.  I had thought
  // the problem we were having was that SIN_TABLE_LENGTH was too short, but that's not it.
  private static final int SIN_TABLE_LENGTH = 65536; //  * 16;
  private static final double SIN_MULTIPLIER = SIN_TABLE_LENGTH / Math.PI / 2;
  private final static int SIN_TABLE_LENGTH_MINUS_1 = SIN_TABLE_LENGTH - 1;
  private final static int SIN_TABLE_LENGTH_DIV_4 = SIN_TABLE_LENGTH / 4;
  private final static double[] sinTable = new double[SIN_TABLE_LENGTH];

  /** A fast approximation of Sine using a lookup table.  40x the speed of Math.sin. */
  public static final double fastSin(double f)
  {
    return sinTable[((int) (f * SIN_MULTIPLIER)) & (SIN_TABLE_LENGTH - 1)];
  }

  /** A fast approximation of Cosine using a lookup table. */
  public static final double fastCos(double f)
  {
    return sinTable[((int) (f * SIN_MULTIPLIER + SIN_TABLE_LENGTH_DIV_4)) & (SIN_TABLE_LENGTH - 1)];
  }


  /**
   * A fast approximation of Sine using a lookup table and Catmull-Rom cubic spline interpolation.
   16x the speed of Math.sin.
   */
  public static final double fastIntSin(double f)
  {
    double v = f * SIN_MULTIPLIER;
    int conv = (int) v;
    double alpha = v - conv;

    int slot1 = conv & SIN_TABLE_LENGTH_MINUS_1;
    int slot0 = (slot1 - 1) & SIN_TABLE_LENGTH_MINUS_1;
    int slot2 = (slot1 + 1) & SIN_TABLE_LENGTH_MINUS_1;
    int slot3 = (slot2 + 1) & SIN_TABLE_LENGTH_MINUS_1;

    double f0 = sinTable[slot0];
    double f1 = sinTable[slot1];
    double f2 = sinTable[slot2];
    double f3 = sinTable[slot3];

    return alpha * alpha * alpha * (-0.5 * f0 + 1.5 * f1 - 1.5 * f2 + 0.5 * f3) +
            alpha * alpha * (f0 - 2.5 * f1 + 2 * f2 - 0.5 * f3) +
            alpha * (-0.5 * f0 + 0.5 * f2) +
            f1;
  }


  /** A fast approximation of Cosine using a lookup table and Catmull-Rom cubic spline interpolation. */
  public static final double fastIntCos(double f)
  {
    double v = f * SIN_MULTIPLIER + SIN_TABLE_LENGTH_DIV_4;
    int conv = (int) v;
    double alpha = v - conv;

    int slot1 = conv & SIN_TABLE_LENGTH_MINUS_1;
    int slot0 = (slot1 - 1) & SIN_TABLE_LENGTH_MINUS_1;
    int slot2 = (slot1 + 1) & SIN_TABLE_LENGTH_MINUS_1;
    int slot3 = (slot2 + 1) & SIN_TABLE_LENGTH_MINUS_1;

    double f0 = sinTable[slot0];
    double f1 = sinTable[slot1];
    double f2 = sinTable[slot2];
    double f3 = sinTable[slot3];

    return alpha * alpha * alpha * (-0.5 * f0 + 1.5 * f1 - 1.5 * f2 + 0.5 * f3) +
            alpha * alpha * (f0 - 2.5 * f1 + 2 * f2 - 0.5 * f3) +
            alpha * (-0.5 * f0 + 0.5 * f2) +
            f1;
  }

  private static double hammingWindow(int index, int max) {
//    return 0.53836 - (1 - 0.53836) * Math.cos(2 * Math.PI * index / max);
    return 0.53836 - (1 - 0.53836) * fastCos(2 * Math.PI * index / max);
  }
  private static double hannWindow(int index, int max) {
//    return Math.sqrt(0.5 - 0.5 * Math.cos(2 * Math.PI * index / max));
    return Math.sqrt(0.5 - 0.5 * fastCos(2 * Math.PI * index / max));
  }
  private static void RecursiveScale(double[] audioPacket) {
    if (audioPacket.length > 1) {
      int half_length = audioPacket.length/2;
      double[] evens = new double[half_length];
      double[] odds = new double[half_length];
      for (int i=0; i < half_length; i++) {
        evens[i] = audioPacket[2*i];
        odds[i] = audioPacket[2*i+1];
      }
      RecursiveScale(evens);
      RecursiveScale(odds);
      for (int i=0; i < half_length; i++) {
//        double coefficient = Math.cos(-2 * Math.PI * i / audioPacket.length);
        double coefficient = fastCos(-2 * Math.PI * i / audioPacket.length);
        audioPacket[i] = evens[i] + coefficient * odds[i];
        audioPacket[i+half_length] = evens[i] - coefficient * odds[i];
      }
    }
  }
  public static void PitchScale(float pitch, double[] audioPacket) {
    // Zero-pad both sides
    double[] padded = new double[audioPacket.length*2];
    double[] output = new double[audioPacket.length*2];
    int padding_length = audioPacket.length/2;

//    for (int i=0; i < audioPacket.length; i++) {
//      padded[i+padding_length] = audioPacket[i];
//    }
    System.arraycopy(audioPacket, 0, padded, padding_length, audioPacket.length);

    // Create chunks
    int chunk_offset = audioPacket.length/4;
    int num_chunks = 5;
    double[][] chunks = new double[num_chunks][audioPacket.length];
    for (int chunk=0; chunk < num_chunks; chunk++) {
      for (int i=0; i < audioPacket.length; i++) {
        chunks[chunk][i] = padded[i + chunk*chunk_offset] * hannWindow(i, audioPacket.length);
      }
      RecursiveScale(chunks[chunk]);
      // Scale the pitch
      for (int i=0; i < audioPacket.length; i++) {
//        chunks[chunk][i] *= pitch;
      }
      RecursiveScale(chunks[chunk]);
      for (int i=0; i < audioPacket.length; i++) {
        output[i + chunk*chunk_offset] += chunks[chunk][i] * hannWindow(i, audioPacket.length);
      }
    }
    System.arraycopy(output, padding_length, audioPacket, 0, audioPacket.length);
  }
}