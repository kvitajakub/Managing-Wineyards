package pdb03.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import oracle.spatial.geometry.JGeometry;

/**
 * Trida pro jeden radek tabulky Spatial_Item.
 * @author Jan Bednarik
 *
 */
public class SpatialObject {
	
	private int id;
	private int valid_from_year;
	private int valid_to_year;
	private String object_type;
	private int wine_grower_id;
	private int wine_type_id;
		
	private boolean new_obj;	// set true for newly created object
	
	private boolean selectedFull;
	private Point2D selectedPoint;
	
	//prepis JGeometry na data pristupna a modifikovatelna
	private int geomType;
	private ArrayList<ArrayList<Point2D>> points; //prvni pole je na multielementy, druhe pole na jednotlive body elementu
	private int newObjCount;  //counter na pocet objektu v tomto objektu (multipolygony a multicary)
	
	/**
	 * Novy zaznam prostoroveho objektu vytvoreny z dat stazenych z databaze.
	 * @param id ID objektu v databazi.
	 * @param valid_from Od kdy je validni.
	 * @param valid_to Do kdy je validni.
	 * @param object_type Druh objektu.
	 * @param geometry Jeho geometrie.
	 * @param wine_grower_id Majitel, jestli nejakeho ma, jinak null.
	 * @param wine_type_id Druh vina co na nem roste, jinak null.
	 */
	public SpatialObject(int id, int valid_from, int valid_to, String object_type, JGeometry geometry,
			int wine_grower_id, int wine_type_id) {
		this.id = id;
		this.valid_from_year = valid_from;
		this.valid_to_year = valid_to;
		this.object_type = object_type;
		this.wine_grower_id = wine_grower_id;
		this.wine_type_id = wine_type_id;
		this.new_obj = false;
		this.selectedFull = false;
						
		this.geomType = geometry.getType();
				
		this.points = convertJGeometryToPoints(geometry);
	}

///////////////////////////////////////////////
	/**
	 * Vytvoreni noveho objektu v aplikaci
	 * @param valid Rok ve kterem je validni.
	 * @param geomType Jeho druh.
	 * @param p Prvni bod ktery byl vlozeny.
	 */
	public SpatialObject(int valid, int geomType, Point2D p) {
		this.id = 0;
		this.valid_from_year = valid;
		this.valid_to_year = valid;
		this.geomType = geomType;
		this.wine_grower_id = 0;
		this.wine_type_id = 0;
		this.new_obj = true;
		this.selectedFull = false;
		this.selectedPoint = p;
		
		switch (geomType) {
		case JGeometry.GTYPE_POINT:        this.object_type = "scarecrow";  break;
		case JGeometry.GTYPE_CURVE:        this.object_type = "road";       break;
		case JGeometry.GTYPE_MULTICURVE:   this.object_type = "wine_row";   break;
		case JGeometry.GTYPE_POLYGON: 	   this.object_type = "wineyard";	break;
		case JGeometry.GTYPE_MULTIPOLYGON: this.object_type = "pond";       break;
		}						
				
		this.points = new ArrayList<ArrayList<Point2D>>();
		this.points.add(new ArrayList<Point2D>());
		this.points.get(0).add(p);
		
		this.newObjCount = 1;
	}	
	
	/**
	 * Dalsi bod geometrie pro nove vytvoreny objekt.
	 * @param p
	 */
	public void addNewPoint(Point2D p){
		if(this.new_obj){
			this.points.get(this.newObjCount-1).add(p);
			this.selectedPoint = p;
		}
	}
	
	/**
	 * Vyznaceni pokroceni k novemu prostorovemu utvaru u viceobjektovych zaznamu
	 *  - rybniky, radky vina.
	 */
	public void addNewObj(){
		if(this.new_obj){
			this.points.add(new ArrayList<Point2D>());
			this.newObjCount++;
		}
	}
	
	/**
	 * Dokonceni vytvareni noveho objektu, pote uz nejde pridavat dalsi body.
	 */
	public void finishedCreation(){
		this.new_obj = false;
		this.selectedPoint = null;
		this.selectedFull = true;
	}
	
	//<<<<<<<<
///////////////////////////////////////////////////////////	
	
	
	public int getWine_grower_id() {
		return wine_grower_id;
	}

	public void setWine_grower_id(int wine_grower_id) {
		this.wine_grower_id = wine_grower_id;
	}

	public int getWine_type_id() {
		return wine_type_id;
	}

	public void setWine_type_id(int wine_type_id) {
		this.wine_type_id = wine_type_id;
	}

	/**
	 * Vykresleni objektu do platna.
	 * @param g2 Kam se bude kreslit.
	 */
	public void paintItem(Graphics2D g2){
		
		switch (geomType) {
			case JGeometry.GTYPE_POINT: 	  
				
				drawPoint(g2, points.get(0).get(0), selectedFull);
								
				break;
							
			case JGeometry.GTYPE_CURVE: 	    
				
				for (int i = 0; i+1 < points.get(0).size(); i++) {
					drawLine(g2, points.get(0).get(i), points.get(0).get(i+1), selectedFull);
				}
				
				break;
				
			case JGeometry.GTYPE_MULTICURVE:    
						
				for(int i=0; i<points.size();i++){
					for(int j=0; j+1<points.get(i).size();j++){
						drawLine(g2, points.get(i).get(j), points.get(i).get(j+1), selectedFull);
					}
				}
				
				break;
				
			case JGeometry.GTYPE_POLYGON: 		
				
				for (int i = 0; i+1 < points.get(0).size(); i++) {
					drawLine(g2, points.get(0).get(i), points.get(0).get(i+1), selectedFull);
				}
				if(points.get(0).size()>1){
					drawLine(g2, points.get(0).get(points.get(0).size()-1), points.get(0).get(0), selectedFull);
				}				
				
				break;
				
			case JGeometry.GTYPE_MULTIPOLYGON:  
				
				for(int i=0; i<points.size();i++){
					for(int j=0; j+1<points.get(i).size();j++){
						drawLine(g2, points.get(i).get(j), points.get(i).get(j+1), selectedFull);
					}
					if(points.get(i).size()>1){
						drawLine(g2, points.get(i).get(points.get(i).size()-1),points.get(i).get(0), selectedFull);
					}
				}
				
				break;
			default:
				
				break;
		}	
		
		if(this.selectedPoint != null){
			drawPoint(g2, selectedPoint, true);
		}
		
	}	
	
	/**
	 * Testovaci vypis objektu, co bylo vlozeno do objektu.
	 */
	public void printData() {
		System.out.println("Data: " + object_type + " " + id + " " + valid_from_year + " " + valid_to_year
				+ " " + wine_grower_id + " " + wine_type_id);
	}

	/**
	 * Vrati rok od kdy je objekt validni.
	 * @return Rok od kdy.
	 */
	public int getValidFromYear() {
		return valid_from_year;
	}

	/**
	 * Vrati rok do kdy je objekt validni.
	 * @return Rok do kdy.
	 */
	public int getValidToYear() {
		return valid_to_year;
	}
	
	/**
	 * Vrati ID objektu.
	 * @return ID objektu.
	 */
	public int getId() {
		return id;
	}
//	public int getPosition() {
//		return position;
//	}
//	public void setPosition(int position) {
//		this.position = position;
//	}
	/**
	 * Vrati jaky ma objekt druh.
	 * @return Druh objektu.
	 */
	public String getType() {
		return object_type;
	}
//	public void setType(String type) {
//		this.object_type = type;
//	}
	
	/**
	 * Oznaci cely objekt, nebo mu zrusi oznaceni.
	 * @param s True/false jestli chceme oznacit.
	 */
	public void setSelect(boolean s) {
		this.selectedFull = s;
		this.selectedPoint = null;

	}
	
	/**
	 * Vrati jestli je objekt oznacen - cely nebo jeden bod.
	 * @return True jestli je oznaceny.
	 */
	public boolean isSelected(){
		return this.selectedFull || this.selectedPoint != null;
	}

	/**
	 * Oznaci objekt jestli je na urcite pozici. Bud bod nebo cely objekt jestli je na primce
	 * mezi body.
	 * @param p Souradnice bodu, ktery chceme oznacit.
	 * @return True/false jestli se oznacil.
	 */
	public boolean selectIfOn(Point p) {
		
		switch (geomType) {
		case JGeometry.GTYPE_POINT: 	  
								
			if(selectPoint(points.get(0).get(0), p)){
				this.selectedFull = true;
				selectedPoint = null;
				return true;
			}
			
			break;
						
		case JGeometry.GTYPE_CURVE: 
		case JGeometry.GTYPE_MULTICURVE:    

			
			for(int i=0; i<points.size();i++){
				for(int j=0; j+1<points.get(i).size();j++){			
					
					if(selectPoint(points.get(i).get(j), p)){
						selectedPoint = points.get(i).get(j);
						selectedFull = false;
						return true;
					}
					if(selectPoint(points.get(i).get(j+1), p)){
						selectedPoint = points.get(i).get(j+1);
						selectedFull = false;
						return true;
					}					
					if(selectLine(new Line2D.Double(points.get(i).get(j),points.get(i).get(j+1)),p)){
						selectedFull = true;
						selectedPoint = null;
						return true;
					}
				}
			}
			break;
			
		case JGeometry.GTYPE_POLYGON: 		
		case JGeometry.GTYPE_MULTIPOLYGON:  

			for(int i=0; i<points.size();i++){
				for(int j=0; j+1<points.get(i).size();j++){
					
					if(selectPoint(points.get(i).get(j), p)){
						selectedPoint = points.get(i).get(j);
						selectedFull = false;
						return true;
					}
					if(selectPoint(points.get(i).get(j+1), p)){
						selectedPoint = points.get(i).get(j+1);
						selectedFull = false;
						return true;
					}
					if(selectLine(new Line2D.Double(points.get(i).get(j),points.get(i).get(j+1)),p)){
						selectedFull = true;
						selectedPoint = null;
						return true;
					}
				}
				if(points.get(i).size()>1){
					if(selectLine(new Line2D.Double(points.get(i).get(points.get(i).size()-1),points.get(i).get(0)),p)){
						selectedFull = true;
						selectedPoint = null;
						return true;
					}
				}
			}
			break;
			
		default:
			
			break;
		}				
			
		return false;
	}
	
	/**
	 * Vrati jestli je bod blizko techto souradnic k oznaceni
	 * @param p1 Bod.
	 * @param p2 Souradnice. (Nebo naopak.)
	 * @return true/false
	 */
	private boolean selectPoint(Point2D p1, Point2D p2){
		if(p1.distance(p2)<7){
			return true;
		}
		return false;
	}
	
	/**
	 * Vrati jestli je usecka blizko techto souradnic k oznaceni
	 * @param line Usecka.
	 * @param p Souradnice.
	 * @return true/false
	 */
	private boolean selectLine(Line2D line, Point2D p){
		
		if(line.ptSegDist(p)<7){
			return true;
		}		
		
		return false;
	}
	
	/**
	 * Vykresleni jednoho bodu s prepinanim jestli je oznacen.
	 * @param g2 Kam se kresli.
	 * @param p Bod ktery kreslime.
	 * @param selected Je oznacen?
	 */
	private void drawPoint(Graphics2D g2, Point2D p, boolean selected){
		
		int size = 5;
		
		if(selected){
			g2.setColor(Color.RED);
			
			g2.fillOval((int)p.getX()-size, (int)p.getY()-size, 2*size, 2*size);		
			
		}else{
			g2.setColor(Color.BLACK);
			
			g2.fillOval((int)p.getX()-size, (int)p.getY()-size, 2*size, 2*size);		

		}
	}
	
	/**
	 * Vykresleni usecky s prepinanim jestli je oznacena.
	 * @param g2 Kam se kresli.
	 * @param p1 Prvni souradnice usecky.
	 * @param p2 Druha souradnice usecky.
	 * @param selected Je oznacena?
	 */
	private void drawLine(Graphics2D g2, Point2D p1, Point2D p2, boolean selected){
		
		int size = 4;
		
		if(selected){
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(size-2));
			
			g2.draw(new Line2D.Double(p1,p2));
			g2.fillOval((int)p1.getX()-size, (int)p1.getY()-size, 2*size, 2*size);		
			g2.fillOval((int)p2.getX()-size, (int)p2.getY()-size, 2*size, 2*size);		
			
		}else{
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1));	
			
			g2.draw(new Line2D.Double(p1,p2));	
			g2.fillOval((int)p1.getX()-size, (int)p1.getY()-size, 2*size, 2*size);		
			g2.fillOval((int)p2.getX()-size, (int)p2.getY()-size, 2*size, 2*size);	
		}
		
	}
	
	/**
	 * Pohne s oznacenym objektem - cely objekt nebo jen jeden bod. 
	 * @param vec Vektor kam se mame pohnout.
	 */
	public void move(Point vec){
				
		if(this.selectedPoint != null){
			this.selectedPoint.setLocation(this.selectedPoint.getX()+vec.getX(),this.selectedPoint.getY()+vec.getY());
		}
		else{
			for (ArrayList<Point2D> ps : points) {
				for (Point2D point : ps) {
					point.setLocation(point.getX()+vec.getX(),point.getY()+vec.getY());
				}
			}
		}
		
	}
	
	/**
	 * Konvertuje JGeometry kterou nam vrati databaze na seznam bodu,
	 * se kterym pracujeme interne v databazi.
	 * @param geometry Geometrie kterou vraci dotazy do databaze.
	 * @return Seznam bodu geometrie objektu.
	 */
	private static ArrayList<ArrayList<Point2D>> convertJGeometryToPoints(JGeometry geometry){
		
		ArrayList<ArrayList<Point2D>> p = new ArrayList<ArrayList<Point2D>>();
		double[] ps;
		
		Object[] obs;
		
		switch (geometry.getType()) {
		case JGeometry.GTYPE_POINT: 	  
								
			p.add(new ArrayList<Point2D>());
			p.get(0).add(geometry.getJavaPoint());
			
			break;
						
		case JGeometry.GTYPE_CURVE: 	    
			
			ps = geometry.getOrdinatesArray();
			p.add(new ArrayList<Point2D>());
			for (int i = 0; i < ps.length; i+=2) {
				p.get(0).add(new Point((int)ps[i],(int)ps[i+1]));
			}			
			break;
			
		case JGeometry.GTYPE_MULTICURVE:    
							
			obs = geometry.getOrdinatesOfElements();
			
			for (int j=0;j<obs.length;j++) {	
				
				ps = (double[]) obs[j];
				
				p.add(new ArrayList<Point2D>());
				
				for (int i = 0; i < ps.length; i+=2) {
					p.get(j).add(new Point((int)ps[i],(int)ps[i+1]));
				}	
			}
			
			break;
			
		case JGeometry.GTYPE_POLYGON: 		
			
			ps = geometry.getOrdinatesArray();
			p.add(new ArrayList<Point2D>());
			for (int i = 0; i < ps.length; i+=2) {
				p.get(0).add(new Point((int)ps[i],(int)ps[i+1]));
			}			
			
			break;
			
		case JGeometry.GTYPE_MULTIPOLYGON:  
				
			obs = geometry.getOrdinatesOfElements();
			
			for (int j=0;j<obs.length;j++) {	
				
				ps = (double[]) obs[j];
				
				p.add(new ArrayList<Point2D>());
				
				for (int i = 0; i < ps.length; i+=2) {
					p.get(j).add(new Point((int)ps[i],(int)ps[i+1]));
				}	
			}
			
			break;
		default:
			
			break;
		}				
				
		return p;
	}
	
	/**
	 * Vrati geometrii objektu pro zapis do databaze.
	 * @return Geometrie.
	 */
	public JGeometry getGeometry(){
		
		return convertPointsToJGeometry(geomType, points);
	}
	
	/**
	 * Prekonvertuje seznam bodu geometrie objektu do JGeometry pro zapis do databaze.
	 * @param geomType Druh objektu ktery body reprezentuji.
	 * @param points Seznam bodu.
	 * @return JGeometrie objektu.
	 */
	private static JGeometry convertPointsToJGeometry(int geomType, ArrayList<ArrayList<Point2D>> points){
		
		JGeometry geometry;
		int [] elemInfo;
		double [] ordinates;
		
		ArrayList<Double> pom;
		int counter;
		
		switch (geomType) {
		case JGeometry.GTYPE_POINT: 	  
					
			geometry = new JGeometry(points.get(0).get(0).getX(),
									 points.get(0).get(0).getY(),
									 0);

			break;
						
		case JGeometry.GTYPE_CURVE: 	    
			
			elemInfo = new int[] {1,2,1};
			ordinates = new double[2*points.get(0).size()];
			
			for(int i=0; i<points.get(0).size();i++){
				ordinates[2*i]=points.get(0).get(i).getX();
				ordinates[2*i+1]=points.get(0).get(i).getY();				
			}
			
			geometry = new JGeometry(2002,0,elemInfo,ordinates);
			
			break;
			
		case JGeometry.GTYPE_MULTICURVE:    
							
			elemInfo = new int[3 * points.size()];
			pom = new ArrayList<Double>();
			
			counter=1;
			
			for(int i=0 ; i<points.size() ; i++){
				elemInfo[3*i] = counter;
				elemInfo[3*i+1] = 2;
				elemInfo[3*i+2] = 1;
				for (int j = 0; j < points.get(i).size(); j++) {
					pom.add(points.get(i).get(j).getX());
					counter++;
					pom.add(points.get(i).get(j).getY());
					counter++;
				}
			}
			
			ordinates = new double[pom.size()];
			for (int i = 0; i < pom.size(); i++) {
				ordinates[i] = pom.get(i).doubleValue();
			}
			
			geometry = new JGeometry(2006,0,elemInfo,ordinates);
			
			break;
			
		case JGeometry.GTYPE_POLYGON: 		
			
			elemInfo = new int[] {1, 1003, 1};
			ordinates = new double[2*points.get(0).size()];
			
			for(int i=0; i<points.get(0).size();i++){
				ordinates[2*i]=points.get(0).get(i).getX();
				ordinates[2*i+1]=points.get(0).get(i).getY();				
			}
			
			geometry = new JGeometry(2003,0,elemInfo,ordinates);
			
			break;
			
		case JGeometry.GTYPE_MULTIPOLYGON:  
				
			elemInfo = new int[3 * points.size()];
			pom = new ArrayList<Double>();
			
			counter=1;
			
			for(int i=0 ; i<points.size() ; i++){
				elemInfo[3*i] = counter;
				elemInfo[3*i+1] = 1003;
				elemInfo[3*i+2] = 1;
				for (int j = 0; j < points.get(i).size(); j++) {
					pom.add(points.get(i).get(j).getX());
					counter++;
					pom.add(points.get(i).get(j).getY());
					counter++;
				}
			}
			
			ordinates = new double[pom.size()];
			for (int i = 0; i < pom.size(); i++) {
				ordinates[i] = pom.get(i).doubleValue();
			}
			
			geometry = new JGeometry(2007,0,elemInfo,ordinates);

			break;
		default:
			geometry = null;
			break;
		}				
		
		return geometry;
	}
	
	public void syncedWithDB(){
		this.new_obj = false;
	}

	/**
	 * Smaze oznaceny objekt. Pokud je cely objekt oznacen tak se rusi uplne.
	 * Jinak maze pouze oznaceny bod, pripadne nic.
	 * @return Vraci true pokud smazal uplne vsechno a nema prostorova data.
	 * Pokud nejaka prostorova data jeste ma tak vraci false.
	 */
	public boolean deleteSelected() {
		
		if(this.selectedFull)
			return true;
		else{
			if(this.selectedPoint != null){
				for(int i=0 ; i<points.size() ; i++){
					//odstranime prvek
					if(points.get(i).remove(this.selectedPoint)){
						
						this.selectedPoint = null;
						//jestli byl nejaky odstraneny tak kontrolujeme delku objektu
						// a kdyz je uz kratky tak jej uplne smazem
						if(points.get(i).size()==0 
							|| (this.geomType == JGeometry.GTYPE_CURVE && points.get(i).size()<=1)
							|| (this.geomType == JGeometry.GTYPE_MULTICURVE && points.get(i).size()<=1)
							|| (this.geomType == JGeometry.GTYPE_POLYGON && points.get(i).size()<=2)
							|| (this.geomType == JGeometry.GTYPE_MULTIPOLYGON && points.get(i).size()<=2)
							){					
							points.remove(i);
						}
					}
				}
				if(points.size()==0) {
					return true;
				}
			}
			return false;
		}
	}

	public void setID(int id) {
		this.id = id;		
	}

	public void setValid_from_year(int validFrom) {
		this.valid_from_year = validFrom;
	}

	public void setValid_to_year(int validTo) {
		this.valid_to_year = validTo;
	}

	/**
	 * Vraci jestli je plne oznacen, ne pouze jeden bod.
	 * @return True pokud je oznaceny cely.
	 */
	public boolean isSelectedFull() {
		return selectedFull;
	}
	
	
}
