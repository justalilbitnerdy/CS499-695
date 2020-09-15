/**
 A simple module which multiplies its INPUT against an AMPLITUDE MOD
 and a SCALE MOD.  The INPUT is treated as SIGNED with no DC offset: 
 its zero point is centered at 0.5.  The AMPLITUDE MOD is meant to run 
 between 0 and 1. The SCALE MOD is typically set to a constant, by 
 default 1 but it can be higher.
 */
 
public class Amplifier extends Module 
    {
    private Module input = new Constant(1.0);
    private Module amplitudeMod = new Constant(1.0);
    private Module scaleMod = new Constant(1.0);
    
    public Module getAmplitudeMod() { return amplitudeMod; }
    public void setAmplitudeMod(Module amplitudeMod) { this.amplitudeMod = amplitudeMod; }

    public Module getScaleMod() { return scaleMod; }
    public void setScaleMod(Module scaleMod) { this.scaleMod = scaleMod; }

    public Module getInput() { return input; }
    public void setInput(Module input) { this.input = input; }

    public double tick(long tickCount) 
        {
	    return (input.getValue() - 0.5) * amplitudeMod.getValue() * scaleMod.getValue() + 0.5;
        }
    }
