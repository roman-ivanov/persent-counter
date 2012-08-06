package ua.pp.bizon.persentcounter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static ua.pp.bizon.persentcounter.Utils.round;

public class Statement {

    private Day persents;

    public void init(String from, String to, double startBalance) throws ParseException {
        init(new SimpleDateFormat("dd-MM-yyyy").parse(from), new SimpleDateFormat("dd-MM-yyyy").parse(to), startBalance);
    }

    protected void init(Date from, Date to, double startBalance) {
        persents = new Day(from, startBalance);
        Date toAdd = (Date) from.clone();
        Day prev = persents;
        do {
            toAdd = new Date(toAdd.getTime() + 86400000);
            prev = new Day(toAdd, prev);
        } while (!toAdd.after(to));
    }

    public void addPayment(Payment p) {
        for (Day i = persents; i != null; i = i.getNext()) {
            if (i.getBillingDate().equals(p.getProcessingData())) {
                i.addPayment(p);
                return;
            }
        }
        LogFactory.getLog(getClass()).info("cant add payment:" + p);
    }

    @Deprecated
    public Double count(double persent, int billingDayOfYearCount) {
        double ret = 0;
        for (Day i = persents; i != null; i = i.getNext()) {
            i.setPersentsCountedForDay(round(i.getBalanceEndDay() * persent / billingDayOfYearCount, 2));
            ret += i.getPersentsCountedForDay();
        }
        return round(ret, 2);
    }

    private Log log = LogFactory.getLog(getClass());

    public void count(double persent, int billingDayOfYearCount, List<Persents> billingDays) throws ParseException {
        for (Persents p: billingDays){
            count(persent, billingDayOfYearCount, p);
        }
    }

    public void count(double persent, int billingDayOfYearCount, Persents billingPeriod) throws ParseException {
        double res = 0;
        for (Day i = persents; i != null; i = i.getNext()) {
            i.setPersentsCountedForDay(round(i.getBalanceEndDay() * persent / billingDayOfYearCount, 2));
            if (billingPeriod.getFrom().after(i.getBillingDate())) {
                log.trace("scipped persents: " + i.toStringNoNext());
                continue;
            } else {
                res += Utils.round(i.getPersentsCountedForDay() > 0 ? i.getPersentsCountedForDay() : 0, 2);
                log.trace("added persents: " + i.toStringNoNext());
                if (!billingPeriod.getTo().after(i.getBillingDate())) {
                    billingPeriod.setValue(res);
                    log.debug("saved persents: " + billingPeriod);
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Persents:\n" + persents;
    }

    public Double endBalanse() {
        for (Day i = persents; i != null; i = i.getNext()) {
            if (i.getNext() == null) {
                return round(i.getBalanceEndDay(), 2);
            }
        }
        ;
        return Double.NaN;
    }

}
