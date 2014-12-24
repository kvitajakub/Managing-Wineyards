package pdb03.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

/**
 * Okno pro vlozeni majitele a casu od do, pro dotazy nad databazi.
 * Co se bude provadet s hodnotami zavisi na tom co jej vyvolalo, dobre je alespon
 * prenastavit zahlavi.
 * @author Jakub Kvita
 *
 */
public class SelectOwnerAndYearsWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboBoxOwner;
	private JComboBox<Integer> comboBoxFrom;
	private JComboBox<Integer> comboBoxTo;
	private JButton btnGo;

	private static Integer[] comboBoxModel = new Integer[] {2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020};
	
	/**
	 * Vytvoreni okna.
	 * Potreba pote zavolat setVisible odjinud.
	 */
	public SelectOwnerAndYearsWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 316, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_1);
		
		comboBoxOwner = new JComboBox<String>();
		comboBoxOwner.setPreferredSize(new Dimension(200, 25));
		comboBoxOwner.setMaximumSize(new Dimension(200, 25));
		panel.add(comboBoxOwner);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut_1);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("From ");
		panel_2.add(lblNewLabel);
		
		comboBoxFrom = new JComboBox<Integer>();
		panel_2.add(comboBoxFrom);
		comboBoxFrom.setPreferredSize(new Dimension(100, 25));
		comboBoxFrom.setMaximumSize(new Dimension(100, 30));
		comboBoxFrom.setModel(new DefaultComboBoxModel<Integer>(comboBoxModel));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("To ");
		lblNewLabel_1.setMinimumSize(new Dimension(27, 14));
		lblNewLabel_1.setMaximumSize(new Dimension(27, 14));
		lblNewLabel_1.setPreferredSize(new Dimension(27, 14));
		panel_3.add(lblNewLabel_1);
		
		comboBoxTo = new JComboBox<Integer>();
		panel_3.add(comboBoxTo);
		comboBoxTo.setPreferredSize(new Dimension(100, 25));
		comboBoxTo.setMaximumSize(new Dimension(100, 30));
		comboBoxTo.setModel(new DefaultComboBoxModel<Integer>(comboBoxModel));
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut_2);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_2);
		
		btnGo = new JButton("GO");
		btnGo.setPreferredSize(new Dimension(90, 30));
		contentPane.add(btnGo, BorderLayout.SOUTH);
	}

	public JComboBox<String> getComboBoxOwner() {
		return comboBoxOwner;
	}

	public JComboBox<Integer> getComboBoxFrom() {
		return comboBoxFrom;
	}

	public JComboBox<Integer> getComboBoxTo() {
		return comboBoxTo;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
}
