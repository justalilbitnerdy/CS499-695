/**
	A module which aways returns a constant value.
*/

public class Constant extends Module 
    {
    public Constant(double value) 
        {
        setValue(value);
        }

    public double tick(long tickCount) 
        {
        return getValue();
        }
    }
