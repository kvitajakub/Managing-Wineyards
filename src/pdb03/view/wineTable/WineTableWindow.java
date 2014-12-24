package pdb03.view.wineTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;


/**
 * GUI pro okno s tabulkou vsech vin, pridavani a mazani, detail obrazku vina.
 * @author Jakub Kvita
 *
 */
public class WineTableWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JButton btnWineDelete;
	private JButton btnWineEdit;
	private JButton btnWineNew;
	private JButton btnWinePick;
	private JButton btnWineRotate;
	private JSpinner spinnerAngle;
	private JLabel lblPicture;
	private Component rigidArea;
	private JButton btnFindSimilar;
	private JPanel panel_4;
	private JPanel panel_5;

	/**
	 * Vytvoreni okna.
	 * @param wineTypeTable Model dat tabulky, pripoji se primo zde.
	 */
	public WineTableWindow(TableModel wineTypeTable) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension(450, 45000));
		scrollPane.setMaximumSize(new Dimension(450, 200));
		scrollPane.setPreferredSize(new Dimension(450, 450));
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setModel(wineTypeTable);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_1.setAutoscrolls(true);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnWineDelete = new JButton("Delete");
		btnWineDelete.setPreferredSize(new Dimension(90, 30));
		btnWineDelete.setMaximumSize(new Dimension(90, 30));
		panel_1.add(btnWineDelete);
		
		btnWineEdit = new JButton("Edit");
		btnWineEdit.setPreferredSize(new Dimension(90, 30));
		panel_1.add(btnWineEdit);
		
		btnWineNew = new JButton("New");
		btnWineNew.setPreferredSize(new Dimension(90, 30));
		panel_1.add(btnWineNew);
		
		btnWinePick = new JButton("Pick");
		btnWinePick.setPreferredSize(new Dimension(90, 30));
		panel_1.add(btnWinePick);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		rigidArea.setVisible(false);
		rigidArea.setPreferredSize(new Dimension(90, 30));
		panel_1.add(rigidArea);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.EAST);
		panel_2.setPreferredSize(new Dimension(250, 10));
		panel_2.setMinimumSize(new Dimension(150, 10));
		panel_2.setLayout(new BorderLayout(0, 0));
		
		lblPicture = new JLabel("");
		lblPicture.setPreferredSize(new Dimension(250, 0));
		lblPicture.setMaximumSize(new Dimension(32767, 32767));
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		lblPicture.setIcon(new ImageIcon(WineTableWindow.class.getResource("/pdb03/sql/1001.png")));
		lblPicture.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_2.add(lblPicture, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(10, 75));
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_4);
		
		btnWineRotate = new JButton("Rotate");
		panel_4.add(btnWineRotate);
		btnWineRotate.setPreferredSize(new Dimension(90, 30));
		
		spinnerAngle = new JSpinner();
		panel_4.add(spinnerAngle);
		spinnerAngle.setPreferredSize(new Dimension(45, 30));
		spinnerAngle.setModel(new SpinnerNumberModel(45, 0, 359, 1));
		
		panel_5 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_5);
		
		btnFindSimilar = new JButton("Similar");
		panel_5.add(btnFindSimilar);
		btnFindSimilar.setPreferredSize(new Dimension(90, 30));
		
		pack();
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnWineDelete() {
		return btnWineDelete;
	}

	public JButton getBtnWineEdit() {
		return btnWineEdit;
	}

	public JButton getBtnWineNew() {
		return btnWineNew;
	}

	public JButton getBtnWinePick() {
		return btnWinePick;
	}

	public JButton getBtnWineRotate() {
		return btnWineRotate;
	}

	public JSpinner getSpinnerAngle() {
		return spinnerAngle;
	}

	public JLabel getLblPicture() {
		return lblPicture;
	}

	public JButton getBtnFindSimilar() {
		return btnFindSimilar;
	}

	/**
	 * Skryti/odkryti tlacitka pick, ktere potrebujeme jen obcas. 
	 * @param b Jestli skryvame nebo zobrazujeme.
	 */
	public void showPick(boolean b){
		btnWinePick.setVisible(b);
		rigidArea.setVisible(!b);
	}
	
	/**
	 * Schovani vsech ovladacich prvku, zbyde jen tabulka a detail obrazku.
	 * Pro pouziti v nejakem dotazu, kdy nechceme manipulovat s vysledky.
	 */
	public void hideButtons(){
		btnFindSimilar.setVisible(false);
		btnWineDelete.setVisible(false);
		btnWineEdit.setVisible(false);
		btnWineNew.setVisible(false);
		btnWinePick.setVisible(false);
		btnWineRotate.setVisible(false);
		spinnerAngle.setVisible(false);
	}
}
