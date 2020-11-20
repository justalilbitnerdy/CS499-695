import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

/**
 * This class is attached to Midi and updates a variety of MIDI 
 
 a class which does a lot of midi message processing
 * It sets a few values internally which are useful to extract in other modules
 * By default, as a module, it outputs the pitch (with bend) as a value from 0 to 1
 **/
 
public class MidiModule extends Module 
    {
    static final double BASE = Math.pow(2, 1 / 12.0);
    private Midi midi;
    private int lastnote = 0;
    private double pitch;
    private double velocity;
    private double bend = 1.0;
    private double rawBend;
    private double aftertouch = 0.5;
    private double[] cc = new double[128];
    private boolean[] newcc = new boolean[128];
    private boolean[] newcc_zero = new boolean[128];

    public MidiModule(Midi midi) { this.midi = midi; }

    public double getBend() { return bend; }
        
    public double getPitch() { return pitch; }

    public double getVelocity() { return velocity; }

    public double getGate() { return velocity > 0 ? 1 : 0; }

	public double getAftertouch() { return aftertouch; }
	
	public double getCC(int param) { return cc[param]; }
	
	public boolean isCCNew(int param) { return newcc[param]; }

    // Processes a PITCH BEND message.
    void processPitchBend(ShortMessage sm) 
        {
        int lsb = sm.getData1();
        int msb = sm.getData2();

        // Linux Java distros have a bug: pitch bend data is treated
        // as a signed two's complement integer, which is wrong, wrong, wrong.
        // So we have to special-case it here. See:
        //
        // https://bugs.openjdk.java.net/browse/JDK-8075073
        // https://bugs.launchpad.net/ubuntu/+source/openjdk-8/+bug/1755640

        if (Utils.isUnix()) 
            {
            if (msb >= 64) 
                {
                msb = msb - 64;
                } 
            else 
                {
                msb = msb + 64;
                }
            }

        int rawBend = (lsb + msb * 128) - 8192;

        if (rawBend < -8191) 
            {
            rawBend = -8191;
            }

        // The 24 at the end is 2 octaves 
        this.rawBend = rawBend / 8191.0 * 2;
        bend = Utils.hybridpow(2.0, rawBend / 8191.0 * 2);
        }

    private class Note 
        {
        public int note, vel;

        public Note(int note, int vel)
            {
            this.note = note;
            this.vel = vel;
            }

        }

    ArrayList<Note> on_notes = new ArrayList();

    public double tick(long tickCount) 
        {
        MidiMessage[] messages = midi.getNextMessages();
        boolean changes = false;
        for (MidiMessage message : messages) 
            {
            if (message instanceof ShortMessage) 
                {
                changes = true;
                ShortMessage sm = (ShortMessage) message;
                switch (sm.getCommand()) 
                    {
                    case ShortMessage.NOTE_ON:
                        {
                        final int note = sm.getData1();
                        final int vel = sm.getData2();
                        on_notes.removeIf(n -> n.note == note);
                        if(vel != 0)
                            {
                            on_notes.add(new Note(note, vel));
                            }
                        break;
                        }
                    case ShortMessage.NOTE_OFF:
                        {
                        final int note = sm.getData1();
                        on_notes.removeIf(n -> n.note == note);
                        break;
                        }
					case ShortMessage.PITCH_BEND:
						{
						processPitchBend(sm);
						}
						break;
					case ShortMessage.CONTROL_CHANGE:
						{
						int param = sm.getData1();
						int val = sm.getData2();
						cc[param] = val / 127.0;	// so it ranges 0...1
						newcc[param] = true;
						}
						break;
					case ShortMessage.CHANNEL_PRESSURE:
						{
						aftertouch = sm.getData1() / 127.0;		// so it ranges 0...1
						}
						break;
                    default:
                        {
                        break;
                        }
                    }
                }
            }
            
        if(changes) 
            {
            if(on_notes.size() == 0) 
                {
                velocity = 0;
                } 
            else 
                {
                Note n = on_notes.get(on_notes.size()-1);
                int note = n.note;
                int vel = n.vel;
                velocity = vel / 127.0;
                pitch = Utils.hzToValue(440.0 * Math.pow(BASE, note - 69) * bend);
                }
            }

        return pitch;
        }

    
    }
