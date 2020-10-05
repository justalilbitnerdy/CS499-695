/** A mixer for multiple inputs, each multiplied by an amplitude. */

public class Mixer extends Module
    {
    Module[] inputs;
    Module[] amplitudeMods;

    public Mixer(int numInputs)
        {
        inputs = new Module[numInputs];
        amplitudeMods = new Module[numInputs];
        for(int i = 0; i < numInputs; i++)
        	{
        	inputs[i] = new Constant(0);
        	amplitudeMods[i] = new Constant(0);
        	}
        }

    public Mixer(Module[] inputs, Module[] amplitudeMods) {
    	this.inputs = inputs;
    	this.amplitudeMods = amplitudeMods;
    }

    public Module getInput(int val) { return inputs[val]; }
    public void setInput(int val, Module input) { inputs[val] = input; }

    public Module getAmplitudeMod(int val) { return amplitudeMods[val]; }
    public void setAmplitudeMod(int val, Module mod) { amplitudeMods[val] = mod; }

    public double tick(long tickCount)
        {
        double val = 0;
        for(int i = 0; i < inputs.length; i++)
            {
            val += ((inputs[i].getValue() - 0.5) * amplitudeMods[i].getValue());
            }
        return val + 0.5;
        }
    }
