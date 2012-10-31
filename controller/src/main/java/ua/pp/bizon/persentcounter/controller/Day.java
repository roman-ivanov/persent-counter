package ua.pp.bizon.persentcounter.controller;

import static ua.pp.bizon.persentcounter.controller.Utils.dateToString;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;

public strictfp class Day {

    private Date billingDate;
    private Day next;
    private Day prev;
    private double persentsCountedForDay;
    private List<Payment> payments = new LinkedList<Payment>();
    private Double balanceBeforeBilling = null;
    private Double balanceEndDay = null;

    private Double balanceStartDay;

    public Day(Date from, double startBalance) {
        this(from);
        this.balanceStartDay = startBalance;
    }

    private Day(Date date) {
        this.billingDate = date;
    }

    public Day(Day prev) {
        this(new Date(prev.billingDate.getTime() + 86400000));
        this.prev = prev;
        prev.next = this;
        this.balanceStartDay = null;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public double getBalanceBeforeBilling() {
        if (balanceBeforeBilling == null) {
            calculateBalance();
        }
        return balanceBeforeBilling;
    }

    public Double getBalanceEndDay() {
        if (balanceEndDay == null) {
            calculateBalance();
        }
        return balanceEndDay;
    }

    protected synchronized Double calculateBalance() {
        if (balanceStartDay == null) {
            balanceStartDay = prev.getBalanceEndDay();
        }
        Double charges = 0.0;
        for (Payment i : payments) {
            if (i.getBillingAmount() < 0) {
                charges += i.getBillingAmount();
            }
        }
        balanceBeforeBilling = balanceStartDay + charges;
        for (Payment i : payments) {
            if (i.getBillingAmount() > 0) {
                charges += i.getBillingAmount();
            }
        }
        balanceEndDay = Utils.round(balanceStartDay + charges, 2);
        return charges;
    }

    public String toStringNext() {
        return toStringNoNext() + (next == null ? "" : "\n" + next);
    }

    @Override
    public String toString() {
        return toStringNoNext();
    }

    public String toStringNoNext() {
        return "" + dateToString(billingDate) + " " + (prev == null ? "startBalanse=" + balanceStartDay : "counted=" + getBalanceBeforeBilling())
                + ", persentsFarDay=" + getPersentsCountedForDay() + ", motion=" + calculateBalance();
    }

    public String paymentsToString() {
        Iterator<Payment> i = payments.iterator();
        if (!i.hasNext())
            return "\n\tNo Payments";

        StringBuilder sb = new StringBuilder();
        sb.append("\n\t");
        for (;;) {
            Payment e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.toString();
            sb.append("\n\t");
        }
    }

    public void addPayment(Payment p) {
        payments.add(p);
        balanceBeforeBilling = null;
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
        next.balanceBeforeBilling = null;
        next.balanceStartDay = null;
    }

    public void setStartBalance(Double startBalance) {
        if (this.prev == null) {
            this.balanceStartDay = startBalance;
        } else {
            LoggerFactory.getLogger(getClass()).error("cant set start balanse " + startBalance + " to " + this);
        }
    }

}
