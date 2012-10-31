package ua.pp.bizon.persentcounter.controller;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

public class DayTest {

    private Day day1;
    private Day day2;
    private Day day3;

    @Before
    public void setUp() throws Exception {
        day1 = new Day(Date.valueOf("2012-01-01"), 100.0);
        day2 = new Day(day1);
        day3 = new Day(day2);
        day1.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "pay1", "UAN", -100.0, -100.0));
        day1.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "return1", "UAN", 100.0, 100.0));
        day2.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "pay1", "UAN", -80.0, -80.0));
        day2.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "return1", "UAN", 80.0, 80.0));
        day3.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "pay1", "UAN", -50.0, -50.0));
        day3.addPayment(new Payment(Date.valueOf("2011-12-30"), day1.getBillingDate(), "return1", "UAN", 50.0, 50.0));
    }

    @Test
    public void testGetBalanceBeforeBilling() {
        assertEquals(0.0, day1.getBalanceBeforeBilling(), 0.0);
        assertEquals(20.0, day2.getBalanceBeforeBilling(), 0.0);
        assertEquals(50.0, day3.getBalanceBeforeBilling(), 0.0);
    }

    @Test
    public void testGetBalanseEndDay() {
        assertEquals(100.0, day1.getBalanceEndDay(), 0.0);
        assertEquals(100.0, day2.getBalanceEndDay(), 0.0);
        assertEquals(100.0, day3.getBalanceEndDay(), 0.0);
    }

}
