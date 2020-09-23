import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Project1 extends Synth {
  public static void main(String[] args) {
    Synth.run(new Project1(), args);
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

    //TODO*************************************************************************************************************
    // BEFORE THE ORGAN:
      // Create an Add which takes the MidiMod pitch as input

      // Build a Hammond Organ with dials and options.  It now takes the *Add* as its frequency mod

      // THE ENVELOPE:
      ADSR adsr = new ADSR();
      // Build an ADSR with dials at least.  It should default to sine waves
      // -- If you were really cool you'd add an options to change the wave type
      // -- If you were REALLY cool you'd add additional options for random LFOs or Sample and Hold Random LFOs.
      // Build an amplifier AAA that takes the Hammond as input, modulated by the ADSR
    //TODO*************************************************************************************************************

    // Build a Hammond Organ with dials and options
    Hammond ham = new Hammond();
    ham.setFrequencyMod(midimod);
    modules.add(ham);

    //TODO Maybe doesn't go here **************************************************************************************
    // THE LESLIE (DOPPLER EFFECT):
      LFO lfo = new LFO();
      // Build an LFO.  Its rate should be a dial multiplied by 0.001 (use Mul) to slow it down
      // First we'll make the Pitch effect:
      // Make a Mul whose input is the LFO, multiplied by a "pitch" dial.
      // Make another Mul whose input is the previous Mul multiplied by 0.001 so the pitch doesn't go nuts
      // Set this final Mul as the adder() for the Add so it affects the organ's pitch
      // Next we'll make the volume effect:
      // Make yet another Mul, with the LFO as input, and an "Amplitude" dial as multiplier.
      // Make another Add, with the Mul as the adder(), and that's it.  No input.  (thus it's 1.0 + Mul)
      // Make an amplifier BBB with the first amplifier AAA as input and this new Add as modulation to flutter the overall volume.
    //TODO Maybe doesn't go here **************************************************************************************

    //TODO Build a Gate amplifier.  Its signal now gets fed *amplifier BBB***********************************************
    Amplifier amp = new Amplifier();
    amp.setInput(ham);
    amp.setAmplitudeMod(gate);
    modules.add(amp);

    // layout for osciloscope output
    Box outputBox = new Box(BoxLayout.Y_AXIS);
    outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
    Dial dial = new Dial(1.0);
    outputBox.add(dial.getLabelledDial("Gain"));

    // Add a Gain amplifier fed the Gate amplifier
    Amplifier gain = new Amplifier();
    gain.setInput(amp);
    gain.setAmplitudeMod(dial.getModule());
    modules.add(gain);

    // Add an Oscillocope
    // Set the output to the gain amplifier (or oscilloscope)
    Oscilloscope oscope = new Oscilloscope();
    Oscilloscope.OModule omodule = oscope.getModule();
    modules.add(omodule);
    omodule.setAmplitudeModule(gain);
    outputBox.add(oscope);

    Box adder = new Box(BoxLayout.Y_AXIS);
    adder.add(adsr.getGUI());
    adder.add(lfo.getGUI());
    outer.add(ham.getGUI());
    outer.add(adder);
    outer.add(outputBox);

    // Set the oscilloscope's output as our audio output.
    setOutput(omodule);


    //on X quit fixes where the program continues running while window is closed
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    // Pack and display the window
    frame.pack();
    frame.setVisible(true);
    }

  }

