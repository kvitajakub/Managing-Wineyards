package pdb03.view.mainWindow;

import java.awt.Graphics;

import javax.swing.JPanel;

import pdb03.model.DataModel;

/**
 * Trida reprezentujici platno nasi aplikace kde se vykresluji prostorove objekty.
 * Pred pouzitim je potreba nastavit odkud bude brat data, metodou setData().
 * Ty jsou pote vykreslovany v paintComponent().
 * @author Jakub Kvita
 *
 */
public class MapCanvas extends JPanel {

	private static final long serialVersionUID = 4501347645246787801L;
	private DataModel data;

	/**
	 * Nastavi odkud brat data.
	 * @param data Datovy model aplikace.
	 */
	public void setData(DataModel data) {
		this.data = data;
	}

	/**
	 * Konstruktor.
	 */
	public MapCanvas() {
		
	}	
	
	/**
	 * Konstruktor nastavujici odkud se berou data pro vykreslovani.
	 * @param data Prostorove objekty aplikace.
	 */
	public MapCanvas(DataModel data) {
		this.data = data;
	}

	/**
	 * Vykresluje komponentu a do ni vsechny objekty, ktere jsou potreba.
	 */
    public void paintComponent(Graphics graphics) {
    	super.paintComponent(graphics);  
    	if (data != null) {
                    data.paintItems(graphics);
    	}        
    }
}
