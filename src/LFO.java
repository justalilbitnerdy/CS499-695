import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.HashMap;

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
  private Box GUI;
  private Dial RateDial;
  private Module Rate;
  private Dial PitchDial;
  private Module Pitch;
  private Dial AmplitudeDial;
  private Module Amplitude;

  public LFO(){
    super();
    buildGUI();
    Rate = RateDial.getModule();
    Pitch = PitchDial.getModule();
    Rate = RateDial.getModule();
  }
  public int getType(){
          return type;
    // implement me
  }

  public void setType(int type) {
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
      // Implement Me
    return 0.0;
  }

  public Box getGUI(){
    return GUI;
  }

  private void buildGUI(){
    GUI = new Box(BoxLayout.Y_AXIS);
    GUI.setBorder(BorderFactory.createTitledBorder("LFO"));
    // build dials for Rate, Pitch, and Amplitude
    RateDial = new Dial(1.0);
    GUI.add(RateDial.getLabelledDial("Rate"));

    PitchDial = new Dial(1.0);
    GUI.add(PitchDial.getLabelledDial("Pitch"));

    AmplitudeDial = new Dial(1.0);
    GUI.add(AmplitudeDial.getLabelledDial("Amplitude"));

    GUI.add(new JLabel("Waves"));
    JComboBox<String> presets = new JComboBox<String>(TYPES);
    presets.addItemListener((ItemListener) new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        @SuppressWarnings("unchecked")
        JComboBox<String> tBox = (JComboBox<String>)itemEvent.getSource();
        switch(tBox.getSelectedItem().toString()) {
          case  "Sine":
            type = 0;
            break;
          case "Ramp":
            type = 1;
            break;
          case "Sawtooth":
            type = 2;
            break;
          case "Square":
            type = 3;
            break;
          case "Triangle":
            type = 4;
        }
      }
    });

    GUI.add(presets);
  }

}