import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Test {
	public static void main(String args[]) throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(0);
		System.out.println(dateFormat.format(date));
		
		DateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
		Calendar cal = new GregorianCalendar();
		TimeZone timeZone = cal.getTimeZone();
		cal = Calendar.getInstance();
		
		String dateT = "2014/05/16 10:00";
		String dateT2 = "2014/05/15 10:00";
		
		System.out.println(dateFormats.parse(dateT));
		System.out.println(cal.getTime());
		System.out.println(dateFormats.format(cal.getTime()));
		
		System.out.println(cal.getTime().after(dateFormats.parse(dateT)));
		
		/*
		
		ArrayList<Integer> a = new ArrayList<Integer>();
		ArrayList<Integer> b = new ArrayList<Integer>();
		
		a.add(1);
		a.add(2);
		
		if( a.size() == 2) {
			b.add(a.get(0));
			a.remove(0);
		}
		
		System.out.println(a.get(0));
		*/
	}
}
