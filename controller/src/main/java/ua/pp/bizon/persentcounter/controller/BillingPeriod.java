package ua.pp.bizon.persentcounter.controller;

import static ua.pp.bizon.persentcounter.controller.Utils.round;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingPeriod {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private LinkedList<Day> days = new LinkedList<Day>();
    
    private int daysCount;
    
    private double persents;

    public BillingPeriod() {
       
    }
    
    public Date getFromDate(){
        if (days.isEmpty()) return null;
        return days.getFirst().getBillingDate();
    }
    
    public Date getToDate(){
        if (days.isEmpty()) return null;
        return days.getLast().getBillingDate();
    }

    public LinkedList<Day> getDays() {
        return days;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public double getPersents() {
        return persents;
    }

    public void setPersents(double persents) {
        this.persents = persents;
        calculate();
    }

    public void addPayments(List<Payment> payments) {
        for (Payment p: payments){
            addPayment(p);
        }
    }
    
    public void addPayment(Payment p) {
        for (Day day: days){
            if (p.getProcessingData().equals(day.getBillingDate())){
                day.addPayment(p);
                return;
            }
        }
        log.warn("payment not added: " + p);
    }

    public int getDayIndex(Date when) {
        for (ListIterator<Day> iterator = days.listIterator(); iterator.hasNext();){
            if (when.equals(iterator.next().getBillingDate())){
                return iterator.nextIndex() - 1; 
            }
        }
        return -1;
    }
    public void calculate() {
        if (daysCount == 0){
            daysCount = 366;
        }
        for (Day i : days) {
            i.setPersentsCountedForDay(round(i.getBalanceEndDay() * persents / daysCount, 2));
        }
    }

    @Override
    public String toString() {
        return "BillingPeriod [days=" + days + ", daysCount=" + daysCount + ", persents=" + persents + "]";
    }
    

    
}
