package pdb03.controller;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import pdb03.model.DataModel;
import pdb03.view.mainWindow.MainWindow;
import pdb03.view.ownerTable.OwnerTableWindow;

/**
 * Ridici logika okna vypisujici procentualni zastoupeni
 * jednotlivych odrud vina v jednom roce.
 * @author Jakub Kvita
 *
 */
public class PercentageControl extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	MainWindow window;
	DataModel data;
	
	OwnerTableWindow res;
	
	ArrayList<Double> percentages;
	
	private String[] columnNames = {
			"ID",
			"Name",
            "Percentage"};   
	
	/**
	 * Konstruktor.
	 * @param window Kam okno vykreslime.
	 * @param data Data ktera potrebujeme.
	 */
	public PercentageControl(MainWindow window, DataModel data) {

		this.window = window;
		this.data = data;
		
		ArrayList<Double> valuesByProportion = new ArrayList<Double>();
		percentages = new ArrayList<>();
		
		for(int i=0; i<data.getTypet().getRowCount();i++){
			valuesByProportion.add(data.getSpatt().wineTypePercentageAtYear(
									(int)data.getTypet().getValueAt(i, 0),
									data.getActiveYear()));
		}
		
		double sum = 0;
		for (double d:valuesByProportion) {
			sum += d;
		}
		for (double d:valuesByProportion) {
			percentages.add((d/sum)*100);
		}
		
		
		this.res = new OwnerTableWindow(this);
				
		res.getTable().getColumnModel().getColumn(0).setPreferredWidth(10);
		res.getTable().getColumnModel().getColumn(1).setPreferredWidth(55);
		res.getTable().getColumnModel().getColumn(2).setPreferredWidth(20);
		
		this.res.hideButtons();
		this.res.setLocationRelativeTo(window.getFrame());
		this.res.setVisible(true);
	}

	@Override
	public int getRowCount() {
		return percentages.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
		case 1:
			return data.getTypet().getValueAt(rowIndex, columnIndex);
		case 2:
			return Math.round(percentages.get(rowIndex));
		default:
			break;
		}
		return null;
	}

}
