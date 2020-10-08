import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.locks.*;


/**
	A special module which presents itself as a GUI dial widget.  Changing the dial with your 
	mouse will change the output of the underlying Module.  You create the Dial first,
	then call getModule() to extract the module that it will control. 
*/


public class IntegerDial extends JPanel {
  public static final int LABELLED_DIAL_WIDTH = 20;
  public static final float DIAL_STROKE_WIDTH = 4.0f;
  public static final Color MOD_COLOR = Color.BLACK;
  public static final BasicStroke DIAL_THIN_STROKE = new BasicStroke(DIAL_STROKE_WIDTH / 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
  public static final BasicStroke DIAL_THICK_STROKE = new BasicStroke(DIAL_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
  public static final Color DIAL_DYNAMIC_COLOR = Color.RED;
  public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10); 

  // The distance the mouse travels to go 0...1
  public static final int SCALE = 256;

  private boolean dynamicallyChanging = false;
  private Color staticColor = MOD_COLOR;

  private double SLOTS;

  // The state when the mouse was pressed 
  private double startState;
  // The mouse position when the mouse was pressed 
  private int startX;
  private int startY;
      
  // Is the mouse pressed?  This is part of a mechanism for dealing with
  // a stupidity in Java: if you PRESS in a widget, it'll be told. But if
  // you then drag elsewhere and RELEASE, the widget is never told.
  private boolean mouseDown;
      
  public Dimension getPreferredSize() { return new Dimension(LABELLED_DIAL_WIDTH, LABELLED_DIAL_WIDTH); }
  public Dimension getMinimumSize() { return new Dimension(LABELLED_DIAL_WIDTH, LABELLED_DIAL_WIDTH); }
      
  private JLabel title = null;
  private JLabel data = null;

  class DialModule extends Module
      {
      ReentrantLock lock = new ReentrantLock();

      public void setValueNoRepaint(double value) 
          {
          lock.lock();
          try
              {
              super.setValue(value);
              }
          finally
              {
              lock.unlock();
              }
          }
              
      public void setValue(double value) 
          {
          setValueNoRepaint(value);
          repaint();
          }
              
      public double getValue()
          {
          lock.lock();
          try
              {
              return super.getValue();
              }
          finally
              {
              lock.unlock();
              }
          }

      public double tick(long tickCount) { return 0; }  // unused
      public void doUpdate(long tickCount) { }
      };

  private DialModule dialModule = new DialModule();

      
  /*
    java bsh.Interpreter
    f = new JFrame();
    d = new Dial();
    g = d.getLabelledDial("Hello World");
    f.add(g);
    f.show();
  */

  void update(double val) {
    double newVal = 0.0;
    for(int i = 0;i<SLOTS;i++){
      double tmp = i*(1/SLOTS);
      if(val<=tmp){
        System.out.println(val + "<" + tmp + "i:" + i);
        newVal = i;
        break;
      }
    }
    setState(newVal+1);

    if (data != null) {
        data.setText(" " + map(newVal+1));
    }
    repaint();
  }

  public String map(double val) {
    return String.format("%1f", val);
  }

  void setState(double val) {
    dialModule.setValueNoRepaint(Math.max(0,val));
  }

  double getState() {
    return dialModule.getValue();
  }

  /** Returns the actual square within which the Dial's circle is drawn. */
  Rectangle getDrawSquare() {
    Insets insets = getInsets();
    Dimension size = getSize();
    int width = size.width - insets.left - insets.right;
    int height = size.height - insets.top - insets.bottom;

    // How big do we draw our circle?
    if (width > height)
        {
        // base it on height
        int h = height;
        int w = h;
        int y = insets.top;
        int x = insets.left + (width - w) / 2;
        return new Rectangle(x, y, w, h);
        }
    else
        {
        // base it on width
        int w = width;
        int h = w;
        int x = insets.left;
        int y = insets.top + (height - h) / 2;
        return new Rectangle(x, y, w, h);
        }
  }


  void mouseReleased(MouseEvent e)
      {
      if (mouseDown)
          {
          mouseDown = false;
          dynamicallyChanging = false;
          repaint();
          if (releaseListener != null)
              Toolkit.getDefaultToolkit().removeAWTEventListener(releaseListener);
          }
      }

  double getProposedState(MouseEvent e) {
      int py = e.getY();
      int y = -(py - startY);
      int min = 0;
      int max = 1;
      double range = (max - min);

      double multiplicand = SCALE / range;

      double proposedState = (startState/SLOTS) + y / multiplicand;
      return proposedState;
  }

  public Module getModule() { return dialModule; }

  public JPanel getLabelledDial(String label)
      {
      JPanel panel = new JPanel();
      JPanel subpanel = new JPanel();
      JPanel superpanel = new JPanel();

      title = new JLabel(label);
      title.setFont(SMALL_FONT);

      data = new JLabel(map(getState()));
      data.setFont(SMALL_FONT);

      panel.setLayout(new BorderLayout());
      subpanel.setLayout(new BorderLayout());
      superpanel.setLayout(new BorderLayout());

      panel.add(this, BorderLayout.WEST);
      subpanel.add(title, BorderLayout.NORTH);
      subpanel.add(data, BorderLayout.CENTER);
      panel.add(subpanel, BorderLayout.CENTER);
      superpanel.add(panel, BorderLayout.NORTH);
      return superpanel;
      }

  public IntegerDial(double initialValue,int slots) {
      SLOTS = slots*1.0;
      if (initialValue < 0) initialValue = 0;
      if (initialValue > 1) initialValue = 1;
      dialModule.setValueNoRepaint(initialValue);
          
      addMouseListener(new MouseAdapter()
          {                        
          public void mousePressed(MouseEvent e)
              {                        
              mouseDown = true;
              startX = e.getX();
              startY = e.getY();
              startState = getState();
              dynamicallyChanging = true;
              repaint();

              if (releaseListener != null)
                  Toolkit.getDefaultToolkit().removeAWTEventListener(releaseListener);

              // This gunk fixes a BAD MISFEATURE in Java: mouseReleased isn't sent to the
              // same component that received mouseClicked.  What the ... ? Asinine.
              // So we create a global event listener which checks for mouseReleased and
              // calls our own private function.  EVERYONE is going to do this.
                              
              Toolkit.getDefaultToolkit().addAWTEventListener( releaseListener = new AWTEventListener()
                {
                public void eventDispatched(AWTEvent e) {
                  if (e instanceof MouseEvent && e.getID() == MouseEvent.MOUSE_RELEASED)
                    {
                      IntegerDial.this.mouseReleased((MouseEvent)e);
                    }
                  }
                }, AWTEvent.MOUSE_EVENT_MASK);
              }
                      
          MouseEvent lastRelease;
          public void mouseReleased(MouseEvent e)
              {
              if (e == lastRelease) // we just had this event because we're in the AWT Event Listener.  So we ignore it
                  return;
                  
              dynamicallyChanging = false;
              repaint();
              if (releaseListener != null)
                  Toolkit.getDefaultToolkit().removeAWTEventListener(releaseListener);
              lastRelease = e;
              }
          });
                      
      addMouseMotionListener(new MouseMotionAdapter()
          {
          public void mouseDragged(MouseEvent e) {
              double proposedState = getProposedState(e);
              // at present we're just going to use y.  It's confusing to use either y or x.
              update(Math.max(0,proposedState));
          }
          });

      repaint();
      }

  AWTEventListener releaseListener = null;

  /** Returns the actual square within which the Dial's circle is drawn. */
  public void paintComponent(Graphics g)
      {
      Graphics2D graphics = (Graphics2D) g;

      Rectangle rect = getBounds();
      rect.x = 0;
      rect.y = 0;
      graphics.setPaint(new JLabel("").getBackground());
      graphics.fill(rect);
      rect = getDrawSquare();

      graphics.setPaint(MOD_COLOR);
      graphics.setStroke(DIAL_THIN_STROKE);
      Arc2D.Double arc = new Arc2D.Double();

      double startAngle = 90 + (270 / 2);
      double interval = -270;

      arc.setArc(rect.getX() + DIAL_STROKE_WIDTH / 2, rect.getY() + DIAL_STROKE_WIDTH/2, rect.getWidth() - DIAL_STROKE_WIDTH, rect.getHeight() - DIAL_STROKE_WIDTH, startAngle, interval, Arc2D.OPEN);

      graphics.draw(arc);
      graphics.setStroke(DIAL_THICK_STROKE);
      arc = new Arc2D.Double();

      double state = getState();
      double min = 1;
      double max = SLOTS;
      interval = -((state - min) / (double)(max - min) * 265) - 5;

      if (dynamicallyChanging)
          {
          graphics.setPaint(DIAL_DYNAMIC_COLOR);
          if (state == min)
              {
              interval = -5;
              }
          }
      else
          {
          graphics.setPaint(staticColor);
          if (state == min)
              {
              interval = 0;
              }
          }

      arc.setArc(rect.getX() + DIAL_STROKE_WIDTH / 2, rect.getY() + DIAL_STROKE_WIDTH/2, rect.getWidth() - DIAL_STROKE_WIDTH, rect.getHeight() - DIAL_STROKE_WIDTH, startAngle, interval, Arc2D.OPEN);            
      graphics.draw(arc);
      }
  }
