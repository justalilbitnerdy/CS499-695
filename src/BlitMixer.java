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

  BPBlit       BpBlit;
  BlitSaw      Saw;
  BlitSquare   Square;
  BlitTriangle Triangle;

  public BlitMixer() {
    super(4);

    // Build dials for the amplitude of each of the oscillators, plus
    //       the pulse width of the square and triangle
    // Put them in a box and put it in the window
    buildGUI();
    // Build the oscillators
    BpBlit   = new BPBlit();
    BpBlit.setAm
    Saw      = new BlitSaw();
    Square   = new BlitSquare();
    Triangle = new BlitTriangle();
  }

  //tick through
  public double tick(long tickCount) {
    double bp = BpBlit.tick();
    double saw = Saw.tick();
    double square = Square.tick();
    double triangle Triangle.tick();
    return 0.0;
  }


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
