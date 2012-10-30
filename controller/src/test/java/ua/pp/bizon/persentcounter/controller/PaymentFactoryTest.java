package ua.pp.bizon.persentcounter.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.pp.bizon.persentcounter.controller.rtf.RtfPaymentFactory;

public class PaymentFactoryTest {

    @BeforeClass
    public static void before() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.TRACE);
    }

    @Test
    public void rtfToPaymentTest() throws IOException, ParserConfigurationException, BadLocationException {
        String filename = "src/test/resources/ua/pp/bizon/persentcounter/1.rtf";
        Controller controller = new Controller();
        new RtfPaymentFactory().process(new FileInputStream(filename), controller);
        Assert.assertNotNull(controller.getBillingPeriods());
        Assert.assertEquals(1, controller.getBillingPeriods().size());
        System.out.println(controller.getBillingPeriods());
        Assert.assertEquals(30, controller.getBillingPeriods().get(0).getDays().size());

    }

}
