package pdb03.model;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

/**
 * Trida pro tabulku Wine_Type.
 * @author Jakub Kvita
 *
 */
public class WineTypeTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	ArrayList<WineType> wineTable;
	DatabaseConnection dbconn;
	
	private String[] columnNames = {
			"ID",
			"Name",
            "Description",
            "Colour",
            "Image"};   
	
	@SuppressWarnings("rawtypes")
	Class[] columnClasses = new Class[]{Integer.class,
						String.class,
						String.class,
						String.class,
						Icon.class};

	
	/**
	 * Vytvori a naplni tabulku.
	 * @param conn Spojeni s databazi.
	 * @throws SQLException Nejaky problem pri zpracovani databaze.
	 */
	public WineTypeTable(DatabaseConnection conn) throws SQLException {
        dbconn = conn;
		reload();
	}
	
	/**
	 * Znovu stahne data z databaze a zacne je vracet.
	 * @throws SQLException
	 */
	public void reload() throws SQLException {
		wineTable = new ArrayList<>();
		try {
			OracleResultSet rset = (OracleResultSet) dbconn.getStatement().executeQuery("SELECT * FROM Wine_type");
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
				wineTable.add(new WineType(
						rset.getInt(1),		// id
						rset.getString(2),	// name
						rset.getString(3),	// description
						rset.getString(4),	// colour
						icon
						));
			}
		} finally {
		}
		
	}
	
	/**
	 * Vrati prvni ulozenou polozku.
	 * @return Objekt jednoho druhu vina.
	 */
	public WineType getFirst() {
		return wineTable.get(0);
	}
	
	/**
	 * Vrati vino s konkretnim ID.
	 * @param id ID vina, ktere chceme.
	 * @return Vino.
	 */
	public WineType getWineWithId(int id) {
		for(WineType w:wineTable) {
			if (w.getId() == id)
				return w;
		}
		return null;
	}

	/**
	 * Misto objektu vina vrati string, ktery reprezentuje data tohoto vina.
	 * Hodi se pri vypisovani do okna pro uzivatele.
	 * @param wineId ID vina, ktere chceme.
	 * @return Retezec jmena vina a jeho popisu.
	 */
	public String getWineText(int wineId) {
		for (WineType w:wineTable) {
			if (w.getId() == wineId) {
                return  w.getName() + "\n" + w.getDescription();
			}
		}
		return "";
	}

	/**
	 * Vrati obrazek u urciteho vina.
	 * @param wine_type_id ID vina, se kterym chceme pracovat.
	 * @return Obrazek ktery mame ulozeny u vina.
	 */
	public Icon getWineIcon(int wine_type_id) {
		for (WineType w:wineTable) {
			if (w.getId() == wine_type_id) {
				return w.getIcon();
			}
		}
		return null;
	}
	
	/**
	 * Vrati vino nejvic podobne vinu, ktere je na obrazku.
	 * @param sample Vzorek se kterym pracujeme.
	 * @return Nejpodobnejsi vino v databazi.
	 */
	public WineType getMostSimilar(WineType sample){
		
		double weightAC = 0.3;
		double weightCH = 0.3;
		double weightPC = 0.3;
		double weightTX = 0.1;
		
		int simId = -1;
		try {
			PreparedStatement pstmt = dbconn.getConnection().prepareStatement(
					"SELECT dst.id, SI_ScoreByFtrList(" +
					"new SI_FeatureList(src.photo_ac,?,src.photo_ch,?,src.photo_pc,?,src.photo_tx,?),dst.photo_si) " +
					"AS similarity FROM Wine_type src, Wine_type dst " +
					"WHERE (src.id <> dst.id) " +
					"AND src.id=? ORDER BY similarity ASC");
			try {
				pstmt.setDouble(1, weightAC);
	            pstmt.setDouble(2, weightCH);
	            pstmt.setDouble(3, weightPC);
	            pstmt.setDouble(4, weightTX);
	            pstmt.setInt(5, sample.getId());
	            ResultSet rset = pstmt.executeQuery();
	            try {
	            	if (rset.next()) {
	            		simId = rset.getInt(1);
	            	}
	            } finally {
	            	rset.close();
	            }
			} finally {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (WineType w:wineTable) {
			if(w.getId() == simId) {
				return w;
			}
		}
		return null;
	}
	
	/**
	 * Provede rotaci obrazku druhu vina ktere je na vstupu.
	 * @param wt Druh vina s jehoz obrazkem chceme pracovat.
	 * @param angle O kolik stupnu chceme rotovat.
	 */
	public void rotateImage(WineType wt, float angle) {
        if (wt.getIcon().getIconHeight() != -1 && wt.getIcon() != null) { 
			try {
	            OraclePreparedStatement stmt = null;
	            try {
	                stmt = (OraclePreparedStatement) dbconn.getConnection().prepareStatement(
	                        "SELECT photo FROM Wine_type WHERE id=? FOR UPDATE");
	                stmt.setInt(1, wt.getId());
	                OrdImage imgProxy = null;
	                OracleResultSet rset = (OracleResultSet) stmt.executeQuery();
	                if (rset.next()) {
	                    imgProxy = (OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory());
	                }
	                rset.close();
	                stmt.close();
	                imgProxy.process("rotate="+angle);
	                stmt = (OraclePreparedStatement) dbconn.getConnection().prepareStatement(
	                		"UPDATE Wine_type SET photo=? WHERE id=?");
	                stmt.setORAData(1, imgProxy);
	                stmt.setInt(2, wt.getId());
	                stmt.executeUpdate();
	                stmt.close();
	                PreparedStatement stmt3 = dbconn.getConnection().prepareStatement(
	                        "UPDATE Wine_type w set w.photo_si=SI_StillImage(w.photo.getContent()) WHERE Id=?");
	                stmt3.setInt(1, wt.getId());
	                stmt3.executeUpdate();
	                stmt3.close();
	                PreparedStatement stmt4 = dbconn.getConnection().prepareStatement(
	                        "UPDATE Wine_type w set " +
	                        "w.photo_ac=SI_AverageColor(w.photo_si), " +
	                        "w.photo_ch=SI_ColorHistogram(w.photo_si), " +
	                        "w.photo_pc=SI_PositionalColor(w.photo_si), " + 
	                        "w.photo_tx=SI_Texture(w.photo_si) WHERE Id = ?");
	                stmt4.setInt(1, wt.getId());
	                stmt4.executeUpdate();
	                stmt4.close();
	                reload();
	            } finally {
	                stmt.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
        }
	}

	@Override
	public int getRowCount() {
		return wineTable.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) {
			case 0:
			case 1:
			case 2:
			case 3:
				return wineTable.get(rowIndex).getColumn(columnIndex);
			case 4:
				return wineTable.get(rowIndex).getColumn(columnIndex);
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {		
	}
	
	/**
	 * Zrusi zaznam o druhu vina.
	 * @param ID ID vina ktere chceme zrusit.
	 */
	public void deleteWineType(int ID){
        PreparedStatement pstmt;
		try {
			pstmt = dbconn.getConnection().prepareStatement(
			        "DELETE FROM Wine_type WHERE id = ?");
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
	 * Vlozi novy zaznam vina do databaze.
	 * @param name Jmeno noveho zaznamu.
	 * @param description Popis noveho druhu vina.
	 * @param colour Barva noveho vina.
	 * @param icon Obrazek noveho druhu vina.
	 */
	public void insertNewWineType(String name, String description, String colour, Icon icon){
        PreparedStatement pstmt;
        try {
            boolean autoCommit = dbconn.getConnection().getAutoCommit();
            dbconn.getConnection().setAutoCommit(false);
            try {
                // insert values
                String insertQuery =  "INSERT INTO Wine_type VALUES(Wine_type_seq.nextval, '" +
                name + "', '" + description + "', '" + colour + "', ORDSYS.ORDIMAGE.INIT(), null, null, null, null, null)";
                pstmt = dbconn.getConnection().prepareStatement(insertQuery, new String[] {"id"});
                int myId = -1;
                if (pstmt.executeUpdate() > 0) {
                    System.out.println("Viac ako nula");
                    ResultSet keyset = pstmt.getGeneratedKeys();
                    if (keyset != null && keyset.next()) {
                        myId = (int) keyset.getLong(1);
                    }
                }
                System.out.println("ID noveho vina:" + myId);
                pstmt.close();

                // get proxy for image
                OrdImage imgProxy = null;
                OraclePreparedStatement stmt1 =(OraclePreparedStatement)
                        dbconn.getConnection().prepareStatement("SELECT * FROM Wine_type WHERE Id = ? FOR UPDATE");
                stmt1.setInt(1, myId);
                OracleResultSet rset = (OracleResultSet) stmt1.executeQuery();
                try {
                    if (rset.next()) {
                        imgProxy = (OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory());
                    }
                } finally {
                    rset.close();
                }
                stmt1.close();
                // put icon into db
                if(icon != null){
	                Image im = ((ImageIcon)icon).getImage();
	                ByteArrayOutputStream os = new ByteArrayOutputStream();
	                ImageIO.write((RenderedImage) im,"png", os);
	                InputStream is = new ByteArrayInputStream(os.toByteArray());
	                os.close();
	                imgProxy.loadDataFromInputStream(is);
	                is.close();
	                imgProxy.setProperties();
	                String updateSQL1 = "UPDATE Wine_type SET photo=? WHERE Id = ?";
	                OraclePreparedStatement stmt2 = (OraclePreparedStatement) dbconn.getConnection().prepareStatement(updateSQL1);
	                stmt2.setORAData(1, imgProxy);
	                stmt2.setInt(2, myId);
	                stmt2.executeUpdate();
	                stmt2.close();
	                
	                // set stillimage
	                PreparedStatement stmt3 = dbconn.getConnection().prepareStatement(
	                        "UPDATE Wine_type w set w.photo_si=SI_StillImage(w.photo.getContent()) WHERE Id=?");
	                stmt3.setInt(1, myId);
	                stmt3.executeUpdate();
	                stmt3.close();
	                
	                // and set the rest
	                PreparedStatement stmt4 = dbconn.getConnection().prepareStatement(
	                        "UPDATE Wine_type w set " +
	                        "w.photo_ac=SI_AverageColor(w.photo_si), " +
	                        "w.photo_ch=SI_ColorHistogram(w.photo_si), " +
	                        "w.photo_pc=SI_PositionalColor(w.photo_si), " + 
	                        "w.photo_tx=SI_Texture(w.photo_si) WHERE Id = ?");
	                stmt4.setInt(1, myId);
	                stmt4.executeUpdate();
	                stmt4.close();
                }
                reload();
            } finally {
	        	dbconn.getConnection().setAutoCommit(autoCommit);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
	}
	/**
	 * Updatuje nejaky druh vina v databazi o nove informace. Nemeni se jeho ID!
	 * @param ID ID vina ktere chceme updatovat. Nemeni se!
	 * @param name Nove jmeno.
	 * @param description Novy popis.
	 * @param colour Nova barva.
	 * @param icon Novy obrazek.
	 */
	public void updateWineType(int ID, String name, String description, String colour, Icon icon){
        try {
            boolean autoCommit = dbconn.getConnection().getAutoCommit();
            dbconn.getConnection().setAutoCommit(false);
            try {
                // get proxy for image
                OrdImage imgProxy = null;
                OraclePreparedStatement stmt1 =(OraclePreparedStatement)
                        dbconn.getConnection().prepareStatement("SELECT photo FROM Wine_type WHERE Id = ? FOR UPDATE");
                stmt1.setInt(1, ID);
                OracleResultSet rset = (OracleResultSet) stmt1.executeQuery();
                if (rset.next()) {
                	imgProxy = (OrdImage) rset.getORAData(1, OrdImage.getORADataFactory());
                }
                rset.close();
                stmt1.close();
                if (icon == null || ((ImageIcon)icon).getImage() == null) {
                	imgProxy.deleteContent();
                    PreparedStatement stmt = dbconn.getConnection().prepareStatement(
                            "UPDATE Wine_type SET name=?, description=?, colour=?,  " +
                            "photo=ORDSYS.ORDIMAGE.INIT(), photo_si=null, " +
                            "photo_ac=null, " +
                            "photo_ch=null, " +
                            "photo_pc=null, " + 
                            "photo_tx=null WHERE Id = ?");
                    stmt.setString(1, name);
                    stmt.setString(2, description);
                    stmt.setString(3,  colour);
                    stmt.setInt(4, ID);
                    stmt.executeUpdate();
                    stmt.close();
                } else {
                    Image im = ((ImageIcon)icon).getImage();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write((RenderedImage) im,"png", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    os.close();
                    imgProxy.loadDataFromInputStream(is);
                    is.close();
                    imgProxy.setProperties();
                    String updateQuery = "UPDATE Wine_type SET name=?, description=?, colour=?, photo=? " +
            							"WHERE id=?";
                    OraclePreparedStatement ps = (OraclePreparedStatement) dbconn.getConnection().prepareStatement(updateQuery);
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setString(3,  colour);
                    ps.setORAData(4, imgProxy);
                    ps.setInt(5,  ID);
                    ps.executeUpdate();
                    ps.close();

                    // set stillimage
                    PreparedStatement stmt3 = dbconn.getConnection().prepareStatement(
                            "UPDATE Wine_type w set w.photo_si=SI_StillImage(w.photo.getContent()) WHERE Id=?");
                    stmt3.setInt(1, ID);
                    stmt3.executeUpdate();
                    stmt3.close();
                    
                    // and set the rest
                    PreparedStatement stmt4 = dbconn.getConnection().prepareStatement(
                            "UPDATE Wine_type w set " +
                            "w.photo_ac=SI_AverageColor(w.photo_si), " +
                            "w.photo_ch=SI_ColorHistogram(w.photo_si), " +
                            "w.photo_pc=SI_PositionalColor(w.photo_si), " + 
                            "w.photo_tx=SI_Texture(w.photo_si) WHERE Id = ?");
                    stmt4.setInt(1, ID);
                    stmt4.executeUpdate();
                    stmt4.close();
                }
                reload();
            } finally {
	        	dbconn.getConnection().setAutoCommit(autoCommit);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
	}
	

}