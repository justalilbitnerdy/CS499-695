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
    static final int WAIT_TIME = 2*(int)Config.SAMPLING_RATE;
    int stage = START;
    double state = 0.0;

    Module attackLevel = new Constant(1.0);
    Module attackTime = new Constant(0.01);
    Module decayTime = new Constant(0.5);
    Module sustainLevel = new Constant(1.0);
    Module releaseTime = new Constant(0.01);
    Module gate = new Constant(0);

    // You should find these handy
    double starttime = 0;
    double endtime = Double.POSITIVE_INFINITY;;
    double startlevel = 0;
    double endlevel = 0;
    private Box GUI;
    private Dial AttackDial;
    private Dial AttackLevelDial;
    private Dial DecayDial;
    private Dial SustainDial;
    private Dial ReleaseDial;
    private String name;

    public double getAttackTime() { return attackTime.getValue(); }
    public void setAttackTime(Module attackTime) { this.attackTime = attackTime; }
    public double getAttackLevel() {
        // Don't let the attack level be less than the sustain
        return Math.max(attackLevel.getValue(), sustainLevel.getValue());
    }
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
        this.name = "Env";
        buildGUI();
        setAttackTime(AttackDial.getModule());
        setAttackLevel(AttackLevelDial.getModule());
        setDecayTime(DecayDial.getModule());
        setSustainLevel(SustainDial.getModule());
        setReleaseTime(ReleaseDial.getModule());
    }
    public ADSR(String name){
      super();
      this.name = name;
      buildGUI();
      setAttackTime(AttackDial.getModule());
      setAttackLevel(AttackLevelDial.getModule());
      setDecayTime(DecayDial.getModule());
      setSustainLevel(SustainDial.getModule());
      setReleaseTime(ReleaseDial.getModule());
  }

  public ADSR(Module AttackTime,
              Module AttackLevel,
              Module Decay,
              Module Sustain,
              Module Release){
    super();
    setAttackTime(AttackTime);
    setAttackLevel(AttackLevel);
    setDecayTime(Decay);
    setSustainLevel(Sustain);
    setReleaseTime(Release);
}

public double tick(long tickCount) {
    //setup for next stage if required
    switch (stage){
      case START:
        // if I should transition to ATTACK
        if(getGate()>0){
          //   set the start time for the ATTACK period
          starttime = tickCount;
          //   set the end time for the ATTACK period
          endtime = starttime + WAIT_TIME * getAttackTime();
          //   set the start level for the ATTACK period
          startlevel = getValue();
          //   set the end level for the ATTACK period
          endlevel = getAttackLevel();
          //   transition to ATTACK
          stage = ATTACK;
        }
        break;
      case ATTACK:
        if(tickCount>=endtime){
          // if I should transition to DECAY
          starttime = tickCount;
          //   set the end time for the DECAY period
          endtime = starttime + WAIT_TIME * getDecayTime();
          //   set the start level for the DECAY period
          startlevel = getValue();
          //   set the end level for the DECAY period
          endlevel = sustainLevel.getValue();
          //   transition to DECAY
          stage = DECAY;
        }
        //switch to release if required. Split this out since it is duplicated
        //a lot
        switchToReleaseIfNeeded(tickCount);
        break;
      case DECAY:
        // if I should transition to SUSTAIN
        if(tickCount>=endtime){
          //   set the start time for the SUSTAIN period
          starttime = tickCount;
          //   set the end time for the SUSTAIN period
          endtime = Double.POSITIVE_INFINITY;
          //   set the start level for the SUSTAIN period
          startlevel = getValue();
          //   set the end level for the SUSTAIN period
          endlevel = getSustainLevel();
          //   transition to SUSTAIN
          stage = SUSTAIN;
        }
        //switch to release if required. Split this out since it is duplicated
        //a lot
        switchToReleaseIfNeeded(tickCount);
        break;
      case SUSTAIN:
        //switch to release if required. Split this out since it is duplicated
        //a lot
        switchToReleaseIfNeeded(tickCount);
        break;
      case RELEASE:
        // if I should transition to START
        if(tickCount>=endtime){
          //   set the start time for the START period
          starttime = tickCount;
          //   set the end time for the START period
          endtime = Double.POSITIVE_INFINITY;
          //   set the start level for the START period
          startlevel = 0;
          //   set the end level for the START period
          endlevel = 0;
          //   transition to START
          stage = START;
        }
        //Stole from start, since if you attack during release it should still
        // increase instead of waiting till the end.
        // if I should transition to ATTACK
        if(getGate()>0){
          //   set the start time for the ATTACK period
          starttime = tickCount;
          //   set the end time for the ATTACK period
          endtime = starttime + WAIT_TIME * getAttackTime();
          //   set the start level for the ATTACK period
          startlevel = getValue();
          //   set the end level for the ATTACK period
          endlevel = getAttackLevel();
          //   transition to ATTACK
          stage = ATTACK;
        }
        break;
    }
    // check for divide by zero, I don't think this will ever get hit... again
    // It certainly got hit a lot during testing
    if(starttime >=endtime){
      return endlevel;
    }
    // given the current start time, end time, start level, end level,
    //      and current time, compute and return the current level.
    //      This is just linear interpolation.  Consider
    //      situations where you might divide by zero and handle
    //      those appropriately.  I'd always set the start level
    //      to be whatever level you're currently at.
    return Utils.lerp(startlevel,endlevel,4.0,tickCount,starttime,endtime);
  }

  private void switchToReleaseIfNeeded(double tickCount){
    if(getGate()<1){
      //   set the start time for the RELEASE period
      starttime = tickCount;
      //   set the end time for the RELEASE period
      endtime = starttime + WAIT_TIME * getReleaseTime();
      //   set the start level for the RELEASE period
      startlevel = getValue();
      //   set the end level for the RELEASE period
      endlevel = 0;
      //   set the end level for the RELEASE period
      stage = RELEASE;
    }
  }
    public Box getGUI(){
        return GUI;
      }

    private void buildGUI(){
      GUI = new Box(BoxLayout.Y_AXIS);
      GUI.setBorder(BorderFactory.createTitledBorder(name));
      // build dials for Attack, Decay, Sustain and Release
      AttackDial = new Dial(1.0);
      GUI.add(AttackDial.getLabelledDial("Attack Time"));
      AttackDial.update(.1);

      AttackLevelDial = new Dial(1.0);
      GUI.add(AttackLevelDial.getLabelledDial("Attack Level"));

      DecayDial = new Dial(1.0);
      GUI.add(DecayDial.getLabelledDial("Decay Time"));
      DecayDial.update(0);

      SustainDial = new Dial(1.0);
      GUI.add(SustainDial.getLabelledDial("Sustain"));

      ReleaseDial = new Dial(1.0);
      GUI.add(ReleaseDial.getLabelledDial("Release Time"));
      ReleaseDial.update(.1);
    }
}