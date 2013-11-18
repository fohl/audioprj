package oscreceiver;

import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Date;

import com.illposed.osc.*;

/**
 * Klasse zum Empfangen von OSC-Nachrichten des E-Technik Laptops und uebermitteln der Geschwindigkeit an die Controller
 *
 */
public class Receiver {
	
	private OSCPortIn receiver;
	private OSCListener listener;
	private java.util.Date date= new java.util.Date();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 0 && args[0].equals("--help")){
			System.out.println("Usage: java -jar WonderSimulator.jar <Listen-Port>");
			System.out.println("OSC-Listen-Path is /WONDER/source/position");
		} else if(args.length != 0) {
			Receiver rec = new Receiver(Integer.parseInt(args[0]), "/WONDER/source/position");
		}
	}
	
	public Receiver (int port, String path) {
		
		//Empfaenger starten, laeuft im separaten Thread
		try {
			receiver = new OSCPortIn(port);
			
			listener = new OSCListener() {
				
				public void acceptMessage(java.util.Date time, OSCMessage message) {
					date = new Date();
					System.out.println(new Timestamp(date.getTime())+": Got a Message: " +
							"ID: "+(int)message.getArguments()[0]+" "+ 
							"X: "+(float)message.getArguments()[1]+" "+ 
							"Y: "+(float)message.getArguments()[2]+" "+
							"Z: "+(float)message.getArguments()[3]+" "+ 
							"Time: "+(float)message.getArguments()[4]+" "+ 
							"Duration: "+(float)message.getArguments()[5]);
				}
			};
			
			//Pfad auf den gehoert wird (alle)
			receiver.addListener(path, listener);
			receiver.startListening();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Setzt den Port und den Path neu auf den der OSC Empfaenger hoert
	 * 
	 * @param port
	 */
	public void updateReceiver (int port, String path) {
		try {
			receiver.stopListening();
			receiver.close();
			receiver = new OSCPortIn(port);
			receiver.addListener(path, listener);
			receiver.startListening();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schaltet den Listener wieder aus
	 */
	public void exit() {
		receiver.stopListening();
	}

}
