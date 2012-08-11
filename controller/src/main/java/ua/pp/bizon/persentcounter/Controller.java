package ua.pp.bizon.persentcounter;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Controller {

    public Controller() {
        reinit();
    }

    private LinkedList<BillingPeriod> billingPeriods;

    public List<BillingPeriod> getBillingPeriods() {
        return billingPeriods;
    }

    protected void reinit() {
        billingPeriods = new LinkedList<BillingPeriod>();
    }

    public void addPayments(List<Payment> payments) {
        Payment lastPayment = payments.get(payments.size() - 1);
        Payment firstPayment = payments.get(0);
        if (billingPeriods.isEmpty()) {
            BillingPeriod period = new BillingPeriod();
            configure(period, firstPayment.getProcessingData(), lastPayment.getProcessingData());
            period.addPayments(payments);
            billingPeriods.add(period);
        } else if (billingPeriods.size() >= 1) {
            BillingPeriod period = billingPeriods.getFirst();
            if (period.getFromDate().after(firstPayment.getProcessingData())) {
                // first payment before billing exist
                BillingPeriod before = new BillingPeriod();
                configure(before, firstPayment.getProcessingData(), new Date(period.getFromDate().getTime() - 86400000));
                before.getDays().getLast().setNext(billingPeriods.getFirst().getDays().getFirst());
                billingPeriods.addFirst(before);
            }
            period = billingPeriods.getLast();
            if (period.getToDate().before(lastPayment.getProcessingData())) {
                // last payment after billing exist
                BillingPeriod next = new BillingPeriod();
                configure(next, new Date(period.getToDate().getTime() + 86400000), lastPayment.getProcessingData());
                billingPeriods.getLast().getDays().getLast().setNext(next.getDays().getFirst());
                billingPeriods.addLast(next);
            }
            Iterator<BillingPeriod> iterator = billingPeriods.iterator();
            period = iterator.next();
            for (Payment p : payments) {
                while (p.getProcessingData().after(period.getDays().getLast().getBillingDate()) && iterator.hasNext()) {
                    period = iterator.next();
                }
                period.addPayment(p);
            }
        }
    }

    /**
     * Split 2 periods - first and first + 1
     * 
     * @param first
     */
    public void splitPeriods(int first) {
        billingPeriods.get(first).getDays().addAll(billingPeriods.get(first + 1).getDays());
        billingPeriods.remove(first + 1);
    }

    public void dividePeriods(Date when) {
        BillingPeriod to = new BillingPeriod();
        for (ListIterator<BillingPeriod> iterator = billingPeriods.listIterator(); iterator.hasNext();) {
            BillingPeriod p = iterator.next();
            if ((p.getDays().getFirst().getBillingDate().before(when)) && (p.getDays().getLast().getBillingDate().after(when))) {
                System.out.println("here");
                to.setDaysCount(p.getDaysCount());
                to.setPersents(p.getPersents());
                int toIndex = p.getDayIndex(when);
                to.getDays().addAll(p.getDays().subList(0, toIndex));
                p.getDays().removeAll(to.getDays());
                iterator.previous();
                iterator.add(to);
                System.out.println("here");
                break;
            }
        }
    }

    protected void configure(BillingPeriod period, Date from, Date to) {
        LinkedList<Day> days = period.getDays();
        days.add(new Day(from, 0));
        Date toAdd = (Date) from.clone();
        Day prev = days.getFirst();
        do {
            toAdd = new Date(toAdd.getTime() + 86400000);
            prev = new Day(toAdd, prev);
            days.add(prev);
        } while (toAdd.before(to));
    }

}
