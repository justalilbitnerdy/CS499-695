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

    public double tick(long tickCount) 
    {
	// IMPLEMENT ME
    }
