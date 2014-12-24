/**
 * 
 */
package pdb03.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import oracle.jdbc.OraclePreparedStatement;

/**
 * Trida zpracovava dotaz na majitele kteri nekdy neco pestovali
 * na urcitem vinohrade.
 * @author Peter Lacko
 *
 */
public class SelectWineGrower extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<WineGrower> growerList;

	private ArrayList<Integer> fromDates;
	private ArrayList<Integer> toDates;

	private String[] columnNames = {
			"ID",
			"From",
			"To",
            "Name",
            "Surname",
            "Address"};  
	
	/**
	 * Vytvoreni objektu a natahnuti dat z databaze podle dotazu.
	 * @param wineyard_id ID pole se kterym pracujeme.
	 * @throws SQLException
	 */
	public SelectWineGrower(int wineyard_id) throws SQLException {
		growerList = new ArrayList<>();
		fromDates = new ArrayList<>();
		toDates = new ArrayList<>();
		DatabaseConnection conn = new DatabaseConnection();
		OraclePreparedStatement pstmt = null;
		try {
            pstmt = (OraclePreparedStatement) conn.getConnection().prepareStatement(
            		"SELECT w.*, s1.valid_from, s1.valid_to FROM Wine_grower w, Spatial_object s1 " +
            		"WHERE w.id=s1.wine_grower_id AND s1.object_type='wine_row' " +
            		"AND EXISTS( SELECT s2.id FROM Spatial_object s2 " +
            		"WHERE s2.id=? AND s2.object_type='wineyard' " +
            		"AND SDO_GEOM.SDO_INTERSECTION(s2.geometry, s1.geometry, 0.005) IS NOT NULL)");
            pstmt.setInt(1, wineyard_id);
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()) {
            	growerList.add(new WineGrower(
            			rset.getInt(1),
            			rset.getString(2),
            			rset.getString(3),
            			rset.getString(4)));
            	fromDates.add(rset.getInt(5));
            	toDates.add(rset.getInt(6));
            }
            rset.close();

		} finally {
			pstmt.close();
			conn.disconnect();
		}
	}
	
	/**
	 * Zretezeny text shrnujici data majitele.
	 * @return Data majitele.
	 */
	public String getGrowerText () {
		String str = "";
		System.out.println("Grower list size: " + growerList.size());
		for(int i = 0; i < growerList.size(); i++) {
			str += fromDates.get(i) + "-" + toDates.get(i) + "\n";
			str += growerList.get(i).getName() + " " + growerList.get(i).getSurname() + ": "
					+ growerList.get(i).getAddress() + "\n";
		}
		return str;
	}

	@Override
	public int getRowCount() {
		return growerList.size();
	}

    public String getColumnName(int col) {
        return columnNames[col];
    }
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
				return growerList.get(rowIndex).getId();
		case 1:
				return fromDates.get(rowIndex);
		case 2:
				return toDates.get(rowIndex);
		case 3:
				return growerList.get(rowIndex).getName();
		case 4:
				return growerList.get(rowIndex).getSurname();
		case 5:
				return growerList.get(rowIndex).getAddress();
		default:
			break;
		}
		return null;
	}

}
