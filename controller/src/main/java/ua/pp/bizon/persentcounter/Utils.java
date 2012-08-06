package ua.pp.bizon.persentcounter;

import java.text.DateFormat;
import java.util.Date;

public class Utils {
    
    public static String dateToString(Date date){
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
    
    public static Double round(Double from, int accuracy) {
        double deg = Math.pow(10 , accuracy);
        return Math.round( from  * deg ) / deg; 
    }

}
