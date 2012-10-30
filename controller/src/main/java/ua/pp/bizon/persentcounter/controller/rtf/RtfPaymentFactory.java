package ua.pp.bizon.persentcounter.controller.rtf;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.pp.bizon.persentcounter.controller.Controller;
import ua.pp.bizon.persentcounter.controller.PaymentFactory;
import ua.pp.bizon.persentcounter.controller.Utils;

public class RtfPaymentFactory extends PaymentFactory {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public String getSignature() {
        return "{\\rtf";
    }
    public void process(InputStream stream, Controller controller) throws IOException, BadLocationException{
        RTFDocument document = Rtf2String.convert(stream);
        log.debug("payments size: " + document.getPayments().size());
        controller.addPayments(Utils.sort(document.getPayments()));
        controller.getBillingPeriods().get(0).getDays().getFirst().setStartBalance(document.getStartBalanse());
    }

}
