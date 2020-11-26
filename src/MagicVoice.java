import java.lang.reflect.Array;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Collection;

public class MagicVoice {
  private static final float     SAMPLING_RATE = 44100.0f;
  private static final int       SAMPLE_SIZE   = 16;
  private static final int       NUM_CHANNELS  = 2;
  private static final boolean   SIGNED        = true;
  private static final boolean   BIG_ENDIAN    = false;

  /**
   * @param aIsInput Whether or not you want the Mixers that support input
   * @param aFormat  The Audio Format that you wish the Mixer to support
   * @return An array of Mixer.Info that represents the supported Mixers for the
   *         Given Format
   */
  public static Mixer.Info[] getSupportedMixers(Boolean aIsInput,AudioFormat aFormat)
  {
    DataLine.Info lineInfo;
    if(aIsInput){
      lineInfo = new DataLine.Info(TargetDataLine.class, aFormat);
    }else{
      lineInfo = new DataLine.Info(SourceDataLine.class, aFormat);
    }
    Mixer.Info[] info = AudioSystem.getMixerInfo();
    int count = 0;

    for (int i = 0; i < info.length; i++)
        {
        Mixer m = AudioSystem.getMixer(info[i]);
        if (m.isLineSupported(lineInfo))
            {
            count++;
            }
        }

    Mixer.Info[] options = new Mixer.Info[count];
    count = 0;
    for (int i = 0; i < info.length; i++)
        {
        Mixer m = AudioSystem.getMixer(info[i]);
        if (m.isLineSupported(lineInfo))
            options[count++] = info[i];
        }
    for(Mixer.Info MixInfo:options){
      System.out.println(MixInfo.getName());
    }
    return options;
  }



  private static void createAndShowGUI() {
    VoiceChanger vc;
    //Create and set up the window.
    JFrame frame = new JFrame("MagicVoice");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    vc = new VoiceChanger();
    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        AudioFormat format = new AudioFormat( SAMPLING_RATE,
                                              SAMPLE_SIZE,
                                              NUM_CHANNELS,
                                              SIGNED,
                                              BIG_ENDIAN);
        System.out.println("INPUTS------------------------------------------");
        getSupportedMixers(true,format);
        System.out.println("OUTPUTS-----------------------------------------");
        getSupportedMixers(false,format);
        //createAndShowGUI();
      }
    });
  }
}