package ua.pp.bizon.persentcounter.web.dao;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.pp.bizon.persentcounter.MainApp;
import ua.pp.bizon.persentcounter.Statement;

@Component
//@Scope(value = "globalSession", proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Transactional
public class StatementProxy {
    
    private Log log = LogFactory.getLog(getClass());

  
    private MainApp app = new MainApp();
    private String sessionId;

    public StatementProxy() {
        log.trace("StatementProxy constructor");
    }
    
    public StatementProxy(String id) {
        log.trace("StatementProxy constructor: id " + id);
        sessionId = id;
    }

    public MainApp getApp() {
        return app;
    }

    public void setApp(MainApp app) {
        this.app = app;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Statement getStatement() {
        log.trace(app.prepareStatement().toString());
        return app.prepareStatement();
    }

    public void loadStatement(InputStream statement) {
        try {
            app.loadStatement(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(statement));
            log.debug("statement parsed : " + app.length());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
