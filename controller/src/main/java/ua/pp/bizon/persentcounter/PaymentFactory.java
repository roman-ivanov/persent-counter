package ua.pp.bizon.persentcounter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PaymentFactory {
    

    private static final Log log = LogFactory.getLog(PaymentFactory.class);
    
    public static LinkedList<Payment> readPayments(String... paths) throws SAXException, IOException, ParserConfigurationException{
        LinkedList<Payment> payments = new LinkedList<Payment>();
        for (String path : paths){
            log.trace("read file: " + path);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            read(document, payments);
        }
        return payments;
    }

    public static void read(Document  document, LinkedList<Payment> result) throws SAXException, IOException, ParserConfigurationException {
        Node table = document.getDocumentElement().getElementsByTagName("s:Table").item(0);
        NodeList tableList = table.getChildNodes();
        for (int i = 0; i < tableList.getLength(); i++){
            if (tableList.item(i).getNodeName().equals("s:Row")){
                Node row = tableList.item(i);
                Payment p = process(row, i);
                if (p != null && !result.contains(p)){
                    result.add(p);
                }
            }
        }
    }
    
    public static LinkedList<Payment> read(Document  document) throws SAXException, IOException, ParserConfigurationException {
        LinkedList<Payment>list = new LinkedList<Payment>();
        read(document, list);
        Utils.sort(list);
        return list;
    }

    protected static Payment process(Node row, int i) {
        
        StringBuffer message = new StringBuffer( i + "\t");
        for (int j = 0; j < row.getChildNodes().getLength(); j++){
            if (row.getChildNodes().item(j).getNodeName().equals("s:Cell")){
                Node cell = row.getChildNodes().item(j);
                for (int k = 0; k < cell.getChildNodes().getLength(); k++){
                   if (cell.getChildNodes().item(k).getNodeName().equals("s:Data")){
                       Node data = cell.getChildNodes().item(k);
                       if (data.hasChildNodes()){
                       message.append(data.getFirstChild().getNodeValue() + "\t");
                       }else {
                           message.append("\t");
                       }
                   }
                }
            }
        }
        log.trace("read line: " + message);
        return split(message.toString());
    }

    public static Payment split(String message) {
        Payment p = new Payment();
        String[] splitData = message.split("\t");
        if (splitData.length > 1 &&    splitData[1].matches("[0-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")){
            try {
                p.setHoldingData(new SimpleDateFormat("yyyy-MM-dd").parse(splitData[1]));
                p.setProcessingData(new SimpleDateFormat("yyyy-MM-dd").parse(splitData[7]));
                p.setDescription(splitData[2]);
                p.setCurrecy(splitData[3]);
                p.setProcessingAmount(Double.valueOf(splitData[4]));
                p.setBillingAmount(Double.valueOf(splitData[5].isEmpty() ? "-" + splitData[6]: splitData[5]));
                return p;
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

}
