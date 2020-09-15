/**
	A small module which outputs 1 when a note is down and 0 when the note is up. 
*/

public class MidiGate extends Module 
    {
    MidiModule midiModule;

    public MidiGate(MidiModule midiModule) { this.midiModule = midiModule; }

    public double tick(long tickCount) 
        {
        return this.midiModule.getGate();
        }
    }
