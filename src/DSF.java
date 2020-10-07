public class DSF extends Osc{
  Module f_beta;
  Module a;
  Double f_beta_Multiplier = 4.0;

  public DSF(Module f_beta,Module a){
    super();
    this.f_beta = f_beta;
    this.a      = a;
  }

  public double tick(long tickCount) {
    return 0.0;
  }

}
