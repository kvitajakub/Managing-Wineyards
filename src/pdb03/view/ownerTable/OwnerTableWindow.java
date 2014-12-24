package pdb03.view.ownerTable;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

/**
 * Okno pro zpracovani vsech Owneru, tabulka s jejich hodnotami a ovladacimi prvky.
 * @author Jakub Kvita
 *
 */
public class OwnerTableWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JButton btnOwnerDelete;
	private JButton btnOwnerEdit;
	private JButton btnOwnerNew;
	private JButton btnOwnerPick;
	private Component rigidAreaOwner;

	/**
	 * Vytvoreni okna.
	 * @param wineGrowerTable Data pro tabulku
	 */
	public OwnerTableWindow(TableModel wineGrowerTable) {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1);
		
		table = new JTable();
		table.setModel(wineGrowerTable);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		
		table.setAutoCreateRowSorter(true);		
		scrollPane_1.setViewportView(table);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		btnOwnerDelete = new JButton("Delete");
		btnOwnerDelete.setPreferredSize(new Dimension(90, 30));
		btnOwnerDelete.setMaximumSize(new Dimension(70, 30));
		panel.add(btnOwnerDelete);
		
		btnOwnerEdit = new JButton("Edit");
		btnOwnerEdit.setPreferredSize(new Dimension(90, 30));
		panel.add(btnOwnerEdit);
		
		btnOwnerNew = new JButton("New");
		btnOwnerNew.setPreferredSize(new Dimension(90, 30));
		panel.add(btnOwnerNew);
		
		btnOwnerPick = new JButton("Pick");
		btnOwnerPick.setPreferredSize(new Dimension(90, 30));
		panel.add(btnOwnerPick);
		
		rigidAreaOwner = Box.createRigidArea(new Dimension(20, 20));
		rigidAreaOwner.setPreferredSize(new Dimension(90, 30));
		rigidAreaOwner.setVisible(false);
		panel.add(rigidAreaOwner);

		pack();
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnOwnerDelete() {
		return btnOwnerDelete;
	}

	public JButton getBtnOwnerEdit() {
		return btnOwnerEdit;
	}

	public JButton getBtnOwnerNew() {
		return btnOwnerNew;
	}

	public JButton getBtnOwnerPick() {
		return btnOwnerPick;
	}

	/**
	 * Skryti/odkryti tlacitka pick, ktere potrebujeme jen obcas. 
	 * @param b Jestli skryvame nebo zobrazujeme.
	 */
	public void showPick(boolean b){
			btnOwnerPick.setVisible(b);
			rigidAreaOwner.setVisible(!b);
	}	
	
	/**
	 * Skryti tlacitek pro manipulaci s tabulkou. Kdyz toto okno pouzivame pro
	 * vypsani vystupu nejakeho dotazu.
	 */
	public void hideButtons(){
		btnOwnerDelete.setVisible(false);
		btnOwnerEdit.setVisible(false);
		btnOwnerNew.setVisible(false);
		btnOwnerPick.setVisible(false);
	}
	
}
