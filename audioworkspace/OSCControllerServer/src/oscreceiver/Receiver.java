package oscreceiver;

import java.net.SocketException;

import osccontroller.IController;

import com.illposed.osc.*;

/**
 * Klasse zum Empfangen von OSC-Nachrichten des E-Technik Laptops und uebermitteln der Geschwindigkeit an die Controller
 *
 */
public class Receiver {
	
	private OSCPortIn receiver;
	OSCListener listener;

	public Receiver (IController controll, int port, String path) {
		
		final IController controller = controll;
		
		//Empfaenger starten, laeuft im separaten Thread
		try {
			receiver = new OSCPortIn(port);
			
			listener = new OSCListener() {
				
				public void acceptMessage(java.util.Date time, OSCMessage message) {
					controller.updateSpeed((float)message.getArguments()[0]);
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
