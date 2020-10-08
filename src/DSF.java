public class DSF extends Osc{
  Module a = new Constant(1.0);
  Module f_beta_Multiplier = new Constant(4.0);

  private Module getFBetaMultiplier() { return this.f_beta_Multiplier; }
  public void setFBetaMultiplier(Module f_beta_Multiplier) { this.f_beta_Multiplier = f_beta_Multiplier; }

  private Module getA() { return this.a; }
  public void setA(Module a) { this.a = a; }

  private double theta(long tickCount) {
    return getFrequencyMod().getValue() * 2.0 * Math.PI * (double)tickCount;
  }

  private double beta(long tickCount) {
    return theta(tickCount) * getFBetaMultiplier().getValue();
  }

  public double tick(long tickCount) {
    super.tick(tickCount);
    double n = Config.NYQUIST_LIMIT;
    double a_val = getA().getValue();
    double theta = theta(tickCount);
    double beta = beta(tickCount);
    return (
            (Math.sin(theta) - a_val*Math.sin(tickCount - beta) - Utils.fastpow(a_val, n+1)*(
                    Math.sin(theta + n*beta + beta) - a_val*Math.sin(theta + n*beta)
                    )
            ) / (1 + a_val * a_val - 2 * a_val * Math.cos(beta))
    );
  }

}
