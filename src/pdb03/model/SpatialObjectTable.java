package pdb03.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

/**
 * Trida pro tabulku Spatial_Object.
 * @author Jakub Kvita
 *
 */
@SuppressWarnings("deprecation")
public class SpatialObjectTable {

	private ArrayList<SpatialObject> soTable;
	private DatabaseConnection conn;
	private PreparedStatement pstmt;

	/**
	 * Vytvori a nacte tabulku z databaze.
	 * @param dbconn Pripojeni k databazi.
	 * @throws SQLException Neco spatneho se stalo pri komunikaci.
	 */
	public SpatialObjectTable(DatabaseConnection dbconn) throws SQLException {
		soTable = new ArrayList<>();
		conn = dbconn;
		try {
			//System.out.println("TRYING");
			ResultSet rset = conn.getStatement().executeQuery("SELECT * FROM Spatial_object");
			try {
				while(rset.next()) {
					soTable.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} finally {
			// stmt.close();
		}
	}
	
	/**
	 * Stazeni prostorovych dat z aktualniho roku.
	 * @param year Aktualni rok se kterym pracujeme.
	 * @return Seznam aktualnich prostorovych objektu.
	 */
	public ArrayList<SpatialObject> getDataFrom(int year) {
		ArrayList<SpatialObject> so = new ArrayList<>();
		try {
			System.out.println("TRYING to get active");
			ResultSet rset = conn.getStatement().executeQuery
					("SELECT * FROM Spatial_object WHERE Valid_from<= " + year + " AND Valid_to>= " + year);
			try {
				while(rset.next()) {
					so.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} catch(SQLException e) {
			System.err.println("SQLException: "); 
            e.printStackTrace();
		} finally { 
		}
		return so;
	}
	
	/**
	 * Ulozi novy objekt do databaze.
	 * @param o SpatialObject ktery ukladam insertem do db.
	 * @throws Exception Neco bylo spatne.
	 */
	public void insertItemToDB(SpatialObject o) throws Exception{
				
			//zjistim ID noveho objektu
			ResultSet rs = conn.getStatement().executeQuery("SELECT spatial_object_seq.nextval FROM Spatial_object");  
			if ( rs.next() ) {  
			  o.setID(rs.getInt(1));  
			}
			
			//pripravim statement a vlozim objekt
		    pstmt = conn.getConnection().prepareStatement(
		    		"INSERT INTO Spatial_object VALUES (?, ?, ?, ?, "
		    		+ "?,"
		    		+ "?,?)");
		    
		    pstmt.setInt(1, o.getId());
		    pstmt.setInt(2, o.getValidFromYear());
		    pstmt.setInt(3, o.getValidToYear());
		    pstmt.setString(4, o.getType());
		    
		    STRUCT obj = JGeometry.store(conn.getConnection(), o.getGeometry());
		    pstmt.setObject(5, obj);
		    
		    //NULLy
		    if(o.getWine_grower_id() == 0){
		    	pstmt.setNull(6, Types.INTEGER);
		    }
		    else{
		    	pstmt.setInt(6, o.getWine_grower_id());
		    }
		    if(o.getWine_type_id() == 0){
		    	pstmt.setNull(7, Types.INTEGER);
		    }
		    else{
		    	pstmt.setInt(7, o.getWine_type_id());
		    }
		    System.out.println("Insertin in db");
		    pstmt.executeUpdate();
	}
	
	/**
	 * Updatuje nejaky zaznam v databazi, nemuze menit primarni klic,
	 * coz ve ID, cas od a cas do.
	 * @param o Objekt ktery upravujeme.
	 * @throws Exception 
	 */
	public void updateItemInDB(SpatialObject o) throws Exception{
		
	    pstmt = conn.getConnection().prepareStatement(
	    		"UPDATE Spatial_object "
	    		+ "SET "
	    			+ "object_type=?, "
	    			+ "geometry=?, "
	    			+ "wine_grower_id=?, "
	    			+ "wine_type_id=? "
	    		+ "WHERE valid_from=? AND "
	    			+ "valid_to=? AND "
		    		+ "id=?");
	    
	    pstmt.setString(1, o.getType());
	    
	    STRUCT obj = JGeometry.store(conn.getConnection(), o.getGeometry());
	    pstmt.setObject(2, obj);
	    
	    //NULLy
	    if(o.getWine_grower_id() == 0){
	    	pstmt.setNull(3, Types.INTEGER);
	    }
	    else{
	    	pstmt.setInt(3, o.getWine_grower_id());
	    }
	    if(o.getWine_type_id() == 0){
	    	pstmt.setNull(4, Types.INTEGER);
	    }
	    else{
	    	pstmt.setInt(4, o.getWine_type_id());
	    }
	    
	    pstmt.setInt(5, o.getValidFromYear());
	    pstmt.setInt(6, o.getValidToYear());
	    pstmt.setInt(7, o.getId());

	    System.out.println("Updatin from db");
	    pstmt.executeUpdate();
	}
	
	/**
	 * Odstraneni zaznamu z databaze.
	 * @param o Objekt, ktery chceme odstranit.
	 * @throws SQLException
	 */
	public void removeItemFromDB(SpatialObject o) throws SQLException{
		
	    pstmt = conn.getConnection().prepareStatement("DELETE FROM Spatial_object "
	    		+ "WHERE valid_from=? AND "
    			+ "valid_to=? AND "
	    		+ "id=?");
	    
	    pstmt.setInt(1, o.getValidFromYear());
	    pstmt.setInt(2, o.getValidToYear());
	    pstmt.setInt(3, o.getId());

	    
	    System.out.println("Removin from db");
	    pstmt.executeUpdate();
	}
	
	
	/**
	 * Vytskne data stazena z databaze.
	 */
	public void printData() {
		
		System.out.println("Prostorova data stazena z databaze:");
		for (SpatialObject so:soTable) {
			so.printData();
		}
		System.out.println("=====================================");
	}

	/**
	 * Nastavi novou validitu nejakemu objektu.
	 * @param o Objekt ktery chceme updatovat.
	 * @param validfrom Novy cas od ktereho bude platny.
	 * @param validto Novy cas do kdy bude platny.
	 */
	public void setValidity(SpatialObject o, int validfrom, int validto) {
		PreparedStatement pstmt;
		// nepouzivat pre updatovanie geometry a pod
		if (o.getValidFromYear()!=validfrom || o.getValidToYear()!=validto) {
            try {
                // -----|---DELETE----|------
                pstmt = conn.getConnection().prepareStatement(
                        "DELETE FROM Spatial_object WHERE id = ? " +
                        "AND Valid_from >= ? AND Valid_to <= ? " +
                        "AND Valid_from <> ? AND Valid_to <> ?");
                pstmt.setInt(1, o.getId());
                pstmt.setInt(2, validfrom);
                pstmt.setInt(3, validto);
                pstmt.setInt(4, o.getValidFromYear());
                pstmt.setInt(5, o.getValidToYear());
                pstmt.executeUpdate();
                pstmt.close();
                // -----UPD|ATE----|-----
                pstmt = conn.getConnection().prepareStatement(
                		"UPDATE Spatial_object SET Valid_to = ? " +
                		"WHERE id = ? AND Valid_from < ? AND Valid_to >= ? AND Valid_to <= ? " + 
                		"AND Valid_from <> ? AND Valid_to <> ?");
                pstmt.setInt(1, validfrom-1);
                pstmt.setInt(2, o.getId());
                pstmt.setInt(3, validfrom);
                pstmt.setInt(4, validfrom);
                pstmt.setInt(5, validto);
                pstmt.setInt(6, o.getValidFromYear());
                pstmt.setInt(7, o.getValidToYear());
                pstmt.executeUpdate();
                pstmt.close();
                // ------|-----UPD|ATE-----
                pstmt = conn.getConnection().prepareStatement(
                		"UPDATE Spatial_object SET Valid_from = ? " +
                		"WHERE id = ? AND Valid_from >= ? AND Valid_from <= ? AND Valid_to > ? " +
                		"AND Valid_from <> ? AND Valid_to <> ?");
                pstmt.setInt(1, validto+1);
                pstmt.setInt(2, o.getId());
                pstmt.setInt(3, validfrom);
                pstmt.setInt(4, validto);
                pstmt.setInt(5, validto);
                pstmt.setInt(6, o.getValidFromYear());
                pstmt.setInt(7, o.getValidToYear());
                pstmt.executeUpdate();
                pstmt.close();
                // ------INSERT|--------|UPDATE----
                pstmt = conn.getConnection().prepareStatement(
                		"INSERT INTO Spatial_object " + 
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, s.valid_from, ?," +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE s.Id=? AND s.Valid_from < ? AND s.Valid_to > ? " +
                		"AND Valid_from <> ? AND Valid_to <> ?");
                pstmt.setInt(1, validfrom-1);
                pstmt.setInt(2, o.getId());
                pstmt.setInt(3, validfrom);
                pstmt.setInt(4, validto);
                pstmt.setInt(5, o.getValidFromYear());
                pstmt.setInt(6, o.getValidToYear());
                pstmt.executeUpdate();
                pstmt.close();
                pstmt = conn.getConnection().prepareStatement(
                		"UPDATE Spatial_object SET Valid_from = ? " + 
                		"WHERE Id=? AND Valid_from < ? AND Valid_to > ? " +
                		"AND Valid_from <> ? AND Valid_to <> ?");
                pstmt.setInt(1, validto+1);
                pstmt.setInt(2, o.getId());
                pstmt.setInt(3, validfrom);
                pstmt.setInt(4, validto);
                pstmt.setInt(5, o.getValidFromYear());
                pstmt.setInt(6, o.getValidToYear());
                pstmt.executeUpdate();
                pstmt.close();
                // and finally update record...
                pstmt = conn.getConnection().prepareStatement(
                		"UPDATE Spatial_object SET Valid_from=?, " +
                		"Valid_to=? WHERE Id=? AND Valid_from=? AND Valid_to=?");
                pstmt.setInt(1, validfrom);
                pstmt.setInt(2, validto);
                pstmt.setInt(3, o.getId());
                pstmt.setInt(4, o.getValidFromYear());
                pstmt.setInt(5, o.getValidToYear());
                pstmt.executeUpdate();
                System.out.println("Still ok");
                pstmt.close();
            } catch (SQLException e) {
            	System.err.print(e);
            }
            finally {
            	o.setValid_from_year(validfrom);
            	o.setValid_to_year(validto);
            }
		}
	}

	/**
	 * Nastaveni noveho majitele prostoroveho objektu na celem intervalu platnosti.
	 * @param selected Objekt se kterym pracujeme.
	 * @param new_grower_id Novy majitel.
	 */
	public void setWineGrower(SpatialObject selected, int new_grower_id) {
		//jen pro tento interval
        PreparedStatement pstmt;
		try {
			pstmt = conn.getConnection().prepareStatement(
			        "UPDATE Spatial_object SET wine_grower_id = ? WHERE id = ? " +
			        " AND Valid_from = ? AND Valid_to = ?");
			pstmt.setInt(1, new_grower_id);
			pstmt.setInt(2, selected.getId());
			pstmt.setInt(3, selected.getValidFromYear());
            pstmt.setInt(4, selected.getValidToYear());
            pstmt.execute();
            pstmt.close();
            
            selected.setWine_grower_id(new_grower_id);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Smazani objektu v jednom konkretnim roce, v ostatnich letech zustane.
	 * @param obj Objekt se kterym chceme pracovat.
	 * @param activeYear Rok ve kterem uz nema byt platny.
	 */
	public void deleteInYear(SpatialObject obj, int activeYear) {
		PreparedStatement pstmt;
		try {
			// YEAR|DELETE|YEAR
            pstmt = conn.getConnection().prepareStatement(
            		"DELETE FROM Spatial_object WHERE id = ? AND Valid_from = ? AND Valid_to = ?");
            pstmt.setInt(1, obj.getId());
            pstmt.setInt(2, activeYear);
            pstmt.setInt(3, activeYear);
            pstmt.executeUpdate();
            pstmt.close();
            // YEAR|DELETE------|------
            pstmt = conn.getConnection().prepareStatement(
            		"UPDATE Spatial_object SET Valid_from=? WHERE Id=? AND Valid_from=?");
            pstmt.setInt(1, activeYear+1);
            pstmt.setInt(2, obj.getId());
            pstmt.setInt(3, activeYear);
            pstmt.executeUpdate();
            pstmt.close();
            // -------|------DELETE|------
            pstmt = conn.getConnection().prepareStatement(
            		"UPDATE Spatial_object SET Valid_to=? WHERE Id=? AND Valid_to=?");
            pstmt.setInt(1, activeYear-1);
            pstmt.setInt(2, obj.getId());
            pstmt.setInt(3, activeYear);
            pstmt.executeUpdate();
            pstmt.close();
            // -------|------DELETE------|------
            pstmt = conn.getConnection().prepareStatement(
            		"INSERT INTO Spatial_object" +
                		"(Id, Valid_from, Valid_to, Object_type, Geometry, Wine_grower_id, Wine_type_id) " +
                		"SELECT s.id, s.valid_from, ?," +
                		"s.object_type, s.geometry, " +
                		"s.wine_grower_id, s.wine_type_id " +
                		"FROM Spatial_object s " +
                		"WHERE Id=? AND Valid_from<? AND Valid_to>?");
            pstmt.setInt(1, activeYear-1);
            pstmt.setInt(2, obj.getId());
            pstmt.setInt(3, activeYear);
            pstmt.setInt(4, activeYear);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.getConnection().prepareStatement(
            		"UPDATE Spatial_object SET Valid_from=? " +
                		"WHERE Id=? AND Valid_from<? AND Valid_to>?");
            pstmt.setInt(1, activeYear+1);
            pstmt.setInt(2, obj.getId());
            pstmt.setInt(3, activeYear);
            pstmt.setInt(4, activeYear);
            pstmt.executeUpdate();
            pstmt.close();
            
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * Nastaveni noveho druhu vina pro oznaceny objekt.
	 * @param selected Objekt ktery obsahuje referenci na druh vina.
	 * @param new_wine_type Novy druh vina ktery ma.
	 */
	public void setWineType(SpatialObject selected, int new_wine_type) {
		//jen pro tento interval
        PreparedStatement pstmt;
		try {
			pstmt = conn.getConnection().prepareStatement(
			        "UPDATE Spatial_object SET wine_type_id = ? WHERE id = ? " +
			        " AND Valid_from = ? AND Valid_to = ?");
			pstmt.setInt(1, new_wine_type);
			pstmt.setInt(2, selected.getId());
			pstmt.setInt(3, selected.getValidFromYear());
            pstmt.setInt(4, selected.getValidToYear());
            pstmt.execute();
            pstmt.close();
            
            selected.setWine_type_id(new_wine_type);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
//	public void getValidBetween(int from, int to) {
//		PreparedStatement pstmt1, pstmt2, pstmt3;
//		try {
//			// cele vnutri 
//            pstmt1 = conn.getConnection().prepareStatement(
//            		"SELECT * FROM Spatial_table WHERE " + 
//                        "WHERE s1.valid_to>? AND s1.valid_to<=? AND EXISTS( " +
//                        "SELECT s2.id FROM Spatial_object s2 WHERE " +
//                        "s2.id=s1.id AND s2.valid_to=s1.valid_from-1 " +
//                        "AND SDO_EQUAL(s2.geometry, s1.geometry)='TRUE')");
//            pstmt1.setInt(1, from);
//            pstmt1.setInt(2, to);
//            //pstmt1.setInt(3, from);
//            //pstmt1.setInt(4, to);
//            ResultSet rset1 = pstmt1.executeQuery();
//            while(rset1.next()) {
//            	System.out.println("Dalšia položka 1: " + rset1.getInt(1));
//            }
//
//            pstmt2 = conn.getConnection().prepareStatement(
//                "SELECT s1.id FROM Spatial_object s1 " + 
//                        "WHERE s1.valid_from>? AND s1.valid_from<=? AND EXISTS( " +
//                        "SELECT s2.id FROM Spatial_object s2 WHERE " +
//                        "s2.id=s1.id AND s2.valid_to=s1.valid_from-1 " +
//                        "AND SDO_EQUAL(s2.geometry, s1.geometry)='TRUE')");
//            pstmt2.setInt(1, from);
//            pstmt2.setInt(2, to);
//            ResultSet rset2 = pstmt2.executeQuery();
//            while(rset2.next()) {
//            	System.out.println("Dalšia položka 2: " + rset2.getInt(1));
//            }
//
//            pstmt3 = conn.getConnection().prepareStatement(
//                "SELECT s1.id FROM Spatial_object s1 " + 
//                        "WHERE (s1.valid_from<=? AND s1.valid_to=?) OR " +
//                        "(s1.valid_from=? AND s1.valid_to>=?) OR " +
//                        "(s1.valid_from<? AND s1.valid_to>?)");
//            pstmt3.setInt(1, from);
//            pstmt3.setInt(2, to);
//            pstmt3.setInt(3, from);
//            pstmt3.setInt(4, to);
//            pstmt3.setInt(5, from);
//            pstmt3.setInt(6, to);
//            ResultSet rset3 = pstmt3.executeQuery();
//            while(rset3.next()) {
//            	System.out.println("Dalšia položka 3: " + rset3.getInt(1));
//            }
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Vrati jeden druh objektu v nejakem roce.
	 * @param obj_type Druh objektu ktery chceme.
	 * @param year Aktivni rok ve kterem objekty chceme.
	 * @return Seznam objektu.
	 */
	public ArrayList<SpatialObject> getDataTypeFrom(String obj_type, int year) {
		ArrayList<SpatialObject> so = new ArrayList<>();
		try {
			System.out.println("TRYING to get active");
			ResultSet rset = conn.getStatement().executeQuery
					("SELECT * FROM Spatial_object WHERE Valid_from<= " + year + " AND Valid_to>= " + year
							+ " AND object_type = '"  + obj_type + "'");
			try {
				while(rset.next()) {
					so.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} catch(SQLException e) {
			System.err.println("SQLException: "); 
            e.printStackTrace();
		} finally { 
		}
		return so;
	}
	
	/**
	 * Vrati rok kdy nejaky objekt zacina svou validitu.
	 * @param id ID objektu ktery nas zajima.
	 * @return Rok ve kterem zacne byt platny.
	 */
	int getMinValidity(int id) {
		int ret = -1;
		try {
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT valid_from FROM (SELECT valid_from FROM Spatial_object " +
					"WHERE id=? ORDER BY valid_from ASC) WHERE ROWNUM=1");
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();
			rset.next();
			ret = rset.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Vrati rok kdy nejaky objekt konci svou validitu.
	 * @param id ID objektu ktery nas zajima.
	 * @return Rok ve kterem skonci byt platny.
	 */
	int getMaxValidity(int id) {
		int ret = -1;
		try {
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT valid_to FROM (SELECT valid_to FROM Spatial_object " +
					"WHERE id=? ORDER BY valid_from DESC) WHERE ROWNUM=1");
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();
			rset.next();
			ret = rset.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Vrati seznam vinic, ktere jsou v blizkosti vody.
	 * @param n Pocet vinic nejblizsich k nejake vode.
	 * @param year Rok ktery nas zajima.
	 * @return Seznam objektu.
	 */
	public ArrayList<SpatialObject> getWineyardsNearWater(int n, int year) {
		soTable = new ArrayList<>();
		try {
			//System.out.println("TRYING");
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT /*+ INDEX(s1 spatial_object_idx) */ s1.* FROM Spatial_object s2, Spatial_object s1 WHERE " +
					"s1.Valid_from<=? AND s1.Valid_to>=? " +
					"AND s2.Valid_from<=? AND s2.Valid_to>=? " +
					"AND s2.object_type='pond' AND s1.object_type='wineyard' " +
					"AND SDO_NN(s1.geometry, s2.geometry)='TRUE' AND ROWNUM<=?");
			ps.setInt(1, year);
			ps.setInt(2, year);
			ps.setInt(3, year);
			ps.setInt(4, year);
			ps.setInt(5, n);
			ResultSet rset = ps.executeQuery();
			try {
				while(rset.next()) {
					soTable.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// stmt.close();
		}
		return soTable;
	}
	
	/**
	 * Vraci vsechny strasaky ktere jsou na dane vinici v nejakem roce.
	 * @param wineyard_id ID vinice ktera nas zajima.
	 * @param year Rok ktery nas zajima.
	 * @return Seznam objektu.
	 */
	public ArrayList<SpatialObject> getScarecrowsOnWineyard(int wineyard_id, int year) {
		soTable = new ArrayList<>();
		try {
			//System.out.println("TRYING");
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT s2.* FROM Spatial_object s1, Spatial_object s2 WHERE " +
					"s1.id=? AND s1.Valid_from<=? AND s1.Valid_to>=? " +
					"AND s2.Valid_from<=? AND s2.Valid_to>=? " +
					"AND s2.object_type='scarecrow' " +
					"AND SDO_FILTER(s2.geometry, s1.geometry,'querytype=WINDOW')='TRUE'");
					//"AND SDO_GEOM.SDO_INTERSECTION(s2.geometry, s1.geometry, 0.005) IS NOT NULL");
			ps.setInt(1, wineyard_id);
			ps.setInt(2, year);
			ps.setInt(3, year);
			ps.setInt(4, year);
			ps.setInt(5, year);
			ResultSet rset = ps.executeQuery();
			try {
				while(rset.next()) {
					soTable.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// stmt.close();
		}
		return soTable;
	}

	/**
	 * Vrati vsechny vinice v blizkosti nejake cesty.
	 * @param road_id ID cesty ktera nas zajima.
	 * @param distance Maximalni vzdalenost mezi cestou a vinici.
	 * @param year Rok ktery nas zajima.
	 * @return Seznam objektu.
	 */
	public ArrayList<SpatialObject> getWineyardsNearRoad(int road_id, int distance, int year) {
		soTable = new ArrayList<>();
		try {
			//System.out.println("TRYING");
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT s2.* FROM Spatial_object s1, Spatial_object s2 WHERE " +
					"s1.id=? AND s1.Valid_from<=? AND s1.Valid_to>=? " +
					"AND s2.Valid_from<=? AND s2.Valid_to>=? " +
					"AND s2.object_type='wineyard' " +
					"AND SDO_WITHIN_DISTANCE(s1.geometry, s2.geometry, 'distance="+distance+"')='TRUE'");
			ps.setInt(1, road_id);
			ps.setInt(2, year);
			ps.setInt(3, year);
			ps.setInt(4, year);
			ps.setInt(5, year);
			ResultSet rset = ps.executeQuery();
			try {
				while(rset.next()) {
					soTable.add(new SpatialObject(
							rset.getInt(1),							//id
							rset.getInt(2),							//valid_from
							rset.getInt(3),							//valid_to
							rset.getString(4),						//type
							JGeometry.load((STRUCT) rset.getObject(5)),		//geometry
							rset.getInt(6),							//winegrower
							rset.getInt(7)							//winetype
							));
				}
			} finally {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// stmt.close();
		}
		return soTable;
	}

	
	//------------------------------------------------------------------------
	/**
	 * Vypocita plochu daneho typu vina v danom roku
	 * @param wine_type_id id pozadovaneho typu vina
	 * @param year Z pozadovaneho roku.
	 * @return Obsah plochy na ktorom rastie pozadovane vino.
	 */
	public double wineTypeAreaAtYear(int wine_type_id, int year) {
		System.out.println("Wine type id: " + wine_type_id);
		double wineArea = 0;
		boolean oneRow = false;
		try {
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT SDO_GEOM.SDO_AREA(SDO_GEOM.SDO_CONVEXHULL(s1.geometry," +
					"m.diminfo), m.diminfo) " +
					"FROM Spatial_object s1, user_sdo_geom_metadata m " +
					"WHERE s1.object_type='wine_row' " +
					"AND s1.wine_type_id=? " +
					"AND s1.Valid_from<=? AND s1.Valid_to>=? ");
			ps.setInt(1, wine_type_id);
			ps.setInt(2, year);
			ps.setInt(3, year);
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				if (rset.getDouble(1)==0) {
					oneRow = true;
				}
				wineArea += rset.getDouble(1);
			}
			if (wineArea==0 && oneRow==true) {
                PreparedStatement ps1 = conn.getConnection().prepareStatement(
                		"SELECT SDO_GEOM.SDO_LENGTH(s1.geometry, m.diminfo) " +
                		"FROM Spatial_object s1, user_sdo_geom_metadata m " +
                		"WHERE s1.object_type='wine_row' " +
                        "AND s1.wine_type_id=? " +
                        "AND s1.Valid_from<=? AND s1.Valid_to>=? ");
                ps1.setInt(1, wine_type_id);
                ps1.setInt(2, year);
                ps1.setInt(3, year);
                ResultSet rset1 = ps1.executeQuery();
                if (rset1.next()) {
                	return rset1.getDouble(1) * 20;
                } else {
                	rset1.close();
                	ps1.close();
                	return 0;
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wineArea;
	}
	
	/**
	 * Vypocita percentualny podiel daneho typu vina v danom roku.
	 * @param wine_type_id id pozadovaneho typu vina
	 * @param year pozadovany rok
	 * @return percentualne zastupenie vina v danom roku
	 */
	public double wineTypePercentageAtYear(int wine_type_id, int year) {
		Map<Integer, WineyardAreaLength> wineyards = new HashMap<>();
		System.out.println("Wine type id: " + wine_type_id);
		double wineArea = 0;
		double totalLength = 0;
		try {
			// vrati id wineyardu a celkovu dlzku sadu
			PreparedStatement ps2 = conn.getConnection().prepareStatement(
					"SELECT DISTINCT s1.id, SUM(SDO_GEOM.SDO_LENGTH(s2.geometry, m.diminfo)) " +
					"FROM Spatial_object s1, Spatial_object s2, user_sdo_geom_metadata m " +
					"WHERE s1.object_type='wineyard' AND s2.object_type='wine_row' " +
					"AND s1.Valid_from<=? AND s1.Valid_to>=? " +
					"AND s2.Valid_from<=? AND s2.Valid_to>=? " +
					"AND SDO_GEOM.SDO_INTERSECTION(s1.geometry, s2.geometry, 0.005) IS NOT NULL " +
					"GROUP BY s1.id");
            ps2.setInt(1, year);
            ps2.setInt(2, year);
            ps2.setInt(3, year);
            ps2.setInt(4, year);
            ResultSet rset2 = ps2.executeQuery();
            while (rset2.next()) {
            	//System.out.println("1: " + rset2.getInt(1) + " " +rset2.getDouble(2) +" "+ rset2.getDouble(3) );
            	wineyards.put(rset2.getInt(1), new WineyardAreaLength(0, rset2.getDouble(2)));
            	totalLength += rset2.getDouble(2);
            }
            rset2.close();
            ps2.close();

			// vrati id wineyardu a dlzku daneho typu vina v nom
			PreparedStatement ps = conn.getConnection().prepareStatement(
					"SELECT DISTINCT s1.id, SDO_GEOM.SDO_LENGTH(s2.geometry, m.diminfo) " +
					"FROM Spatial_object s1, Spatial_object s2, user_sdo_geom_metadata m " +
					"WHERE s1.object_type='wineyard' AND s2.object_type='wine_row' " +
					"AND s1.Valid_from<=? AND s1.Valid_to>=? " +
					"AND s2.Valid_from<=? AND s2.Valid_to>=? " +
					"AND s2.wine_type_id=? " +
					"AND SDO_GEOM.SDO_INTERSECTION(s2.geometry, s1.geometry, 0.005) IS NOT NULL");
			ps.setInt(1, year);
			ps.setInt(2, year);
			ps.setInt(3, year);
			ps.setInt(4, year);
			ps.setInt(5, wine_type_id);
			ResultSet rset = ps.executeQuery();
			while (rset.next()) {
				wineArea += rset.getDouble(2)/totalLength;
			}
			rset.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wineArea;
	}
}
