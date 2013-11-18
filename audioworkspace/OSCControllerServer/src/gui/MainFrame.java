package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SpinnerListModel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import com.illposed.osc.OSCPortOut;

import osccontroller.IController;
import osccontroller.OSCSender;
import osccontroller.SpeedDistanceControll;
import osccontroller.PlaceParameter;
import oscreceiver.Receiver;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainFrame extends JFrame {

	//Wonder Sender Daten
	private static String wonderIP = "192.168.10.40";
	private static int wonderPort = 58100;
	
	//Cubase Sender Daten
	private static String cubaseIP = "localhost";
	private static int cubasePort = 10001;
	
	//Controller Receiver Daten
	private static String receiverPath = "/speed";
	private static int receiverPort = 25000;

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private String oldTextField;
	private static DefaultTableModel model;
	private static IController controller;
	private static MainFrame frame;
	private static Receiver receiver;
	private static OSCSender sender;
	private Boolean editColumn = false;
	private int alterColumn = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			sender = new OSCSender(
					new OSCPortOut(InetAddress.getByName(wonderIP), wonderPort), 
					new OSCPortOut(InetAddress.getByName(cubaseIP), cubasePort));
			controller = new SpeedDistanceControll(sender);
		} catch (SocketException | UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		receiver = new Receiver(controller, receiverPort, receiverPath);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("OSC-ControllerServer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 448);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenue = new JMenu("Menu");
		menuBar.add(mnMenue);
		
		JMenuItem mntmBeenden = new JMenuItem("Exit");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenuItem mntmConnections = new JMenuItem("Connections");
		mntmConnections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JDialog dia = new JDialog();
				dia.getContentPane().setLayout(new MigLayout("", "[90px, grow][90px,grow]", "[20px][20px][20px][20px][20px][20px][20px]"));
				
				dia.getContentPane().add(new JLabel("Cubase IP"),  "cell 0 0,grow");
				final JTextField cub_ip = new JTextField(cubaseIP);
				dia.getContentPane().add(cub_ip,  "cell 1 0,grow");
				dia.getContentPane().add(new JLabel("Cubase Port"),  "cell 0 1,grow");
				final JTextField cub_port = new JTextField(""+cubasePort);
				dia.getContentPane().add(cub_port,  "cell 1 1,grow");
				
				dia.getContentPane().add(new JLabel("Wonder IP"),  "cell 0 2,grow");
				final JTextField won_ip = new JTextField(wonderIP);
				dia.getContentPane().add(won_ip,  "cell 1 2,grow");
				dia.getContentPane().add(new JLabel("Wonder Port"),  "cell 0 3,grow");
				final JTextField won_port = new JTextField(""+wonderPort);
				dia.getContentPane().add(won_port,  "cell 1 3,grow");
				
				dia.getContentPane().add(new JLabel("Local Path"),  "cell 0 4,grow");
				final JTextField clientPath = new JTextField(receiverPath);
				dia.getContentPane().add(clientPath,  "cell 1 4,grow");
				dia.getContentPane().add(new JLabel("Local Port"),  "cell 0 5,grow");
				final JTextField clientPort = new JTextField(""+receiverPort);
				dia.getContentPane().add(clientPort,  "cell 1 5,grow");
				
				JButton btnAccept = new JButton("Apply");
				btnAccept.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cubaseIP = cub_ip.getText();
						cubasePort = Integer.parseInt(cub_port.getText());
						
						wonderIP = won_ip.getText();
						wonderPort = Integer.parseInt(won_port.getText());
						
						receiverPath = clientPath.getText();
						receiverPort = Integer.parseInt(clientPort.getText());
						
						//update Sender
						try {
							sender.setCubase(new OSCPortOut(InetAddress.getByName(cubaseIP), cubasePort));
							sender.setWonder(new OSCPortOut(InetAddress.getByName(wonderIP), wonderPort));
						} catch (SocketException | UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//update receiver
						receiver.updateReceiver(receiverPort, receiverPath);
						
						dia.setVisible(false);
						dia.dispose();
					}
				});
				dia.getContentPane().add(btnAccept,  "cell 1 6,grow");
				
				JButton btnDecline = new JButton("Cancel");
				btnDecline.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dia.setVisible(false);
						dia.dispose();
					}
				});
				dia.getContentPane().add(btnDecline,  "cell 0 6,grow");
				dia.setSize(300, 220);
				dia.setLocation(300, 150);
				dia.setVisible(true);
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JDialog dia = new JDialog();
				dia.getContentPane().setLayout(new MigLayout("", "[90px, grow][90px,grow]", "[20px][20px][20px][20px][20px][20px][20px]"));
				dia.getContentPane().add(new JLabel("Path"),  "cell 0 0,grow");
				final JTextField savePath = new JTextField("/home/user/save.txt");
				dia.getContentPane().add(savePath,  "cell 1 0,grow");
				JButton btnBrowse = new JButton("Browse");
				dia.getContentPane().add(btnBrowse, "cell 2 0, grow");
				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser choose = new JFileChooser();
						
						choose.setFileFilter(new FileFilter() {
							
							@Override
							public String getDescription() {
								return "Textdatei *.txt";
							}
							
							@Override
							public boolean accept(File f) {
								return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
							}
						});
						int state = choose.showOpenDialog(null);
						if (state == JFileChooser.APPROVE_OPTION) {
							savePath.setText(choose.getSelectedFile().getAbsolutePath());
						}
						
					}
				});
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							FileOutputStream file = new FileOutputStream(new File(savePath.getText()));
							ObjectOutputStream objectOutput = new ObjectOutputStream(file);
							objectOutput.writeObject(wonderIP);
							objectOutput.writeObject(wonderPort);
							objectOutput.writeObject(cubaseIP);
							objectOutput.writeObject(cubasePort);
							objectOutput.writeObject(receiverPath);
							objectOutput.writeObject(receiverPort);
							objectOutput.writeObject(controller.getTrackList());
							objectOutput.flush();
							objectOutput.close();
							dia.setVisible(false);
							dia.dispose();
						} catch (IOException ex) {
							System.out.println(ex);
						}
					}
				});
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dia.setVisible(false);
						dia.dispose();
					}
				});
				dia.getContentPane().add(btnClose, "cell 1 1, grow");
				dia.getContentPane().add(btnSave, "cell 0 1, grow");
				dia.setSize(500, 100);
				dia.setLocation(300, 150);
				dia.setVisible(true);
			}
		});
		mnMenue.add(mntmSave);
	
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JDialog dia = new JDialog();
				dia.getContentPane().setLayout(new MigLayout("", "[90px, grow][90px,grow]", "[20px][20px][20px][20px][20px][20px][20px]"));
				dia.getContentPane().add(new JLabel("Path"),  "cell 0 0,grow");
				final JTextField loadPath = new JTextField("/home/user/save.txt");
				dia.getContentPane().add(loadPath,  "cell 1 0,grow");
				JButton btnBrowse = new JButton("Browse");
				dia.getContentPane().add(btnBrowse, "cell 2 0, grow");
				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser choose = new JFileChooser();
						
						choose.setFileFilter(new FileFilter() {
							
							@Override
							public String getDescription() {
								return "Textdatei *.txt";
							}
							
							@Override
							public boolean accept(File f) {
								return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
							}
						});
						int state = choose.showOpenDialog(null);
						if (state == JFileChooser.APPROVE_OPTION) {
							loadPath.setText(choose.getSelectedFile().getAbsolutePath());
						}
						
					}
				});
				
				JButton btnLoad = new JButton("Load");
				btnLoad.addActionListener(new ActionListener() {
					@SuppressWarnings("unchecked")
					public void actionPerformed(ActionEvent e) {
						try {
							FileInputStream file = new FileInputStream(new File(loadPath.getText()));
							ObjectInputStream objectInput = new ObjectInputStream(file);
							wonderIP = (String)objectInput.readObject();
							wonderPort = (int)objectInput.readObject();
							cubaseIP = (String)objectInput.readObject();
							cubasePort = (int)objectInput.readObject();
							receiverPath = (String)objectInput.readObject();
							receiverPort = (int)objectInput.readObject();
							//update Sender
							try {
								sender.setCubase(new OSCPortOut(InetAddress.getByName(cubaseIP), cubasePort));
								sender.setWonder(new OSCPortOut(InetAddress.getByName(wonderIP), wonderPort));
							} catch (SocketException | UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//update receiver
							receiver.updateReceiver(receiverPort, receiverPath);
							controller.setTrackList((List<PlaceParameter>)objectInput.readObject());
							objectInput.close();
							for(int i = model.getRowCount() - 1; i >= 0 ; i--) {
								model.removeRow(i);
							}
							for(int i = 0; i < controller.getTrackList().size(); i++) {
								model.addRow(new Object[] {controller.getTrack(i).getOscPath(), controller.getTrack(i).getWonderID(), controller.getTrack(i).getPlaceMeter() / 1000.0F, controller.getTrack(i).getSide()});	
							}
							dia.setVisible(false);
							dia.dispose();
						} catch (IOException ex) {
							System.out.println(ex);
						} catch (ClassNotFoundException e1) {
							System.out.println(e1);
						}
					}
				});
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dia.setVisible(false);
						dia.dispose();
					}
				});
				dia.getContentPane().add(btnClose, "cell 1 1, grow");
				dia.getContentPane().add(btnLoad, "cell 0 1, grow");
				dia.setSize(500, 100);
				dia.setLocation(300, 150);
				dia.setVisible(true);
			}
		});
		
		mnMenue.add(mntmLoad);
		mnMenue.add(mntmConnections);
		
		mnMenue.add(mntmBeenden);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {

					PlaceParameter place = controller.getTrack((table.getSelectedRow()));
					textField.setText(place.getOscPath());
					oldTextField = textField.getText();
					spinner.setValue(place.getWonderID());
					spinner_1.setValue(place.getPlaceMeter() / 1000.0F);
					spinner_2.setValue(place.getSide());
					alterColumn = table.getSelectedRow();
					editColumn = true;
				}
					
			}
		});
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"OSC Pfad", "Wonder ID", "Ab Kilometer", "Seite"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Float.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		((JLabel)table.getDefaultRenderer(String.class)).setHorizontalAlignment (JLabel.RIGHT);  
		splitPane.setRightComponent(new JScrollPane(table));
		model.removeRow(0);
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		
		JLabel lblNewLabel = new JLabel("OSC Path");
		
		textField = new JTextField();
		
		JLabel lblWonderId = new JLabel("Wonder Src-ID");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		
		JLabel lblAbKilometer = new JLabel("Position (KM)");
		
		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Float(1.2), new Float(1.2), null, new Float(0.1)));
		
		JLabel lblSeite = new JLabel("Passing Side");
		
		spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerListModel(new String[] {"left", "right"}));
		panel.setLayout(new MigLayout("", "[100px][100px]", "[20px][20px][20px][20px][][][][][][][][][]"));
		
		panel.add(lblNewLabel, "cell 0 0,grow");
		panel.add(textField, "cell 1 0,grow");
		panel.add(lblWonderId, "cell 0 1,grow");
		panel.add(spinner, "cell 1 1,grow");
		panel.add(lblAbKilometer, "cell 0 2,grow");
		panel.add(spinner_1, "cell 1 2,grow");
		panel.add(lblSeite, "cell 0 3,grow");
		panel.add(spinner_2, "cell 1 3,grow");
		
		final JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.findTrackByName(textField.getText()) == 0) {
					model.addRow(new Object[] {textField.getText(), spinner.getValue(), spinner_1.getValue(), spinner_2.getValue()});
					controller.addTrack(textField.getText(), (Integer)spinner.getValue(), (float)spinner_1.getValue(), (String)spinner_2.getValue());
				}
			}
		});
		
		final JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editColumn && controller.findTrackByName(textField.getText()) == 0 || oldTextField.equals(textField.getText()) ) {
					model.removeRow(alterColumn);
					model.insertRow(alterColumn, new Object[] {textField.getText(), spinner.getValue(), spinner_1.getValue(), spinner_2.getValue()});
					controller.deleteTrackByName(oldTextField);
					controller.addTrack(textField.getText(), (Integer)spinner.getValue(), (float)spinner_1.getValue(), (String)spinner_2.getValue());
					alterColumn = 0;
					editColumn = false;
				}
			}
		});
		panel.add(btnEdit, "cell 0 4, grow");
		panel.add(btnApply, "cell 1 4, grow");
		
		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (alterColumn > 0) {
					
					if (controller.deleteTrackByName(textField.getText()))
					{
						model.removeRow(alterColumn);	
					}
				}
			}
		});
		panel.add(btnRemove, "cell 0 5, grow");
		
		JLabel lblSpeedscaler = new JLabel("SpeedScaler");
		panel.add(lblSpeedscaler, "cell 0 6,grow");
		
		final JSpinner spinner_4 = new JSpinner();
		spinner_4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				controller.updateSpeedScaler((float)spinner_4.getValue());
			}
		});
		spinner_4.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(2), new Float(0.1)));
		panel.add(spinner_4, "cell 1 6,grow");
		
		final JButton btnStart = new JButton("Start");

		panel.add(btnStart, "cell 0 8, grow");
		
		final JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.stopDriving();
				controller.allPluginsSilent();
				btnApply.setEnabled(true);
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				btnEdit.setEnabled(true);
				btnRemove.setEnabled(true);
			}
		});
		btnStop.setEnabled(false);
		panel.add(btnStop, "cell 1 8, grow");
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startDriving();
				btnApply.setEnabled(false);
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				btnEdit.setEnabled(false);
				btnRemove.setEnabled(false);
			}
		});
		
		JLabel lblGeschw = new JLabel("Speed (KM/h)");
		final JSpinner spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.1)));
		final JButton btnFake = new JButton("Fake");
		panel.add(lblGeschw, "cell 0 12,grow");
		panel.add(spinner_3, "cell 1 12, grow");
		panel.add(btnFake, "cell 1 13, grow");
		
		btnFake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.updateSpeed((float)spinner_3.getValue());
			}
		});
		
	}
	
	public static void refreshGUI () {
		for(int i = model.getRowCount() - 1; i >= 0 ; i--) {
			model.removeRow(i);
		}
		for(int i = 0; i < controller.getTrackList().size(); i++) {
			model.addRow(new Object[] {controller.getTrack(i).getOscPath(), controller.getTrack(i).getWonderID(), controller.getTrack(i).getPlaceMeter() / 1000.0F, controller.getTrack(i).getSide()});	
		}
	}
}



