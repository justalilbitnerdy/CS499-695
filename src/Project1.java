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
    Add add = new Add();
    add.setInput(midimod);
    modules.add(add);
    // Build a Hammond Organ with dials and options.  It now takes the *Add* as its frequency mod
    Hammond ham = new Hammond();
    ham.setFrequencyMod(add);
    modules.add(ham);

      // THE ENVELOPE:
    ADSR adsr = new ADSR();
    modules.add(adsr);
    adsr.setGate(gate);
      // Build an ADSR with dials at least.  It should default to sine waves
      // -- If you were really cool you'd add an options to change the wave type
      // -- If you were REALLY cool you'd add additional options for random LFOs or Sample and Hold Random LFOs.
    // Build an amplifier AAA that takes the Hammond as input, modulated by the ADSR
    Amplifier AAA = new Amplifier();
    AAA.setInput(ham);
    AAA.setAmplitudeMod(adsr);
    modules.add(AAA);
    //TODO*************************************************************************************************************
    // THE LESLIE (DOPPLER EFFECT):
    // Build an LFO.
    Box lfoGUI = new Box(BoxLayout.Y_AXIS);
    lfoGUI.setBorder(BorderFactory.createTitledBorder("LFO"));
    LFO lfo = new LFO();
    modules.add(lfo);

    //Its rate should be a dial multiplied by 0.001 (use Mul) to slow it down
    Dial RateDial = new Dial(1.0);
    lfoGUI.add(RateDial.getLabelledDial("Rate"));
    Mul rateMul = new Mul();
    modules.add(rateMul);
    rateMul.setInput(RateDial.getModule());
    rateMul.setMultiplier(new Constant(0.001));
    lfo.Rate = rateMul;

    // First we'll make the Pitch effect:
    // Make a Mul whose input is the LFO, multiplied by a "pitch" dial.
    Dial PitchDial = new Dial(1.0);
    lfoGUI.add(PitchDial.getLabelledDial("Pitch"));
    Mul pitchMul = new Mul();
    modules.add(pitchMul);
    pitchMul.setInput(lfo);
    pitchMul.setMultiplier(PitchDial.getModule());

    // Make another Mul whose input is the previous Mul multiplied by 0.001 so the pitch doesn't go nuts
    Mul noNutsMul = new Mul();
    modules.add(noNutsMul);
    noNutsMul.setInput(pitchMul);
    noNutsMul.setMultiplier(new Constant(0.001));

    // Set this final Mul as the adder() for the Add so it affects the organ's pitch
    add.setAdder(noNutsMul);

    // Next we'll make the volume effect:
    // Make yet another Mul, with the LFO as input, and an "Amplitude" dial as multiplier.
    Dial AmplitudeDial = new Dial(1.0);
    lfoGUI.add(AmplitudeDial.getLabelledDial("Amplitude"));
    Mul ampMul = new Mul();
    modules.add(ampMul);
    ampMul.setInput(lfo);
    ampMul.setMultiplier(AmplitudeDial.getModule());

    // Make another Add, with the Mul as the adder(), and that's it.  No input.  (thus it's 1.0 + Mul)
    Add tAdd = new Add();
    tAdd.setAdder(ampMul);
    modules.add(tAdd);

    // Make an amplifier BBB with the first amplifier AAA as input and this new Add as modulation to flutter the overall volume.
    Amplifier BBB = new Amplifier();
    BBB.setInput(AAA);
    BBB.setAmplitudeMod(tAdd);
    modules.add(BBB);

    // layout for osciloscope output
    Box outputBox = new Box(BoxLayout.Y_AXIS);
    outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
    Dial dial = new Dial(1.0);
    outputBox.add(dial.getLabelledDial("Gain"));

    Amplifier gain = new Amplifier();
    gain.setInput(BBB);
    gain.setAmplitudeMod(dial.getModule());
    modules.add(gain);

    // Add an Oscillocope
    // Set the output to the gain amplifier (or oscilloscope)
    Oscilloscope oscope = new Oscilloscope();
    Oscilloscope.OModule omodule = oscope.getModule();
    modules.add(omodule);
    omodule.setAmplitudeModule(gain);
    outputBox.add(oscope);

    //setup gui
    Box adder = new Box(BoxLayout.Y_AXIS);
    adder.add(adsr.getGUI());
    Options presets = lfo.getOptions();
    lfoGUI.add(presets);
    adder.add(lfoGUI);
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

