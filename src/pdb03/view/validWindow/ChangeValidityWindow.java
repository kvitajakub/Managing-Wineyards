package pdb03.view.validWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Trida pro okno zmeny nastaveni validity selectovaneho objektu.
 * Nemuze byt sama, potrebuje MainWindow pro zjisteni s kterym objektem budeme pracovat.
 * @author Jakub Kvita
 *
 */
public class ChangeValidityWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JComboBox<Integer> comboBoxValidFrom;
	private JComboBox<Integer> comboBoxValidTo;
	private JButton btnValidSelect;
	
	private static Integer[] comboBoxModel = new Integer[] {2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020};
	private JLabel lblValidError;

	/**
	 * Vytvoreni okna.
	 * Potreba pote zavolat setVisible odjinud.
	 */
	public ChangeValidityWindow() {
		
		setTitle("Set Validity of Selected Item");
		getContentPane().setMinimumSize(new Dimension(400, 200));
		getContentPane().setPreferredSize(new Dimension(400, 200));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 100));
		panel.setPreferredSize(new Dimension(10, 100));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalStrut_3.setMinimumSize(new Dimension(5, 0));
		horizontalStrut_3.setMaximumSize(new Dimension(5, 32767));
		panel.add(horizontalStrut_3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(180, 100));
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("Valid from:");
		panel_1.add(lblNewLabel);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setMinimumSize(new Dimension(15, 0));
		horizontalStrut_1.setMaximumSize(new Dimension(15, 32767));
		panel_1.add(horizontalStrut_1);
		
		comboBoxValidFrom = new JComboBox<Integer>();
		comboBoxValidFrom.setModel(new DefaultComboBoxModel<Integer>(comboBoxModel));
		comboBoxValidFrom.setMaximumSize(new Dimension(32767, 30));
		panel_1.add(comboBoxValidFrom);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		JPanel panel_2 = new JPanel();
		panel_2.setMaximumSize(new Dimension(180, 100));
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Valid to:");
		panel_2.add(lblNewLabel_1);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setMinimumSize(new Dimension(15, 0));
		horizontalStrut_2.setMaximumSize(new Dimension(15, 32767));
		panel_2.add(horizontalStrut_2);
		
		comboBoxValidTo = new JComboBox<Integer>();
		comboBoxValidTo.setModel(new DefaultComboBoxModel<Integer>(comboBoxModel));
		comboBoxValidTo.setMaximumSize(new Dimension(32767, 30));
		panel_2.add(comboBoxValidTo);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		horizontalStrut_4.setMinimumSize(new Dimension(5, 0));
		horizontalStrut_4.setMaximumSize(new Dimension(5, 32767));
		panel.add(horizontalStrut_4);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		
		lblValidError = new JLabel("");
		panel_4.add(lblValidError);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setPreferredSize(new Dimension(0, 15));
		verticalStrut.setMinimumSize(new Dimension(0, 10));
		verticalStrut.setMaximumSize(new Dimension(32767, 10));
		panel_4.add(verticalStrut);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		btnValidSelect = new JButton("Choose");
		panel_5.add(btnValidSelect);
		btnValidSelect.setPreferredSize(new Dimension(90, 30));
		
		pack();
	}

	public JComboBox<Integer> getComboBoxValidFrom() {
		return comboBoxValidFrom;
	}

	public JComboBox<Integer> getComboBoxValidTo() {
		return comboBoxValidTo;
	}

	public JButton getBtnValidSelect() {
		return btnValidSelect;
	}

	public JLabel getLblValidError() {
		return lblValidError;
	}
	
}
