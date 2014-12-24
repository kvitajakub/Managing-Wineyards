package pdb03.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import pdb03.model.DataModel;
import pdb03.view.WineyardAreaWindow;
import pdb03.view.mainWindow.MainWindow;

/**
 * Ridici logika pro okno kde se pocitaji plochy zabrane urcitym druhem vina.
 * @author Jakub Kvita
 *
 */
public class WineAreaControl implements ActionListener {

	MainWindow window;
	DataModel data;
	WineyardAreaWindow w;
	
	/**
	 * Inicializace.
	 * @param window Hlavni okno at vime kam na obrazovce vykreslit toto.
	 * @param data Data ktera se budou vypisovat.
	 */
	public WineAreaControl(MainWindow window, DataModel data) {

		this.window = window;
		this.data = data;
		
		w = new WineyardAreaWindow();
		
		//param.getComboBoxOwner().setModel();
		ArrayList<String> wines  = new ArrayList<String>();
		
		for(int i=0; i<data.getTypet().getRowCount();i++){
			wines.add((String)data.getTypet().getValueAt(i, 1));
		}
		w.getComboBoxWines().setModel(new DefaultComboBoxModel<Object>(wines.toArray()));	
		w.getBtnGo().addActionListener(this);
		
		w.setTitle("Wine Type Area in selected Year");
		w.setLocationRelativeTo(window.getFrame());
		w.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int id = (int)data.getTypet().getValueAt(w.getComboBoxWines().getSelectedIndex(), 0);

		double res = Math.round(data.getSpatt().wineTypeAreaAtYear(id, data.getActiveYear()));
		
		w.getLblResult().setText("<html>"+res+" m<sup>2</sup></html>");
	}
}
