package ua.pp.bizon.persentcounter.controller;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;


public class Utils {
    
    public static String dateToString(Date date){
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
    
    public static Double round(Double from, int accuracy) {
        double deg = Math.pow(10 , accuracy);
        return Math.round( from  * deg ) / deg; 
    }

    public static LinkedList<Payment> sort(LinkedList<Payment> list) {
        Collections.sort(list, new Comparator<Payment>() {
            public int compare(Payment o1, Payment o2) {
                return o1.getProcessingData().compareTo(o2.getProcessingData());
            }
        });
        return list;
    }

}
