/**
   A top-level superclass for all Modules.  Each module overrides a method called tick()
   to return the current value of the Module for the given tick count.
*/
 

public abstract class Module 
    {
    private double value = 0.0;

    /** Returns the current value of the Module.  This is normally updated during doUpdate()
        except for Constants where it may be set once and left like that. */
    public double getValue() { return value; }

    /** Sets the current value of the Module.  This is normally called in doUpdate(), 
        except for Constants where it may be set once and left like that. */
    public void setValue(double value) { this.value = value; }

    /** Override this method to return the current value associated with the given tick.
        This method will be called by doUpdate to update the Module's value returned by getValue().
    */
    protected abstract double tick(long tickCount);

    /** Updates the current value returned by getValue() by calling tick(...) */
    public void doUpdate(long tickCount) 
        {
        setValue(tick(tickCount));
        }
    }
