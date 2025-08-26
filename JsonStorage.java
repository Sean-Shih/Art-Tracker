import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {
	private static final String FILENAME = "artworks.json";
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();
	
	public static void saveArtworks(List<Artwork> artworks) {
		try (Writer writer = new FileWriter(FILENAME)) { 
			gson.toJson(artworks, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Artwork> loadArtworks() {
		
		try (Reader reader = new FileReader(FILENAME)) {
			
			Type listType = new TypeToken<ArrayList<Artwork>>() {}.getType();
			return gson.fromJson(reader, listType);
			
		} catch (FileNotFoundException e) {
			return new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
}
