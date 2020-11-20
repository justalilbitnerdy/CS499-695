import javax.swing.*;
import java.awt.*;
import java.util.*;


public class Project3 extends Synth {
  int NUM_OPERATORS = 4;
  public static void main(String[] args) {
    Synth.run(new Project3(), args);
    }

  public void setup() {
    // Build a MidiModule
    MidiModule midimod = new MidiModule(getMidi());
    modules.add(midimod);
    // Build a MidiGate
    MidiGate gate = new MidiGate(midimod);
    modules.add(gate);
    // Create the window
    JFrame frame = new JFrame();
    Box outer = new Box(BoxLayout.X_AXIS);
    frame.setContentPane(outer);
    // I moved this up here so I can add the boxes to the gui
    // Make an Other box
    Box outputBox = new Box(BoxLayout.Y_AXIS);
    outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
    // FOR EACH OPERATOR (4 operators)
    // Due to the From Operator portion I can't make Operator its own class...
    // This should be fun
    ArrayList<PM> ops = new ArrayList(NUM_OPERATORS);
    Dial dials[][] = new Dial[NUM_OPERATORS][NUM_OPERATORS];		// Incoming modulation dial [To][From]
    for(int i=0;i<NUM_OPERATORS;i++){
      for(int j = 1;j<=NUM_OPERATORS;j++){
        Dial signalDial = new Dial(1.0);
        signalDial.update(.1);
        dials[i][j-1] = signalDial;
      }
    }
    Dial[] outDials = new Dial[NUM_OPERATORS];// Out dials
    for(int i = 1;i<=NUM_OPERATORS;i++){
      // Make a box
      Box opBox = new Box(BoxLayout.Y_AXIS);
      opBox.setBorder(BorderFactory.createTitledBorder("Operator "+i));
      // setup dials
      Dial relativeFreq = new Dial(.1);
      opBox.add(relativeFreq.getLabelledDial("Relative Frequency"));
      Dial AttackDial = new Dial(1.0);
      opBox.add(AttackDial.getLabelledDial("Attack Time"));
      AttackDial.update(.1);
      Dial AttackLevelDial = new Dial(1.0);
      opBox.add(AttackLevelDial.getLabelledDial("Attack Level"));
      Dial DecayDial = new Dial(1.0);
      opBox.add(DecayDial.getLabelledDial("Decay Time"));
      DecayDial.update(0);
      Dial SustainDial = new Dial(1.0);
      opBox.add(SustainDial.getLabelledDial("Sustain"));
      Dial ReleaseDial = new Dial(1.0);
      opBox.add(ReleaseDial.getLabelledDial("Release Time"));
      ReleaseDial.update(.1);
      // Add an Out dial
      Dial gainDial = new Dial(1.0);
      opBox.add(gainDial.getLabelledDial("gain"));
      // Make the operator
      PM op = new PM();
      ops.add(op);
      // Add Relative Frequency

      // Add an ADSR
      ADSR adsr = new ADSR(AttackDial.getModule(),
                           AttackLevelDial.getModule(),
                           DecayDial.getModule(),
                           SustainDial.getModule(),
                           ReleaseDial.getModule());
      // Add Attack Time, Decay Time, Sustain, Release Time
      // *Multiply* (not amplify) the ADSR output against a Gain
      // Add a Mixer to mix in incoming signals from all four operators
      // Add dials for the four signals
      for(int j = 0;j<dials[i-1].length;j++){
        opBox.add(dials[i-1][j].getLabelledDial("Operator " + j));
      }
      outer.add(opBox);
    }
    // Add Algorithm.  I'm nice and provided most of it for you below

    final String[] ALGORITHMS = new String[] {
      "1>2>3>4",
      "1>2>4, 3",
      "1>2>4, 3>4",
      "1>4, 2>4, 3>4",
      "1>2, 3>4",
      "1>2, 3, 4",
      "1, 2, 3, 4"
    };

    Options opt = new Options("Algorithms", ALGORITHMS, 6) {
      public void update(int val) {
        // This is a race condition, but I'll just update the dials, it won't be too bad
        for(int i = 0; i < dials.length; i++)
          for(int j = 0; j < dials[i].length; j++)
            dials[i][j].getModule().setValue(0);

        for(int i = 0; i < dials.length; i++)
          outDials[i].getModule().setValue(0);

        switch(val) {
          case 0:
            dials[3][2].getModule().setValue(1);
            dials[2][1].getModule().setValue(1);
            dials[1][0].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 1:
            dials[3][1].getModule().setValue(1);
            dials[1][0].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            outDials[2].getModule().setValue(1);
            break;
          case 2:
            dials[3][1].getModule().setValue(1);
            dials[1][0].getModule().setValue(1);
            dials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 3:
            dials[3][1].getModule().setValue(1);
            dials[3][0].getModule().setValue(1);
            dials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 4:
            dials[1][0].getModule().setValue(1);
            dials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            outDials[1].getModule().setValue(1);
            break;
          case 5:
            dials[1][0].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            outDials[2].getModule().setValue(1);
            outDials[1].getModule().setValue(1);
            break;
          case 6:
            outDials[3].getModule().setValue(1);
            outDials[2].getModule().setValue(1);
            outDials[1].getModule().setValue(1);
            outDials[0].getModule().setValue(1);
            break;
        }
      }
    };
    outputBox.add(opt);
    // Make a final mixer whose inputs are the operators, controlled by their Out dials
    // Make a VCA fed by the mixer, controlled by a final Gain dial
    Dial gainDial = new Dial(1.0);
    outputBox.add(gainDial.getLabelledDial("Gain"));
    Amplifier VCA = new Amplifier();
    VCA.setInput(midimod);
    VCA.setAmplitudeMod(gainDial.getModule());
    modules.add(VCA);
    // Make an oscilloscope fed by the VCA.  I'd set	 oscope.setDelay(1);
    Oscilloscope oscope = new Oscilloscope();
    oscope.setDelay(1);
    Oscilloscope.OModule omodule = oscope.getModule();
    modules.add(omodule);
    omodule.setAmplitudeModule(VCA);
    outputBox.add(oscope);
    //setup display
    outer.add(outputBox);
    // Output the VCA or oscilloscope
    setOutput(omodule);
    // Pack and display the window
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    // Pack and display the window
    frame.pack();
    frame.setVisible(true);

    // Some code you might find useful
  }
}