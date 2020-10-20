public class LPF extends Filter 
    {
    Module frequencyMod = new Constant(1.0);
        
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

    void updateFilter(double CUTOFF, double Q)
        {
        // IMPLEMENT ME
        }
        
    public LPF()
        {
        super(new double[2], new double[2], 0);
        }
        
    public static final double MIN_CUTOFF = 10.0;		// don't set the cutoff below this (in Hz)
    public double tick(long tickCount) 
        {
        double q = (resonanceMod.getValue() * 10 + 1) * Math.sqrt(0.5);
        double cutoff = Math.max(MIN_CUTOFF, Utils.valueToHz(frequencyMod.getValue()));
        updateFilter(cutoff, q);
        return super.tick(tickCount);
        }
    }
