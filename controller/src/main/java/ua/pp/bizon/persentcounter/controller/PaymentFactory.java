package ua.pp.bizon.persentcounter.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ua.pp.bizon.persentcounter.controller.excel.ExcelPaymentFactory;
import ua.pp.bizon.persentcounter.controller.rtf.RtfPaymentFactory;
import ua.pp.bizon.persentcounter.controller.text.TextPaymentFactory;

public abstract class PaymentFactory {

    private static Set<PaymentFactory> registered = new HashSet<PaymentFactory>();

    public PaymentFactory() {
        registered.add(this);
    }

    public abstract String getSignature();

    public abstract void process(InputStream stream, Controller controller) throws IOException, BadLocationException, SAXException,
            ParserConfigurationException;

    public static void read(InputStream inputStream, Controller controller) {
        if (registered.isEmpty()) {
            init();
        }
        for (PaymentFactory p : registered) {
            try {
                p.process(inputStream, controller);
            } catch (Exception e) {
                LoggerFactory.getLogger(PaymentFactory.class).warn(e.getMessage());
            }
        }
    }

    private static void init() { // TODO should be moved to spring
        new ExcelPaymentFactory();
        new RtfPaymentFactory();
        new TextPaymentFactory();
    }

}
