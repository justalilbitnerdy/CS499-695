import javax.swing.*;

public class LPF extends Filter {
  Module frequencyMod = new Constant(1.0);
  private Box GUI;
  private Dial CutoffDial;
  private Dial ResonanceDial;

  public void setFrequencyMod(Module frequencyMod) {
    this.frequencyMod = frequencyMod;
  }

  public Module getFrequencyMod() {
    return this.frequencyMod;
  }

  Module resonanceMod = new Constant(1.0);

  public void setResonanceMod(Module resonanceMod) {
    this.resonanceMod = resonanceMod;
  }

  public Module getResonanceMod() {
    return this.resonanceMod;
  }

  void updateFilter(double CUTOFF, double Q) {
    // IMPLEMENT ME
    double cutoff = 2 * Math.PI * CUTOFF;
    double w2qt2 = cutoff*cutoff*Q*Config.INV_SAMPLING_RATE*Config.INV_SAMPLING_RATE;
    double J = 4*Q + 2*cutoff*Config.INV_SAMPLING_RATE + w2qt2;
    this.b0 = w2qt2 / J;
    this.b[0] = 2 * this.b0;
    this.b[1] = this.b0;
    this.a[0] = (2*w2qt2 - 8*Q) / J;
    this.a[1] = (4*Q - 2*cutoff*Config.INV_SAMPLING_RATE + w2qt2) / J;
  }

  public LPF() {
    super(new double[2], new double[2], 0);
    buildGUI();
    resonanceMod = ResonanceDial.getModule();
  }

  public static final double MIN_CUTOFF = 10.0;		// don't set the cutoff below this (in Hz)
  public double tick(long tickCount) {
    double q = (resonanceMod.getValue() * 10 + 1) * Math.sqrt(0.5);
    double cutoff = Math.max(MIN_CUTOFF, Utils.valueToHz(frequencyMod.getValue()));
    updateFilter(cutoff, q);
    return super.tick(tickCount);
  }

  public Box getGUI(){
    return GUI;
  }

  public Dial getCutoffDial(){
    return CutoffDial;
  }

  private void buildGUI(){
    GUI = new Box(BoxLayout.Y_AXIS);
    GUI.setBorder(BorderFactory.createTitledBorder("Filter"));
    // build dials for Attack, Decay, Sustain and Release
    CutoffDial = new Dial(1.0);
    GUI.add(CutoffDial.getLabelledDial("Cutoff"));

    ResonanceDial = new Dial(0);
    GUI.add(ResonanceDial.getLabelledDial("Resonance"));
  }
}
