import javax.swing.*;
import java.awt.*;


public class Project1 extends Synth
	{
    public static void main(String[] args) 
    	{
    	Synth.run(new Project1(), args);
    	}

    public void setup() 
    	{
    	// Implement me:
    	
    	// Build a MidiModule
    	// Build a MidiGate
    	// Create the window
    	
    	// BEFORE THE ORGAN:
 		// Create an Add which takes the MidiMod pitch as input	
 		
    	// Build a Hammond Organ with dials and options.  It now takes the *Add* as its frequency mod
    	
    	// THE ENVELOPE:
    	
    	// Build an ADSR with dials at least.  It should default to sine waves
    	// -- If you were really cool you'd add an options to change the wave type
    	// -- If you were REALLY cool you'd add additional options for random LFOs or Sample and Hold Random LFOs.
    	// Build an amplifier AAA that takes the Hammond as input, modulated by the ADSR
 
 		// THE LESLIE (DOPPLER EFFECT):

    	// Build an LFO.  Its rate should be a dial multiplied by 0.001 (use Mul) to slow it down
    	// First we'll make the Pitch effect:
    	// Make a Mul whose input is the LFO, multiplied by a "pitch" dial.
    	// Make another Mul whose input is the previous Mul multiplied by 0.001 so the pitch doesn't go nuts
    	// Set this final Mul as the adder() for the Add so it affects the organ's pitch
    	// Next we'll make the volume effect:
    	// Make yet another Mul, with the LFO as input, and an "Amplitude" dial as multiplier.
    	// Make another Add, with the Mul as the adder(), and that's it.  No input.  (thus it's 1.0 + Mul)
    	// Make an amplifier BBB with the first amplifier AAA as input and this new Add as modulation to flutter the overall volume.

    	// Build an Output
    	// Build a Gate amplifier.  Its signal now gets fed *amplifier BBB*
    	// Add a Gain amplifier fed the Gate amplifier
    	// Add an Oscillocope
    	// Set the output to the gain amplifier (or oscilloscope)
    	// Pack and display the window
        }
        
    }

