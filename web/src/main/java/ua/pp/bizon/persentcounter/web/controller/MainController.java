package ua.pp.bizon.persentcounter.web.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ua.pp.bizon.persentcounter.web.dao.StatementProxy;

@Controller
public class MainController {

    private static final Log LOG = LogFactory.getLog(MainController.class);

    @RequestMapping(value = "/main.html")
    public ModelAndView main(HttpServletRequest request) {
        StatementProxy statementProxy = null;
        String sessionId = request.getParameter("id");
        if (sessionId == null) {
            sessionId = "" + System.currentTimeMillis() + ":" + Math.random();
            map.put(sessionId, new StatementProxy(sessionId));
            LOG.debug("main controller - statementDAO created");
        }
        statementProxy = map.get(sessionId);
        LOG.debug("main controller - sessionID: " + sessionId);
        if (request.getMethod().equals("POST")) {
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                @SuppressWarnings("unchecked")
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.getName() != null) {
                        statementProxy.loadStatement(item.getInputStream());
                    }else {
                        System.out.println(item.getFieldName());
                        System.out.println(item.getString());
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return new ModelAndView("statement.jsp", "statementDao", statementProxy);
    }

    private static Map<String, StatementProxy> map = new TreeMap<String, StatementProxy>();

}
