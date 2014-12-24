package pdb03.view.mainWindow;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Tato trida obsahuje tabulku v soucasnosti vykreslovanych objektu v canvasu.
 * Nevytvari si vlastni model dat, ale ocekava, ze jej nastavi nekdo jiny pres
 * metodu getTable(). 
 * @author Jakub Kvita
 *
 */
public class ItemListPanel extends JPanel {

	private static final long serialVersionUID = -3698803742911861609L;
	private JTable table;

	/**
	 * Vytvoreni okna.
	 */
	public ItemListPanel() {
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		scrollPane.setViewportView(table);
	}

	public JTable getTable() {
		return table;
	}
}
