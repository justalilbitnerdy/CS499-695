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
      // Build the oscillators
      // Build dials for the amplitude of each of the oscillators, plus
      //       the pulse width of the square and triangle
      // Put them in a box and put it in the window
      // Build a mixer for the various oscillators
      
      // Build an ADSR
      ADSR adsr = new ADSR();
      adsr.setGate(gate);
      // Build a VCA controlled by the ADSR which gets its input from the mixer
      // Make an output box
      modules.add(adsr);
      // Add a Gain amplifier
      Box outputBox = new Box(BoxLayout.Y_AXIS);
      outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
      Dial dial = new Dial(1.0);
      outputBox.add(dial.getLabelledDial("Gain"));

      Amplifier gain = new Amplifier();
      gain.setInput(adsr);
      gain.setAmplitudeMod(dial.getModule());
      modules.add(gain);

      // Add an oscilloscope
      Oscilloscope oscope = new Oscilloscope();
      Oscilloscope.OModule omodule = oscope.getModule();
      modules.add(omodule);
      omodule.setAmplitudeModule(gain);
      outputBox.add(oscope);
      // Set the output to the gain amplifier (or oscilloscope)
      setOutput(omodule);

      //setup gui
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

