import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GetLocalDate {
	
	LocalDate date;
	
	GetLocalDate() {	
		this.date = LocalDate.now();
		FormatDate();
	}
	
	public String FormatDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
	    String formattedDate = this.date.format(formatter);
	    return formattedDate;
	}
	
}
