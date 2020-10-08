public class BlitTriangle extends BlitSquare
    {
    // this might be useful
    double prev = 0;
    
    protected double blittriangle(long tickCount)
        {
            double f = Utils.valueToHz(getFrequencyMod().getValue());
            double a = 1-.1*Math.min(1,f/1000);

            prev = a * prev + blitsquare(tickCount)/getP();
            return prev;
        }
        
    public double tick(long tickCount) 
        {
        if (tickCount <= 0)
            return 0;
        else
            {
            if (Utils.valueToHz(getFrequencyMod().getValue()) == 0)
                {
                return 0;
                }
                        
            return blittriangle(tickCount) * 4.0 + 0.5;
            }
        }
    }
