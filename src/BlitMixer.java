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
  Dial WhiteNoiseAmplitude;
  Dial DSFAmplitude;
  Dial DSFbeta;
  Dial DSFalpha;

  // I'm 90% sure that we don't need to hold references to these, but I'm
  //  leaving them in for...... future use.. Yeah, let's go with that.
  Blit         _Blit;
  BPBlit       _BpBlit;
  BlitSaw      _Saw;
  BlitSquare   _Square;
  BlitTriangle _Triangle;
  WhiteNoise   _WhiteNoise;
  DSF          _DSF;

  // I really wanted to make a setFrequencyMod function, but the fact that a
  //  Mixer doesn't use a frequency, discouraged me.
  //  Admittedly, this isn't really better though
  public BlitMixer() {
    // big ol' hack, but Dang it I've spend a lot of time here, and I'm going to
    // make it work!
    super(0);
    // Build dials for the amplitude of each of the oscillators, plus
    //       the pulse width of the square and triangle
    // Put them in a box and put it in the window
    buildGUI();
    // Build the oscillators
    _Blit     = new Blit();
    _BpBlit   = new BPBlit();
    _Saw      = new BlitSaw();
    _Square   = new BlitSquare();
    _Square.setPhaseMod(SquarePulseWidth.getModule());

    _Triangle = new BlitTriangle();
    _Triangle.setPhaseMod(TrianglePulseWidth.getModule());

    _WhiteNoise = new WhiteNoise();
    _DSF        = new DSF(DSFbeta.getModule(),DSFalpha.getModule());

    //stolen from Mixer.java
    inputs = new Module[7];
    amplitudeMods = new Module[7];
    inputs[0] = _Blit;
    amplitudeMods[0] = BPBlitAmplitude.getModule();
    inputs[1] = _BpBlit;
    amplitudeMods[1] = SawAmplitude.getModule();
    inputs[2] = _Saw;
    amplitudeMods[2] = SquareAmplitude.getModule();
    inputs[3] = _Square;
    amplitudeMods[3] = SquareAmplitude.getModule();
    inputs[4] = _Triangle;
    amplitudeMods[4] = TriangleAmplitude.getModule();
    inputs[5] = _WhiteNoise;
    amplitudeMods[5] = WhiteNoiseAmplitude.getModule();
    inputs[6] = _DSF;
    amplitudeMods[6] = DSFAmplitude.getModule();
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

    WhiteNoiseAmplitude = new Dial(1.0);
    GUI.add(WhiteNoiseAmplitude.getLabelledDial("White Noise Amplitude"));
    WhiteNoiseAmplitude.update(0);

    Box DSFBox = new Box(BoxLayout.X_AXIS);
    DSFBox.setBorder(BorderFactory.createTitledBorder("DSF"));
    DSFAmplitude = new Dial(1.0);
    DSFBox.add(DSFAmplitude.getLabelledDial("Amplitude  "));
    DSFAmplitude.update(1);

    DSFbeta = new Dial(1.0);
    DSFBox.add(DSFbeta.getLabelledDial("beta"));
    DSFbeta.update(0);

    DSFalpha = new Dial(1.0);
    DSFBox.add(DSFalpha.getLabelledDial("alpha"));
    DSFalpha.update(0);

    GUI.add(DSFBox);
  }

  public Box getGUI() {
    return GUI;
  }

}