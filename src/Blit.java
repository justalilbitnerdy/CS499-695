public class Blit extends Osc 
    {
    public double sincm(double x, double m)
        {
        /// IMPLEMENT ME
        return 0.0;
        }

    public double tick(long tickCount)
        {
        return blit(tickCount, 0);
        }

	/// d goes from 0...1.  The purpose of d is to allow an offset in phase.
	/// What might be able to take advantage of this function?
	
    protected double blit(long tickCount, double d) 
        {
        double phase = super.tick(tickCount);
        
        double freq = Utils.valueToHz(getFrequencyMod().getValue());
        if(freq < 0.001) freq = 0.001; 
        
        double p = Config.SAMPLING_RATE / freq;
    
        double m = Math.floor(p / 2.0) * 2.0 + 1.0;
        return (m /p) * sincm((tickCount - d/m) * m/p, m);
        }
    }
