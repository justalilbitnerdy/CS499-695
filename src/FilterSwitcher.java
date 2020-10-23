import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FilterSwitcher extends Module implements ActionListener {

  private Box GUI;
  LPF lpf;
  HPF hpf;
  BPF bpf;
  Notch notch;
  String currentFilter = "lpf";

  public FilterSwitcher() {

    // Build the filters
    lpf = new LPF();
    hpf = new HPF();
    bpf = new BPF();
    notch = new Notch();

    // Put them in a box and put it in the window
    buildGUI();
  }

  public double tick(long tickCount) {
    double value = 0.0;
    switch(currentFilter){
      case "lpf":
        value = lpf.getValue();
        break;
      case "hpf":
        value = hpf.getValue();
        break;
      case "bpf":
        value = bpf.getValue();
        break;
      case "notch":
        value = notch.getValue();
        break;
    }
    return value;
  }

  public Module[] getModules(){
    return new Module[]{lpf,hpf,bpf,notch};
  }


  //stole starting values from the demo
  public void buildGUI() {
    GUI = new Box(BoxLayout.Y_AXIS);
    GUI.setBorder(BorderFactory.createTitledBorder("Filters"));
    Box lpfBox = new Box(BoxLayout.X_AXIS);
    JRadioButton lpfBtn = new JRadioButton();
    lpfBtn.setSelected(true);
    lpfBtn.setActionCommand("lpf");
    lpfBtn.addActionListener(this);
    lpfBox.add(lpfBtn);
    lpfBox.add(lpf.getGUI());

    Box hpfBox = new Box(BoxLayout.X_AXIS);
    JRadioButton hpfBtn = new JRadioButton();
    hpfBtn.setActionCommand("hpf");
    hpfBtn.addActionListener(this);
    hpfBox.add(hpfBtn);
    hpfBox.add(hpf.getGUI());

    Box bpfBox = new Box(BoxLayout.X_AXIS);
    JRadioButton bpfBtn = new JRadioButton();
    bpfBtn.setActionCommand("bpf");
    bpfBtn.addActionListener(this);
    bpfBox.add(bpfBtn);
    bpfBox.add(bpf.getGUI());

    Box notchBox = new Box(BoxLayout.X_AXIS);
    JRadioButton notchBtn = new JRadioButton();
    notchBtn.setActionCommand("notch");
    notchBtn.addActionListener(this);
    notchBox.add(notchBtn);
    notchBox.add(notch.getGUI());

    //setup btns for the different filters
    ButtonGroup group = new ButtonGroup();
    group.add(lpfBtn);
    group.add(hpfBtn);
    group.add(bpfBtn);
    group.add(notchBtn);

    GUI.add(lpfBox);
    GUI.add(hpfBox);
    GUI.add(bpfBox);
    GUI.add(notchBox);
  }

  public void actionPerformed(ActionEvent e) {
    currentFilter = e.getActionCommand();
  }

  public Box getGUI() {
    return GUI;
  }

}
