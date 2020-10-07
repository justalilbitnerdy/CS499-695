public class BlitSaw extends Blit 
    {
    // this might be useful
    double prev = 0;

	public double blitsaw(long tickCount)
		{
		/// IMPLEMENT ME
            prev = (1 - 1/getP()) * prev + blit(tickCount, 0.0);
            return prev;
		}
		
    public double tick(long tickCount) 
        {
            /// IMPLEMENT ME.  Note how the other implementations of tick worked
            if (tickCount <= 0) return 0.0;
            else {
                if (Utils.valueToHz(getFrequencyMod().getValue()) == 0) return 0.0;
                return blitsaw(tickCount) - 0.5;
            }
        }
    }
