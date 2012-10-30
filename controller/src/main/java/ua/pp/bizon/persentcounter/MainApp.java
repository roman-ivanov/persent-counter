package ua.pp.bizon.persentcounter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ua.pp.bizon.persentcounter.controller.Payment;
import ua.pp.bizon.persentcounter.controller.Persents;
import ua.pp.bizon.persentcounter.controller.excel.ExcelPaymentFactory;

@Deprecated
public class MainApp {

    static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public MainApp() {
        System.out.println("mainApp constructor");
    }

    /**
     * @param args
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     */
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, ParseException {
        //Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%p %c %x - %m%n")));
        //Logger.getRootLogger().setLevel(Level.TRACE);
        MainApp app = new MainApp();
        app.configure("Operations.xls", "Operations (1).xls", "Operations (2).xls", "Operations (3).xls");
        app.setStartAccountState(0);

        Payment obsl = new Payment();
        obsl.setProcessingData(new SimpleDateFormat("dd/MM/yyyy").parse("28/4/2012"));
        obsl.setHoldingData(new SimpleDateFormat("dd/MM/yyyy").parse("28/4/2012"));
        obsl.setBillingAmount(-99.0);
        obsl.setDescription("service");
        app.addServicePayments(obsl);
        log.info("read payments: " + app.payments.size());
        Statement statement = app.prepareStatement();
        log.info("end balanse: " + statement.endBalanse());
        List<Persents> persents = new LinkedList<Persents>();
        persents.add(new Persents("1/4/2012", "28/4/2012"));
        persents.add(new Persents("1/5/2012", "31/5/2012"));
        persents.add(new Persents("1/6/2012", "27/6/2012"));
        persents.add(new Persents("1/7/2012", "31/7/2012"));
        app.countPersents(0.14, 366, persents);
        for (Persents p : persents) {
            log.info("persents: " + p);
        }
    }

    protected void logPersents() {
        for (Payment payment : payments) {
            if (payment.getDescription().equals("Выплата начисленных процентов на остаток основногоСКС  ")) {
                log.debug("persents on payment period: " + payment);
            }
        }
    }

    LinkedList<Payment> payments = new LinkedList<Payment>();
    private double startAccountState;
    private Statement statement;

    public void configure(String... string) throws SAXException, IOException, ParserConfigurationException {
        payments = ExcelPaymentFactory.readPayments(string);
        sortPayments();
    }

    public void setStartAccountState(double startAccountState) {
        this.startAccountState = startAccountState;
    }

    public void addServicePayments(Payment... payments) {
        this.payments.addAll(Arrays.asList(payments));
        sortPayments();
    }

    @Deprecated
    public List<Persents> countPersents(double rate, int countDaysInYear, List<Persents> countedPersents) throws ParseException {
        prepareStatement();
        statement.count(rate, countDaysInYear, countedPersents);
        return countedPersents;
    }

    protected void sortPayments() {
        statement = null;
        Collections.sort(payments, new Comparator<Payment>() {
            public int compare(Payment o1, Payment o2) {
                return o1.getProcessingData().compareTo(o2.getProcessingData());
            }
        });
    }

    public Statement prepareStatement() {
        if (statement == null) {
            sortPayments();
            statement = new Statement();
            if (payments.size() > 0) {
                statement.init(payments.getFirst().getProcessingData(), payments.getLast().getProcessingData(), startAccountState);
                for (Payment i : payments) {
                    statement.addPayment(i);
                }
            }
        }
        return statement;
    }

    public void loadStatement(Document parse) throws SAXException, IOException, ParserConfigurationException {
        ExcelPaymentFactory.read(parse, payments);
        sortPayments();
    }

    public int length() {
        return payments == null ? 0 : payments.size();
    }


}
