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
    PM    ops[] = new PM[NUM_OPERATORS];
    Mixer opMixers[] = new Mixer[NUM_OPERATORS];
    Dial inDials[][] = new Dial[NUM_OPERATORS][NUM_OPERATORS];// Incoming modulation dial [To][From]
    Dial[] outDials = new Dial[NUM_OPERATORS];// Out dials
    Module[] opModules = new Module[NUM_OPERATORS];// Out dials
    for(int i=0;i<NUM_OPERATORS;i++){
      outDials[i] = new Dial(1.0);
      modules.add(outDials[i].getModule());
      opModules[i] = outDials[i].getModule();
      ops[i] = new PM();
      ops[i].setFrequencyMod(midimod);
      for(int j = 0;j<NUM_OPERATORS;j++){
        Dial signalDial = new Dial(0);
        modules.add(signalDial.getModule());
        inDials[i][j] = signalDial;
      }
    }
    for(int i = 1;i<=NUM_OPERATORS;i++){
      // Make a box
      Box opBox = new Box(BoxLayout.Y_AXIS);
      opBox.setBorder(BorderFactory.createTitledBorder("Operator "+i));
      // setup dials
      Dial relativeFreq = new Dial(.1);
      opBox.add(relativeFreq.getLabelledDial("Relative Frequency"));
      modules.add(relativeFreq.getModule());

      Dial AttackDial = new Dial(.1);
      opBox.add(AttackDial.getLabelledDial("Attack Time"));
      modules.add(AttackDial.getModule());

      Dial AttackLevelDial = new Dial(1.0);
      opBox.add(AttackLevelDial.getLabelledDial("Attack Level"));
      modules.add(AttackLevelDial.getModule());

      Dial DecayDial = new Dial(0);
      opBox.add(DecayDial.getLabelledDial("Decay Time"));
      modules.add(DecayDial.getModule());

      Dial SustainDial = new Dial(1.0);
      opBox.add(SustainDial.getLabelledDial("Sustain"));
      modules.add(SustainDial.getModule());

      Dial ReleaseDial = new Dial(.1);
      opBox.add(ReleaseDial.getLabelledDial("Release Time"));
      modules.add(ReleaseDial.getModule());
      // Add an Out dial
      Dial gainDial = new Dial(1.0);
      opBox.add(gainDial.getLabelledDial("gain"));
      modules.add(gainDial.getModule());

      // Make the operator
      opModules[i-1] = gainDial.getModule();

      // Add Relative Frequency
      ops[i-1].setRelativeFrequency(relativeFreq.getModule());

      // Add an ADSR
      // Add Attack Time, Decay Time, Sustain, Release Time
      ADSR adsr = new ADSR(AttackDial.getModule(),
                           AttackLevelDial.getModule(),
                           DecayDial.getModule(),
                           SustainDial.getModule(),
                           ReleaseDial.getModule());
      adsr.setGate(gate);
      modules.add(adsr);
      // *Multiply* (not amplify) the ADSR output against a Gain
      Mul opMul = new Mul();
      opMul.setInput(adsr);
      opMul.setMultiplier(gainDial.getModule());
      modules.add(opMul);
      ops[i-1].setOutputAmplitude(opMul);
      modules.add(ops[i-1]);

      //setup input operator Dials for mixer
      Module inOpsDials[] = new Module[NUM_OPERATORS];
      for(int j = 0;j<NUM_OPERATORS;j++){
        inOpsDials[j] = inDials[i-1][j].getModule();
      }

      // Add a Mixer to mix in incoming signals from all four operators
      Mixer opMixer = new Mixer(ops,inOpsDials);
      opMixers[i-1] = opMixer;
      ops[i-1].setPhaseModulator(opMixer);
      modules.add(opMixer);

      // Add dials for the four signals
      for(int j = 0;j<inDials[i-1].length;j++){
        opBox.add(inDials[i-1][j].getLabelledDial("Operator " + (j+1)));
      }
      opBox.add(outDials[i-1].getLabelledDial("Out"));
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
        for(int i = 0; i < inDials.length; i++){
          outDials[i].getModule().setValue(0);
          for(int j = 0; j < inDials[i].length; j++)
            inDials[i][j].getModule().setValue(0);
        }

        switch(val) {
          case 0:
            inDials[3][2].getModule().setValue(1);
            inDials[2][1].getModule().setValue(1);
            inDials[1][0].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 1:
            inDials[3][1].getModule().setValue(1);
            inDials[1][0].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            outDials[2].getModule().setValue(1);
            break;
          case 2:
            inDials[3][1].getModule().setValue(1);
            inDials[1][0].getModule().setValue(1);
            inDials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 3:
            inDials[3][1].getModule().setValue(1);
            inDials[3][0].getModule().setValue(1);
            inDials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            break;
          case 4:
            inDials[1][0].getModule().setValue(1);
            inDials[3][2].getModule().setValue(1);
            outDials[3].getModule().setValue(1);
            outDials[1].getModule().setValue(1);
            break;
          case 5:
            inDials[1][0].getModule().setValue(1);
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

    Mixer opMixer = new Mixer(ops, opModules);
    modules.add(opMixer);
    // Make a VCA fed by the mixer, controlled by a final Gain dial
    Dial gainDial = new Dial(1.0);
    outputBox.add(gainDial.getLabelledDial("Gain"));
    modules.add(gainDial.getModule());

    Amplifier VCA = new Amplifier();
    VCA.setInput(opMixer);
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