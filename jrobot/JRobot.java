/*     */ package jrobot;
/*     */ import ch.unige.rvm1.AdvancedRVM1;
/*     */ import ch.unige.rvm1.CommandQueueListener;
/*     */ import ch.unige.rvm1.Connection;
/*     */ import ch.unige.rvm1.RVM1Listener;
/*     */ import ch.unige.rvm1.RVM1State;
/*     */ import ch.unige.rvm1.RVM1Exception;
/*     */ import ch.unige.rvm1.SerialConnection;
/*     */ import ch.unige.rvm1.SocketConnection;
/*     */ import ch.unige.rvm1.TestConnection;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ 
/*     */ public class JRobot extends JFrame implements ActionListener {
/*     */   public AdvancedRVM1 rvm1;
/*     */   OutputPane outputPane;
/*     */   ProgramPane programPane;
/*     */   CameraPane cameraPane;
/*     */   VirtualRVM1Pane virtualPane;
/*     */   JTabbedPane tabbedPane;
/*     */   ArrayList cmds;
/*     */   Pliers pliers;
/*     */   Signal signal;
/*     */   public MySlider slider0;
/*     */   public MySlider slider1;
/*     */   public MySlider slider2;
/*     */   public MySlider slider3;
/*     */   public MySlider slider4;
/*     */   SpeedSlider speedSlider;
/*     */   JPanel p1;
/*     */   JPanel p_sliders;
/*  49 */   static final String[] argsJoystick = new String[] { "-d:0.1", "-i:50" };
/*     */ 
/*     */   
/*     */   JButton joystickButton;
/*     */   
/*     */   JButton aboutButton;
/*     */   
/*     */   JDialog jconf;
/*     */   
/*     */   public boolean blockSlidersEvent;
/*     */ 
/*     */   
/*     */   public JRobot(String[] args) {
/*  62 */     super("JRobot"); TestConnection testConnection; SocketConnection socketConnection; this.p1 = null; this.p_sliders = null; this.joystickButton = null; this.aboutButton = null;
/*     */     this.blockSlidersEvent = false;
/*  64 */     JPanel p = new JPanel();
/*  65 */     p.setLayout(new BorderLayout());
/*     */     
/*  67 */     this.p1 = new JPanel();
/*  68 */     GridBagLayout gridbag = new GridBagLayout();
/*  69 */     GridBagConstraints c = new GridBagConstraints();
/*  70 */     c.fill = 3;
/*  71 */     this.p1.setLayout(gridbag);
/*     */     
/*  73 */     this.p_sliders = new JPanel();
/*  74 */     this.p_sliders.setLayout(new GridLayout(6, 1));
/*  75 */     c.gridheight = 3;
/*  76 */     c.weighty = 0.0D;
/*  77 */     c.weightx = 0.0D;
/*  78 */     c.gridx = 0;
/*  79 */     c.gridy = 0;
/*  80 */     gridbag.setConstraints(this.p_sliders, c);
/*  81 */     this.p1.add(this.p_sliders);
/*     */     
/*  83 */     JPanel p_down = new JPanel();
/*  84 */     p_down.setLayout(new GridLayout(2, 1));
/*  85 */     c.gridheight = 1;
/*  86 */     c.weighty = 0.0D;
/*  87 */     c.weightx = 0.0D;
/*  88 */     c.gridy = -1;
/*  89 */     c.gridx = 0;
/*  90 */     gridbag.setConstraints(p_down, c);
/*  91 */     this.p1.add(p_down);
/*     */     
/*  93 */     this.slider0 = new MySlider(this, "Waist", 0, 300, 300, 50, 10);
/*  94 */     this.p_sliders.add(this.slider0);
/*  95 */     this.slider1 = new MySlider(this, "Shoulder", 0, 130, 130, 25, 5);
/*  96 */     this.p_sliders.add(this.slider1);
/*  97 */     this.slider2 = new MySlider(this, "Elbow", 0, 110, 110, 25, 5);
/*  98 */     this.p_sliders.add(this.slider2);
/*  99 */     this.slider3 = new MySlider(this, "Pitch", 0, 180, 0, 45, 5);
/* 100 */     this.p_sliders.add(this.slider3);
/* 101 */     this.slider4 = new MySlider(this, "Roll", 0, 360, 360, 90, 10);
/* 102 */     this.p_sliders.add(this.slider4);
/* 103 */     this.pliers = new Pliers(this);
/* 104 */     this.p_sliders.add(this.pliers);
/*     */     
/* 106 */     this.speedSlider = new SpeedSlider(this, 4);
/* 107 */     p_down.add(this.speedSlider);
/* 108 */     this.signal = new Signal();
/* 109 */     p_down.add(this.signal);
/*     */     
/* 111 */     p.add(this.p1, "West");
/*     */     
/* 113 */     JPanel p2 = new JPanel();
/* 114 */     p2.setLayout(new BorderLayout());
/* 115 */     this.tabbedPane = new JTabbedPane();
/* 116 */     this.outputPane = new OutputPane(this);
/* 117 */     this.tabbedPane.addTab("Output", this.outputPane);
/* 118 */     this.programPane = new ProgramPane(this);
/* 119 */     this.tabbedPane.addTab(this.programPane.getNoFile(), this.programPane);
/* 120 */     this.cameraPane = new CameraPane(this);
/* 121 */     this.tabbedPane.addTab("Camera", this.cameraPane);
/* 122 */     this.virtualPane = new VirtualRVM1Pane(this);
/* 123 */     this.tabbedPane.addTab("Virtual", this.virtualPane);
/* 124 */     HelpPane helpPane = new HelpPane();
/* 125 */     this.tabbedPane.addTab("Help", helpPane);
/* 126 */     p2.add(this.tabbedPane, "Center");
/*     */     
/* 128 */     this.tabbedPane.setSelectedComponent(this.virtualPane);
/*     */     
/* 130 */     JPanel p3 = new JPanel();
/* 131 */     p3.setLayout(new FlowLayout());
/*     */ 
/*     */     
/* 134 */     this.joystickButton = new JButton("Joystick Control");
/* 135 */     this.joystickButton.setActionCommand("joystick");
/* 136 */     this.joystickButton.addActionListener(this);
/* 137 */     this.joystickButton.setPreferredSize(new Dimension(170, 30));
/* 138 */     this.joystickButton.setBackground(new Color(210, 180, 255));
/* 139 */     p3.add(this.joystickButton);
/*     */ 
/*     */     
/* 142 */     CommandLine cmdLine = new CommandLine(this);
/* 143 */     p3.add(cmdLine);
/*     */ 
/*     */     
/* 146 */     this.aboutButton = new JButton("About");
/* 147 */     this.aboutButton.setActionCommand("about");
/* 148 */     this.aboutButton.addActionListener(this);
/* 149 */     this.aboutButton.setPreferredSize(new Dimension(100, 30));
/* 150 */     this.aboutButton.setBackground(new Color(150, 220, 200));
/* 151 */     p3.add(this.aboutButton);
/*     */     
/* 153 */     p2.add(p3, "South");
/* 154 */     p.add(p2, "Center");
/*     */     
/* 156 */     getContentPane().add(p);
/*     */     
/* 158 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent e) {
/* 160 */             JRobot.this.programPane.closeProgramFile();
/* 161 */             JRobot.this.rvm1.dispose();
/* 162 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/* 166 */     Connection comm = null;
/*     */     
/* 168 */     char connectType = args[0].charAt(0);
                socketConnection = null;
/* 169 */     switch (connectType) {
/*     */       case 's':
/* 171 */         testConnection = new TestConnection(1000);
/*     */         break;
/*     */       case 'l':
/*     */         try {
/* 175 */           SerialConnection serialConnection = new SerialConnection(args[1], "JRobot"); // see JRobotStart.java
                    System.out.println("I'm trying to connect to the robot from: " + args[1]);
/*     */         }
/* 177 */         catch (RVM1Exception e) {
                    System.out.println("I failed to connect to the robot from: " + args[1]);
/* 178 */           errorMessage("No RVM1 robot connected");            // look this :)
/* 179 */           System.exit(0);
/*     */         } 
/*     */         break;
/*     */       case 'r':
/* 183 */         if (args.length < 3) {
/* 184 */           System.out.println("Not enough parameters, 'r' option needs <hostname> and <port>");
/* 185 */           System.exit(0);
/*     */         } 
/*     */         try {
/* 188 */           InetAddress remoteRVM1 = InetAddress.getByName(args[1]);
/* 189 */           int portNumber = Integer.parseInt(args[2]);
/* 190 */           socketConnection = new SocketConnection(remoteRVM1, portNumber);
/*     */         }
/* 192 */         catch (Exception e) {
/* 193 */           errorMessage("Can't connect to supposed RVM1 server");
/* 194 */           System.exit(0);
/*     */         } 
/*     */         break;
/*     */       default:
/* 198 */         System.out.println("Not an option. Try without parameters for instructions.");
/* 199 */         System.exit(0);
/*     */         break;
/*     */     } 
/* 202 */     this.rvm1 = new AdvancedRVM1((Connection) socketConnection);
/*     */     
/* 204 */     this.rvm1.getCommandQueue().addCommandQueueListener(new CommandQueueListener() {
/*     */           public void executed(String command) {
/* 206 */             String action = "Execution: ";
/* 207 */             if (JRobot.this.rvm1.isSimulation()) {
/* 208 */               action = "Simulation: ";
/*     */             }
/* 210 */             JRobot.this.outputPane.append(action + action + "\n");
/*     */             
/* 212 */             JRobot.this.virtualPane.sendCommand(command);
/*     */           }
/*     */         });
/*     */     
/* 216 */     this.rvm1.addRVM1Listener(new RVM1Listener() {
/*     */           public void ready() {
/* 218 */             JRobot.this.signal.setGreen();
/* 219 */             if (JRobot.this.rvm1.getCommandQueue().isEmpty()) {
/*     */               
/* 221 */               JRobot.this.blockSlidersEvent = true;
/* 222 */               RVM1State state = JRobot.this.rvm1.getState();
/* 223 */               JRobot.this.slider0.setValue((int)state.getBasePos());
/* 224 */               JRobot.this.slider1.setValue((int)state.getShoulderPos());
/* 225 */               JRobot.this.slider2.setValue((int)state.getElbowPos());
/* 226 */               JRobot.this.slider3.setValue((int)state.getPitchPos());
/* 227 */               JRobot.this.slider4.setValue((int)state.getRollPos());
/* 228 */               JRobot.this.speedSlider.setValue(state.getSpeed());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 235 */               JRobot.this.blockSlidersEvent = false;
/* 236 */               JRobot.this.inputsEnabled(true);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void busy() {
/* 242 */             JRobot.this.signal.setRed();
/*     */           }
/*     */           
/*     */           public void error(int errType, String command, String message) {
/* 246 */             JRobot.this.rvm1.getCommandQueue().purge();
/* 247 */             JRobot.this.errorMessage(command + "\n" + command);
/* 248 */             JRobot.this.inputsEnabled(true);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 253 */     inputsEnabled(false);
/*     */     
/* 255 */     setSize(1000, 700);
/* 256 */     Dimension d1 = getToolkit().getScreenSize();
/* 257 */     Dimension d2 = getSize();
/* 258 */     setLocation(d1.width / 2 - d2.width / 2, d1.height / 2 - d2.height / 2);
/*     */     
/* 260 */     show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 268 */     String command = e.getActionCommand();
/* 269 */     if (command.equals("joystick")) {
/* 270 */       this.joystickButton.setText("Keyboard Control");
/* 271 */       this.joystickButton.setActionCommand("keyboard");
/*     */       
/* 273 */       this.jconf = new JDialog(this, "Setting to Joystick Control", true);
/* 274 */       this.jconf.setDefaultCloseOperation(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 290 */     else if (command.equals("keyboard")) {
/* 291 */       this.joystickButton.setText("Joystick Control");
/* 292 */       this.joystickButton.setActionCommand("joystick");
/*     */ 
/*     */ 
/*     */       
/* 296 */       msgMessage("Setting to Keyboard Control");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 305 */       this.p1.add(this.p_sliders, 0);
/* 306 */       this.p1.doLayout();
/* 307 */       this.p_sliders.doLayout();
/* 308 */       this.pliers.doLayout();
/*     */     }
/* 310 */     else if (command.equals("ok_j")) {
/* 311 */       System.out.println("Chosen a joystick");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 339 */     else if (command.equals("cancel_j")) {
/* 340 */       System.out.println("NO joystick");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 346 */       this.jconf.dispose();
/* 347 */       this.joystickButton.setText("Joystick Control");
/* 348 */       this.joystickButton.setActionCommand("joystick");
/*     */     }
/* 350 */     else if (command.equals("about")) {
/* 351 */       ImageIcon icon = new ImageIcon("jrobot/cui.gif");
/* 352 */       JOptionPane.showMessageDialog(this, "JRobot v.1.0\nUniversity of Geneva\nComputer Science Department\nApril 2002", "About JRobot", 1, icon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 363 */     if (args.length == 0) {
/* 364 */       System.out.println("Welcome to JRobot, please read instructions\n");
/* 365 */       System.out.println("JVM execution");
/* 366 */       System.out.println("\t>java");
/* 367 */       System.out.println("\t>java -Dj3d.sharedctx=false  (if you observe abnormal crashes)\n");
/*     */       
/* 369 */       System.out.println("Program Execution\n");
/* 370 */       System.out.println("\t>java JRobot <execution mode> <parameters separated by space>\n");
/*     */       
/* 372 */       System.out.println("Execution Mode\n");
/* 373 */       System.out.println("\ts\tSimulation Mode (Connection with robot is simulated)");
/* 374 */       System.out.println("\tl\tNormal Mode (Robot is connected to serial com)");
/* 375 */       System.out.println("\tr\tRemote Mode (Robot connection is through IP + port, see Parameters)\n");
/*     */       
/* 377 */       System.out.println("Parameters (only remote mode)\n");
/* 378 */       System.out.println("\tIP\t(RVM1 Server Name or IP number)");
/* 379 */       System.out.println("\tPort\t(Port number)\n");
/*     */       
/* 381 */       System.exit(0);
/*     */     } 
/* 383 */     new JRobot(args);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initRobot() {
/* 388 */     inputsEnabled(false);
/* 389 */     this.rvm1.nest();
/* 390 */     this.rvm1.speed(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void inputsEnabled(boolean state) {
/* 395 */     this.slider0.setEnabled(state);
/* 396 */     this.slider1.setEnabled(state);
/* 397 */     this.slider2.setEnabled(state);
/* 398 */     this.slider3.setEnabled(state);
/* 399 */     this.slider4.setEnabled(state);
/* 400 */     this.pliers.setEnabled(state);
/* 401 */     this.speedSlider.setEnabled(state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void errorMessage(String error) {
/* 406 */     JOptionPane.showMessageDialog(null, error, "Error", 0);
/*     */   }
/*     */   
/*     */   private void msgMessage(String msg) {
/* 410 */     JOptionPane.showMessageDialog(null, msg, "MEssage", 1);
/*     */   }
/*     */ }


/* Location:              /home/jim_bug/rvm-robot/!/jrobot/JRobot.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */