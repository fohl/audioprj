package osccontroller;

import java.util.Comparator;

public class PlaceComparator implements Comparator<PlaceParameter> {

	/**
	 * Ueberschreibt die Methode compare aus Comparator
	 * Vergleicht zwei Kilometerangaben.
	 * 
	 * @return
	 * 		Vergleichsergebnis
	 */
	public int compare (PlaceParameter p1, PlaceParameter p2) {
		    	
		float tmp = p1.getPlaceMeter() - p2.getPlaceMeter();
		
		return (tmp > 0) ? 1 : (tmp == 0) ? 0 : -1;
    }
}


