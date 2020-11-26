import javax.sound.sampled.*;

public class VoiceChanger{

    public VoiceChanger(){
        TargetDataLine line;
        AudioFormat format = new AudioFormat(44100,//sampleRate
                                             16,   //sampleSizeInBits
                                             1,    //channels
                                             true, //signed?
                                             true);//bigEndian?
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
        if (!AudioSystem.isLineSupported(info)) {
            //Just like cry a bit
            System.out.println("Like if you cry evr tim.");

        }
        // Obtain and open the line.
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            System.out.println("start");
            int off = 0;
            byte b[] = new byte[2];
            while(true){
              line.read(b,off,2);
              off+=2;
              System.out.println(b[0]);
            }
        } catch (LineUnavailableException ex) {
            //Just like cry a bit
            System.out.println("Like if you cry evr tim.");
        }
    }
}