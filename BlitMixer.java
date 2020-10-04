public class BlitMixer extends Mixer{

    public BlitMixer(){
        super(4);
        buildGUI();
    }

    //tick through 
    public double tick(long tickCount) {
        return blit(tickCount, 0);
    }


    public void buildGUI(){

    }

    public Box getGui(){
        return GUI;
    }
    
}
