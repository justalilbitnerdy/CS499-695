PROJECT 1: Additive Synthesis Part 1.  You will create a small MONOPHONIC additive Hammond organ.
			In Part 2 you'll add some modulation, but not now.  To build the organ you will:

	1. Build a module class called Hammond.java
	2. Copy over your Sine.java file from project0
	3. Modify the Project1.java setup() method



NOTES
	A Hammond organ has nine TONE WHEELS which produce sine waves and spin at different frequencies relative to the base frequency of your note
	(that's actually all a lie, it reuses tonewheels to allow for chords etc., but we'll go for it as it's conceptually simple).
	Each tonewheel is associated with a DRAW BAR the musician can pull out to specify the amplitude of the tonewheel's sine wave.
	The draw bar goes 0...8, where 0 is off and 8 is maximally loud.
	Your module should be able to have each of these values set independently or collectively according to one of several presets.
	I provide some presets for you.

	When you play a note, the organ samples its nine tone wheels, multiplies the samples against their drawbar amplitudes, averages them, and returns that.
	Thus your hammond organ module will maintain nine modules within it and must call doUpdate() on the Multipliers and the Tone Wheels it is ticked.

	Note that the nine drawbars are not *entirely* in frequency order.  That's traditional.

	When averaging the sine waves, keep in mind that they are centred around 0.5 as their zero-point.  You'll have to do a tiny bit of math.



RUNNING THE DEMO

The demo is jar/project1.jar

1. If you are running on a Mac, you must put the library coremidi4j-1.1.jar in your /Library/Java/Extensions/  directory.

2. You can run the code as

	java -jar jar/project1.jar
        ... to get the list of MIDI devices and audio devices.

3. Then you run the code with TWO arguments (the MIDI and audio device you have chosen) to fire things up:

	java -jar jar/project1.jar 2 1		[for example]
