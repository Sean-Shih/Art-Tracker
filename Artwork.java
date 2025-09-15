import java.io.File;
import java.time.LocalDate;

/**
 * 
 * Class to represent each individual artwork
 * 
 * @author seanshih
 * 
 */
public class Artwork {

	/** artTitle represents the artwork's title */
	private String artTitle;
	/** filename represents the artwork's filepath/name */
	private String filename;
	/** caption represents the artwork's caption */
	private String caption;
	/** date stores the date when the artwork was saved */
//	private LocalDate date;
	private String date;
	
	/** 
	 * Constructor
	 * 
	 * */
	Artwork() {
		
	}
	
	/**
	 * returns the title of the artwork
	 * 
	 * @return artTitle
	 */
	public String getArtTitle() {
		return this.artTitle;
	}
	
	/**
	 * returns the file path of the artwork
	 * 
	 * @return
	 */
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * returns the artwork's caption
	 * 
	 * @return
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * returns the date when the artwork was stored
	 * 
	 * @return
	 */
	public String getDate() {
//	public LocalDate getDate() {
		return this.date;
	}
	
	/**
	 * sets the title of the artwork
	 * 
	 * @param artTitle
	 */
	public void setArtTitle(String artTitle) {
		this.artTitle = artTitle;
	}
	
	/**
	 * sets the file path of the artwork image file
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * sets the caption of the artwork
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * sets the date of the artwork
	 * 
	 */
	public void setDate(String date) {
//		this.date = LocalDate.now();
		this.date = date;
	}
}