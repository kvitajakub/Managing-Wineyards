package pdb03.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.spatial.geometry.JGeometry;
import pdb03.model.DataModel;
import pdb03.model.SpatialObject;
import pdb03.view.AboutWindow;
import pdb03.view.HelpWindow;
import pdb03.view.mainWindow.MainWindow;
import pdb03.view.mainWindow.StatusBarTexts;
import pdb03.view.validWindow.ChangeValidityWindow;

/**
 * Trida ridici hlavni okno aplikace s vnitrni logikou.
 * @author Jakub Kvita
 *
 */
public class MainControl implements ChangeListener, MouseListener, ListSelectionListener, MouseMotionListener, ActionListener, KeyListener, WindowListener {
	
	private MainWindow window;
	private DataModel data;
	
	private ChangeValidityWindow valid;
	private OwnerTableControl ownerWindow;
	private WineTableControl wineWindow;
	
	private Point mouseLocation;
	
	private MouseMode mode;
	private boolean addNewStart;
	
	private boolean dragging;
	
	public enum MouseMode {
	    SELECTION, ADD_POINT, ADD_LINE, ADD_MULTILINE,
	    ADD_POLYGON, ADD_MULTIPOLYGON
	}
	
	private static final String ADD_ACTION_POINT = "point";
	private static final String ADD_ACTION_LINE = "line";
	private static final String ADD_ACTION_MULTILINE = "multiline";
	private static final String ADD_ACTION_POLYGON = "polygon";
	private static final String ADD_ACTION_MULTIPOLY = "multipolygon";
	
	private static final String VALID_SET_SELECTED = "set active validity";
	private static final String PICK_OWNER = "choose owner";
	private static final String PICK_WINE = "choose wine";

	/**
	 * Konstruktor kde se napojuji veskere listenery.
	 */
	public MainControl() {
				window = new MainWindow();
				
				this.dragging = false;
				
				mode = MouseMode.SELECTION;
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				addNewStart = true;
				
	        	window.getItemDetailPanel().showWine(false);
            	window.getItemDetailPanel().showOwner(false);
	        	
				data = new DataModel();
				window.getItemListPanel().getTable().setModel(data);	
				//nastaveni sirky protoze to nejde bez nastaveneho modelu
				window.getItemListPanel().getTable().getColumnModel().getColumn(0).setPreferredWidth(45);
				window.getItemListPanel().getTable().getColumnModel().getColumn(1).setPreferredWidth(45);
				window.getItemListPanel().getTable().getColumnModel().getColumn(2).setPreferredWidth(45);
				window.getItemListPanel().getTable().getColumnModel().getColumn(3).setPreferredWidth(75);
				window.getItemListPanel().getTable().getSelectionModel().addListSelectionListener(this);
				
				window.getItemDetailPanel().getBtnEditOwner().addActionListener(this);
				window.getItemDetailPanel().getBtnEditWine().addActionListener(this);
				
				window.getLeftPanel().getTimeSlider().addChangeListener(this);
				window.getLeftPanel().getDrawPointButton().addActionListener(this);
				window.getLeftPanel().getDrawPointButton().setActionCommand(ADD_ACTION_POINT);		
				window.getLeftPanel().getDrawLineButton().addActionListener(this);
				window.getLeftPanel().getDrawLineButton().setActionCommand(ADD_ACTION_LINE);
				window.getLeftPanel().getDrawMultiLineButton().addActionListener(this);
				window.getLeftPanel().getDrawMultiLineButton().setActionCommand(ADD_ACTION_MULTILINE);
				window.getLeftPanel().getDrawPolygonButton().addActionListener(this);
				window.getLeftPanel().getDrawPolygonButton().setActionCommand(ADD_ACTION_POLYGON);
				window.getLeftPanel().getDrawMultiPolygonButton().addActionListener(this);
				window.getLeftPanel().getDrawMultiPolygonButton().setActionCommand(ADD_ACTION_MULTIPOLY);
				
				window.getMapCanvas().setData(data);
				window.getMapCanvas().setFocusable(true);
				window.getMapCanvas().requestFocusInWindow();
				window.getMapCanvas().addMouseListener(this);
				window.getMapCanvas().addMouseMotionListener(this);
				window.getMapCanvas().addKeyListener(this);
				
				
				window.getItemListPanel().getTable().addKeyListener(this);
				
				window.getMntmFillDB().addActionListener(this);
				window.getMntmManual().addActionListener(this);
				window.getMntmAbout().addActionListener(this);
				window.getMntmSetValidity().addActionListener(this);
				window.getMntmOwnerTable().addActionListener(this);
				window.getMntmWineTypeTable().addActionListener(this);
				window.getMntmDeleteSelectedInYear().addActionListener(this);
				window.getMntmQueryTemporalSelectLines().addActionListener(this);
				window.getMntmQueryTemporalSelectMultilines().addActionListener(this);
				window.getMntmQueryTemporalSelectMultiPolygons().addActionListener(this);
				window.getMntmQueryTemporalSelectPoints().addActionListener(this);
				window.getMntmQueryTemporalSelectPolygons().addActionListener(this);
				window.getMntmQueryTemporalOwner().addActionListener(this);
				window.getMntmQueryTemporalWine().addActionListener(this);
				window.getMntmSetIntervalOwner().addActionListener(this);
				
				window.getMntmClosest1().addActionListener(this);
				window.getMntmClosest2().addActionListener(this);
				window.getMntmClosest3().addActionListener(this);
				window.getMntmClosest4().addActionListener(this);
				
				window.getMntmScarecrowsInSelected().addActionListener(this);
				
				window.getMenuItemDistance10().addActionListener(this);
				window.getMenuItemDistance20().addActionListener(this);
				window.getMenuItemDistance30().addActionListener(this);
				window.getMenuItemDistance40().addActionListener(this);
				window.getMenuItemDistance50().addActionListener(this);
				window.getMenuItemDistance60().addActionListener(this);
				window.getMenuItemDistance70().addActionListener(this);
				window.getMenuItemDistance80().addActionListener(this);
				window.getMenuItemDistance90().addActionListener(this);
				window.getMenuItemDistance100().addActionListener(this);
				
				window.getMntmWineArea().addActionListener(this);
				
				window.getMntmPercentagesOfWines().addActionListener(this);
				
				window.frame.setVisible(true);
	}

	/**
	 * GUI ktere propojuje canvas, tabulku a detail je svazano a tady se refreshuje
	 * vsechno najednou.
	 */
	public void refreshGui(){	
		
		int selected = data.getSelectedRow();
        if(selected == -1){
        	
        	window.getItemListPanel().getTable().clearSelection();
        	window.getItemDetailPanel().showWine(false);
        	window.getItemDetailPanel().showOwner(false);

        }
        else{  	
        	//prepinani mezi cisly radku modelu a cisly radku jak jsou zobrazeny
    		int tablesel = window.getItemListPanel().getTable().convertRowIndexToView(selected);
    		window.getItemListPanel().getTable().setRowSelectionInterval(tablesel, tablesel);
    		
    		
            if(data.getSelected().getType().equals("wine_row")){
            	window.getItemDetailPanel().showWine(true);
            	window.getItemDetailPanel().showOwner(true);
            	
            	window.getItemDetailPanel().getPicture().setIcon(data.getSelectedPicture());            	
            	window.getItemDetailPanel().getTextPaneWineType().setText(data.getSelectedWineText());
            	window.getItemDetailPanel().getTextPaneOwner().setText(data.getSelectedOwnerText());

            }
            else{
            	window.getItemDetailPanel().showWine(false);
            	
                if(data.getSelected().getType().equals("wineyard")
                	||data.getSelected().getType().equals("scarecrow")){
	                	window.getItemDetailPanel().showWine(false);
	                	window.getItemDetailPanel().showOwner(true);
	                	
	                	window.getItemDetailPanel().getTextPaneOwner().setText(data.getSelectedOwnerText());
                }
                else{
                	window.getItemDetailPanel().showWine(false);
                	window.getItemDetailPanel().showOwner(false);
                }
            }
    		
        }			
		

        
		window.getItemListPanel().getTable().repaint();
		window.getMapCanvas().repaint();
		window.getItemDetailPanel().repaint();
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
        if (!window.getLeftPanel().getTimeSlider().getValueIsAdjusting()) {
            data.setActiveYear(window.getLeftPanel().getTimeSlider().getValue());
            refreshGui();
        }    
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

        mouseLocation = e.getPoint();
		
		if(mode == MouseMode.SELECTION){
			data.selectItemOnPosition(e.getPoint());
	        
		}else if(mode == MouseMode.ADD_POINT){
			if(this.addNewStart){
				data.deselect();
				data.addNewSpatial(JGeometry.GTYPE_POINT, e.getPoint());
				data.addNewSpatialFinished();
			}
			
		}else if(mode == MouseMode.ADD_LINE){
		
			if(this.addNewStart){
				data.deselect();
				data.addNewSpatial(JGeometry.GTYPE_CURVE, e.getPoint());
				this.addNewStart = false;
			}
			else{
				data.addNewSpatialNextPoint(e.getPoint());
			}
			
		}else if(mode == MouseMode.ADD_MULTILINE){
			
			if(this.addNewStart){
				data.deselect();
				data.addNewSpatial(JGeometry.GTYPE_MULTICURVE, e.getPoint());
				this.addNewStart = false;
			}
			else{
				data.addNewSpatialNextPoint(e.getPoint());
			}
			
		}else if(mode == MouseMode.ADD_POLYGON){
			
			if(this.addNewStart){
				data.deselect();
				data.addNewSpatial(JGeometry.GTYPE_POLYGON, e.getPoint());
				this.addNewStart = false;
			}
			else{
				data.addNewSpatialNextPoint(e.getPoint());
			}
			
		}else if(mode == MouseMode.ADD_MULTIPOLYGON){
			
			if(this.addNewStart){
				data.deselect();
				data.addNewSpatial(JGeometry.GTYPE_MULTIPOLYGON, e.getPoint());
				this.addNewStart = false;
			}
			else{
				data.addNewSpatialNextPoint(e.getPoint());
			}
		}
        refreshGui();
		window.getMapCanvas().requestFocusInWindow();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
		if(this.dragging && data.getSelected() != null){
			data.updateSelectedInDB();
		}
		this.dragging = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.dragging = true;
		data.moveSelected(new Point(e.getPoint().x-mouseLocation.x,e.getPoint().y-mouseLocation.y));
		mouseLocation = e.getPoint();
        refreshGui();
		window.getMapCanvas().requestFocusInWindow();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || window.getItemListPanel().getTable().getSelectedRow() == -1) {
            return;
        }
        data.selectItemOnRow(window.getItemListPanel().getTable().convertRowIndexToModel(window.getItemListPanel().getTable().getSelectedRow()));
        refreshGui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		System.out.println("ACTION PERFORMED");
		if(!this.addNewStart){
			data.addNewSpatialFinished();
			this.addNewStart = true;
		}
		
		
		if(e.getActionCommand().equals(ADD_ACTION_POINT)){
						
			if(mode == MouseMode.ADD_POINT){
				
				((JToggleButton)e.getSource()).setSelected(false);
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				mode = MouseMode.SELECTION;
			}
			else{
				
				window.getLeftPanel().getDrawPointButton().setSelected(false);
				window.getLeftPanel().getDrawLineButton().setSelected(false);
				window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
				window.getLeftPanel().getDrawPolygonButton().setSelected(false);
				window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);
				
				((JToggleButton)e.getSource()).setSelected(true);
				window.getStatusLabel().setText(StatusBarTexts.MODE_POINT);
				mode = MouseMode.ADD_POINT;
				
				data.deselect();
			}
		}
		else if(e.getActionCommand().equals(ADD_ACTION_LINE)){

			if(mode == MouseMode.ADD_LINE){
				
				((JToggleButton)e.getSource()).setSelected(false);
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				mode = MouseMode.SELECTION;
			}
			else{
	
				window.getLeftPanel().getDrawPointButton().setSelected(false);
				window.getLeftPanel().getDrawLineButton().setSelected(false);
				window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
				window.getLeftPanel().getDrawPolygonButton().setSelected(false);
				window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);				
				
				((JToggleButton)e.getSource()).setSelected(true);
				window.getStatusLabel().setText(StatusBarTexts.MODE_LINE);
				mode = MouseMode.ADD_LINE;
				
				data.deselect();
			}			
		}
		else if(e.getActionCommand().equals(ADD_ACTION_MULTILINE)){

			if(mode == MouseMode.ADD_MULTILINE){
				
				((JToggleButton)e.getSource()).setSelected(false);
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				mode = MouseMode.SELECTION;
			}
			else{
				
				window.getLeftPanel().getDrawPointButton().setSelected(false);
				window.getLeftPanel().getDrawLineButton().setSelected(false);
				window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
				window.getLeftPanel().getDrawPolygonButton().setSelected(false);
				window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);				
				
				((JToggleButton)e.getSource()).setSelected(true);
				window.getStatusLabel().setText(StatusBarTexts.MODE_MULTILINE);
				mode = MouseMode.ADD_MULTILINE;
				
				data.deselect();
			}
		}
		else if(e.getActionCommand().equals(ADD_ACTION_POLYGON)){

			if(mode == MouseMode.ADD_POLYGON){
				
				((JToggleButton)e.getSource()).setSelected(false);
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				mode = MouseMode.SELECTION;
			}
			else{
				
				window.getLeftPanel().getDrawPointButton().setSelected(false);
				window.getLeftPanel().getDrawLineButton().setSelected(false);
				window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
				window.getLeftPanel().getDrawPolygonButton().setSelected(false);
				window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);				
				
				((JToggleButton)e.getSource()).setSelected(true);
				window.getStatusLabel().setText(StatusBarTexts.MODE_POLYGON);
				mode = MouseMode.ADD_POLYGON;
				
				data.deselect();
			}			
		}
		else if(e.getActionCommand().equals(ADD_ACTION_MULTIPOLY)){

			if(mode == MouseMode.ADD_MULTIPOLYGON){
				
				((JToggleButton)e.getSource()).setSelected(false);				
				window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
				mode = MouseMode.SELECTION;
			}
			else{
				
				window.getLeftPanel().getDrawPointButton().setSelected(false);
				window.getLeftPanel().getDrawLineButton().setSelected(false);
				window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
				window.getLeftPanel().getDrawPolygonButton().setSelected(false);
				window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);				
				
				((JToggleButton)e.getSource()).setSelected(true);
				window.getStatusLabel().setText(StatusBarTexts.MODE_MULTIPOLYGON);
				mode = MouseMode.ADD_MULTIPOLYGON;
				
				data.deselect();				
			}					
		}
		else{					
			//nejake kliknuti v menu
			handleMenuActions(e);
		}
        refreshGui();
	}

	/**
	 * Pokracovani metody actionPerformed pro tlacitka v menu.
	 * @param e
	 */
	private void handleMenuActions(ActionEvent e) {
		
		if(e.getSource() == window.getMntmFillDB()){
			
			data.fill();
			
		}
		else if(e.getSource() == window.getMntmManual()){
	
	        HelpWindow inst = new HelpWindow();
	        inst.setLocationRelativeTo(null);
	        inst.setVisible(true);       
			
		}
		else if(e.getSource() == window.getMntmAbout()){

	        AboutWindow inst = new AboutWindow();
	        inst.setLocationRelativeTo(null);
	        inst.setVisible(true);  			
			
		}
		else if(e.getSource() == window.getMntmSetValidity()){
				
			valid = new ChangeValidityWindow();

			valid.getBtnValidSelect().setActionCommand(VALID_SET_SELECTED);
			valid.getBtnValidSelect().addActionListener(this);
			
			valid.setLocationRelativeTo(window.getFrame());
			valid.setVisible(true);
			
		}
		else if(e.getActionCommand().equals(VALID_SET_SELECTED)){
			
			int validfrom = (Integer)valid.getComboBoxValidFrom().getSelectedItem();
			int validto = (Integer)valid.getComboBoxValidTo().getSelectedItem();
			
			if(validfrom<=validto){
				SpatialObject o = data.getSelected();
				
				if(o != null){
					//vse ok					
					data.setValidity(o,validfrom,validto);
					
					valid.dispose();
					valid = null;
				}
				else{
					valid.getLblValidError().setText("No item selected.");
				}
			}
			else{
				valid.getLblValidError().setText("Valid from must be lower than valid to.");
			}
			
		}
		else if(e.getSource() == window.getMntmOwnerTable()){
			
			ownerWindow = new OwnerTableControl(data);
			
			ownerWindow.setLocationRelativeTo(window.getFrame());
			ownerWindow.getWindow().addWindowListener(this);
			ownerWindow.showPick(false);
			ownerWindow.setVisible(true);
			
		}
		else if(e.getSource() == window.getItemDetailPanel().getBtnEditOwner()){
			
			ownerWindow = new OwnerTableControl(data);
			ownerWindow.setActionCommand(PICK_OWNER);
			ownerWindow.addPickActionListener(this);
			
			ownerWindow.setLocationRelativeTo(window.getFrame());
			ownerWindow.setVisible(true);
			
		}
		else if(e.getActionCommand().equals(PICK_OWNER)){
			
			if(ownerWindow.getSelectedIndex() != -1){
				data.setSelectedWine_grower_id(ownerWindow.getSelectedIndex());
				ownerWindow.dispose();
				refreshGui();
			}
			else{
				//nejaky komentar kdyz zkousime vybrat nic?
			}
			
		}
		else if(e.getSource() == window.getMntmWineTypeTable()){
			
			wineWindow = new WineTableControl(data.getTypet());
			
			wineWindow.setLocationRelativeTo(window.getFrame());
			wineWindow.getWindow().addWindowListener(this);
			wineWindow.showPick(false);
			wineWindow.setVisible(true);
			
		}
		else if(e.getSource() == window.getItemDetailPanel().getBtnEditWine()){
			
			wineWindow = new WineTableControl(data.getTypet());
			wineWindow.setActionCommand(PICK_WINE);
			wineWindow.addPickActionListener(this);
			
			wineWindow.setLocationRelativeTo(window.getFrame());
			wineWindow.setVisible(true);
			
		}
		else if(e.getActionCommand().equals(PICK_WINE)){
			
			if(wineWindow.getSelectedIndex() != -1){
				data.setSelectedWineTypeID(wineWindow.getSelectedIndex());
				wineWindow.dispose();
				refreshGui();
			}
			else{
				//nejaky komentar kdyz zkousime vybrat nic?
			}
			
		}
		else if(e.getSource() == window.getMntmDeleteSelectedInYear()){

			data.deleteSelectedInActiveYear();
		}		
		
		else if(e.getSource() == window.getMntmQueryTemporalSelectPoints()){

			data.selectKind(JGeometry.GTYPE_POINT);
		}		
		else if(e.getSource() == window.getMntmQueryTemporalSelectLines()){

			data.selectKind(JGeometry.GTYPE_CURVE);
		}		
		else if(e.getSource() == window.getMntmQueryTemporalSelectMultilines()){

			data.selectKind(JGeometry.GTYPE_MULTICURVE);
		}		
		else if(e.getSource() == window.getMntmQueryTemporalSelectPolygons()){

			data.selectKind(JGeometry.GTYPE_POLYGON);
		}		
		else if(e.getSource() == window.getMntmQueryTemporalSelectMultiPolygons()){

			data.selectKind(JGeometry.GTYPE_MULTIPOLYGON);
		}		
		else if(e.getSource() == window.getMntmQueryTemporalOwner()){
			
			if(data.getSelected() != null && data.getSelected().getType().equals("wineyard")){

				new SelectOwnerControl(window, data.getSelected().getId());				
			}
		}		
		else if(e.getSource() == window.getMntmQueryTemporalWine()){

			//vytvoreni controlni tridy na dotaz, zbytek si poresi sama
			new SelectWineControl(window, data.getGrowt());
		}		
		else if(e.getSource() == window.getMntmSetIntervalOwner()){

			if(data.getSelected() != null){

				new ChangeOwnerIntervalControl(this,data);
			}
		}		
		else if(e.getSource() == window.getMntmClosest1()){
			data.getWineyardsNearWater(1);
		}		
		else if(e.getSource() == window.getMntmClosest2()){
			data.getWineyardsNearWater(2);
		}		
		else if(e.getSource() == window.getMntmClosest3()){
			data.getWineyardsNearWater(3);
		}		
		else if(e.getSource() == window.getMntmClosest4()){
			data.getWineyardsNearWater(4);
		}		
		else if(e.getSource() == window.getMntmScarecrowsInSelected()){
			data.getScarecrowsInSelectedWineyard();
		}		
		else if(e.getSource() == window.getMenuItemDistance10()){
			data.getWineyardsCloseToSelectedRoad(10);
		}		
		else if(e.getSource() == window.getMenuItemDistance20()){
			data.getWineyardsCloseToSelectedRoad(20);
		}		
		else if(e.getSource() == window.getMenuItemDistance30()){
			data.getWineyardsCloseToSelectedRoad(30);
		}		
		else if(e.getSource() == window.getMenuItemDistance40()){
			data.getWineyardsCloseToSelectedRoad(40);
		}		
		else if(e.getSource() == window.getMenuItemDistance50()){
			data.getWineyardsCloseToSelectedRoad(50);
		}		
		else if(e.getSource() == window.getMenuItemDistance60()){
			data.getWineyardsCloseToSelectedRoad(60);
		}		
		else if(e.getSource() == window.getMenuItemDistance70()){
			data.getWineyardsCloseToSelectedRoad(70);
		}		
		else if(e.getSource() == window.getMenuItemDistance80()){
			data.getWineyardsCloseToSelectedRoad(80);
		}		
		else if(e.getSource() == window.getMenuItemDistance90()){
			data.getWineyardsCloseToSelectedRoad(90);
		}		
		else if(e.getSource() == window.getMenuItemDistance100()){
			data.getWineyardsCloseToSelectedRoad(100);
		}
		else if(e.getSource() == window.getMntmWineArea()){

			new WineAreaControl(window,data);
		}	
		else if(e.getSource() == window.getMntmPercentagesOfWines()){

			new PercentageControl(window,data);
		}	
		else{
			//neco je spatne, poslouchame na necem co neumime zpracovat
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {		
	}

	@Override
	public void keyPressed(KeyEvent e) {
				
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
        	System.out.println("ENTER");
        	if (!this.addNewStart) {
        		data.addNewSpatialFinished();
        		this.addNewStart = true;
			}
			window.getLeftPanel().getDrawPointButton().setSelected(false);
			window.getLeftPanel().getDrawLineButton().setSelected(false);
			window.getLeftPanel().getDrawMultiLineButton().setSelected(false);
			window.getLeftPanel().getDrawPolygonButton().setSelected(false);
			window.getLeftPanel().getDrawMultiPolygonButton().setSelected(false);
			window.getStatusLabel().setText(StatusBarTexts.MODE_SELECT);
			mode = MouseMode.SELECTION;
        }	
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
        	System.out.println("SPACE");
        	if (!this.addNewStart) {
        		data.addNewSpatialNextObject();
        	}
    	}
        else if(e.getKeyCode() == KeyEvent.VK_DELETE){
        	System.out.println("DELETE");
        	
        	window.getItemListPanel().getTable().clearSelection();
        	window.getItemListPanel().getTable().setAutoCreateRowSorter(true);
        	data.deleteSelected();
        	this.addNewStart = true;
        	
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
        	        	
        }
        
        refreshGui();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		refreshGui();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	public MainWindow getWindow() {
		return window;
	}
	
}
