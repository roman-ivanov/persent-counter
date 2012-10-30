package ua.pp.bizon.persentcounter.controller;

import static junit.framework.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import ua.pp.bizon.persentcounter.controller.Controller;
import ua.pp.bizon.persentcounter.controller.Day;
import ua.pp.bizon.persentcounter.controller.Payment;
import ua.pp.bizon.persentcounter.controller.excel.ExcelPaymentFactory;

public class ControllerTest {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void before() {
        /*StaticLoggerBinder
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
        });*/
    }

    Controller controller = null;

    @Test
    public void testAddPayments() {
        List<Payment> payments = new LinkedList<Payment>();
        payments.add(ExcelPaymentFactory.split("51\t2012-07-21\tSome summary UKR\tUAN.\t0\t1000\t\t2012-07-24"));
        payments.add(ExcelPaymentFactory.split("51\t2012-07-22\tSome summary UKR\tUAN.\t0\t\t100\t2012-07-24"));
        payments.add(ExcelPaymentFactory.split("51\t2012-07-23\tSome summary UKR\tUAN.\t0\t\t200\t2012-07-26"));
        payments.add(ExcelPaymentFactory.split("51\t2012-07-24\tSome summary UKR\tUAN.\t0\t\t100\t2012-07-26"));
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
        payments.add(ExcelPaymentFactory.split("51\t2012-06-21\tSome summary UKR\tUAN.\t0\t1000\t\t2012-06-24"));
        payments.add(ExcelPaymentFactory.split("51\t2012-06-22\tSome summary UKR\tUAN.\t0\t\t100\t2012-06-24"));
        payments.add(ExcelPaymentFactory.split("51\t2012-08-23\tSome summary UKR\tUAN.\t0\t\t200\t2012-08-26"));
        payments.add(ExcelPaymentFactory.split("51\t2012-08-24\tSome summary UKR\tUAN.\t0\t\t100\t2012-08-26"));
        controller.addPayments(payments);
        assertEquals(controller.getBillingPeriods().size(), 3);
        log.trace(controller.getBillingPeriods().get(0).getDays().size() + "");

        log.trace(controller.getBillingPeriods().get(1).getDays().size() + "");
        for (Day day : controller.getBillingPeriods().get(1).getDays()) {
            log.trace(day.toStringNoNext());
        }

        log.trace(controller.getBillingPeriods().get(2).getDays().size() + "");
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
