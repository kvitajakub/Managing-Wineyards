package pdb03;

import javax.swing.SwingUtilities;

import pdb03.controller.ControlConnection;

/**
 * Trida obsahujici metodu main, spustenim teto tridy se startuje aplikace.
 * @author Jakub Kvita
 */
public class Main {

	/**
	 * Funkce main. Kontroluje nastaveni system properties, jestli jsou nastavene
	 * a jestli ne, tak je vytvori.
	 * @param args Parametry prikazove radky, nepracujeme s nimi, nekontroluji se.
	 */
	public static void main(String[] args) {

		//pokud nemam nastavene properties tak si je vymyslim
		if(System.getProperty("connectionString") == null){
			System.setProperty("connectionString", "jdbc:oracle:thin:@gort.fit.vutbr.cz:1521:dbgort");
		}
		if(System.getProperty("login") == null){
			System.setProperty("login", "xkvita01");
		}
		if(System.getProperty("password") == null){
			System.setProperty("password", "dmqnrmzy");	
		}
		if(System.getProperty("SQLpath") == null){
			System.setProperty("SQLpath", "src/pdb03/sql/");	
//			System.setProperty("SQLpath", "trunk/src/pdb03/sql/");	
		}	
		
		//spustim aplikaci otevrenim prvniho okna
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ControlConnection();
		    }
		});
	}
}
