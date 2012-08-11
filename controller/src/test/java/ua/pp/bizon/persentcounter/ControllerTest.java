package ua.pp.bizon.persentcounter;

import static junit.framework.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.BeforeClass;
import org.junit.Test;

public class ControllerTest {

    private final Log log = LogFactory.getLog(getClass());

    @BeforeClass
    public static void before() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ALL);
        BasicConfigurator.configure(new AppenderSkeleton() {
            
            @Override
            public boolean requiresLayout() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public void close() {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            protected void append(LoggingEvent event) {
                if (event.getLevel().isGreaterOrEqual(Level.WARN)){
                    fail();
                }
            }
        });
    }

    Controller controller = null;

    @Test
    public void testAddPayments() {
        List<Payment> payments = new LinkedList<Payment>();
        payments.add(PaymentFactory.split("51\t2012-07-21\tSome summary UKR\tUAN.\t0\t1000\t\t2012-07-24"));
        payments.add(PaymentFactory.split("51\t2012-07-22\tSome summary UKR\tUAN.\t0\t\t100\t2012-07-24"));
        payments.add(PaymentFactory.split("51\t2012-07-23\tSome summary UKR\tUAN.\t0\t\t200\t2012-07-26"));
        payments.add(PaymentFactory.split("51\t2012-07-24\tSome summary UKR\tUAN.\t0\t\t100\t2012-07-26"));
        controller = new Controller();
        controller.addPayments(payments);
        assertEquals(controller.getBillingPeriods().size(), 1);
        for (Day day : controller.getBillingPeriods().get(0).getDays()) {
            log.trace(day.toStringNoNext());
        }
        assertEquals(3, controller.getBillingPeriods().get(0).getDays().size());
    }

    @Test
    public void testAddPayments2() {
        testAddPayments();
        List<Payment> payments = new LinkedList<Payment>();
        payments.add(PaymentFactory.split("51\t2012-06-21\tSome summary UKR\tUAN.\t0\t1000\t\t2012-06-24"));
        payments.add(PaymentFactory.split("51\t2012-06-22\tSome summary UKR\tUAN.\t0\t\t100\t2012-06-24"));
        payments.add(PaymentFactory.split("51\t2012-08-23\tSome summary UKR\tUAN.\t0\t\t200\t2012-08-26"));
        payments.add(PaymentFactory.split("51\t2012-08-24\tSome summary UKR\tUAN.\t0\t\t100\t2012-08-26"));
        controller.addPayments(payments);
        assertEquals(controller.getBillingPeriods().size(), 3);
        log.trace(controller.getBillingPeriods().get(0).getDays().size());

        log.trace(controller.getBillingPeriods().get(1).getDays().size());
        for (Day day : controller.getBillingPeriods().get(1).getDays()) {
            log.trace(day.toStringNoNext());
        }

        log.trace(controller.getBillingPeriods().get(2).getDays().size());
        for (Day day : controller.getBillingPeriods().get(2).getDays()) {
            log.trace(day.toStringNoNext());
        }
        assertEquals(30, controller.getBillingPeriods().get(0).getDays().size());
        assertEquals(3, controller.getBillingPeriods().get(1).getDays().size());
        assertEquals(31, controller.getBillingPeriods().get(2).getDays().size());

    }

    @Test
    public void testSplitPeriods() {
        testAddPayments2();
        controller.splitPeriods(1);
        assertEquals(2, controller.getBillingPeriods().size());
        assertEquals(30, controller.getBillingPeriods().get(0).getDays().size());
        assertEquals(34, controller.getBillingPeriods().get(1).getDays().size());

    }

    @Test
    public void testDividePeriods() throws ParseException {
        testAddPayments2();
        log.trace("testDividePeriods");
        controller.dividePeriods(new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2012"));
        for (Day day : controller.getBillingPeriods().get(1).getDays()) {
            log.trace(day.toStringNoNext());
        }

        assertEquals(4, controller.getBillingPeriods().size());
        assertEquals(7, controller.getBillingPeriods().get(0).getDays().size());
        assertEquals(23, controller.getBillingPeriods().get(1).getDays().size());
    }

}
