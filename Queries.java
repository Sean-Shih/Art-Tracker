import java.sql.DriverManager;
import java.sql.SQLException;

public class Queries {

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
	
	public String[] selectFromQuery(String calendarDate) {
		String url = "jdbc:sqlite:/Users/seanshih/Projects/ArtTrackerProject/artworks";
		String sql = "SELECT title, caption, filepath, date FROM artworks WHERE date = ?";
//		String calendarDate = "2025-09-03";
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
				
//				return artData;
				
			} catch (SQLException e) {
	            System.err.println(e.getMessage());
			}
		
//		for (int i = 0; i < artData.length; i++) {
//			System.out.println(artData[i]);
//		}
		return artData;
		
	}
	
}
