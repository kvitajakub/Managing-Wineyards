package pdb03.model;

/**
 * Obsahuje data potrebne pre vypocet obsahu odrod
 * @author peterson
 *
 */
public class WineyardAreaLength {
	private double wineyard_area;
	private double wineyard_length;
	
	public WineyardAreaLength(double area, double length) {
		wineyard_area = area;
		wineyard_length = length;
	}
	
	public double getArea() {
		return wineyard_area;
	}
	
	public double getLength() {
		return wineyard_length;
	}
	
}
