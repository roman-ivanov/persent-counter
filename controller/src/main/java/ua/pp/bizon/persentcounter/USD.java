package ua.pp.bizon.persentcounter;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.xml.sax.SAXException;

public class USD {
    
    private static Log log = LogFactory.getLog(USD.class);

    /**
     * @param args
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws ParseException 
     */
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, ParseException {
        Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%p %c %x - %m%n")));
        Logger.getRootLogger().setLevel(Level.TRACE);
        MainApp app = new MainApp();
        app.configure("Operations (4).xls");
        app.setStartAccountState(0);
        log.info("read payments: " + app.payments.size());
        Statement statement = app.prepareStatement();
        log.info("end balanse: " + statement.endBalanse());
        List<Persents> persents = new LinkedList<Persents>();
        persents.add(new Persents("1/4/2012", "28/4/2012"));
        persents.add(new Persents("1/5/2012", "31/5/2012"));
        persents.add(new Persents("1/6/2012", "27/6/2012"));
        persents.add(new Persents("1/7/2012", "31/7/2012"));
        persents.add(new Persents("28/6/2012", "30/6/2012"));
        app.countPersents(0.05, 366, persents);
        app.logPersents();
        for (Persents p : persents) {
            log.info("persents: " + p);
        }
    }

}
