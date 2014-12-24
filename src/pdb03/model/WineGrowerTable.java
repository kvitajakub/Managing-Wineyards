package pdb03.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import oracle.jdbc.OracleResultSet;

/**
 * Trida pro tabulku Wine_Grower.
 * @author Jakub Kvita
 *
 */
public class WineGrowerTable extends AbstractTableModel {

	private static final long serialVersionUID = -6707887053859488205L;
	ArrayList<WineGrower> wgTable;
	DatabaseConnection dbconn;
	
	private String[] columnNames = {
			"ID",
            "Name",
            "Surname",
            "Address"};   
	
	/**
	 * Vytvori a naplni tabulku.
	 * @param conn Spojeni s databazi.
	 * @throws SQLException Nejaky problem pri zpracovani databaze.
	 */
	public WineGrowerTable(DatabaseConnection conn) throws SQLException {
		dbconn = conn;
		reload();
	}

	/**
	 * Znovu stahne data z databaze a zacne je vracet.
	 * @throws SQLException
	 */
	public void reload() throws SQLException {
		wgTable = new ArrayList<>();
		try {
			OracleResultSet rset = (OracleResultSet) dbconn.getStatement().executeQuery("SELECT * FROM Wine_grower");
			while(rset.next()) {
				wgTable.add(new WineGrower(
						rset.getInt(1),		// id
						rset.getString(2),	// name
						rset.getString(3),	// surname
						rset.getString(4)	// adress
						));
			}
		} finally {
		}
	}

	@Override
	public int getRowCount() {
		return wgTable.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return wgTable.get(rowIndex).getColumn(columnIndex);
	}

	@Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
		
	/**
	 * Vrati prijmeni majitele s ID na vstupu.
	 * @param ID ID majitele ktery nas zajima.
	 * @return Jeho prijmeni.
	 */
	public String getSurname(int ID){
		for (WineGrower w:wgTable) {
			if (w.getId() == ID) {
                return  w.getSurname();
			}
		}
		return "DB Error";
	}
	
	/**
	 * Vrati majitele jako text se vsecmi informacemi ktere ma.
	 * @param wine_grower_id ID majitele ktery nas zajima.
	 * @return Data ve forme Stringu.
	 */
	public String getOwnerText(int wine_grower_id) {
		for (WineGrower w:wgTable) {
			if (w.getId() == wine_grower_id) {
                return  "Name: " + w.getName() +" "+ w.getSurname() + "\n" + "Address: " + w.getAddress();
			}
		}
		return "";
	}

	/**
	 * Smaze majitele s urcitym ID.
	 * @param ID ID majitele ktereho mame smazat.
	 */
	public void deleteOwner(int ID){
        PreparedStatement pstmt;
		try {
			pstmt = dbconn.getConnection().prepareStatement(
			        "DELETE FROM Wine_grower WHERE id = ?");
            pstmt.setInt(1, ID);
            pstmt.execute();
            pstmt.close();
            reload();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Delete"+ID);
	}
	
	/**
	 * Vlozi noveho majitele do databaze.
	 * @param name Jeho jmeno.
	 * @param surname Jeho prijmeni.
	 * @param address Jeho adresa.
	 */
	public void insertNewOwner(String name, String surname, String  address){
        PreparedStatement pstmt;
		try {
			pstmt = dbconn.getConnection().prepareStatement(
			        "INSERT INTO Wine_grower VALUES(Wine_grower_seq.nextval, ?, ?, ?)");
			// o ID noveho objektu sa stará databáza
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			pstmt.setString(3, address);
            pstmt.execute();
            pstmt.close();
            reload();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updatuje nejakeho majitele novymi daty. Neda se menit ID!
	 * @param ID ID uzivatele ktery nas zajima. Nemeni se!
	 * @param name Nove jmeno.
	 * @param surname Nove prijmeni.
	 * @param address Nova adresa.
	 */
	public void updateOwner(int ID, String name, String surname, String  address){
        PreparedStatement pstmt;
		try {
			pstmt = dbconn.getConnection().prepareStatement(
			        "UPDATE Wine_grower SET name=?, surname=?, address=? WHERE id = ?");
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			pstmt.setString(3, address);
            pstmt.setInt(4, ID);
            pstmt.execute();
            pstmt.close();
            reload();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Update"+ID);

	}

	/**
	 * Nastaveni noveho majitele pro prostorovy objekt v urcitem casovem obdobi.
	 * @param so_id ID objektu se kterym pracujeme.
	 * @param wine_grower_id ID noveho majitele tohoto objektu.
	 * @param from_year Od ktereho roku jej ma vlastnit.
	 * @param to_year Do ktereho roku jej ma vlastnit.
	 */
	public void setOwnerPeriod(int so_id, int wine_grower_id, int from_year, int to_year) {
		try {
			// right overlap with interval
			PreparedStatement stmt1 = dbconn.getConnection().prepareStatement(
					"INSERT INTO Spatial_object " +
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, s.valid_from, ?," +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE s.id=? AND s.valid_from<? AND s.valid_to>=? AND s.valid_to<=? " +
                		"AND s.wine_grower_id<>?");
			stmt1.setInt(1, from_year-1);
			stmt1.setInt(2, so_id);
			stmt1.setInt(3, from_year);
			stmt1.setInt(4, from_year);
			stmt1.setInt(5, to_year);
			stmt1.setInt(6, wine_grower_id);
			stmt1.executeUpdate();
			stmt1.close();
			PreparedStatement stmt2 = dbconn.getConnection().prepareStatement(
					"UPDATE Spatial_object SET Valid_from=?, wine_grower_id=? " +
                	"WHERE id=? AND valid_from<? AND valid_to>=? AND valid_to<=? " +
                    "AND wine_grower_id<>?");
			stmt2.setInt(1, from_year);
			stmt2.setInt(2, wine_grower_id);
			stmt2.setInt(3, so_id);
			stmt2.setInt(4, from_year);
			stmt2.setInt(5, from_year);
			stmt2.setInt(6, to_year);
			stmt2.setInt(7, wine_grower_id);
			stmt2.executeUpdate();
			stmt2.close();
			
			// to iste ale z druhej strany
			stmt1 = dbconn.getConnection().prepareStatement(
					"INSERT INTO Spatial_object " +
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, ?, s.valid_to, " +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE s.id=? AND s.valid_from>=? AND s.valid_from<=? AND s.valid_to>? " +
                        "AND s.wine_grower_id<>?");
			stmt1.setInt(1, to_year+1);
			stmt1.setInt(2, so_id);
			stmt1.setInt(3, from_year);
			stmt1.setInt(4, to_year);
			stmt1.setInt(5, to_year);
			stmt1.setInt(6, wine_grower_id);
			stmt1.executeUpdate();
			stmt1.close();
			stmt2 = dbconn.getConnection().prepareStatement(
					"UPDATE Spatial_object SET Valid_to=?, wine_grower_id=? " +
                    "WHERE id=? AND valid_from>=? AND valid_from<=? AND valid_to>? " +
                        "AND wine_grower_id<>?");
			stmt2.setInt(1, to_year);
			stmt2.setInt(2, wine_grower_id);
			stmt2.setInt(3, so_id);
			stmt2.setInt(4, from_year);
			stmt2.setInt(5, to_year);
			stmt2.setInt(6, to_year);
			stmt2.setInt(7, wine_grower_id);
			stmt2.executeUpdate();
			stmt2.close();
			
			// celý vnutri, jednoduchy update
			PreparedStatement stmt3 = dbconn.getConnection().prepareStatement(
					"UPDATE Spatial_object SET wine_grower_id=? " +
					"WHERE id=? AND valid_from>=? AND valid_to<=? " +
                        "AND wine_grower_id<>?");
			stmt3.setInt(1, wine_grower_id);
			stmt3.setInt(2, so_id);
			stmt3.setInt(3, from_year);
			stmt3.setInt(4, to_year);
			stmt3.setInt(5, wine_grower_id);
			stmt3.executeUpdate();
			stmt3.close();
			
			// prekrýva celý interval, 2x insert a 1 update
			PreparedStatement stmt4 = dbconn.getConnection().prepareStatement(
					"INSERT INTO Spatial_object " +
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, s.valid_from, ?, " +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE s.id=? AND s.valid_from<? AND s.valid_to>? " +
                        "AND s.wine_grower_id<>?");
			stmt4.setInt(1, from_year-1);
			stmt4.setInt(2, so_id);
			stmt4.setInt(3, from_year);
			stmt4.setInt(4, to_year);
			stmt4.setInt(5, wine_grower_id);
			stmt4.executeUpdate();

			PreparedStatement stmt5 = dbconn.getConnection().prepareStatement(
					"INSERT INTO Spatial_object " +
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, ?, s.valid_to, " +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE s.id=? AND s.valid_from<? AND s.valid_to>? " +
                        "AND s.wine_grower_id<>?");
			stmt5.setInt(1, to_year+1);
			stmt5.setInt(2, so_id);
			stmt5.setInt(3, from_year);
			stmt5.setInt(4, to_year);
			stmt5.setInt(5, wine_grower_id);
			stmt5.executeUpdate();

			stmt2 = dbconn.getConnection().prepareStatement(
					"UPDATE Spatial_object SET Valid_from=?, Valid_to=?, wine_grower_id=? " +
                	"WHERE id=? AND valid_from<? AND valid_to>? " + 
                    "AND wine_grower_id<>?");
			stmt2.setInt(1, from_year);
			stmt2.setInt(2, to_year);
			stmt2.setInt(3, wine_grower_id);
			stmt2.setInt(4, so_id);
			stmt2.setInt(5, from_year);
			stmt2.setInt(6, to_year);
			stmt2.setInt(7, wine_grower_id);
			stmt2.executeUpdate();
			stmt2.close();
			// hotovo
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
