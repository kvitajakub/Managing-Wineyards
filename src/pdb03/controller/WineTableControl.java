package pdb03.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pdb03.model.WineType;
import pdb03.model.WineTypeTable;
import pdb03.view.ImageRenderer;
import pdb03.view.wineTable.WineDetail;
import pdb03.view.wineTable.WineTableWindow;

/**
 * Ridici logika okna tabulky druhu vin, ovlada vsechny listenery a manipuluje
 * podle toho s gui.
 * @author Jakub Kvita
 *
 */
public class WineTableControl implements ActionListener, ListSelectionListener {

	WineTableWindow window;
	WineDetail detail;
	
	/**
	 * Inicializace.
	 * @param wineTypeTable Model dat pro tabulku vin stazeny z databaze.
	 */
	public WineTableControl(WineTypeTable wineTypeTable) {
		
		window = new WineTableWindow(wineTypeTable);
		
		window.getLblPicture().setIcon(null);
		
		window.getTable().getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
		window.getTable().getColumnModel().getColumn(0).setPreferredWidth(15);
		window.getTable().getColumnModel().getColumn(1).setPreferredWidth(70);
		window.getTable().getColumnModel().getColumn(2).setPreferredWidth(150);
		window.getTable().getColumnModel().getColumn(3).setPreferredWidth(20);
		window.getTable().getColumnModel().getColumn(4).setPreferredWidth(40);
		
		window.getBtnWineDelete().addActionListener(this);
		window.getBtnWineEdit().addActionListener(this);
		window.getBtnWineNew().addActionListener(this);
		window.getBtnWinePick().addActionListener(this);
		window.getBtnWineRotate().addActionListener(this);
		window.getBtnFindSimilar().addActionListener(this);
		
		window.getTable().getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		WineTypeTable model = (WineTypeTable) window.getTable().getModel();
		
		if(e.getSource() == window.getBtnWineDelete()){

			if(window.getTable().getRowCount()!= 0 && window.getTable().getSelectedRow()!= -1){
				int row = window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
				model.deleteWineType(((int)model.getValueAt(row, 0)));
				window.getTable().getSelectionModel().clearSelection();
				refreshGUI();
			}
		}	
		else if(e.getSource() == window.getBtnWineRotate()){
			if(getSelectedIndex() != -1){
				model.rotateImage(model.getWineWithId((Integer)model.getValueAt(getSelectedIndex(), 0)), (Integer)window.getSpinnerAngle().getValue());
				refreshGUI();
			}
		}
		else if(e.getSource() == window.getBtnFindSimilar()){
			if(getSelectedIndex() != -1){

				WineType res = model.getMostSimilar(model.getWineWithId((Integer)model.getValueAt(getSelectedIndex(), 0)));
				
				for(int i=0;i<window.getTable().getRowCount();i++){
					if(((Integer)window.getTable().getValueAt(i, 0)).equals(new Integer(res.getId()))){
						window.getTable().getSelectionModel().setSelectionInterval(i, i);
					}
				}
				
				refreshGUI();
			}
		}
		else if(e.getSource() == window.getBtnWineEdit()){
    
			if(window.getTable().getSelectedRow()!= -1){
				int row = window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
				
				detail = new WineDetail((int)model.getValueAt(row,0),
						 (String)model.getValueAt(row,1),
						 (String)model.getValueAt(row,2),
						 (String)model.getValueAt(row,3),
						 (Icon)model.getValueAt(row,4));
				
				detail.getBtnDone().addActionListener(this);
				detail.getBtnNewPicture().addActionListener(this);
				detail.getBtnDeleteImage().addActionListener(this);
				
				detail.setLocationRelativeTo(window.getContentPane());
				detail.setVisible(true);
			}
		}	
		else if(e.getSource() == window.getBtnWineNew()){
						
			//vytvoreni noveho okna
			detail = new WineDetail();
			
			detail.getBtnDone().addActionListener(this);
			detail.getBtnNewPicture().addActionListener(this);
			detail.getBtnDeleteImage().addActionListener(this);
			
			detail.setLocationRelativeTo(window.getContentPane());
			detail.setVisible(true);
			
		}
		else if(detail != null && e.getSource() == detail.getBtnDone()){
			  //TLACITKO DONE BYLO ZMACKNUTO
				
			if(detail.getWineID() == 0){
				//NEW
				model.insertNewWineType(detail.getTextFieldName().getText(),
										detail.getTextPaneDescription().getText(),
										(String)detail.getComboBox().getSelectedItem(),
										detail.getLblPicture().getIcon());
			}
			else{
				//EDIT
				model.updateWineType(detail.getWineID(),
								detail.getTextFieldName().getText(),
								detail.getTextPaneDescription().getText(),
								(String)detail.getComboBox().getSelectedItem(),
								detail.getLblPicture().getIcon());
			}
			detail.dispose();
			detail = null;
			refreshGUI();
		}
		else if(detail != null && e.getSource() == detail.getBtnNewPicture()){
			  //TLACITKO NEW Picture BYLO ZMACKNUTO
				
			JFileChooser chooser = new JFileChooser();
//			FileNameExtensionFilter filter = new FileNameExtensionFilter(
//			    "JPG & GIF Images", "jpg", "gif");
//			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(detail);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
//			   System.out.println("You chose to open this file: " +
//			        chooser.getSelectedFile().getName());
				BufferedImage img = null;
				try {
				    img = ImageIO.read(chooser.getSelectedFile());
				    detail.getLblPicture().setIcon(new ImageIcon(img));
				} catch (IOException e1) {
					//nic nedelame, pouze nejde nacist
				}
			}
		}
		else if(detail != null && e.getSource() == detail.getBtnDeleteImage()){
			  //TLACITKO DELETE IMAGE BYLO ZMACKNUTO
				
		    detail.getLblPicture().setIcon(null);
		}
	}
	
	private void refreshGUI(){
		WineTypeTable model = (WineTypeTable) window.getTable().getModel();
		model.fireTableDataChanged();
		//nastavime obrazek podle selectu
		if(getSelectedIndex() == -1){
			window.getLblPicture().setIcon(null);
		}
		else{
			window.getLblPicture().setIcon((Icon)model.getValueAt(getSelectedIndex(), 4));
		}
	}

	public void setLocationRelativeTo(JFrame frame) {
		window.setLocationRelativeTo(frame);
		
	}

	public void setVisible(boolean b) {
		window.setVisible(b);
	}

	public Window getWindow() {
		return window;
	}
	
	/**
	 * Ovladani viditelnosti tlacitka Pick, ktere chceme jen obcas.
	 * @param b
	 */
	public void showPick(boolean b) {
		window.showPick(b);		
	}

	public void setActionCommand(String cmd) {
		window.getBtnWinePick().setActionCommand(cmd);
		
	}
	
	public void addPickActionListener(ActionListener c){
		window.getBtnWinePick().addActionListener(c);
	}

	public void dispose() {
		window.dispose();
	}
	
	/**
	 * Vrati index selectovaneho radku tabulky.
	 * @return Index
	 */
	public int getSelectedIndex() {
		if(window.getTable().getSelectedRow() != -1){
			return window.getTable().convertRowIndexToModel(window.getTable().getSelectedRow());
		}
		else{
			return -1;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		refreshGUI();
	}
	
}
