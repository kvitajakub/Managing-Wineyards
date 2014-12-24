package pdb03.controller;

import java.sql.SQLException;

import pdb03.model.SelectWineGrower;
import pdb03.view.mainWindow.MainWindow;
import pdb03.view.ownerTable.OwnerTableWindow;

/**
 * Ridici logika pro okno ktere se pta na vsechny majitele kteri neco pestovali.
 * @author Jakub Kvita
 *
 */
public class SelectOwnerControl {

	SelectWineGrower model;
	MainWindow window;
	OwnerTableWindow res;
	
	/**
	 * Konstruktor.
	 * @param window Kam mame vykreslovat okno.
	 * @param wineyardID ID vinohradu se kterym pracujeme.
	 */
	public SelectOwnerControl(MainWindow window, int wineyardID) {
		
		try {
			model = new SelectWineGrower(wineyardID);
			this.window = window;
			this.res = new OwnerTableWindow(model);
			
			res.getTable().getColumnModel().getColumn(0).setPreferredWidth(10);
			res.getTable().getColumnModel().getColumn(1).setPreferredWidth(10);
			res.getTable().getColumnModel().getColumn(2).setPreferredWidth(10);
			res.getTable().getColumnModel().getColumn(3).setPreferredWidth(40);
			res.getTable().getColumnModel().getColumn(4).setPreferredWidth(55);
			res.getTable().getColumnModel().getColumn(5).setPreferredWidth(100);
			
			this.res.hideButtons();
			this.res.setLocationRelativeTo(window.getFrame());
			this.res.setVisible(true);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
