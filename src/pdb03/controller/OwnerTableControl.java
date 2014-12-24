package pdb03.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import pdb03.model.DataModel;
import pdb03.model.WineGrowerTable;
import pdb03.view.ownerTable.OwnerDetail;
import pdb03.view.ownerTable.OwnerTableWindow;

/**
 * Ridici logika pro tabulku vsech majitelu, jejich pridavani, ubirani a editaci.
 * @author Jakub Kvita
 *
 */
public class OwnerTableControl implements ActionListener {

	OwnerTableWindow window;
	OwnerDetail detail = null;
	boolean b = true;
	
	DataModel data;
	
	/**
	 * Konstruktor.
	 * @param data Natahnute data z databaze.
	 */
	public OwnerTableControl(DataModel data) {
		window = new OwnerTableWindow(data.getGrowt());
					
		this.data = data;
		
		window.getBtnOwnerDelete().addActionListener(this);
		window.getBtnOwnerEdit().addActionListener(this);
		window.getBtnOwnerNew().addActionListener(this);
		
	}

	public void setLocationRelativeTo(JFrame frame) {
		window.setLocationRelativeTo(frame);
		
	}

	public void setVisible(boolean b) {
		window.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		WineGrowerTable model = (WineGrowerTable) window.getTable().getModel();
		
		if(e.getSource() == window.getBtnOwnerDelete()){
			if(window.getTable().getSelectedRow()!= -1){
				int row = window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
				model.deleteOwner((int)model.getValueAt(row, 0));
				model.fireTableDataChanged();
				data.setActiveYear(data.getActiveYear());
			}
		}
		else if(e.getSource() == window.getBtnOwnerEdit()){
    
			if(window.getTable().getSelectedRow()!= -1){
				int row = window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
	
				detail = new OwnerDetail((int)model.getValueAt(row,0),
										 (String)model.getValueAt(row,1),
										 (String)model.getValueAt(row,2),
										 (String)model.getValueAt(row,3));
				
				detail.getBtnDone().addActionListener(this);
				
				detail.setLocationRelativeTo(window.getContentPane());
				detail.setVisible(true);
			}
			
		}
		else if(e.getSource() == window.getBtnOwnerNew()){
	  
			//vytvoreni noveho okna viz vyse
			detail = new OwnerDetail();
			
			detail.getBtnDone().addActionListener(this);
			
			detail.setLocationRelativeTo(window.getContentPane());
			detail.setVisible(true);
			
		}
		else if(detail != null && e.getSource() == detail.getBtnDone()){
			  //TLACITKO DONE BYLO ZMACKNUTO
				
			if(detail.getOwnerID() == 0){
				//NEW
				model.insertNewOwner(detail.getTextFieldName().getText(), detail.getTextFieldSurname().getText(), detail.getTextAreaAddress().getText());
				detail.dispose();
				detail = null;
				model.fireTableDataChanged();
			}
			else{
				//EDIT
				model.updateOwner(detail.getOwnerID(),
								detail.getTextFieldName().getText(),
								detail.getTextFieldSurname().getText(),
								detail.getTextAreaAddress().getText());
				detail.dispose();
				detail = null;
				model.fireTableDataChanged();
			}
		}	
	}

	
	public OwnerTableWindow getWindow() {
		return window;
	}

	public int getSelectedIndex(){
		if(window.getTable().getSelectedRow() != -1){
			return window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
		}
		else{
			return -1;
		}
	}

	public void showPick(boolean c) {
		window.showPick(c);
		
	}

	public void setActionCommand(String cmd) {
		window.getBtnOwnerPick().setActionCommand(cmd);
		
	}
	
	/**
	 * Prida listener pro tlacitko Pick ktere se ovlada odjinud.
	 * @param c
	 */
	public void addPickActionListener(ActionListener c){
		window.getBtnOwnerPick().addActionListener(c);
	}

	public void dispose() {
		window.dispose();
	}
	
}
