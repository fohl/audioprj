package osccontroller;

import java.util.List;

/**
 * Interface fuer die Methoden, die die GUI und der Receiver zum uebermitteln der Daten an den Controller verwenden
 * @author lucas
 *
 */
public interface IController {

	// Zeit die bis zur Streckenberechnung gefahren wird
	final int _DRIVINGTIME = 100;
	
	/**
	 * Methode zum hinzufuegen von SoundSpuren.
	 *  
	 * @param[in] oscPath
	 * 		Verbindungs Path zu einem OSC - Plugin
	 * @param[in] wonderID
	 * 		ID zur Indentifizierung einer Wonder-Quelle
	 * @param[in] placeByKM
	 * 		Standort in Kilometern eines virtuellen Ortes
	 * @param[in] side
	 * 		Seite der Platzierung der Quelle (links oder rechts) in Bezug auf den Hoehrer
	 */
	public void addTrack (String oscPath, int wonderID, float placeByKM, String side);
	
	/**
	 * Mutet alle eingetragenen Cubase Plugins
	 */
	public void allPluginsSilent ();
	
	/**
	 * Aktualisiert den Speed Scaler
	 * 
	 * @param scaler
	 */
	public void updateSpeedScaler(float scaler);
	
	/**
	 * Gibt die aktuelle Geschwindigkeit zurueck
	 * 
	 * @return Speed
	 */
	public float getSpeed ();
	
	/**
	 * Durchsucht die Liste der Placeparameter nach einem OSC Path und gibt den Idex zurueck
	 * 
	 * @param oscPath
	 */
	public int findTrackByName(String oscPath);
	
	/**
	 * Loescht einen Placeparameter identifiziert durch seinen OSC Path
	 * 
	 * @param oscPath
	 */
	public Boolean deleteTrackByName(String oscPath);
	/**
	 * Liefert einen PlaceParameter aus der Liste am angegebenen Index
	 * 
	 * @param[in] index
	 * 
	 * @return PlaceParameter
	 */
	public PlaceParameter getTrack (int index);
	
	/**
	 * Liefert eine Liste der aktuellen PlaceParameter
	 * 
	 * @return Liste<PlaceParameter>
	 */
	public List<PlaceParameter> getTrackList();
	
	/**
	 * Setzt die Liste der PlaceParameter neu
	 * 
	 * @param newListe<PlaceParameter>
	 */
	public void setTrackList(List<PlaceParameter> newListe);
	
	/**
	 * Methode zum updaten der Entfernung
	 * 
	 * @param speed - Aktuelle Geschwindigkeit des Autos
	 * @param time - Zeitpunkt der Messung 
	 */
	public void updateSpeed (float speed);
	
	/**
	 * Startet die Fahrt des virtuellen Autos.
	 */
	public void startDriving ();
	
	/**
	 * Stoppt die Fahrt des virtuellen Autos.
	 */
	public void stopDriving ();
}
