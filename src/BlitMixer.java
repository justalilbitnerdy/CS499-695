import javax.swing.*;

public class BlitMixer extends Mixer {

  private Box GUI;

  Dial BlitAmplitude;
  Dial BPBlitAmplitude;
  Dial SawAmplitude;
  Dial SquareAmplitude;
  Dial SquarePulseWidth;
  Dial TriangleAmplitude;
  Dial TrianglePulseWidth;

  // I'm 90% sure that we don't need to hold references to these, but I'm
  //  leaving them in for...... future use.. Yeah, let's go with that.
  Blit         _Blit;
  BPBlit       _BpBlit;
  BlitSaw      _Saw;
  BlitSquare   _Square;
  BlitTriangle _Triangle;

  // I really wanted to make a setFrequencyMod function, but the fact that a
  //  Mixer doesn't use a frequency, discouraged me.
  //  Admittedly, this isn't really better though
  public BlitMixer(Module frequencyMod) {
    // big ol' hack, but Dang it I've spend a lot of time here, and I'm going to
    // make it work!
    super(0);
    // Build dials for the amplitude of each of the oscillators, plus
    //       the pulse width of the square and triangle
    // Put them in a box and put it in the window
    buildGUI();
    // Build the oscillators
    _Blit     = new Blit();
    _Blit.setFrequencyMod(frequencyMod);

    _BpBlit   = new BPBlit();
    _BpBlit.setFrequencyMod(frequencyMod);

    _Saw      = new BlitSaw();
    _Saw.setFrequencyMod(frequencyMod);

    _Square   = new BlitSquare();
    _Square.setFrequencyMod(frequencyMod);
    _Square.setPhaseMod(SquarePulseWidth.getModule());

    _Triangle = new BlitTriangle();
    _Triangle.setFrequencyMod(frequencyMod);
    _Triangle.setPhaseMod(TrianglePulseWidth.getModule());
    
    //stolen from Mixer.java
    this.inputs = new Module[]{ _Blit, _BpBlit, _Saw, _Square, _Triangle };
    this.amplitudeMods = new Module[]{ BlitAmplitude.getModule(),
                                       BPBlitAmplitude.getModule(),
                                       SawAmplitude.getModule(),
                                       SquareAmplitude.getModule(),
                                       SquarePulseWidth.getModule(),
                                       TriangleAmplitude.getModule(),
                                       TrianglePulseWidth.getModule()};
  }

  public double tick(long tickCount) {
    return super.tick(tickCount);
  }

  public Module[] getModules(){
    return inputs;
  }


  //stole starting values from the demo
  public void buildGUI() {
    GUI = new Box(BoxLayout.Y_AXIS);
    GUI.setBorder(BorderFactory.createTitledBorder("Blits"));
    // build dials for Attack, Decay, Sustain and Release
    BlitAmplitude = new Dial(1.0);
    GUI.add(BlitAmplitude.getLabelledDial("Blit Amplitude"));
    BlitAmplitude.update(0);

    BPBlitAmplitude = new Dial(1.0);
    GUI.add(BPBlitAmplitude.getLabelledDial("BPBlit Amplitude"));
    BPBlitAmplitude.update(0);

    SawAmplitude = new Dial(1.0);
    GUI.add(SawAmplitude.getLabelledDial("Saw Amplitude"));
    SawAmplitude.update(1);

    SquareAmplitude = new Dial(1.0);
    GUI.add(SquareAmplitude.getLabelledDial("Square Amplitude"));
    SquareAmplitude.update(0);

    SquarePulseWidth = new Dial(1.0);
    GUI.add(SquarePulseWidth.getLabelledDial("Square Pulse Width"));
    SquarePulseWidth.update(.5);

    TriangleAmplitude = new Dial(1.0);
    GUI.add(TriangleAmplitude.getLabelledDial("Triangle Amplitude"));
    TriangleAmplitude.update(0);

    TrianglePulseWidth = new Dial(1.0);
    GUI.add(TrianglePulseWidth.getLabelledDial("Triangle Pulse Width"));
    TrianglePulseWidth.update(.5);
  }

  public Box getGUI() {
    return GUI;
  }

}
