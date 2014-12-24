package pdb03.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import pdb03.model.DataModel;
import pdb03.view.SelectOwnerAndYearsWindow;

/**
 * Ridici logika pro zmenu validity selectovaneho objektu.
 * @author Jakub Kvita
 *
 */
public class ChangeOwnerIntervalControl implements ActionListener {

	MainControl mainControl;
	DataModel data;
	SelectOwnerAndYearsWindow w;
	
	int selectedID;
	
	/**
	 * Konstruktor.
	 * @param mainControl Kam vykreslime okno.
	 * @param data Data pro zpracovani.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ChangeOwnerIntervalControl(MainControl mainControl, DataModel data) {
		
		this.mainControl = mainControl;
		this.data = data;
		
		selectedID = data.getSelected().getId();
		
		w = new SelectOwnerAndYearsWindow();
		
		ArrayList<String> owners  = new ArrayList<String>();
		
		for(int i=0; i<data.getGrowt().getRowCount();i++){
			owners.add((String)data.getGrowt().getValueAt(i, 2));
		}
		w.getComboBoxOwner().setModel(new DefaultComboBoxModel(owners.toArray()));
		
		w.getBtnGo().addActionListener(this);
		
		w.setLocationRelativeTo(mainControl.getWindow().getFrame());
		w.setVisible(true);
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int id = (int)data.getGrowt().getValueAt(w.getComboBoxOwner().getSelectedIndex(), 0);
		int from = (int)w.getComboBoxFrom().getSelectedItem();
		int to = (int)w.getComboBoxTo().getSelectedItem();
		
		data.getGrowt().setOwnerPeriod(selectedID, id, from, to);
		
		w.dispose();
		data.setActiveYear(data.getActiveYear());
		mainControl.refreshGui();
	}
}
