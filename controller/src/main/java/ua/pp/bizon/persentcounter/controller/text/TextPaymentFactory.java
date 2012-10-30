package ua.pp.bizon.persentcounter.controller.text;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ua.pp.bizon.persentcounter.controller.Controller;
import ua.pp.bizon.persentcounter.controller.PaymentFactory;
import ua.pp.bizon.persentcounter.controller.Utils;
import ua.pp.bizon.persentcounter.controller.rtf.RTFDocument;
import ua.pp.bizon.persentcounter.controller.rtf.Rtf2String;

public class TextPaymentFactory extends PaymentFactory {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String getSignature() {
        return "";
    }

    @Override
    public void process(InputStream stream, Controller controller) throws IOException, BadLocationException, SAXException, ParserConfigurationException {
        RTFDocument document = Rtf2String.parse(IOUtils.toString(stream));
        log.debug("payments size: " + document.getPayments().size());
        controller.addPayments(Utils.sort(document.getPayments()));
        controller.getBillingPeriods().get(0).getDays().getFirst().setStartBalance(document.getStartBalanse());
    }

}
