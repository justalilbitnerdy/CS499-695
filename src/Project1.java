import javax.swing.*;
import java.awt.*;


public class Project1 extends Synth{
  public static void main(String[] args){
    Synth.run(new Project1(), args);
  }

  public void setup(){
    // Implement me:

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
    //layout for osciloscope output
    Box outputBox = new Box(BoxLayout.Y_AXIS);
    outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
    outer.add(outputBox);
    Dial dial = new Dial(1.0);
    outputBox.add(dial.getLabelledDial("Gain"));

    // Build a Hammond Organ with dials and options
    Hammond ham = new Hammond();
    ham.setFrequencyMod(midimod);
    modules.add(ham);

    // Build an Output
    Box hammondBox = new Box(BoxLayout.Y_AXIS);
    hammondBox.setBorder(BorderFactory.createTitledBorder("Hammond"));
    outer.add(hammondBox);

    //build gui for Hammond
    for(int i= 1;i<=8;i++){
      hammondBox.add((new Dial(i)).getLabelledDial(Integer.toString(i)));
    }
    String[] names = new String[Hammond.PRESETS.length];
    for(int i=0;i<Hammond.PRESETS.length;i++){
      names[i] = (String) Hammond.PRESETS[i][0];
      //Multiple presets have the same name as others, Do not uncomment for fear
      // of immense confusion!!!
      //trim the leading numbers from the names
      //names[i] = names[i].split(" ")[1];
    }
    JComboBox<String> presets = new JComboBox<String>(names);

    hammondBox.add(presets);

		// Build a Gate amplifier
		Amplifier amp = new Amplifier();
    amp.setInput(ham);
    amp.setAmplitudeMod(gate);
		modules.add(amp);

    // Add a Gain amplifier
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

    // Set the oscilloscope's output as our audio output.
    // We could have also set the gain module output as our audio output as well, doesn't matter.
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

