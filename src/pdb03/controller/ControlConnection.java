package pdb03.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import pdb03.model.DatabaseConnection;
import pdb03.view.connectWindow.ConnectionSetWindow;

/**
 *	Startovaci trida, otevre okno pro pripojeni k databazi a pri uspechu otevre
 *	hlavni okno aplikace.
 * @author Jakub Kvita
 */
public class ControlConnection implements ActionListener {
	
	private ConnectionSetWindow startWindow;
	
	/**
	 * Konstruktor volajici se z mainu.
	 */
	public ControlConnection() {
		super();
		startWindow = new ConnectionSetWindow();
		
		startWindow.getConnectionTextField().setText(System.getProperty("connectionString"));
		startWindow.getLoginTextField().setText(System.getProperty("login"));
		startWindow.getPasswordField().setText(System.getProperty("password"));
		
		startWindow.getConnectButton().addActionListener(this);
		startWindow.getConnectButton().setActionCommand("connect");
		startWindow.getDefaultButton().addActionListener(this);
		startWindow.getDefaultButton().setActionCommand("default");
		startWindow.setVisible(true);
	}

	
	private void openMainWindow() {		
		new MainControl();
	}	
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {

		startWindow.getConnectionErrorLabel().setText(null);
		
		String connectionString = System.getProperty("connectionString");
		String login = System.getProperty("login");
		String password = System.getProperty("password");
		
		if(e.getActionCommand().equals("connect")){
//----------------------------------------------------------------------------------	
			
			System.setProperty("login", startWindow.getLoginTextField().getText());
			System.setProperty("connectionString", startWindow.getConnectionTextField().getText());
			System.setProperty("password", startWindow.getPasswordField().getText());
			
			System.out.println("Connection string:  "+System.getProperty("connectionString"));
			System.out.println("Login:              "+System.getProperty("login"));
			System.out.println("Password:           "+System.getProperty("password"));
			
			try {
				//zkusime vytvorit spojeni s databazi s nastavenyma hodnotama
				new DatabaseConnection();
				
				//pokud se povedlo tak skoncime a vytvorime vlastni main okno
				
				startWindow.dispose();
				
				openMainWindow();
//				====================
				
			} catch (SQLException e1) {
				
				//nepovedlo se, nastavime properties zpatky na puvodni hodnoty a vypiseme error
				e1.printStackTrace();
				startWindow.getConnectionErrorLabel().setText("Pripojeni se nezdarilo.");			

				System.setProperty("connectionString", connectionString);
				System.setProperty("login", login);
				System.setProperty("password", password);	
			}
			
		}
		else if(e.getActionCommand().equals("default")){
//---------------------------------------------------------------------------	
			
			startWindow.getConnectionTextField().setText(System.getProperty("connectionString"));
			startWindow.getLoginTextField().setText(System.getProperty("login"));
			startWindow.getPasswordField().setText(System.getProperty("password"));
			//jen nastavime pole, takze konec
			return;
		}
		else{		
//----------------------------------------------------------------------------		
			
			//some action which was not prepared
			startWindow.getConnectionErrorLabel().setText("Nejaky errror na ktery nejsme pripraveni. rn.33455433");
			return;
		}		

	}
}
