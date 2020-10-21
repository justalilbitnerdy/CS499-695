public class BlitSquare extends BPBlit {
  // this might be useful
  double prev = 0;

  protected double blitsquare(long tickCount) {
    prev = 0.999 * prev + bpblit(tickCount);
    return prev;
  }

  public double tick(long tickCount) {
    if (tickCount < -0)
      return 0;
    else {
      if (Utils.valueToHz(getFrequencyMod().getValue()) == 0) {
        return getValue();
      }

      double phase = getPhaseMod().getValue();
      return (blitsquare(tickCount) + phase) * 0.7 + 0.15;
    }
  }
}
