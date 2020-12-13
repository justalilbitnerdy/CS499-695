import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.util.HashMap;
import java.lang.Thread;

public class MagicVoice extends JFrame{

  //mixer stuff
  private boolean        isActive = false;
  private boolean        isReady = true;
  private TargetDataLine targetDataLine; // our microphone
  private SourceDataLine sourceDataLine; // out speaker

  //Sample Info
  private float SAMPLE_RATE = 44100; //8000,11025,16000,22050,44100hz
  private int SAMPLE_SIZE_IN_BITS = 16; //8,16
  private int NUMBER_OF_CHANNELS = 1; //1,2
  private boolean SIGNED = true; //true,false
  private boolean BIG_ENDIAN = false; //true,false
  private AudioFormat SAMPLE_FORMAT = new AudioFormat(SAMPLE_RATE,
                                                      SAMPLE_SIZE_IN_BITS,
                                                      NUMBER_OF_CHANNELS,
                                                      SIGNED,
                                                      BIG_ENDIAN);

  private HashMap<String,Mixer.Info> INPUT_MIXERS = new HashMap<>();
  private String                     Input_Mixer;
  private HashMap<String,Mixer.Info> OUTPUT_MIXERS = new HashMap<>();
  private String                     Output_Mixer;

  // this bitmask will allow us to choose what filters we are using
  final private byte                 PITCH_MASK = 1;
  private byte                       activeFilters = PITCH_MASK;

  private double[] decodeSample(int numBytes,byte[] byteSample){
    double[] doubleSample = new double[numBytes/2];
    for(int i=0,j=0;i<numBytes-1;i+=2,j++){
      long longSample;
      if (BIG_ENDIAN) {
          longSample = (((byteSample[i] & 0xffL) << 8) | ((byteSample[i+1] & 0xffL)));
      } else {
          longSample = ((byteSample[i] & 0xffL) | ((byteSample[i + 1] & 0xffL) << 8));
      }
      int bitsToExtend = Long.SIZE - SAMPLE_SIZE_IN_BITS;
      float sample = (longSample << bitsToExtend) >> bitsToExtend;
      doubleSample[j] = sample/32768.0;
    }
    return doubleSample;
  }

  private byte[] encodeSample(double[] doubleSample){
    byte[] byteSample = new byte[doubleSample.length*2];

    for(int i=0,j=0;i<doubleSample.length;i++,j+=2){
      long sample = (long) (doubleSample[i]*32768);
      if (BIG_ENDIAN) {
        byteSample[j]   = (byte) ((sample >>> 8) & 0xffL);
        byteSample[j+1] = (byte) ( sample        & 0xffL);
      } else {
        byteSample[j]   = (byte) ( sample        & 0xffL);
        byteSample[j+1] = (byte) ((sample >>> 8) & 0xffL);
      }
    }
    return byteSample;
  }

  /***
   * Applies any given filter
   * @param bytes
   */
  private void filterAudio(int numBytes,byte[] audioArray){
    //get the double version of the output
    double[] sample = decodeSample(numBytes,audioArray);
    switch(activeFilters){
      case PITCH_MASK:
        //do the things*********************************************************
        break;
    }

    byte[] newByteSample = encodeSample(sample);
    for(int i=0;i<audioArray.length;i++){
      System.out.print(audioArray[i] + " = ");
      System.out.print(newByteSample[i]);
      System.out.println();
    }
    for(int i = 0; i<newByteSample.length;i++){
      audioArray[i] = newByteSample[i];
    }

  }

  @SuppressWarnings("unchecked")
  public MagicVoice(){
    final JToggleButton runningBtn = new JToggleButton("Start");
    runningBtn.setEnabled(true);
    getContentPane().add(runningBtn);
    runningBtn.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JToggleButton btn = (JToggleButton)e.getSource();
          //we should start playing
          if (btn.isSelected()){
            isActive = true;
            btn.setText("Active");
            startCapture();
          }else{
            isActive = false;
            btn.setText("Start");
          }
        }
      }
    );

    //setup both boxes for input and output mixers
    Mixer.Info[] mixerInfo = getSupportedMixers(true, SAMPLE_FORMAT);
    for(int i = 0; i < mixerInfo.length; i++){
      INPUT_MIXERS.put(mixerInfo[i].getName(),mixerInfo[i]);
    }
    mixerInfo = getSupportedMixers(false, SAMPLE_FORMAT);
    for(int i = 0; i < mixerInfo.length; i++){
      OUTPUT_MIXERS.put(mixerInfo[i].getName(),mixerInfo[i]);
    }

    //list of input Mixers
    JComboBox<String> inputList =
        new JComboBox(INPUT_MIXERS.keySet().toArray());
    inputList.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          switchMixer(true,
                      (String)((JComboBox)e.getSource()).getSelectedItem());
        }
      }
    );
    inputList.setSelectedIndex(0);
    getContentPane().add(inputList);

    //list of output Mixers
    JComboBox<String> outputList =
        new JComboBox(OUTPUT_MIXERS.keySet().toArray());
    outputList.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          switchMixer(false,
                      (String)((JComboBox)e.getSource()).getSelectedItem());
        }
      }
    );
    outputList.setSelectedIndex(0);

    //setup window stuff
    getContentPane().add(outputList);
    getContentPane().setLayout(new FlowLayout());//stuff moves around, maybe bad
    setTitle("MagicVoice");
    setDefaultCloseOperation(EXIT_ON_CLOSE);     //actually close when X is hit
    setSize(500,150);                            //default window size
    setLocationRelativeTo(null);                 //center in screen
    setVisible(true);                            // show window
  }

  /***
   * switches the mixer over to whatever is given,
   * pausing input/output if possible
   * @param isInput If the mixer desired is input or output
   * @param mixerStr The desired mixer to switch either the input our output to
   */
  private void switchMixer(boolean isInput,String mixerStr){
    if(isInput){
      Input_Mixer = mixerStr;
    }else{
      Output_Mixer = mixerStr;
    }
    //if we are currently running, we should stop and switch mixers then restart
    if(isActive){
      isActive = false;
      //spin wait till everything closes If the thread fails we exit anyways.
      while(!isReady)
        try{
          Thread.sleep(1);
        }catch(Exception e){} //do nothing if we wake up we just sleep again
      //restart audio
      isActive = true;
      startCapture();
    }
  }

  private void startCapture() {
    try{
      isReady = false;
      //get our desired input mixer and its input line and get it ready
      DataLine.Info dataLineInfo = new DataLine.Info( TargetDataLine.class,
                                                      SAMPLE_FORMAT);
      Mixer mixer = AudioSystem.getMixer(INPUT_MIXERS.get(Input_Mixer));
      targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
      targetDataLine.open(SAMPLE_FORMAT);
      targetDataLine.start();

      //get our desired output mixer and its output line and get it ready
      dataLineInfo = new DataLine.Info(SourceDataLine.class,SAMPLE_FORMAT);
      mixer = AudioSystem.getMixer(OUTPUT_MIXERS.get(Output_Mixer));
      sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo);
      sourceDataLine.open(SAMPLE_FORMAT);
      sourceDataLine.start();

      //Start reading audio from the input and pushing it out
      new AudioThread().start();
    } catch (Exception e) {
      System.out.println(e);
      System.exit(0);
    }
  }

  //inspired by FLOW and the oracle demo
  private Mixer.Info[] getSupportedMixers(Boolean isInput,AudioFormat format) {
    DataLine.Info lineInfo;
    if(isInput){
      lineInfo = new DataLine.Info(TargetDataLine.class, format);
    }else{
      lineInfo = new DataLine.Info(SourceDataLine.class, format);
    }
    Mixer.Info[] info = AudioSystem.getMixerInfo();
    int numMixers = 0;

    for (int i = 0; i < info.length; i++) {
      Mixer m = AudioSystem.getMixer(info[i]);
      if (m.isLineSupported(lineInfo)) {
          numMixers++;
      }
    }

    Mixer.Info[] options = new Mixer.Info[numMixers];
    numMixers = 0;
    for (int i = 0; i < info.length; i++) {
      Mixer m = AudioSystem.getMixer(info[i]);
      if (m.isLineSupported(lineInfo)){
        options[numMixers++] = info[i];
      }
    }
    return options;
  }

  //This thread captures audio from the Microphone, and plays it back
  class AudioThread extends Thread {
    byte buffer[] = new byte[100];
    public void run(){
      try{
        while(isActive){
          // Read data from the internal buffer of the data line.
          int bytesRead = targetDataLine.read(buffer, 0, buffer.length);
          filterAudio(bytesRead,buffer);// the magic is happening here
          sourceDataLine.write(buffer,0,bytesRead);
        }
        System.out.println("Stopping");
        // close the microphone line
        targetDataLine.flush();
        targetDataLine.stop();
        targetDataLine.close();
        // close speaker line
        sourceDataLine.flush();
        sourceDataLine.stop();
        sourceDataLine.close();
        System.out.println("Stopped");
      }catch (Exception e) {
        System.out.println(e);
        System.exit(0);
      }

      isReady = true;
    }
  }

  public static void main(String args[]){
    new MagicVoice();
  }
}