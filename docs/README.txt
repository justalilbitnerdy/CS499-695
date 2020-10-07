PROJECT 2 (Part 1).  Oscillators using BLIT.   You will create several BLIT-based oscillators, mix them,
and feed them through an ADSR-based amplifier. You'll reuse your previous ADSR.  To do this you will:

	1. Build a module class called Blit.java
	2. Build a module class called BPBlit.java
	3. Build a module class called BlitSquare.java
	4. Build a module class called BlitTriangle.java
	5. Build a module class called BlitSaw.java
	6. Modify the Project2 setup() method

This is PART 1 of the project.  PART 2 will introduce a filter and possibly an additional ADSR.


RUNNING THE PROVIDED CODE

	1. If you are running on a Mac, you must put the library coremidi4j-1.1.jar in your /Library/Java/Extensions/	directory.    

	2. You run the code with no arguments...
		java Synth
	... to get the list of MIDI devices and audio devices.

	3. Then you run the code with TWO arguments (the MIDI and audio device you have chosen) to fire things up:
		java Project2 2 1		[for example]


STRUCTURE

	Various BlitFoo classes rely on the output of their superclasses.
	They should not call super.tick(), but rather super.bpblit() or
			super.blit() or super.blitsquare() or whatnot.
	super.tick() is called if you want the output of a given class as a final result.

	I have provided you with a new class called Mixer.java which mixes together
	some N sources weighted by amplitudes.  You will find it useful.

