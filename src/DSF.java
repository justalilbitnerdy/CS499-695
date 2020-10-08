public class DSF extends Osc{
  Module a = new Constant(1.0);
  Module f_beta_Multiplier = new Constant(4.0);

  private Module getFBetaMultiplier() { return this.f_beta_Multiplier; }
  public void setFBetaMultiplier(Module f_beta_Multiplier) { this.f_beta_Multiplier = f_beta_Multiplier; }

  private Module getA() { return this.a; }
  public void setA(Module a) { this.a = a; }

  private double theta(double timestep) {
    return getFrequencyMod().getValue() * 2.0 * Math.PI * timestep;
  }

  private double beta(double timestep) {
    return theta(timestep) * getFBetaMultiplier().getValue();
  }

  public double tick(long tickCount) {
    double freq = Utils.valueToHz(getFrequencyMod().getValue());
    if(freq < 0.001) freq = 0.001;
    double timestep = super.tick(tickCount) * Config.SAMPLING_RATE / freq;

    double n = Config.NYQUIST_LIMIT;
    double a_val = getA().getValue();
    double theta = theta(timestep);
    double beta = beta(timestep);
    return (
            (Math.sin(theta) - a_val*Math.sin(tickCount - beta) - Utils.fastpow(a_val, n+1)*(
                    Math.sin(theta + n*beta + beta) - a_val*Math.sin(theta + n*beta)
                    )
            ) / (1 + a_val * a_val - 2 * a_val * Math.cos(beta))
    );
  }

}
