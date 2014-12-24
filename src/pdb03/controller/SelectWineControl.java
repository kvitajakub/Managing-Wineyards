package pdb03.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pdb03.model.SelectWineType;
import pdb03.model.WineGrowerTable;
import pdb03.view.ImageRenderer;
import pdb03.view.SelectOwnerAndYearsWindow;
import pdb03.view.mainWindow.MainWindow;
import pdb03.view.wineTable.WineTableWindow;

/**
 * Ridici logika pro okno dotazu na druhy vina pestovane majitelem v nejake roky.
 * @author Jakub Kvita
 *
 */
public class SelectWineControl implements ActionListener, ListSelectionListener {

	MainWindow window;
	SelectOwnerAndYearsWindow param;
	WineGrowerTable wgTable;
	SelectWineType selwine;
	WineTableWindow res;
	
	/**
	 * Vytvori okno na pozici, kterou urcuje nadrazene okno window.
	 * @param window Kam umistime okno.
	 * @param wgTable Tabulka majitelu at mame z ceho vybirat.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SelectWineControl(MainWindow window, WineGrowerTable wgTable) {

		this.wgTable = wgTable;
		this.window = window;
		
		//vytvoreni jednoho okna
		param = new SelectOwnerAndYearsWindow();
		
		//param.getComboBoxOwner().setModel();
		ArrayList<String> owners  = new ArrayList<String>();
		
		for(int i=0; i<wgTable.getRowCount();i++){
			owners.add((String)wgTable.getValueAt(i, 2));
		}
		
		param.getComboBoxOwner().setModel(new DefaultComboBoxModel(owners.toArray()));
		
		param.getBtnGo().addActionListener(this);
		
		param.setLocationRelativeTo(window.getFrame());
		param.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int id = (int)wgTable.getValueAt(param.getComboBoxOwner().getSelectedIndex(), 0);
		int from = (int)param.getComboBoxFrom().getSelectedItem();
		int to = (int)param.getComboBoxTo().getSelectedItem();
		
		param.dispose();
		
		try {
			selwine = new SelectWineType(id, from, to);
			res = new WineTableWindow(selwine);
			res.hideButtons();
			
			res.getLblPicture().setIcon(null);
			
			res.getTable().getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());
			res.getTable().getColumnModel().getColumn(0).setPreferredWidth(15);
			res.getTable().getColumnModel().getColumn(1).setPreferredWidth(15);
			res.getTable().getColumnModel().getColumn(2).setPreferredWidth(15);
			res.getTable().getColumnModel().getColumn(3).setPreferredWidth(70);
			res.getTable().getColumnModel().getColumn(4).setPreferredWidth(150);
			res.getTable().getColumnModel().getColumn(5).setPreferredWidth(20);
			res.getTable().getColumnModel().getColumn(6).setPreferredWidth(50);
			
			res.getTable().getSelectionModel().addListSelectionListener(this);
			
			res.setLocationRelativeTo(window.getFrame());
			res.setVisible(true);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(res.getTable().getSelectedRow() != -1){
			int selected =  res.getTable().convertRowIndexToModel(res.getTable().getSelectedRow());
			res.getLblPicture().setIcon((Icon)selwine.getValueAt(selected, 6));

		}
		else{
			res.getLblPicture().setIcon(null);
		}	
	}
	
	
}
