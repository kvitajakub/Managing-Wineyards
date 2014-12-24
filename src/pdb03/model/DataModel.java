package pdb03.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import oracle.spatial.geometry.JGeometry;

/**
 * Datovy model pro tabulky, muzeme tady skladovat lokalni data stazena z databaze
 *  a pristupovat k nim. Pripojit k tabulce v pdb03.control.
 *  Tri objekty pro pristup k tabulkam a zpet.
 *  Take funguje jako datovy model pro tabulku prostorovych dat.
 * @author Jakub Kvita
 */
public class DataModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = {
			"ID",
            "From",
            "To",
            "Type",            
            "Owner"};   
	
	private SpatialObjectTable spatt;
	private WineGrowerTable growt;
	private WineTypeTable typet;
	
	private ArrayList<SpatialObject> currentObjects;	
	private SpatialObject newlyCreated;
	private DatabaseConnection dbconn;
	
	private int activeYear;

	/**
	 * Vytvoreni noveho datoveho modelu, vsechno si nataha z databaze.
	 * Vytvari jednotlive tabulky pro prostorove objekty, majitele a druhy vin.
	 */
	public DataModel() {
		try {
			this.dbconn = new DatabaseConnection();
			this.spatt = new SpatialObjectTable(this.dbconn);
            this.typet = new WineTypeTable(this.dbconn);
            this.growt = new WineGrowerTable(this.dbconn);
			this.spatt.printData();
		} catch (SQLException e) {
			// database is probably empty
			try {
				// iba vytvorime tabulky
				e.printStackTrace();
                if (e.getErrorCode() == 942) {
                    dbconn.runScriptFile(System.getProperty("SQLpath")+"CreateTablesOnly.sql");
                    this.dbconn = new DatabaseConnection();
                    this.spatt = new SpatialObjectTable(this.dbconn);
                    this.typet = new WineTypeTable(this.dbconn);
                    this.growt = new WineGrowerTable(this.dbconn);
                    this.spatt.printData();
                }
            } catch (Exception e1) {
            	e1.printStackTrace();
            }
		}
        setActiveYear(2000);
        this.newlyCreated = null;
		/* otestujeme nastaveni roku:
		SpatialObject so = spatt.getSpecSo(1000, 2008, 2013);
		System.err.println("PreDDDDDDDDDDDDDDDDDDd");
		if (so != null) {
			System.err.println("so != null?");
			spatt.setValidity(so, 2006, 2015);
		}
        //spatt.getNotChanged(2008, 2015);
        try {
			SelectWineGrower swt = new SelectWineGrower(1001);
			System.out.println(swt.getGrowerText());
		} catch (SQLException e) {
			e.printStackTrace();
		}
        // test getting area
        SpatialObject s = spatt.getWineyardClosestToRoad(1004, 2005);
        System.out.println("Closest to road: " + s.getId());
		*/
        //System.out.println("Lenght: " + spatt.getWinesRowsLength(1001, 2015));
	}
	
	public int getActiveYear() {
		return activeYear;
	}

	public void setActiveYear(int activeYear) {
		this.activeYear = activeYear;
        currentObjects = spatt.getDataFrom(this.activeYear);
	}	
	
	/**
	 * Vykresli prostorova data.
	 * @param g Kam kreslit.
	 */
	public void paintItems(Graphics g){
		
		//zapnuti antialiasingu
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                     RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (SpatialObject obj : currentObjects) {
			obj.paintItem(g2);
		}
		if(newlyCreated != null){
			newlyCreated.paintItem(g2);
		}
		
	}
	
	@Override
	/**
	 * Medoda pro dotazovani jtable kvuli dedicnosti v AbstractTableModel.
	 */
	public int getRowCount() {
		return currentObjects.size();
	}

	@Override
	/**
	 * Medoda pro dotazovani jtable kvuli dedicnosti v AbstractTableModel.
	 */	
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	/**
	 * Metoda vracejici jmena sloupcu;
	 */
    public String getColumnName(int col) {
        return columnNames[col];
    }
	
	@Override
	/**
	 * Medoda pro dotazovani jtable kvuli dedicnosti y AbstractTableModel.
	 */	
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) {
		case 0:
			return currentObjects.get(rowIndex).getId();
		case 1:
			return currentObjects.get(rowIndex).getValidFromYear();
		case 2:
			return currentObjects.get(rowIndex).getValidToYear();
		case 3:
			return currentObjects.get(rowIndex).getType();	
		case 4:
			return currentObjects.get(rowIndex).getWine_grower_id() != 0 ?
					growt.getSurname(currentObjects.get(rowIndex).getWine_grower_id()) : "";		
		}
		return columnIndex;	
	}
	
	/**
	 * Selectne objekt na urcite pozici.
	 * @param p Pozice ktera nas zajima.
	 * @return Pokud se neco selectovalo tak vraci radek itemu. Jinak -1.
	 */
	public int selectItemOnPosition(Point p) {		
		int selected = -1;
		int row = 0;
		for (SpatialObject o : currentObjects) {
			o.setSelect(false);
			if(selected==-1){
				if(o.selectIfOn(p)){
					selected = row;
				}			
			}
			row++;
		}
		return selected;
	}
	
	/**
	 * Vrati jestli mame neco selectovano.
	 * @return Pokud je neco selectovano tak vraci radek itemu. Jinak -1.
	 */
	public int getSelectedRow(){
		int selected = -1;
		int row = 0;
		
		for (SpatialObject o : currentObjects) {
				if(o.isSelected()){
					selected = row;
				}			
			row++;
		}
		
		return selected;
	}
	
	/**
	 * Selectne objekt na radku tabulky.
	 * @param row Radek ktery nas zajima.
	 */
	public void selectItemOnRow(int row) {
		deselect();
		currentObjects.get(row).setSelect(true);
	}
	
	/**
	 * Odznaci vsechny objekty.
	 */
	public void deselect(){
		if(currentObjects != null){
			for (SpatialObject o : currentObjects) {
				o.setSelect(false);
			}
		}
	}

	/**
	 * Pohne s oznacenym objektem.
	 * @param vec Vektor posunu.
	 */
	public void moveSelected(Point vec) {
		
		if(newlyCreated!=null){
			newlyCreated.move(vec);
		}
		else{
			for (Iterator<SpatialObject> iterator = currentObjects.iterator(); iterator.hasNext();) {
				SpatialObject obj = (SpatialObject) iterator.next();
				
				if(obj.isSelected()){
					obj.move(vec);
					return;
				}
			}
		}
	}
	
	/**
	 * Nahraje oznaceny objekt do databaze.
	 */
	public void updateSelectedInDB(){
		try {
			
			for (Iterator<SpatialObject> iterator = currentObjects.iterator(); iterator.hasNext();) {
				SpatialObject obj = (SpatialObject) iterator.next();
				
				if(obj.isSelected()){
					spatt.updateItemInDB(obj);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stahne z databaze jen objekty daneho typu.
	 * @param geomType Typ ktery chceme.
	 */
	public void selectKind(int geomType){
		
		switch (geomType) {
		case JGeometry.GTYPE_POINT:
				
			currentObjects = spatt.getDataTypeFrom("scarecrow", activeYear);
			
			break;
		case JGeometry.GTYPE_CURVE:
			
			currentObjects = spatt.getDataTypeFrom("road", activeYear);
			
			break;
		case JGeometry.GTYPE_MULTICURVE:
			
			currentObjects = spatt.getDataTypeFrom("wine_row", activeYear);

			break;
		case JGeometry.GTYPE_POLYGON:
			
			currentObjects = spatt.getDataTypeFrom("wineyard", activeYear);

			break;
		case JGeometry.GTYPE_MULTIPOLYGON:
			
			currentObjects = spatt.getDataTypeFrom("pond", activeYear);
			break;

		default:
			break;
		}
	}
	
	/**
	 * Prida novy prostorovy objekt.
	 * @param geomType Typ jaky objekt ma.
	 * @param firstPoint Prvni bod geometrie.
	 */
	public void addNewSpatial(int geomType, Point2D firstPoint){
		newlyCreated = new SpatialObject(this.activeYear, geomType, firstPoint);
	}
	
	/**
	 * Prida dalsi bod k prave vytvarenemu objektu.
	 * @param point
	 */
	public void addNewSpatialNextPoint(Point2D point){
		this.newlyCreated.addNewPoint(point);
	}
	
	/**
	 * U prave vytvareneho objektu se posune na dalsi primitivum.
	 */
	public void addNewSpatialNextObject(){
		this.newlyCreated.addNewObj();
	}
	
	/**
	 * Ukonci vytvareni noveho objektu, nahraje jej do databaze.
	 */
	public void addNewSpatialFinished(){
		this.newlyCreated.finishedCreation();
		try {
			spatt.insertItemToDB(this.newlyCreated);
			currentObjects.add(this.newlyCreated);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			this.newlyCreated = null;
		}
	}

	/**
	 * Smaze oznaceny objekt.
	 */
	public void deleteSelected() {
		SpatialObject obj;
		
		if(this.newlyCreated == null){
			for (int i=0; i<currentObjects.size();i++) {
				obj = currentObjects.get(i);
					
					//pokud vrati true tak ho musim smazat uplne
				if(obj.isSelected()){
					if(obj.deleteSelected()){
						System.out.println("Odstranujeme uplne.  "+currentObjects.size());					
						

						try {
							spatt.removeItemFromDB(obj);
						} catch (Exception e) {
							e.printStackTrace();
						}

						currentObjects.remove(obj);
					}
					else{
						
						try {
							spatt.updateItemInDB(obj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		else{
			this.newlyCreated = null;
		}
		
	}
	
	/**
	 * Naplni databazi daty ze skriptu.
	 */
	public void fill(){
		
		try {
			dbconn.runScriptFile(System.getProperty("SQLpath")+"CreateTables.sql");
			this.setActiveYear(this.getActiveYear());
			growt.reload();
			typet.reload();
			
		} catch (SQLException e) {
			try {
				// iba vytvorime tabulky
				e.printStackTrace();
                if (e.getErrorCode() == 942) {
                    dbconn.runScriptFile(System.getProperty("SQLpath")+"CreateTablesOnly.sql");
                }
            } catch (Exception e1) {
            	e1.printStackTrace();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vrati objekt ktery je oznaceny.
	 * @return Oznaceny objekt.
	 */
	public SpatialObject getSelected(){
		
		for (SpatialObject o : currentObjects) {
			if(o.isSelected()){
				return o;
			}			
		}
		return null;
	}
	
	/**
	 * Nastavi validitu urcitemu objektu.
	 * @param o Objekt ktery nas zajima.
	 * @param validfrom Validita od.
	 * @param validto Validita do.
	 */
	public void setValidity(SpatialObject o, int validfrom, int validto) {

		spatt.setValidity(o,validfrom,validto);
		
	}

	public Icon getSelectedPicture() {
		return typet.getWineIcon(getSelected().getWine_type_id());
	}

	public String getSelectedWineText() {
		return typet.getWineText(getSelected().getWine_type_id());
	}

	public String getSelectedOwnerText() {
		return growt.getOwnerText(getSelected().getWine_grower_id());
	}

	public WineGrowerTable getGrowt() {
		return growt;
	}

	public WineTypeTable getTypet() {
		return typet;
	}

	public void setSelectedWine_grower_id(int row) {
		spatt.setWineGrower(getSelected(), (int) growt.getValueAt(row, 0));
	}

	/**
	 * Smaze aktivni objekt v soucasnem roce.
	 */
	public void deleteSelectedInActiveYear() {
		SpatialObject obj;
		
		if(this.newlyCreated == null){
			for (int i=0; i<currentObjects.size();i++) {
				obj = currentObjects.get(i);
					
					//pokud vrati true tak ho musim smazat uplne
				if(obj.isSelectedFull()){
					
					spatt.deleteInYear(obj,activeYear);
					
					//reload databaze
					//neresime selectovaneho protoze jsme ho smazali
					currentObjects = spatt.getDataFrom(this.activeYear);
				}
			}
		}
		else{
			this.newlyCreated = null;
		}
	}

	public void setSelectedWineTypeID(int row) {
		spatt.setWineType(getSelected(), (int) typet.getValueAt(row, 0));
	}
	
	/**
	 * Stahne z databaze vinohrady ktere jsou blizko zdroje vody.
	 * @param n Pocet nejblizsich vinohradu k vode.
	 */
	public void getWineyardsNearWater(int n){
        currentObjects = spatt.getWineyardsNearWater(n, this.activeYear);
	}
	
	/**
	 * Stahne z databaze vsechny strasaky v oznacenem objektu.
	 */
	public void getScarecrowsInSelectedWineyard(){
		if (this.getSelected()!=null) {
            if(this.getSelected().getType().equals("wineyard")){
                currentObjects = spatt.getScarecrowsOnWineyard(this.getSelected().getId(), this.activeYear);
            }
		}
	}
	
	/**
	 * Stahne z databaze vsechny vinohrady se vzdalenosti mensi nez x k oznacene ceste.
	 * @param distance Maximalni vzdalenost.
	 */
	public void getWineyardsCloseToSelectedRoad(int distance){
		if (this.getSelected()!=null) {
            if(this.getSelected().getType().equals("road")){
                currentObjects = spatt.getWineyardsNearRoad(this.getSelected().getId(), distance, this.activeYear);
            }
		}
	}

	public SpatialObjectTable getSpatt() {
		return spatt;
	}
	
	
}
