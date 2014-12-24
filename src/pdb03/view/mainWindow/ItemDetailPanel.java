package pdb03.view.mainWindow;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

/**
 * Treti cast vizualizace informaci v hlavnim okne, zde se vypisuji informace o prave
 * oznacenem objektu. Tato trida nema kontrolu nad vypisovanymi a vykreslovanymi informacemi,
 * ale nechava ostatni objekty aby je vkladali do jednotlivych policek pro vino, obrazek vina
 * a majitele. Umi take zobrazovat a skryvat jednotlive casti,
 * podle toho co je zrovna potreba vypisovat.
 * @author Jakub Kvita
 *
 */
public class ItemDetailPanel extends JPanel {
	
	private static final long serialVersionUID = -5950265576558425266L;
	private JLabel picture;
	private JTextPane textPaneOwner;
	private JButton btnEditOwner;
	private JTextPane textPaneWineType;
	private JButton btnEditWine;
	private JPanel winePanel;
	private JLabel lblWine;
	private JLabel lblOwner;
	private JPanel ownerPanel;

	/**
	 * Vytvoreni panelu.
	 */
	public ItemDetailPanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		picture = new JLabel("");
		picture.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(picture);
		
		lblWine = new JLabel("Wine Type");
		lblWine.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblWine);
		
		winePanel = new JPanel();
		winePanel.setPreferredSize(new Dimension(10, 150));
		winePanel.setMaximumSize(new Dimension(32767, 150));
		add(winePanel);
		winePanel.setLayout(new BoxLayout(winePanel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		winePanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		textPaneWineType = new JTextPane();
		textPaneWineType.setEditable(false);
		panel_1.add(textPaneWineType);
		
		btnEditWine = new JButton("Edit");
		winePanel.add(btnEditWine);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(32767, 2));
		add(separator);
		
		lblOwner = new JLabel("Owner");
		lblOwner.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblOwner);
		
		ownerPanel = new JPanel();
		ownerPanel.setMaximumSize(new Dimension(32767, 80));
		ownerPanel.setPreferredSize(new Dimension(10, 80));
		add(ownerPanel);
		ownerPanel.setLayout(new BoxLayout(ownerPanel, BoxLayout.X_AXIS));
		
		textPaneOwner = new JTextPane();
		textPaneOwner.setPreferredSize(new Dimension(6, 50));
		textPaneOwner.setEditable(false);
		ownerPanel.add(textPaneOwner);
		
		btnEditOwner = new JButton("Edit");
		ownerPanel.add(btnEditOwner);
	}

	public JLabel getPicture() {
		return picture;
	}

	public JTextPane getTextPaneOwner() {
		return textPaneOwner;
	}

	public JButton getBtnEditOwner() {
		return btnEditOwner;
	}

	public JTextPane getTextPaneWineType() {
		return textPaneWineType;
	}

	public JButton getBtnEditWine() {
		return btnEditWine;
	}

	/**
	 * Prekresleni panelu.
	 */
	public void repaint(){
		super.repaint();
	}
	
	/**
	 * Zobrazeni casti s vinem, kdyz je to potreba u selectovaneho objektu.
	 * @param v 
	 */
	public void showWine(boolean v){
		winePanel.setVisible(v);
		lblWine.setVisible(v);
		picture.setVisible(v);
	}
	
	/**
	 * Zobrazeni casti s majitelem, pokud je to potreba u selectovaneho objektu.
	 * @param v
	 */
	public void showOwner(boolean v){
		ownerPanel.setVisible(v);
		lblOwner.setVisible(v);
	}
}
