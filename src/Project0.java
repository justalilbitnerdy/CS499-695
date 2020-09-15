/*
  Copyright 2020 by Sean Luke and Bryan Hoyle
  Please see us for licensing beyond use in CS 499 / 695
*/


import javax.swing.*;
import java.awt.*;


/**
   Project0 sublasses Synth and overrides setup(),
   where you create your module chain, GUI widgets, and set them up.
*/

public class Project0 extends Synth
    {
    public static void main(String[] args)
        {
        Synth.run(new Project0(), args);
        }

    public void setup()
        {
        // Add a MidiModule to the chain.  This module outputs the current note as a frequency mapped to a modulation value.
        // See Utils.java for information about mapping the frequency to modulation.
        MidiModule midimod = new MidiModule(getMidi());
        modules.add(midimod);

        // Add a MidiGate to the chain, using the MidiModule.  This module outputs a 1 or a 0 as its modulation
        // depending on whether a note is being pressed or not.
        MidiGate gate = new MidiGate(midimod);
        modules.add(gate);



        /////// SINE EXAMPLE

        // You modify this.
        // 1. Make a Sine called "osc1"
        Sine osc1 = new Sine();
        // 2. Set its frequency mod to midimod so its fequency changes with the pitch of the current MIDI note
        osc1.setFrequencyMod(midimod);
        // 3. Register it with the modules
        modules.add(osc1);
        // Add a Sine.  Set its frequency to be the frequency specified by the MidiMod



        /////// END SINE EXAMPLE

        // Create an Amplifier and add it to the modules.  The amplifier is fed by the Sine.
        // We'll use it to turn the sound entirely ON or OFF by having its amplitude modulation
        // determined by the MidiGate.
        Amplifier amp = new Amplifier();
        amp.setInput(osc1);
        amp.setAmplitudeMod(gate);
        modules.add(amp);


        // Create our basic GUI.  This consists of a window (JFrame) containing a Box (an object which lays out
        // widgets horizontally or vertically -- in this case, horizontally) as its primary content pane.
        JFrame frame = new JFrame();
        Box outer = new Box(BoxLayout.X_AXIS);
        frame.setContentPane(outer);

        // Next, we'll make another box and stick it in the first box.  This second box lays stuff out
        // vertically.  We'll give it a nice border.  This way we can add additional vertical boxes
        // later on in other projects.
        Box outputBox = new Box(BoxLayout.Y_AXIS);
        outputBox.setBorder(BorderFactory.createTitledBorder("Output"));
        outer.add(outputBox);



        // Let's make a dial.  It'll control the overall volume.  Initially it's 1.0 (maximum volume)
        // Notice that we're not adding the Dial's module to the module chain because it really doesn't
        // need to be ticked (but maybe that's bad style).
        Dial dial = new Dial(1.0);
        outputBox.add(dial.getLabelledDial("Gain"));
        
        // Create ANOTHER amplifier.  This one we attach to the dial so we can control the
        // overall volume of the input signal (from the previous amplifier) via the dial's
        // modulation.
        Amplifier gain = new Amplifier();
        gain.setInput(amp);
        gain.setAmplitudeMod(dial.getModule());
        modules.add(gain);
        
        // Build an Oscilloscope.  We'll feed the signal into its module.  Unlike the Dial, this one
        // has to have its module added to the chain so it's properly updated with data.  Add the
        // oscilloscope to the box so we can see it.
        Oscilloscope oscope = new Oscilloscope();
        Oscilloscope.OModule omodule = oscope.getModule();
        modules.add(omodule);
        omodule.setAmplitudeModule(gain);
        outputBox.add(oscope);
 
        // Set the oscilloscope's output as our audio output.
        // We could have also set the gain module output as our audio output as well, doesn't matter.
        setOutput(omodule);
       
        // Pack our window, which causes it to properly size all its widgets according to their desired
        // sizes.  Then display it.
        frame.pack();
        frame.setVisible(true);
        }
        
    }

