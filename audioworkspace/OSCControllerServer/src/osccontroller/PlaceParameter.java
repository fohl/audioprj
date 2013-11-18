package osccontroller;

import java.io.Serializable;

/**
 * Klasse zur Verwaltung der Parameter eines Platzes der Soundquelle
 * 
 * @author Julia
 *
 */
public class PlaceParameter implements Serializable {
	
	// Verbindungs Path zu einem OSC - Plugin
	private String oscPath;
	// Standort in Kilometern eines virtuellen Ortes
	private float placeMeter;
	// ID zur Indentifizierung einer Wonder-Quelle
	private int wonderID;
	// Seite der Platzierung der Quelle (links oder rechts) in Bezug auf den Hoehrer
	private String side;
	// Boolean zur Seitenbestimmung zur ressourcensparenden Abfrage
	private boolean isLeft = false;
	// Startkilometer wo die ausblendung der aktuellen Quelle beginnt und das einblenden der nachfolgenden
	private float startMeter;
	// Endkilometer wo die ausblendung der aktuellen Quelle beginnt und das einblenden der nachfolgenden
	private float endMeter;
	
	/**
	 * Konstruktor.
	 * Setzt die Parameter eines virtuellen Ortes.
	 *  
	 * @param oscPath
	 * 		Verbindungs Path zu einem OSC - Plugin
	 * @param wonderID
	 * 		ID zur Indentifizierung einer Wonder-Quelle
	 * @param placeByKM
	 * 		Standort in Kilometern eines virtuellen Ortes
	 * @param side
	 * 		Seite der Platzierung der Quelle (links oder rechts) in Bezug auf den Hoehrer
	 */
	public PlaceParameter (String oscPath, int wonderID, float placeByKM, String side, float startKM, float endKM) {
		this.oscPath = oscPath;
		this.wonderID = wonderID;
		this.placeMeter = placeByKM;
		this.side = side;
		this.endMeter = endKM;
		this.startMeter = startKM;
		if(this.side.equals("left")) {
			isLeft = true;
		}
	}

	/**
	 * getOscPath
	 * Liefert den Pfad an den die OSC Nachricht gesendet wird als String zurueck
	 * 
	 * @return OSC Path - String
	 */
	public String getOscPath() {
		return oscPath;
	}

	/**
	 * setOscPath
	 * Setzt den OSC Pfad an den Nachrichten gesendet werden sollen
	 * 
	 * @param oscPath
	 */
	public void setOscPath(String oscPath) {
		this.oscPath = oscPath;
	}

	/**
	 * getPlaceByKM
	 * liefert die Kilometeranzahl nach der eine Soundquelle angeschaltet werden soll
	 * 
	 * @return Kilometer
	 */
	public float getPlaceMeter() {
		return placeMeter;
	}
	
	/**
	 * setPlaceByKM
	 * setzt die Kilometeranzahl nach der eine Soundquelle angeschaltet werden soll
	 * 
	 * @param placeByKM
	 */
	public void setPlaceMeter(float placeByKM) {
		this.placeMeter = placeByKM;
	}

	/**
	 * getWonderID
	 * liefert die Wonder ID die zur Identifizierung der einzelnen Wonder Quellen verwendet wird.
	 * 
	 * @return wonderID
	 */
	public int getWonderID() {
		return wonderID;
	}

	/**
	 * setWonderID
	 * setzt die Wonder ID die zur Identifizierung der einzelnen Wonder Quellen verwendet wird.
	 * 
	 * @param wonderID
	 */
	public void setWonderID(int wonderID) {
		this.wonderID = wonderID;
	}

	/**
	 * getSide
	 * liefert die Seite (links oder rechts) an der die Quelle vorbeilaufen soll
	 * 
	 * @return Seite
	 */
	public String getSide() {
		return side;
	}

	/**
	 * setSide
	 * setzt die Seite (links oder rechts) an der die Quelle vorbeilaufen soll
	 * 
	 * @param side
	 */
	public void setSide(String side) {
		this.side = side;
		if(this.side.equals("left")) {
			isLeft = true;
		}
	}

	/**
	 * @return the startKM
	 */
	public float getStartMeter() {
		return startMeter;
	}

	/**
	 * @param startKM the startKM to set
	 */
	public void setStartMeter(float startKM) {
		this.startMeter = startKM;
	}

	/**
	 * @return the endKM
	 */
	public float getEndMeter() {
		return endMeter;
	}

	/**
	 * @param endKM the endKM to set
	 */
	public void setEndMeter(float endKM) {
		this.endMeter = endKM;
	}
	
	/**
	 * @param is left?
	 */
	public boolean isLeft() {
		return isLeft;
	}
}
