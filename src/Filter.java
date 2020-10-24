public class Filter extends Module {
  Module input = new Constant(0.5);
  double b0;
  double[] b;
  double[] a;
  double[] x;
  double[] y;
  double x0;

  public Filter(double[] a, double[] b, double b0) {
    this.a = a;
    this.b = b;
    this.b0 = b0;
    this.x = new double[b.length];
    this.y = new double[a.length];
  }

  public Module getInput() { return input; }
  public void setInput(Module input) { this.input = input; }

  public double tick(long tickCount) {
    x0 = getInput().getValue() - 0.5;
    double sum = x0 * b0;
    for (int i=0; i<a.length; i++) sum -= a[i] * y[i];
    for (int i=0; i<b.length; i++) sum += b[i] * x[i];
    for (int i=a.length-1; i>=1; i--) y[i] = y[i-1];
    y[0] = sum;
    for (int i=b.length-1; i>=1; i--) x[i] = x[i-1];
    x[0] = x0;
    return sum + 0.5;
  }
}