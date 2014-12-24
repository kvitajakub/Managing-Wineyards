package pdb03.view.connectWindow;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Trida pro vykresleni pocatecniho okna pro pripojeni k databazi.
 * Tlacitka a pole pro vkladani textu, jejich formatovani, pristupuje se pres
 * gettery a v prislusne kontrolni tride se nastavuji listenery.
 * @author Jakub Kvita
 *
 */
public class ConnectionSetWindow extends JFrame {

	private static final long serialVersionUID = -4618183453795246728L;
	private JPanel contentPane;
	private JTextField loginTextField;
	private JPasswordField passwordField;
	private JTextField connectionTextField;
	private JButton connectButton;
	private JLabel connectionErrorLabel;
	private JButton defaultButton;

	public JButton getDefaultButton() {
		return defaultButton;
	}

	public JTextField getLoginTextField() {
		return loginTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JTextField getConnectionTextField() {
		return connectionTextField;
	}

	public JButton getConnectButton() {
		return connectButton;
	}

	public JLabel getConnectionErrorLabel() {
		return connectionErrorLabel;
	}
	
	/**
	 * Vytvoreni okna pro pripojeni, vsechny ovladaci prvky.
	 */
	public ConnectionSetWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 299);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(500, 300));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalStrut_2.setMaximumSize(new Dimension(32767, 40));
		verticalStrut_2.setSize(new Dimension(0, 40));
		verticalStrut_2.setPreferredSize(new Dimension(0, 40));
		verticalStrut_2.setMinimumSize(new Dimension(0, 40));
		contentPane.add(verticalStrut_2);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_2 = new JLabel("Connection string:");
		lblNewLabel_2.setMinimumSize(new Dimension(120, 20));
		lblNewLabel_2.setMaximumSize(new Dimension(120, 20));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_2.setPreferredSize(new Dimension(120, 20));
		panel_2.add(lblNewLabel_2);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setMaximumSize(new Dimension(20, 0));
		panel_2.add(horizontalStrut);
		
		connectionTextField = new JTextField();
		connectionTextField.setMaximumSize(new Dimension(330, 20));
		panel_2.add(connectionTextField);
		connectionTextField.setColumns(10);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut_1);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("Login:");
		lblNewLabel.setMaximumSize(new Dimension(120, 20));
		lblNewLabel.setMinimumSize(new Dimension(120, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setPreferredSize(new Dimension(120, 20));
		panel.add(lblNewLabel);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setMaximumSize(new Dimension(20, 0));
		panel.add(horizontalStrut_1);
		
		loginTextField = new JTextField();
		loginTextField.setMinimumSize(new Dimension(250, 20));
		loginTextField.setPreferredSize(new Dimension(250, 20));
		loginTextField.setMaximumSize(new Dimension(330, 20));
		panel.add(loginTextField);
		loginTextField.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setMinimumSize(new Dimension(120, 20));
		lblNewLabel_1.setMaximumSize(new Dimension(120, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_1.setPreferredSize(new Dimension(120, 20));
		panel_1.add(lblNewLabel_1);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setMaximumSize(new Dimension(20, 0));
		panel_1.add(horizontalStrut_2);
		
		passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(330, 20));
		passwordField.setPreferredSize(new Dimension(250, 20));
		panel_1.add(passwordField);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut_3);
		
		JPanel panel_3 = new JPanel();
		panel_3.setMaximumSize(new Dimension(32767, 50));
		contentPane.add(panel_3);
		
		defaultButton = new JButton("Set default");
		panel_3.add(defaultButton);
		
		connectButton = new JButton("Connect");
		panel_3.add(connectButton);
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		connectionErrorLabel = new JLabel("");
		connectionErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(connectionErrorLabel);
	}

}
