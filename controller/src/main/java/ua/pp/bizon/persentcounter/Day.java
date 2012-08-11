package ua.pp.bizon.persentcounter;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static ua.pp.bizon.persentcounter.Utils.dateToString;

public class Day {

    private Date billingDate;
    private Day next;
    private Day prev;
    private double persentsCountedForDay;
    private List<Payment> payments = new LinkedList<Payment>();
    private Double balanse = null;

    private Double startBalance;

    public Day(Date from, double startBalance) {
        this(from);
        this.startBalance = startBalance;
    }

    private Day(Date date) {
        this.billingDate = date;
    }

    public Day(Date toAdd, Day prev) {
        this(toAdd);
        this.prev = prev;
        prev.next = this;
        this.startBalance = null;
    }

    public Date getBillingDate() {
        return billingDate;
    }
    
    public List<Payment> getPayments() {
        return payments;
    }

    public double getBalanceEndDay() {
        if (balanse == null) {
            balanse = (prev == null ? startBalance : prev.getBalanceEndDay()) + calculateFlowFounds();
        }
        return Utils.round(balanse, 2);
    }

    protected double calculateFlowFounds() {
        double flowFounds = 0.0;
        for (Payment p : payments) {
            if (p.getBillingAmount() < 0 && !p.getDescription().equals("service")){
               flowFounds += p.getBillingAmount();
            }
        }
        if (prev != null){
            for (Payment p : prev.payments) {
                if (p.getBillingAmount() > 0 || p.getDescription().equals("service")){
                   flowFounds += p.getBillingAmount();
                }
            }   
        }
        return  Utils.round(flowFounds, 2);
    }

    public String toStringNext() {
        return toStringNoNext() +  (next == null ? "" : "\n" + next);
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return toStringNoNext();
    }
    
    
    public String toStringNoNext() {
        return "" + dateToString(billingDate) + " " + (prev == null ? "startBalanse=" + startBalance : "counted=" + getBalanceEndDay()) + ", persentsFarDay="
                + getPersentsCountedForDay() + ", motion=" + calculateFlowFounds();
    }
    
    public String paymentsToString(){
        Iterator<Payment> i =payments.iterator();
        if (! i.hasNext())
            return "\n\tNo Payments";

        StringBuilder sb = new StringBuilder();
        sb.append("\n\t");
        for (;;) {
            Payment e = i.next();
            sb.append(e);
            if (! i.hasNext())
            return sb.toString();
            sb.append("\n\t");
        }
    }

    public void addPayment(Payment p) {
        payments.add(p);
        balanse = null;
    }

    public double getPersentsCountedForDay() {
        return persentsCountedForDay;
    }

    public void setPersentsCountedForDay(double persentsCountedForDay) {
        this.persentsCountedForDay = persentsCountedForDay;
    }

    protected double getPersentsCountedForPeriod() {
        return getPersentsCountedForDay() + (prev == null ? 0 : prev.getPersentsCountedForPeriod());
    }

    public Day getNext() {
        return next;
    }

    public void setNext(Day next) {
        this.next = next;
        next.prev = this;
        next.balanse = null;
        next.startBalance = null;
    }

}
