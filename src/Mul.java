/**
	A simple class which multiplies the value of an INPUT against a MULTIPLIER.
	That's all -- it's just mutiplying, just which is the input and which is the multiplier
	is not consequential.  Don't use this for amplification -- use Amplifier instead,
	which treats the input as signed centered at 0.5.
*/

public class Mul extends Module 
{
    private Module input = new Constant(1.0);
    private Module multiplier = new Constant(1.0);

    public Module getMultiplier() { return multiplier; }
    public void setMultiplier(Module multiplier) { this.multiplier = multiplier; }

    public Module getInput() { return input; }
    public void setInput(Module input) { this.input = input; }

    public double tick(long tickCount) 
    {
    return input.getValue() * multiplier.getValue();
    }
    
    public Mul() { super(); }
    public Mul(double multiplier) { super(); setMultiplier(new Constant(multiplier)); }
}