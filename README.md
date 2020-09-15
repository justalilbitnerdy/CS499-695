PROJECT 1: Hello World. You will create a small synthesizer which only outputs a sine wave.

This project has only three tasks:

	0. Familiarize yourself with all the classes provided, and particularly with how
	   Project0.java works.
	1. Build a subclass of Oscillator called Sine.java which outputs a sine wave of
	   the given frequency.
	2. In Project0.java, tie a Sine module into your synthesizer.


RUNNING THE DEMO

The demo is jar/project0.jar

1. If you are running on a Mac, you must put the library coremidi4j-1.1.jar in your /Library/Java/Extensions/  directory.

2. You can run the code as 

	java -jar jar/project0.jar
        ... to get the list of MIDI devices and audio devices.

3. Then you run the code with TWO arguments (the MIDI and audio device you have chosen) to fire things up:

	java -jar jar/project0.jar 2 1		[for example]



STRUCTURE
	SYNTH.java:	This is the abstract superclass of synthesizers.

	PROJECT0.java:	Your synthesizer, a subclass of SYNTH.java.  This class overrides a method called setup(...), which does two things.  (1) It sets up the GUI widgets, some of which are tied to a few modules, and (2) it creates all the modules, registers them with the synthesizer, and hooks them together.

	MODULE.java:	This is the abstract superclass of all modules.  The synthesizer is organized as an array of MODULES, one of which has been designated as the OUTPUT MODULE.  Each module outputs a single VALUE between 0 and 1.  Some modules may use this VALUE to output a current sound wave position; others use it to output a modulation value.  Each time the synthesizer needs to write out another sample, it will call the method TICK on each module in order, first to last.  In the TICK method you update your module's internal state, then return its latest value.  You can access another module's current value (which had been updated in TICK) by calling the method GETVALUE().

	MIDI.java: This class handles MIDI input to the SYNTH.  You won't touch it for now.

	CONFIG.java: This class specifies configuration constants, such as the sampling rate, etc.

	UTILS.java: Various utilities you will find useful.

The various provided modules are:

	OSC.java: A subclass of Module which adds an additional input (module) to provide frequency.  Used as the superclass for oscillators of all types.  By default, Osc.java just outputs a ramp wave going smoothly from 0...1.

	CONSTANTVALUE.java: a MODULE which always outputs a constant number.

	MIDIMODULE.java: a MODULE which outputs the pitch of the current note.  You can determine the volume (velocity) of the note by calling GETVELOCITY().  And you can determine if a note is *pressed* by calling GETGATE()

	MIDIGATE.java: a useful little MODULE which returns 1 if a note is pressed, else 0.  It does this by just calling GETGATE on the MIDIMODULE.

	AMPLIFIER.java: a MODULE which outputs its input with its volume modified according to a modulation.

	OSCILLOSCOPE.java: a GUI widget which displays a wave. It does this by containing within it a MODULE that you can extract and hook up to your synth module chain.

	DIAL.java: a GUI widget which lets you change a dial value with a mouse.  This widget contains a MODULE that you can extract and hook up to your synth module chain, and this module outputs the latest dial value.




WHAT YOU MUST BUILD:

0. Create a module called Sine.java, a subclass of Osc.java, which outputs a Sine wave.

1. Modify Project0.java to tie Sine.java into the synthesizer.

You must return a zip file containing the project0 directory (please zip the directory, NOT jut the contents of the directory).  This project0 directory should contain the new Sine.java file and the modified Project0.java file, plus the relevant other classes.

You should document your classes and changes properly and professionally.

When your code is done, you can run it from the Project0 class file:

        1. If you are running on a Mac, you must put the library coremidi4j-1.1.jar in your /Library/Java/Extensions/  directory.

        2. You run the code with no arguments...
                java Project0
        ... to get the list of MIDI devices and audio devices.

        3. Then you run the code with TWO arguments (the MIDI and audio device you have chosen) to fire things up:
                java Project0 2 1          [for example]