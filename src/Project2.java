import javax.swing.*;
import java.awt.*;


public class Project2 extends Synth {
  public static void main(String[] args) {
    Synth.run(new Project2(), args);
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

    // Build a mixer for the various oscillators
    BlitMixer blitMixer = new BlitMixer();
    //add modules inside blitMixer
    for(Module m:blitMixer.getModules()){
      modules.add(m);
      ((Osc)m).setFrequencyMod(midimod);
    }
    modules.add(blitMixer);

    /// NEW IN PROJECT 2.5*****************************************************************************
	  // Build an ADSR for the filter
    // Build dials for the ADSR and put it in a box
    ADSR filterADSR = new ADSR("Filter Env");
    filterADSR.setGate(gate);
    modules.add(filterADSR);
    FilterSwitcher filterSwitcher = new FilterSwitcher();
    //add modules inside filterSwitcher
    for(Module m:filterSwitcher.getModules()){
      // Build a Mul to multiply the cutoff by the ADSR output
      Mul filterMul = new Mul();
      filterMul.setInput(filterADSR);
      modules.add(filterMul);

      //I don't really want to rewrite Filter, so this sucks but I mean... it works?
      if(m instanceof FilterI){
        ((FilterI)m).setInput(blitMixer);
        // Feed the Mul as the cutoff for a LOW PASS FILTER (also feed in the resonance)
        filterMul.setMultiplier(((FilterI)m).getCutoffDial().getModule());
        ((FilterI)m).setFrequencyMod(filterMul);
      }
      modules.add(m);
    }
    modules.add(filterSwitcher);

    /// END NEW IN PROJECT 2.5************************************************************************

    // Build an ADSR for the VCA
    ADSR adsr = new ADSR("Amp Env");
    adsr.setGate(gate);
    modules.add(adsr);

    // Build a VCA controlled by the ADSR which gets its input from the FILTER <--- NOTE CHANGE
    Amplifier VCA = new Amplifier();
    VCA.setInput(filterSwitcher);
    VCA.setAmplitudeMod(adsr);
    modules.add(VCA);

    // Make an output box
    Box outputBox = new Box(BoxLayout.Y_AXIS);
    outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
    Dial dial = new Dial(1.0);
    outputBox.add(dial.getLabelledDial("Gain"));

    // Add a Gain amplifier
    Amplifier gain = new Amplifier();
    gain.setInput(VCA);
    gain.setAmplitudeMod(dial.getModule());
    modules.add(gain);

    // Add an oscilloscope
    Oscilloscope oscope = new Oscilloscope();
    //from that one lecture where he said to do this. To somebody else.
    oscope.setDelay(1);
    Oscilloscope.OModule omodule = oscope.getModule();
    modules.add(omodule);
    omodule.setAmplitudeModule(gain);
    outputBox.add(oscope);
    // Set the output to the gain amplifier (or oscilloscope)
    setOutput(omodule);

    //setup gui
    outer.add(blitMixer.getGUI());
    outer.add(filterADSR.getGUI());
    outer.add(filterSwitcher.getGUI());
    outer.add(adsr.getGUI());
    outer.add(outputBox);

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

