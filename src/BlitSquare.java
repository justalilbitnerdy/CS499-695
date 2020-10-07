public class BlitSquare extends BPBlit {
  // this might be useful
  double prev = 0;

  protected double blitsquare(long tickCount) {
    /// IMPLEMENT ME
    return 0.0;
  }


  public double tick(long tickCount) {
    if (tickCount < -0) {
      return 0;
    }else{
      if (Utils.valueToHz(getFrequencyMod().getValue()) == 0) {
        return getValue();
      }

      // I'm pretty sure that phase mod is the pulse width, if not, change how
      //   Mixer does it
      double phase = getPhaseMod().getValue();
      return blitsquare(tickCount) * 0.75 + phase;
      }
    }
}
