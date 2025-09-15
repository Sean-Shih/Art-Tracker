import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Used to get the current date based on the machine and convert it into a readable format
 * 
 * @author seanshih
 */
public class GetLocalDate {
	
	/** The local date based on the local machine */
	LocalDate date;
	
	/** 
	 * This methods gets the local date from the local machine and then calls {@link FormatDate} to convert it into a readable format
	 */
	GetLocalDate() {	
		this.date = LocalDate.now();
		FormatDate();
	}
	
	/**
	 * Converts the LocalDate object into a human readable String
	 * @return The local date converted into a readable String
	 */
	public String FormatDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
	    String formattedDate = this.date.format(formatter);
	    return formattedDate;
	}
	
}