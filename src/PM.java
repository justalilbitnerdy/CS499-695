/* 
 * Utility module that provides some common parameters for all modules inteded to be oscillators
 */
public class PM extends Osc 
    {
    public static final double MAX_RELATIVE_FREQUENCY = 8;  // The DX7 uses 32
    
    Module relativeFrequency = new Constant(1.0);
    public void setRelativeFrequency(Module p) { relativeFrequency = p; }
    public Module getRelativeFrequency() { return relativeFrequency; }
    
    Module phaseModulator = new Constant(0.0);
    public void setPhaseModulator(Module p) { phaseModulator = p; }
    public Module getPhaseModulator() { return phaseModulator; }
    
    public static final double MAX_PHASE_AMPLIFICATION = 4;
    
    Module phaseAmplifier = new Constant(1.0);
    public void setPhaseAmplifier(Module p) { phaseAmplifier = p; }
    public Module getPhaseAmplifier() { return phaseAmplifier; }
    
    Module outputAmplitude = new Constant(1.0);
    public void setOutputAmplitude(Module p) { outputAmplitude = p; }
    public Module getOutputAmplitude() { return outputAmplitude; }

    double state = 0;
    
    public double tick(long tickCount) {
        // IMPLEMENT ME
        // REMEMBER TO CENTER THE INCOMING MODULATION WAVE AND THE OUTGOING FINAL WAVE AROUND 0.5
        double hz = Utils.valueToHz(getFrequencyMod().getValue());
        state += relativeFrequency.getValue() * MAX_RELATIVE_FREQUENCY * hz * Config.INV_SAMPLING_RATE;
        if (state >= 1)
        {
            state -= 1;
            reset();
        }
        double phase_mod = (phaseAmplifier.getValue()*MAX_PHASE_AMPLIFICATION)*(phaseModulator.getValue() - 0.5) + 0.5;

        double out = Math.sin(2*Math.PI*(state+phase_mod));
        out /= 2;
        return out + 0.5;
    }
}
