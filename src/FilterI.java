import javax.swing.*;

interface FilterI
{

    public void setFrequencyMod(Module frequencyMod);
    public Module getFrequencyMod();

    public void setResonanceMod(Module resonanceMod);
    public Module getResonanceMod();

    public void setInput(Module input);

    public double tick(long tickCount);

    public Box getGUI();

    public Dial getCutoffDial();
}