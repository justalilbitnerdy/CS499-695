/**
	A simple class which adds the value of an INPUT against an ADDER.
	That's all -- it's just adding, just which is the input and which is the adder
	is not consequential.
*/

public class Add extends Module {
    private Module input = new Constant(1.0);
    private Module adder = new Constant(0.0);

    public Module getAdder() { return adder; }
    public void setAdder(Module adder) { this.adder = adder; }

    public Module getInput() { return input; }
    public void setInput(Module input) { this.input = input; }

    public double tick(long tickCount) {
        return input.getValue() + adder.getValue();
    }
}