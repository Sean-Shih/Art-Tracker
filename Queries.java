import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for queries to be executed
 * 
 * @author seanshih
 * 
 */
public class Queries {

	/**
	 * 
	 * Adds an artwork to the database, all relevant metadata are the parameters given
	 * 
	 * @param title The title of the artwork
	 * @param caption The caption of the artwork
	 * @param filepath The filepath of the artwork
	 * @param date The date of the artwork
	 */
	public void addArtQuery(String title, String caption, String filepath, String date) {
		
		String url = "jdbc:sqlite:/Users/seanshih/Projects/ArtTrackerProject/artworks";
		String sql = "INSERT INTO artworks(title,caption,filepath,date) VALUES(?,?,?,?)";
		
		try (var conn = DriverManager.getConnection(url); var pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			pstmt.setString(2, caption);
			pstmt.setString(3, filepath);
			pstmt.setString(4, date);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteArtQuery(String date) {
		String url = "jdbc:sqlite:/Users/seanshih/Projects/ArtTrackerProject/artworks";
		String sql = "DELETE FROM artworks WHERE date = ?";
//		String date = "2025-09-03";
		
		try (var conn = DriverManager.getConnection(url); var pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, date);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * 
	 * Selects an artwork that matches the user's selected date (from JCalendar)
	 * 
	 * @param calendarDate The date that the user selected from the JCalendar
	 * @return A String array that either holds the attributes of the matched 
	 * artwork or null if there is no artwork in the database with the given date
	 */
	public String[] selectFromQuery(String calendarDate) {
		String url = "jdbc:sqlite:/Users/seanshih/Projects/ArtTrackerProject/artworks";
		String sql = "SELECT title, caption, filepath, date FROM artworks WHERE date = ?";
		String[] artData = null;
		

		try (var conn = DriverManager.getConnection(url); 
				var pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setString(1, calendarDate);
				var rs = pstmt.executeQuery();

				if (rs.next()) {
					artData = new String[4];
					artData[0] = rs.getString("title");
		            artData[1] = rs.getString("caption");
		            artData[2] = rs.getString("filepath");
		            artData[3] = rs.getString("date");
				}				
			} catch (SQLException e) {
	            System.err.println(e.getMessage());
			}
		
		return artData;
		
	}
	
}
