public class Blit extends Osc {

  /**
   * @param x The xTh sample
   * @param m The number of partials (harmonics) to include. Calculated from the Period.
   * @return The sinc value
   */
  public double sincm(double x, double m) {
    double maxHarmonics = m*Math.sin((Math.PI*x)/m);
    // literally always forget the order for this operator, I'm hoping
    // if I keep forcing myself to use it I will remember true comes first
    return maxHarmonics == 0 ? 1.0 : Math.sin(Math.PI*x)/maxHarmonics;
  }

  protected double getP() {
    double freq = Utils.valueToHz(getFrequencyMod().getValue());
    if(freq < 0.001) freq = 0.001;

    return Config.SAMPLING_RATE / freq;
  }

  public double tick(long tickCount) {
    return blit(tickCount, 0);
  }

  /// d goes from 0...1.  The purpose of d is to allow an offset in phase.
  /// What might be able to take advantage of this function?
  //  ************************ saw? ************************

  protected double blit(long tickCount, double d) {
    double phase = super.tick(tickCount);
    double p = getP();
    double m = Math.floor(p / 2.0) * 2.0 + 1.0;
    // phase*p rather than tickCount oops
    return  (m /p) * sincm((phase * p - d * p) * m/p, m);
  }
}
