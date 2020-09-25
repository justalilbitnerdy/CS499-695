import javax.swing.*;
// Copyright 2018 by George Mason University
// Licensed under the Apache 2.0 License


/**
   A superclass for oscillators.  This basic class performs a simple ramp-style oscillation 
   from 0...1 through the period of the wave.  You could use the tick value to compute more
   useful oscillation functions in subclasses.
*/

public class LFO extends Osc {
  public static final String[] TYPES = new String[] { "Sine", "Ramp", "Sawtooth", "Square", "Triangle"};
  public static final int LFO_TYPE_SINE = 0;
  public static final int LFO_TYPE_RAMP = 1;
  public static final int LFO_TYPE_SAW = 2;
  public static final int LFO_TYPE_SQUARE = 3;
  public static final int LFO_TYPE_TRIANGLE = 4;
  int type = LFO_TYPE_SINE;
  Dial RateDial;
  Dial PitchDial;
  Module Pitch;
  Dial AmplitudeDial;
  Module Amplitude;
  private Sine sine;

  public LFO(){
    sine = new Sine();
  }

  public int getType(){
    return type;
  }

  public synchronized void setType(int type) {
      this.type = type;
  }

  public Options getOptions() {
    return new Options("Types", TYPES, 0) {
      public void update(int val) {
          // this is a race condition but whatever
          setType(val);
      }
    };
  }

  public double tick(long tickCount){
    // Get new ramp value used in all but sine.
    // Not a efficient here but more readable.
    double ramp = super.tick(tickCount);
    switch(type){
      case LFO_TYPE_SINE:
        //update sine, but only when our type is sine for efficiency.
        return sine.tick(tickCount);
      case LFO_TYPE_SAW:
        return 1-ramp;
      case LFO_TYPE_SQUARE:
        // I guess square waves are supposed to start at 1, no idea though.
        // does it matter if we can't hear a phase shift?
        return ramp>.5?0:1;
      case LFO_TYPE_TRIANGLE:
        return ramp<.5?ramp*2:-ramp*2+2;
      default:
        return ramp;
    }
  }
}