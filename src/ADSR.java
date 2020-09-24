import javax.sound.midi.SysexMessage;
import javax.swing.*;
// Copyright 2018 by George Mason University
// Licensed under the Apache 2.0 License

/**
	A TIME-based ADSR.  You can make this linear or pseudo-exponential (I'd start with linear).
*/

public class ADSR extends Module
{
     static final int START = 0;
     static final int ATTACK = 1;
     static final int DECAY = 2;
     static final int SUSTAIN = 3;
     static final int RELEASE = 4;
//     int state = START;
    int stage = START;
    double state = 0.0;
    private double gamma;

     Module attackLevel = new Constant(1.0);
     Module attackTime = new Constant(0.01);
     Module decayTime = new Constant(0.5);
     Module sustainLevel = new Constant(1.0);
     Module releaseTime = new Constant(0.01);
     Module gate = new Constant(0);

     Module[] stageTimes = new Module[5]; // Conveniently indexed reference of endtimes

    // You should find these handy
    double starttime = 0;
    double endtime = Double.POSITIVE_INFINITY;
    double startlevel = 0;
    double endlevel = 0;
    private Box GUI;
    private Dial AttackDial;
    private Dial DecayDial;
    private Dial SustainDial;
    private Dial ReleaseDial;

    public double getAttackTime() { return attackTime.getValue(); }
    public void setAttackTime(Module attackTime) { this.attackTime = attackTime; }
    public double getAttackLevel() { return attackLevel.getValue(); }
    public void setAttackLevel(Module attackLevel) { this.attackLevel = attackLevel; }
    public double getDecayTime() { return decayTime.getValue(); }
    public void setDecayTime(Module decayTime) { this.decayTime = decayTime; }
    public double getSustainLevel() { return sustainLevel.getValue(); }
    public void setSustainLevel(Module sustainLevel) { this.sustainLevel = sustainLevel; }
    public double getReleaseTime() { return releaseTime.getValue(); }
    public void setReleaseTime(Module releaseTime) { this.releaseTime = releaseTime; }
    public double getGate() { return gate.getValue(); }
    public void setGate(Module gate) { this.gate = gate; }

    public ADSR(){
        super();
        buildGUI();
        stageTimes[START] = new Constant(Double.POSITIVE_INFINITY);
        setAttackTime(AttackDial.getModule());
        stageTimes[ATTACK] = this.attackTime;
        // Should set attack level here when we get a knob for it
        setDecayTime(DecayDial.getModule());
        stageTimes[DECAY] = this.decayTime;
        setSustainLevel(SustainDial.getModule());
        stageTimes[SUSTAIN] = new Constant(Double.POSITIVE_INFINITY);
        setReleaseTime(ReleaseDial.getModule());
        stageTimes[RELEASE] = this.releaseTime;
    }

    public double tick(long tickCount) {
	// IMPLEMENT ME
        System.out.println("Start: " + Double.toString(startlevel));
        System.out.println("Stage: " + Integer.toString(stage));
        if (stage == START && getGate() == 1) {
            // A note was pressed
            stage = ATTACK;
            state = 0.0;
            setValue(0.0);
            startlevel = 0.0;
            endlevel = getAttackLevel();
            endtime = stageTimes[ATTACK].getValue();
            System.out.println("PRESSED!");
        } else if (START < stage && stage < RELEASE && getGate() == 0) {
            // A note was released
            stage = RELEASE;
            state = 0.0;
            startlevel = getValue();
            endlevel = 0.0;
            endtime = getReleaseTime();
            System.out.println("RELEASED!");
        }

        state += Config.INV_SAMPLING_RATE;
        while (state >= endtime) {
            state -= endtime;
            startlevel = getValue();
            stage = (stage + 1) % 5;
            endtime = stageTimes[stage].getValue();

            if (stage == ATTACK) endlevel = getAttackLevel();
            else if (stage == DECAY || stage == SUSTAIN) endlevel = getSustainLevel();
            else endlevel = 0.0;
            System.out.println("Stage is now " + Integer.toString(stage));
        }
        gamma = state / endtime;
        if (stage == START || stage == SUSTAIN) gamma = 0.0;
        setValue((1 - gamma) * startlevel + (gamma * endlevel));
//        System.out.println(gamma);
        return state;
    }

    public Box getGUI(){
        return GUI;
      }

      private void buildGUI(){
        GUI = new Box(BoxLayout.Y_AXIS);
        GUI.setBorder(BorderFactory.createTitledBorder("ADSR"));
        // build dials for Attack, Decay, Sustain and Release
        AttackDial = new Dial(1.0);
        GUI.add(AttackDial.getLabelledDial("Attack Time"));

        DecayDial = new Dial(1.0);
        GUI.add(DecayDial.getLabelledDial("Decay Time"));

        SustainDial = new Dial(1.0);
        GUI.add(SustainDial.getLabelledDial("Sustain"));

        ReleaseDial = new Dial(1.0);
        GUI.add(ReleaseDial.getLabelledDial("Release Time"));
      }
}