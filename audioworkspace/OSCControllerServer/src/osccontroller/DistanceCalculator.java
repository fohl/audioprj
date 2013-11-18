package osccontroller;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;


/**
 * Klasse zur Berechnung der Strecke die ein virtuelles Auto
 * zurueck gelegt hat.
 * 
 * @author alex
 */
public class DistanceCalculator extends TimerTask {
	
	// Liste mit Orten die virtuell angefahren werden
	private List<PlaceParameter> places;
	// Geschwindigkeit des virtuellen Autos
	private float currentSpeed;
	// Gefahrene Strecke
	private float distanceDriving;
	// Zeit die bis zur Streckenberechnung gefahren wird
	private int _DRIVINGTIME;
	// Aktuelle Position des Autos
	private int position;
	// OSC Sender zum senden von Nachrichten
	private OSCSender sender;
	// Initialposition an der die Soundquelle ein oder ausgeblendet wird 
	private float fadePosition;
	// Offset der auf die fadePosition gerechnet wird
	private float kmOffset;
	//speed Scaler
	private float scaler;
	//wonder posi
	public static final float wonderRight = 7;
	public static final float wonderLeft = -7;
	
	public static final int START_MOV_DIST = 450;	
	public static final float MOV_START_POINT = -12.0f;	
	
	/**
	 * Konstruktor
	 */
	public DistanceCalculator (int dTime, OSCSender sender, float kmOffset) {
		
		this._DRIVINGTIME = dTime;
		
		this.places = new LinkedList<PlaceParameter>();
		this.currentSpeed = 0.0F;
		
		this.distanceDriving = 0.0F;
		this.position = 0;
		
		this.sender = sender;
		
		this.fadePosition = 0.0F;
		
		this.kmOffset = kmOffset;
		
		this.scaler = 1.0F;
	}
	
	/**
	 * Setzt die Liste mit Parametern eines Ortes.
	 * 
	 * @param places
	 * 		Liste mit Parametern eines Ortes.
	 */
	public void setPlaces (List<PlaceParameter> placeParams) {
		
		this.places.add(new PlaceParameter("/sdfkjgsdflk", -1, 0.0F, "", 0.0F, (0.0F) + (2 * kmOffset)));
		this.places.addAll(placeParams);
		
		//this.places = placeParams;
	}
	
	/**
	 * gibt die aktuelle Geschwindigkeit zurueck
	 * 
	 * @return Geschwindigkeit
	 */
	public float getSpeed () {
		return this.currentSpeed;
	}
	
	public void setScaler (float scaler) {
		this.scaler = scaler;
	}
	
	/**
	 * Setzt die Geschwindigkeit neu.
	 * 
	 * @param speed
	 * 		Geschwindigkeit in km/h
	 */
	public void setSpeed (float speed) {
		
		// speed 0.5 entspricht 50 km/h
		this.currentSpeed = (speed * 100) * scaler;
	}
	
	/**
	 * 
	 */
	public void run () {
		
		List<Object> args = new LinkedList<Object>();
		
		int nextPos = this.position + 1;
		int prevPos = this.position - 1;
		
		if ((this.position + 1) >= this.places.size())
			nextPos = 0;
		if (prevPos < 0) 
			prevPos = this.places.size() - 1;
		
		// Strecke berechnen - s = v * t
		//speedvariante
		this.distanceDriving += (this.currentSpeed * (((float)_DRIVINGTIME)/1000.0F));
		//realistisch
		//this.distanceDriving += ((this.currentSpeed / 3600) * (((float)_DRIVINGTIME)/1000.0F)) * 1000.0F;
		
		// Wenn Ende der Liste erreicht, fang von vorne an
		if (this.position >= this.places.size()) {
			
			this.position = 0;
			this.distanceDriving = 0.0F; 
		}

		System.out.println("Distance: "+this.distanceDriving);
		
		// Quellen fliegen durch den Raum
		if ((this.distanceDriving >= (this.places.get(position).getPlaceMeter() - START_MOV_DIST)) && (this.distanceDriving <= (this.places.get(position).getPlaceMeter() + START_MOV_DIST))) {
			
			//gefahrene Strecke im Intervall +-180 Meter um die akt. Quelle
			float drivDist;
			//if(position == 0){
			//	drivDist = this.distanceDriving+90;
			//} else {
				drivDist = (this.distanceDriving - (this.places.get(position).getPlaceMeter() - START_MOV_DIST)) / 3;
			//}
			
			//gefahrene Strecke umrechnen in Wonder-Strecke
			float targetDist = (float)((drivDist/10.0) + MOV_START_POINT);
			
			System.out.println("Source moving to: "+targetDist);
			
			//Wonder bespassen
			args.clear();
			args.add(this.places.get(position).getWonderID());
			if(this.places.get(position).isLeft()) {
				args.add(wonderLeft);
			} else {
				args.add(wonderRight);
			}
			args.add(targetDist);
			args.add(0.0f);
			args.add(0.0f);
			args.add(0.1f);
			sender.sendWonder("/WONDER/source/position", args);
			
		} 
		
		// Spuren ein und ausblenden
		if ((this.distanceDriving >= this.places.get(position).getStartMeter()) && (this.distanceDriving <= this.places.get(position).getEndMeter())) {
			
			System.out.println("x-fading tracks");
			
			this.fadePosition = this.distanceDriving - this.places.get(position).getStartMeter();
			// Lautstaerke altes Plugin aus
			args.clear();
			args.add(1 - (this.fadePosition/(2 * this.kmOffset)));
			sender.sendCubase(this.places.get(this.position).getOscPath(), args);

			// Lautstaerke neues Plugin ein
			args.clear();
			args.add((this.fadePosition/(2 * this.kmOffset)));
			sender.sendCubase(this.places.get(nextPos).getOscPath(), args);
		} else if(this.distanceDriving > this.places.get(position).getEndMeter()) {
			
			// Quelle zuruecksetzen
			args.clear();
			args.add(this.places.get(position).getWonderID());
			if(this.places.get(position).isLeft()) {
				args.add(wonderLeft);
			} else {
				args.add(wonderRight);
			}
			args.add(MOV_START_POINT);
			args.add(0.0f);
			args.add(0.0f);
			args.add(0.0f);
			sender.sendWonder("/WONDER/source/position", args);
			
			this.position++;
		} 

		
	}

}
