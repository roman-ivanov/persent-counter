package ua.pp.bizon.persentcounter.web.bean;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.xml.sax.SAXException;

import ua.pp.bizon.persentcounter.Controller;
import ua.pp.bizon.persentcounter.Payment;
import ua.pp.bizon.persentcounter.PaymentFactory;

@ManagedBean(name = "statement")
@SessionScoped
public class StatementBean {
    
    private Log log = LogFactory.getLog(getClass());

    private Controller controller = new Controller();

    private UploadedFile uploadedFile;

    private String name = "";

    public Controller getController() {
        return controller;
    }

    public String getId() {
        return "qwe";
    }

    public UploadedFile getUpFile() {
        return uploadedFile;
    }

    public void setUpFile(UploadedFile upFile) {
        uploadedFile = upFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void upload() throws IOException {
        try {
            List<Payment> payments = PaymentFactory.read( DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uploadedFile.getInputStream()));
            controller.addPayments(payments);
            log.info("loaded " + payments.size() + " payments");
            return;
        }  catch (Exception e) {
           log.fatal(e.getMessage(), e);
        }
        return;
    }
    
    public void clear() {
        controller.getBillingPeriods().clear();
    }

    public boolean isUploaded() {
        return !controller.getBillingPeriods().isEmpty();
    }
}
