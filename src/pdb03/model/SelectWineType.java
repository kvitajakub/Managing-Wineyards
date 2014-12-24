package pdb03.model;

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

/**
 * Zpracovani dotazu pro select druhu vina, ktere nekdy pestoval majitel.
 * Funguje jako datovy model pro tabulku, ktera bude vypisovat vysledky.
 * @author Peter Lacko
 *
 */
public class SelectWineType extends AbstractTableModel  {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<WineType> selectedWines;
	private ArrayList<Integer> fromDates;
	private ArrayList<Integer> toDates;
	
	private String[] columnNames = {
			"ID",
			"From",
			"To",
			"Name",
            "Description",
            "Colour",
            "Image"};   

	/**
	 * Vytvori objekt a sezene si data.
	 * @param wg_id ID majitele ktereho chceme.
	 * @param from_year Od kdy.
	 * @param to_year Do kdy.
	 * @throws SQLException
	 */
	public SelectWineType (int wg_id, int from_year, int to_year) throws SQLException {
		selectedWines = new ArrayList<>();
		fromDates = new ArrayList<>();
		toDates = new ArrayList<>();
		DatabaseConnection conn = new DatabaseConnection();
		OraclePreparedStatement pstmt = null;
		try {
            pstmt = (OraclePreparedStatement) conn.getConnection().prepareStatement(
                    "SELECT w.*, s.valid_from, s.valid_to " +
                    "FROM Spatial_object s, Wine_type w " +
                    "WHERE s.wine_grower_id=? AND s.wine_type_id=w.id " +
                    "AND ((s.valid_from<? AND s.valid_to>=? AND s.valid_to<=?) OR" +
                    "(s.valid_from>=? AND s.valid_from<=? AND s.valid_to >?) OR " +
                    "(s.valid_from>=? AND s.valid_to<=?) OR " +
                    "(s.valid_from<? AND s.valid_to>?))");
            pstmt.setInt(1, wg_id);
            pstmt.setInt(2, from_year);
            pstmt.setInt(3, from_year);
            pstmt.setInt(4, to_year);
            pstmt.setInt(5, from_year);
            pstmt.setInt(6, to_year);
            pstmt.setInt(7, to_year);
            pstmt.setInt(8, from_year);
            pstmt.setInt(9, to_year);
            pstmt.setInt(10, from_year);
            pstmt.setInt(11, to_year);
			OracleResultSet rset = (OracleResultSet) pstmt.executeQuery();
			while(rset.next()) {
				Icon icon = null;
                OrdImage imgProxy = (OrdImage) rset.getORAData(5, OrdImage.getORADataFactory());
                java.io.InputStream istream = imgProxy.getDataInStream();
                try {
                    Image img = ImageIO.read(istream);
                    if(img != null)
                    	icon = new ImageIcon(img);
                    else
                    	icon = new ImageIcon();

                } catch (IOException e) {
                    e.printStackTrace();
                }
				selectedWines.add(new WineType(
						rset.getInt(1),		// id
						rset.getString(2),	// name
						rset.getString(3),	// description
						rset.getString(4),	// colour
						icon
						));
				fromDates.add(rset.getInt(11));
				toDates.add(rset.getInt(12));
				
			}
		} finally {
			pstmt.close();
			conn.disconnect();
		}
	}
	
	/**
	 * Vrati retezec ktery reprezentuje data z vina.
	 * @return Retezec pro vino.
	 */
	public String getWinesText() {
		String str = "";
		for (int i = 0; i < selectedWines.size(); i++) {
			str += fromDates.get(i) + "-" + toDates.get(i) + "\n";
            str += "Name: " + selectedWines.get(i).getName() + "\n" + "Desc: " + selectedWines.get(i).getDescription() + "\n";
		}
		return str;
	}

	@Override
	public int getRowCount() {
		return selectedWines.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
				return selectedWines.get(rowIndex).getId();
		case 1:
				return fromDates.get(rowIndex);
		case 2:
				return toDates.get(rowIndex);
		case 3:
				return selectedWines.get(rowIndex).getName();
		case 4:
				return selectedWines.get(rowIndex).getDescription();
		case 5:
				return selectedWines.get(rowIndex).getColour();
		case 6:
				return selectedWines.get(rowIndex).getIcon();
		default:
			break;
		}
		return null;
	}
}
