public class BlitTriangle extends BlitSquare 
    {
    // this might be useful
    double prev = 0;
    
    protected double blittriangle(long tickCount)
        {
        // IMPLEMENT ME
        return 0.0;
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
                        
            return blittriangle(tickCount) * 4 + 1;
            }
        }
    }
