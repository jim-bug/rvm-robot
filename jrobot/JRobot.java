package jrobot;

import ch.unige.rvm1.AdvancedRVM1;
import ch.unige.rvm1.CommandQueueListener;
import ch.unige.rvm1.Connection;
import ch.unige.rvm1.RVM1Exception;
import ch.unige.rvm1.RVM1Listener;
import ch.unige.rvm1.RVM1State;
import ch.unige.rvm1.SerialConnection;
import ch.unige.rvm1.SocketConnection;
import ch.unige.rvm1.TestConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class JRobot extends JFrame implements ActionListener {
  public AdvancedRVM1 rvm1;
  
  OutputPane outputPane;
  
  ProgramPane programPane;
  
  CameraPane cameraPane;
  
  VirtualRVM1Pane virtualPane;
  
  JTabbedPane tabbedPane;
  
  ArrayList cmds;
  
  Pliers pliers;
  
  Signal signal;
  
  public MySlider slider0;
  
  public MySlider slider1;
  
  public MySlider slider2;
  
  public MySlider slider3;
  
  public MySlider slider4;
  
  SpeedSlider speedSlider;
  
  JPanel p1;
  
  JPanel p_sliders;
  
  static final String[] argsJoystick = new String[] { "-d:0.1", "-i:50" };
  
  JButton joystickButton;
  
  JButton aboutButton;
  
  JDialog jconf;
  
  public boolean blockSlidersEvent;
  
  public JRobot(String[] args) {
    super("JRobot");
    TestConnection testConnection;
    SocketConnection socketConnection;
    this.p1 = null;
    this.p_sliders = null;
    this.joystickButton = null;
    this.aboutButton = null;
    this.blockSlidersEvent = false;
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    this.p1 = new JPanel();
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = 3;
    this.p1.setLayout(gridbag);
    this.p_sliders = new JPanel();
    this.p_sliders.setLayout(new GridLayout(6, 1));
    c.gridheight = 3;
    c.weighty = 0.0D;
    c.weightx = 0.0D;
    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(this.p_sliders, c);
    this.p1.add(this.p_sliders);
    JPanel p_down = new JPanel();
    p_down.setLayout(new GridLayout(2, 1));
    c.gridheight = 1;
    c.weighty = 0.0D;
    c.weightx = 0.0D;
    c.gridy = -1;
    c.gridx = 0;
    gridbag.setConstraints(p_down, c);
    this.p1.add(p_down);
    this.slider0 = new MySlider(this, "Waist", 0, 300, 300, 50, 10);
    this.p_sliders.add(this.slider0);
    this.slider1 = new MySlider(this, "Shoulder", 0, 130, 130, 25, 5);
    this.p_sliders.add(this.slider1);
    this.slider2 = new MySlider(this, "Elbow", 0, 110, 110, 25, 5);
    this.p_sliders.add(this.slider2);
    this.slider3 = new MySlider(this, "Pitch", 0, 180, 0, 45, 5);
    this.p_sliders.add(this.slider3);
    this.slider4 = new MySlider(this, "Roll", 0, 360, 360, 90, 10);
    this.p_sliders.add(this.slider4);
    this.pliers = new Pliers(this);
    this.p_sliders.add(this.pliers);
    this.speedSlider = new SpeedSlider(this, 4);
    p_down.add(this.speedSlider);
    this.signal = new Signal();
    p_down.add(this.signal);
    p.add(this.p1, "West");
    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout());
    this.tabbedPane = new JTabbedPane();
    this.outputPane = new OutputPane(this);
    this.tabbedPane.addTab("Output", this.outputPane);
    this.programPane = new ProgramPane(this);
    this.tabbedPane.addTab(this.programPane.getNoFile(), this.programPane);
    this.cameraPane = new CameraPane(this);
    this.tabbedPane.addTab("Camera", this.cameraPane);
    this.virtualPane = new VirtualRVM1Pane(this);
    this.tabbedPane.addTab("Virtual", this.virtualPane);
    HelpPane helpPane = new HelpPane();
    this.tabbedPane.addTab("Help", helpPane);
    p2.add(this.tabbedPane, "Center");
    this.tabbedPane.setSelectedComponent(this.virtualPane);
    JPanel p3 = new JPanel();
    p3.setLayout(new FlowLayout());
    this.joystickButton = new JButton("Joystick Control");
    this.joystickButton.setActionCommand("joystick");
    this.joystickButton.addActionListener(this);
    this.joystickButton.setPreferredSize(new Dimension(170, 30));
    this.joystickButton.setBackground(new Color(210, 180, 255));
    p3.add(this.joystickButton);
    CommandLine cmdLine = new CommandLine(this);
    p3.add(cmdLine);
    this.aboutButton = new JButton("About");
    this.aboutButton.setActionCommand("about");
    this.aboutButton.addActionListener(this);
    this.aboutButton.setPreferredSize(new Dimension(100, 30));
    this.aboutButton.setBackground(new Color(150, 220, 200));
    p3.add(this.aboutButton);
    p2.add(p3, "South");
    p.add(p2, "Center");
    getContentPane().add(p);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            JRobot.this.programPane.closeProgramFile();
            JRobot.this.rvm1.dispose();
            System.exit(0);
          }
        });
    Connection comm = null;
    char connectType = args[0].charAt(0);
    switch (connectType) {
      case 's':
        comm = new TestConnection(1000);
        break;
      case 'l':
        try {
           comm = new SerialConnection(args[1], "JRobot");
          System.out.println("Status Connection: " + args[1] + " OK");
        } catch (RVM1Exception e) {
            System.out.println("Status Connection: " + args[1] + " BAD");
          errorMessage("No RVM1 robot connected");
          System.exit(0);
        } 
        break;
      case 'r':
        if (args.length < 3) {
          System.out.println("Not enough parameters, 'r' option needs <hostname> and <port>");
          System.exit(0);
        } 
        try {
          InetAddress remoteRVM1 = InetAddress.getByName(args[1]);
          int portNumber = Integer.parseInt(args[2]);
          comm = new SocketConnection(remoteRVM1, portNumber);
        } catch (Exception e) {
          errorMessage("Can't connect to supposed RVM1 server");
          System.exit(0);
        } 
        break;
      default:
        System.out.println("Not an option. Try without parameters for instructions.");
        System.exit(0);
        break;
    } 
    this.rvm1 = new AdvancedRVM1(comm); // comm
    this.rvm1.getCommandQueue().addCommandQueueListener(new CommandQueueListener() {
          public void executed(String command) {
            String action = "Execution: ";
            if (JRobot.this.rvm1.isSimulation())
              action = "Simulation: "; 
            JRobot.this.outputPane.append(action + action + "\n");
            JRobot.this.virtualPane.sendCommand(command);
          }
        });
    this.rvm1.addRVM1Listener(new RVM1Listener() {
          public void ready() {
            JRobot.this.signal.setGreen();
            if (JRobot.this.rvm1.getCommandQueue().isEmpty()) {
              JRobot.this.blockSlidersEvent = true;
              RVM1State state = JRobot.this.rvm1.getState();
              JRobot.this.slider0.setValue((int)state.getBasePos());
              JRobot.this.slider1.setValue((int)state.getShoulderPos());
              JRobot.this.slider2.setValue((int)state.getElbowPos());
              JRobot.this.slider3.setValue((int)state.getPitchPos());
              JRobot.this.slider4.setValue((int)state.getRollPos());
              JRobot.this.speedSlider.setValue(state.getSpeed());
              JRobot.this.blockSlidersEvent = false;
              JRobot.this.inputsEnabled(true);
            } 
          }
          
          public void busy() {
            JRobot.this.signal.setRed();
          }
          
          public void error(int errType, String command, String message) {
            JRobot.this.rvm1.getCommandQueue().purge();
            JRobot.this.errorMessage(command + "\n" + command);
            JRobot.this.inputsEnabled(true);
          }
        });
    inputsEnabled(false);
    setSize(1000, 700);
    Dimension d1 = getToolkit().getScreenSize();
    Dimension d2 = getSize();
    setLocation(d1.width / 2 - d2.width / 2, d1.height / 2 - d2.height / 2);
    show();
  }
  
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("joystick")) {
      this.joystickButton.setText("Keyboard Control");
      this.joystickButton.setActionCommand("keyboard");
      this.jconf = new JDialog(this, "Setting to Joystick Control", true);
      this.jconf.setDefaultCloseOperation(0);
    } else if (command.equals("keyboard")) {
      this.joystickButton.setText("Joystick Control");
      this.joystickButton.setActionCommand("joystick");
      msgMessage("Setting to Keyboard Control");
      this.p1.add(this.p_sliders, 0);
      this.p1.doLayout();
      this.p_sliders.doLayout();
      this.pliers.doLayout();
    } else if (command.equals("ok_j")) {
      System.out.println("Chosen a joystick");
    } else if (command.equals("cancel_j")) {
      System.out.println("NO joystick");
      this.jconf.dispose();
      this.joystickButton.setText("Joystick Control");
      this.joystickButton.setActionCommand("joystick");
    } else if (command.equals("about")) {
      ImageIcon icon = new ImageIcon("jrobot/cui.gif");
      JOptionPane.showMessageDialog(this, "JRobot v.1.0\nUniversity of Geneva\nComputer Science Department\nApril 2002", "About JRobot", 1, icon);
    } 
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Welcome to JRobot, please read instructions\n");
      System.out.println("JVM execution");
      System.out.println("\t>java");
      System.out.println("\t>java -Dj3d.sharedctx=false  (if you observe abnormal crashes)\n");
      System.out.println("Program Execution\n");
      System.out.println("\t>java JRobot <execution mode> <parameters separated by space>\n");
      System.out.println("Execution Mode\n");
      System.out.println("\ts\tSimulation Mode (Connection with robot is simulated)");
      System.out.println("\tl\tNormal Mode (Robot is connected to serial com)");
      System.out.println("\tr\tRemote Mode (Robot connection is through IP + port, see Parameters)\n");
      System.out.println("Parameters (only remote mode)\n");
      System.out.println("\tIP\t(RVM1 Server Name or IP number)");
      System.out.println("\tPort\t(Port number)\n");
      System.exit(0);
    } 
    new JRobot(args);
  }
  
  public void initRobot() {
    inputsEnabled(false);
    this.rvm1.nest();
    this.rvm1.speed(4);
  }
  
  public void inputsEnabled(boolean state) {
    this.slider0.setEnabled(state);
    this.slider1.setEnabled(state);
    this.slider2.setEnabled(state);
    this.slider3.setEnabled(state);
    this.slider4.setEnabled(state);
    this.pliers.setEnabled(state);
    this.speedSlider.setEnabled(state);
  }
  
  private void errorMessage(String error) {
    JOptionPane.showMessageDialog(null, error, "Error", 0);
  }
  
  private void msgMessage(String msg) {
    JOptionPane.showMessageDialog(null, msg, "MEssage", 1);
  }
}
