import java.util.Random;

public class WhiteNoise extends Osc{

  private static Random seed = new Random();

  //little worried how easy this is. But the book says that's all there is.
  public double tick(long tickCount) {
    return (seed.nextDouble()-.5)*2;
  }
}
