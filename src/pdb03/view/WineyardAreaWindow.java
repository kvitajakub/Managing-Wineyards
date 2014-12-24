package pdb03.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * GUI pro dotaz na plochu radku vina nejake odrudy v urcitem roce.
 * @author Jakub Kvita
 *
 */
public class WineyardAreaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<Object> comboBoxWines;
	private JButton btnGo;
	private JLabel lblResult;

	/**
	 * Vytvori okno.
	 * Potreba pote zavolat setVisible odjinud.
	 */
	public WineyardAreaWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		comboBoxWines = new JComboBox<Object>();
		comboBoxWines.setPreferredSize(new Dimension(28, 25));
		comboBoxWines.setMaximumSize(new Dimension(32767, 25));
		panel.add(comboBoxWines);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_1);
		
		btnGo = new JButton("Go");
		panel.add(btnGo);
		btnGo.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGo.setMaximumSize(new Dimension(90, 30));
		btnGo.setMinimumSize(new Dimension(90, 30));
		btnGo.setPreferredSize(new Dimension(90, 30));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		lblResult = new JLabel("");
		panel_1.add(lblResult);
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResult.setMaximumSize(new Dimension(32767, 20));
		lblResult.setMinimumSize(new Dimension(54, 20));
		lblResult.setPreferredSize(new Dimension(54, 20));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut);
	}

	public JComboBox<Object> getComboBoxWines() {
		return comboBoxWines;
	}

	public JButton getBtnGo() {
		return btnGo;
	}

	public JLabel getLblResult() {
		return lblResult;
	}
	
}
