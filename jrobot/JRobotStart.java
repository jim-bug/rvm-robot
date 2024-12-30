package jrobot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JRobotStart extends JFrame implements ActionListener {
  int width = 350;
  
  int height = 200;
  
  JTextField ipfield = null;
  
  JTextField portfield = null;
  JTextField portNameField = null;
  JPanel p_selected;
  
  String[] argsJRobot;
  
  boolean testm = true;
  
  boolean localm = false;
  
  boolean remotem = false;
  
  String testmode = "testmode";
  
  String localmode = "localmode";
  
  String remotemode = "remotemode";
  
  String testText = "\n You will run JRobot simulating\n the communication with a\n RVM1 robot";
  
  String localText = "\n JRobot will try to connect to a\n RVM1 robot connected to\n your computer\nPlease you write the USB port name.\nWhere RVM1 robot is connected.";
  
  String remoteText = "\n Another computer running a\n RVM1 server will receive the\n commands this application\n will send";
  
  public JRobotStart() {
    super("Welcome to JRobot");
    setDefaultCloseOperation(0);
    getContentPane().setLayout(new GridLayout(1, 1));
    JPanel mainPanel = new JPanel();
    JPanel selector = new JPanel();
    selector.setBorder(BorderFactory.createTitledBorder("  Select Mode  "));
    this.p_selected = new JPanel();
    this.p_selected.setLayout(new BorderLayout());
    JRadioButton testButton = new JRadioButton("Test Mode");
    testButton.setActionCommand(this.testmode);
    testButton.setSelected(true);
    JRadioButton localButton = new JRadioButton("Local Mode");
    localButton.setActionCommand(this.localmode);
    JRadioButton remoteButton = new JRadioButton("Remote Mode");
    remoteButton.setActionCommand(this.remotemode);
    ButtonGroup group = new ButtonGroup();
    group.add(testButton);
    group.add(localButton);
    group.add(remoteButton);
    RadioListener myListener = new RadioListener();
    testButton.addActionListener(myListener);
    localButton.addActionListener(myListener);
    remoteButton.addActionListener(myListener);
    JPanel selectButtons = new JPanel();
    selectButtons.setLayout(new GridLayout(0, 1));
    selectButtons.add(testButton);
    selectButtons.add(localButton);
    selectButtons.add(remoteButton);
    selector.add(selectButtons);
    JTextArea ta1 = new JTextArea(this.testText, 5, 30);
    this.p_selected.add(ta1, "Center");
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(selector, "West");
    mainPanel.add(this.p_selected, "Center");
    selector.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    JPanel p1 = new JPanel();
    p1.setLayout(new GridLayout(1, 2, 10, 2));
    JButton b1 = new JButton("Start");
    b1.setActionCommand("ok");
    b1.addActionListener(this);
    b1.setMinimumSize(new Dimension(40, 20));
    b1.setBackground(new Color(180, 255, 180));
    p1.add(b1);
    b1 = new JButton("Quit");
    b1.setActionCommand("quit");
    b1.addActionListener(this);
    b1.setMinimumSize(new Dimension(40, 20));
    b1.setBackground(new Color(180, 180, 255));
    p1.add(b1);
    mainPanel.add(p1, "South");
    mainPanel.setPreferredSize(new Dimension(this.width, this.height));
    getContentPane().add(mainPanel);
    Dimension d1 = getToolkit().getScreenSize();
    Dimension d2 = new Dimension(this.width, this.height);
    setLocation(d1.width / 2 - d2.width / 2, d1.height / 2 - d2.height / 2);
    pack();
    setResizable(false);
    show();
  }
  
  public void close() {
    dispose();
    System.exit(1);
  }
  
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("quit")) {
      close();
    } else if (command.equals("ok")) {
      if (this.testm) {
        this.argsJRobot = new String[1];
        this.argsJRobot[0] = "s";
      } else if (this.localm) {
        this.argsJRobot = new String[2];
        this.argsJRobot[0] = "l";
        this.argsJRobot[1] = portNameField.getText();     // usb port name
        System.out.println(argsJRobot[1]);
      } else if (this.remotem) {
        this.argsJRobot = new String[3];
        this.argsJRobot[0] = "r";
        this.argsJRobot[1] = this.ipfield.getText();
        this.argsJRobot[2] = this.portfield.getText();
      } 
      dispose();
      startJRobot(this, this.argsJRobot); // LOOK THIS :)
    } 
  }
  
  class RadioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      JTextArea ta1 = null;
      JRobotStart.this.p_selected.removeAll();
      if (command.equals(JRobotStart.this.testmode)) {
          JRobotStart.this.testm = true;
          JRobotStart.this.localm = false;
          JRobotStart.this.remotem = false;
          ta1 = new JTextArea(JRobotStart.this.testText, 5, 30);
          ta1.setEditable(false);
          JRobotStart.this.p_selected.add(ta1, "Center");
          JRobotStart.this.p_selected.doLayout();
      } else if (command.equals(JRobotStart.this.localmode)) {
          JRobotStart.this.localm = true;
          JRobotStart.this.testm = false;
          JRobotStart.this.remotem = false;

          JPanel inputTextPanel = new JPanel(new BorderLayout());
          JPanel inputPanel = new JPanel(new BorderLayout());
          inputTextPanel.add(new JLabel("USB Port Name"), "North");
          portNameField = new JTextField("ttyUSB0", 30);

          ta1 = new JTextArea(JRobotStart.this.localText, 5, 30);
          ta1.setEditable(false);
          inputTextPanel.add(inputPanel, "Center");
          p_selected.add(ta1, "Center");
          inputPanel.add(portNameField, "Center");
          p_selected.add(inputTextPanel, "South");
          JRobotStart.this.p_selected.doLayout();
          inputTextPanel.doLayout();
          inputPanel.doLayout();
      } else if (command.equals(JRobotStart.this.remotemode)) {
          JRobotStart.this.remotem = true;
          JRobotStart.this.testm = false;
          JRobotStart.this.localm = false;
          ta1 = new JTextArea(JRobotStart.this.remoteText, 5, 30);
          ta1.setEditable(false);
          JRobotStart.this.p_selected.add(ta1, "Center");
          JPanel p1 = new JPanel();
          p1.setLayout(new BorderLayout());
          p1.add(new JLabel("IP or Name  +  Port Number"), "North");
          JPanel p2 = new JPanel();
          p2.setLayout(new BorderLayout());
          JRobotStart.this.ipfield = new JTextField("129.194.71.23", 30);
          JRobotStart.this.portfield = new JTextField("8888", 5);
          p2.add(JRobotStart.this.ipfield, "Center");
          p2.add(JRobotStart.this.portfield, "East");
          p1.add(p2, "Center");
          JRobotStart.this.p_selected.add(p1, "South");
          JRobotStart.this.p_selected.doLayout();
          p1.doLayout();
          p2.doLayout();
      } 
    }
  }
  
  public static void main(String[] args) {
    JRobotStart jrs = null;
    jrs = new JRobotStart();
  }
  
  public static void startJRobot(JRobotStart jrs, String[] args) {
    jrs = null;
    // System.out.println(args[1]);
    new JRobot(args);    // LOOK THIS :)
  }
}