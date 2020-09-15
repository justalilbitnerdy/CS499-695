import java.util.HashMap;

/** Sine.java A midi module that 
 * 
 */
public class Sine extends Osc {
  //Number of indecies/values we can actually interpret. Higher = better = more accurate
  double ACCURACY = 20.0;
  double[] SIN_VALUES;
  double[] _SIN_VALUES;
  Sine(){
    int tN = (int)Math.pow(2.0,ACCURACY);
    SIN_VALUES = new double[tN];
    for(int i = 0;i<tN;i++){
      SIN_VALUES[i] = Math.sin(((2*Math.PI*i)/tN)*(2*Math.PI));
    }
  }

  @Override
  public double tick(long tickCount){
    return innacurateSine(tickCount);
  }


  private double innacurateSine(long tickCount){
    int tN = (int)Math.pow(2.0,ACCURACY);
    //generate a sine wave with a period of 1, and get the value at x (instantanious period I believe)
    int tIndex = (int) Math.floor(super.tick(tickCount)*(tN/(2*Math.PI)))%tN;
    double tOut = SIN_VALUES[tIndex];
    //clamp the sine amplitude from -.5 to 5;
    tOut /=2;
    // //shift the sine wave up so it fits.
    tOut +=.5;
    return tOut;
  }

  private double slowSine(long tickCount){
    //generate a sine wave with a period of 1, and get the value at x
    double tOut = Math.sin(super.tick(tickCount)*(2*Math.PI));
    //clamp the sine amplitude from -.5 to 5;
    tOut /=2;
    //shift the sine wave up so it fits.
    tOut +=.5;
    return tOut;
  }
}