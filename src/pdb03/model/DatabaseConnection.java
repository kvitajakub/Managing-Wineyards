package pdb03.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ord.im.OrdImage;

/**
 * Trida pro vytvoreni pripojeni k databazi z properties connectionString,
 * login a password, ktere predpokladame, ze jsou nastaveny pred zavolanim
 * konstruktoru. Vytvari vlastni statement, ke kteremu se pote pristupuje
 *  pres getter.
 * @author Jakub Kvita
 */
public class DatabaseConnection {

	private OracleDataSource ods;
    private Connection conn;
    private Statement statement;
    
	public Statement getStatement() {
		return statement;
	}

	/**
	 * Vytvori nove spojeni s databazi.
	 * @throws SQLException
	 */
	public DatabaseConnection() throws SQLException{
		ods = new OracleDataSource();
//		ods.setURL("jdbc:oracle:thin:@gort.fit.vutbr.cz:1521:dbgort");
		ods.setURL(System.getProperty("connectionString"));// -DconnectionString=<value>
		ods.setUser(System.getProperty("login"));// -Dlogin=<value>
		ods.setPassword(System.getProperty("password")); // -Dpassword=<value>

		conn = ods.getConnection();
		statement = conn.createStatement();
	}
	
	/**
	 * Odpojeni od databaze, pote se uz neumi pripojit, je potreba vytvorit
	 * znovu.
	 * @throws SQLException Problem na strane databaze.
	 */
	public void disconnect() throws SQLException {
		if (!conn.isClosed()) {
			conn.close();
		}
	}

	/**
	 * Metoda precte sql skript a spusti statementy nad pripojenim k databazi.
	 * @param path Cesta k souboru, muze byt i z aktivniho adresare.
	 * @throws SQLException Problem s statementem.
	 * @throws IOException Problem s ctenim souboru.
	 */
	public void runScriptFile(String path) throws SQLException, IOException{
		
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		
	    String currentLine;
	    String query = "";
	    
	    try {
		    while ((currentLine = input.readLine()) != null) {
		        // preskocime prazdne radky a komentare
		        if (currentLine.length() == 0
		        	|| currentLine.charAt(0) == '-') {
		            	continue;
		        }
		
		        // pridam radek do statementu
		        query += currentLine;
		
		        // kdyz je na konci strednik tak ho mam cely
		        if (query.charAt(query.length() - 1) == ';') {
		            
		            // smazu strednik
		            query = query.replace(';', ' ');
		            System.out.println(query);
		            try {
		            	//spustim statement
		                statement.executeUpdate(query);
		            }
		            catch(SQLException e) {
		                // kdyz mame problem s dropnutim neexistujici tabulky
		                if(! currentLine.substring(0, 3).toUpperCase().equals("DROP") 
	                		|| (!(e.getErrorCode() == 942) && !(e.getErrorCode() == 1418))) {
			                	throw e;
		                }
		            }
		            
		            //vycistim a pripravim na dalsi
		            query = "";
		        }
		    }
		    /* teraz vlozim obrazky
		    */
		    int size = 1000+5;		
	        boolean autoCommit = conn.getAutoCommit();
	        conn.setAutoCommit(false);
	        try {
                for (int id = 1000; id < size; id++) {
                    System.out.println("id: " + id);
                    OrdImage imgProxy = null;
                    OraclePreparedStatement stmt1 =(OraclePreparedStatement)
                            conn.prepareStatement("SELECT * FROM Wine_type WHERE Id = ? FOR UPDATE");
                    stmt1.setInt(1, id);
                    OracleResultSet rset = (OracleResultSet) stmt1.executeQuery();
                    try {
                        if (rset.next()) {
                            imgProxy = (OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory());
                        }
                    } finally {
                        rset.close();
                    }
                    stmt1.close();
                    String fileName = System.getProperty("SQLpath") + id + ".png";
                    imgProxy.loadDataFromFile(fileName);
                    imgProxy.setProperties();
                    String updateSQL1 = "update Wine_type SET photo=? WHERE Id = ?";
                    OraclePreparedStatement stmt2 = (OraclePreparedStatement) conn.prepareStatement(updateSQL1);
                    stmt2.setORAData(1, imgProxy);
                    stmt2.setInt(2, id);
                    stmt2.executeUpdate();
                    stmt2.close();
                    PreparedStatement stmt3 = conn.prepareStatement(
                    		"UPDATE Wine_type w set w.photo_si=SI_StillImage(w.photo.getContent()) WHERE Id=?");
                    stmt3.setInt(1, id);
                    stmt3.executeUpdate();
                    stmt3.close();
                    PreparedStatement stmt4 = conn.prepareStatement(
                    		"UPDATE Wine_type w set " +
                            "w.photo_ac=SI_AverageColor(w.photo_si), " +
                            "w.photo_ch=SI_ColorHistogram(w.photo_si), " +
                            "w.photo_pc=SI_PositionalColor(w.photo_si), " + 
                            "w.photo_tx=SI_Texture(w.photo_si) WHERE Id = ?");
                    stmt4.setInt(1, id);
                    stmt4.executeUpdate();
                    stmt4.close();
                }
                conn.commit();
	        } finally {
	        	conn.setAutoCommit(autoCommit);
	        }
		} finally{
			input.close();
		}
	}

	public Connection getConnection() {
		return conn;
	}
}
