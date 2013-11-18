package osccontroller;

import java.util.List;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class OSCSender {
	// OSC SPort zum senden von Nachrichten an Wonder
	private OSCPortOut wonder;
	// OSC SPort zum senden von Nachrichten an Cubase
	private OSCPortOut cubase;
	
	public OSCSender(OSCPortOut wonder, OSCPortOut cubase) {
		this.cubase = cubase;
		this.wonder = wonder;
	}
	
	/**
	 * @param wonder the wonder to set
	 */
	public void setWonder(OSCPortOut wonder) {
		this.wonder.close();
		this.wonder = wonder;
	}

	/**
	 * @param cubase the cubase to set
	 */
	public void setCubase(OSCPortOut cubase) {
		this.cubase.close();
		this.cubase = cubase;
	}
	
	/**
	 * benutzt eine Objekt Liste zum Versenden von OSC Nachrichten
	 * @param path OSC - Path
	 * @param msg Objekt Array mit Nachrichten
	 */
	public void sendWonder (String path, List<Object> msg) {
		 
		 try {
			wonder.send(new OSCMessage(path, msg));
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
		 }
	}
	
	/**
	 * benutzt eine Objekt Liste zum Versenden von OSC Nachrichten
	 * @param path OSC - Path
	 * @param msg Objekt Array mit Nachrichten
	 */
	public void sendCubase (String path, List<Object> msg) {
		 
		 try {
			cubase.send(new OSCMessage(path, msg));
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
		 }
	}
}
