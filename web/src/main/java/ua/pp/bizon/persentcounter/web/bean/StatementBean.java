package ua.pp.bizon.persentcounter.web.bean;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.pp.bizon.persentcounter.controller.BillingPeriod;
import ua.pp.bizon.persentcounter.controller.Controller;
import ua.pp.bizon.persentcounter.controller.PaymentFactory;
import ua.pp.bizon.persentcounter.controller.Utils;
import ua.pp.bizon.persentcounter.controller.rtf.RTFDocument;
import ua.pp.bizon.persentcounter.controller.rtf.Rtf2String;

@ManagedBean(name = "statement")
@SessionScoped
public class StatementBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Controller controller = new Controller();

    private UploadedFile uploadedFile;

    private String upText = "";

    public List<BillingPeriod> getBillingPeriods() {
        return controller.getBillingPeriods();
    }

    public UploadedFile getUpFile() {
        return uploadedFile;
    }

    public void setUpFile(UploadedFile upFile) {
        uploadedFile = upFile;
    }

    public void upload() throws IOException {
        try {
            if (uploadedFile != null && uploadedFile.getInputStream() != null && uploadedFile.getSize() > 1) {
                PaymentFactory.read(uploadedFile.getInputStream(), controller);
            } else if (getUpText() != null && !getUpText().isEmpty()){
                RTFDocument document = Rtf2String.parse(getUpText());
                log.debug("payments size: " + document.getPayments().size());
                controller.addPayments(Utils.sort(document.getPayments()));
                controller.getBillingPeriods().get(0).getDays().getFirst().setStartBalance(document.getStartBalanse());
            }
            return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return;
    }

    public void clear() {
        controller.getBillingPeriods().clear();
    }

    public boolean isUploaded() {
        return !controller.getBillingPeriods().isEmpty();
    }

    public String getUpText() {
        return upText;
    }

    public void setUpText(String upText) {
        this.upText = upText;
    }
}
