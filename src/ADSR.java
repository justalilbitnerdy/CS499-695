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
     int state = START;

     Module attackLevel = new Constant(1.0);
     Module attackTime = new Constant(0.01);
     Module decayTime = new Constant(0.5);
     Module sustainLevel = new Constant(1.0);
     Module releaseTime = new Constant(0.01);
     Module gate = new Constant(0);

    // You should find these handy
    double starttime = 0;
    double endtime = 0;
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
        attackTime = AttackDial.getModule();
        decayTime = DecayDial.getModule();
        releaseTime = ReleaseDial.getModule();
    }

    public double tick(long tickCount) {
        return 0.0;
	// IMPLEMENT ME
    }

    public Box getGUI(){
        return GUI;
      }

      private void buildGUI(){
        GUI = new Box(BoxLayout.Y_AXIS);
        GUI.setBorder(BorderFactory.createTitledBorder("Env"));
        // build dials for Attack, Decay, Sustain and Release
        AttackDial = new Dial(1.0);
        GUI.add(AttackDial.getLabelledDial("Attack"));

        DecayDial = new Dial(1.0);
        GUI.add(DecayDial.getLabelledDial("Decay"));

        SustainDial = new Dial(1.0);
        GUI.add(SustainDial.getLabelledDial("Sustain"));

        ReleaseDial = new Dial(1.0);
        GUI.add(ReleaseDial.getLabelledDial("Release"));
      }
}