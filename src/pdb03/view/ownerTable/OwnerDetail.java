package pdb03.view.ownerTable;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

/**
 * Okno pro detail jednoho majitele, kde je mozne menit jeho hodnoty.
 * @author Jakub Kvita
 *
 */
public class OwnerDetail extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldName;
	private JTextField textFieldSurname;
	private JTextArea textAreaAddress;
	private JButton btnDone;

	private int ownerID = 0;
	
	/**
	 * Vytvori nove prazdne okno, pro noveho majitele.
	 */
	public OwnerDetail() {
		setTitle("New Owner");
		createGUI();
	}
	
	/**
	 * Vytvori okno s predvyplnenymi hodnotami majitele se kterym pracuje.
	 * @param ID ID majitele.
	 * @param name  Jmeno majitele.
	 * @param surname Prijmeni majitele.
	 * @param address Adresa majitele.
	 */
	public OwnerDetail(int ID, String name, String surname,
			String address) {
		setTitle("Edit Owner");
		this.ownerID = ID;
		createGUI();
		textFieldName.setText(name);
		textFieldSurname.setText(surname);
		textAreaAddress.setText(address);
	}

	private void createGUI(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 360);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JLabel lblName = new JLabel("Name");
		lblName.setPreferredSize(new Dimension(27, 20));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setMaximumSize(new Dimension(32767, 25));
		lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setMinimumSize(new Dimension(6, 25));
		textFieldName.setPreferredSize(new Dimension(6, 25));
		textFieldName.setMaximumSize(new Dimension(2147483647, 25));
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setPreferredSize(new Dimension(42, 20));
		lblSurname.setMinimumSize(new Dimension(42, 20));
		lblSurname.setHorizontalAlignment(SwingConstants.CENTER);
		lblSurname.setMaximumSize(new Dimension(32767, 25));
		lblSurname.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(lblSurname);
		
		textFieldSurname = new JTextField();
		textFieldSurname.setPreferredSize(new Dimension(6, 25));
		textFieldSurname.setMinimumSize(new Dimension(6, 25));
		textFieldSurname.setMaximumSize(new Dimension(2147483647, 25));
		contentPane.add(textFieldSurname);
		textFieldSurname.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setPreferredSize(new Dimension(39, 20));
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAddress.setMaximumSize(new Dimension(32767, 25));
		contentPane.add(lblAddress);
		
		textAreaAddress = new JTextArea();
		textAreaAddress.setMaximumSize(new Dimension(2147483647, 3568675));
		contentPane.add(textAreaAddress);
		
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 40));
		contentPane.add(panel);
		
		btnDone = new JButton("Done");
		btnDone.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDone.setPreferredSize(new Dimension(90, 30));
		panel.add(btnDone);
	}

	public JTextField getTextFieldName() {
		return textFieldName;
	}

	public JTextField getTextFieldSurname() {
		return textFieldSurname;
	}

	public JTextArea getTextAreaAddress() {
		return textAreaAddress;
	}

	public JButton getBtnDone() {
		return btnDone;
	}

	public int getOwnerID() {
		return ownerID;
	}

}
