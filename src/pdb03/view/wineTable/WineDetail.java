package pdb03.view.wineTable;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;

import javax.swing.JTextPane;
import javax.swing.JButton;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Vytvoreni okna pro detail jednoho druhu vina, kde se daji editovat jednotlive polozky
 * a pridavat obrazek i mazat.
 * @author Jakub Kvita
 *
 */
public class WineDetail extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldName;
	private JButton btnNewPicture;
	private JTextPane textPaneDescription;
	private JComboBox<String> comboBox;
	private JLabel lblPicture;
	private JButton btnDone;
	
	private int wineID = 0;
	private JButton btnDeleteImage;
	
	/**
	 * Vytvoreni noveho prazdneho okna bez naplneni hodnotami.
	 */
	public WineDetail() {
		createGUI();
	}
	
	/**
	 * Vytvoreni okna, ktere se naplni hodnotami z parametru, pro editaci druhu vina,
	 * ktere uz mame v databazi.
	 * @param ID ID vina.
	 * @param name Jmeno vina.
	 * @param description Popis vina.
	 * @param colour Barva vina.
	 * @param icon Obrazek vina.
	 */
	public WineDetail(int ID, String name, String description, String colour, Icon icon) {
		createGUI();
		this.wineID = ID;
		this.textFieldName.setText(name);
		this.textPaneDescription.setText(description);
		this.comboBox.setSelectedItem(colour);
		this.lblPicture.setIcon(icon);
	}
	
	private void createGUI(){
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setPreferredSize(new Dimension(27, 20));
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		textFieldName = new JTextField();
		textFieldName.setPreferredSize(new Dimension(6, 25));
		panel_1.add(textFieldName);
		textFieldName.setMaximumSize(new Dimension(2147483647, 20));
		textFieldName.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Description");
		lblNewLabel_2.setPreferredSize(new Dimension(53, 20));
		panel_1.add(lblNewLabel_2);
		lblNewLabel_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		textPaneDescription = new JTextPane();
		textPaneDescription.setPreferredSize(new Dimension(6, 50));
		textPaneDescription.setMaximumSize(new Dimension(2147483647, 125));
		panel_1.add(textPaneDescription);
		
		JLabel lblNewLabel_3 = new JLabel("Colour");
		lblNewLabel_3.setPreferredSize(new Dimension(31, 20));
		panel_1.add(lblNewLabel_3);
		lblNewLabel_3.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(28, 25));
		panel_1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"red", "white", "pink"}));
		comboBox.setMaximumSize(new Dimension(100, 20));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setMinimumSize(new Dimension(0, 10));
		verticalStrut.setMaximumSize(new Dimension(0, 10));
		panel_1.add(verticalStrut);
		verticalStrut.setPreferredSize(new Dimension(0, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(150, 150));
		panel_1.add(scrollPane);
		
		lblPicture = new JLabel("");
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setViewportView(lblPicture);
		lblPicture.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setMinimumSize(new Dimension(0, 10));
		verticalStrut_1.setMaximumSize(new Dimension(0, 10));
		panel_1.add(verticalStrut_1);
		verticalStrut_1.setPreferredSize(new Dimension(0, 10));
		
		JPanel panel = new JPanel();
		panel_1.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		btnDeleteImage = new JButton("Delete Image");
		btnDeleteImage.setMaximumSize(new Dimension(100, 30));
		btnDeleteImage.setMinimumSize(new Dimension(100, 30));
		btnDeleteImage.setPreferredSize(new Dimension(120, 30));
		panel.add(btnDeleteImage);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setMaximumSize(new Dimension(20, 0));
		panel.add(horizontalStrut);
		
		btnNewPicture = new JButton("New Image");
		btnNewPicture.setMaximumSize(new Dimension(100, 30));
		panel.add(btnNewPicture);
		btnNewPicture.setPreferredSize(new Dimension(100, 30));
		btnNewPicture.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setMaximumSize(new Dimension(20, 0));
		panel.add(horizontalStrut_1);
		
		btnDone = new JButton("Done");
		btnDone.setMaximumSize(new Dimension(100, 30));
		btnDone.setPreferredSize(new Dimension(100, 30));
		panel.add(btnDone);
		
		pack();
	}
	
	public JTextField getTextFieldName() {
		return textFieldName;
	}
	public JButton getBtnNewPicture() {
		return btnNewPicture;
	}
	public JTextPane getTextPaneDescription() {
		return textPaneDescription;
	}
	public JComboBox<String> getComboBox() {
		return comboBox;
	}
	public JLabel getLblPicture() {
		return lblPicture;
	}
	public JButton getBtnDone() {
		return btnDone;
	}
	public int getWineID() {
		return wineID;
	}

	public JButton getBtnDeleteImage() {
		return btnDeleteImage;
	}
}
