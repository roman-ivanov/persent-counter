package ua.pp.bizon.persentcounter.controller.rtf;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.swing.text.AbstractDocument.BranchElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ua.pp.bizon.persentcounter.controller.Payment;

public class Rtf2String {

    private static Logger log = LoggerFactory.getLogger(Rtf2String.class);

    private static StringBuilder expandElement(javax.swing.text.Element rtfElement, StyledDocument styledDocument) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < rtfElement.getElementCount(); i++) {
            javax.swing.text.Element rtfNextElement = rtfElement.getElement(i);
            if (rtfNextElement.isLeaf()) {
                try {
                    b.append(addElement(rtfNextElement, styledDocument));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                b.append(expandElement(rtfNextElement, styledDocument));
            }
        }
        return b;
    }

    private static StringBuilder addElement(javax.swing.text.Element rtfElement, StyledDocument document) throws UnsupportedEncodingException,
            BadLocationException {
        return new StringBuilder(document.getText(rtfElement.getStartOffset(), rtfElement.getEndOffset() - rtfElement.getStartOffset()));

    }

    public static RTFDocument convert(InputStream inputStream) throws IOException, BadLocationException {
        StyledDocument rtfSource = new DefaultStyledDocument();
        RTFEditorKit kit = new RTFEditorKit();
        kit.read(inputStream, rtfSource, 0);
        BranchElement rtfRoot = (BranchElement) rtfSource.getDefaultRootElement();
        StringBuilder to = expandElement(rtfRoot, rtfSource);
        RTFDocument document = parse(to.toString());
        return document;
    }

    private static enum Place {
        BEFORE_ALL, READED_START_BALANCE, READ_BANK_OPERATIONS, READ_BANK_OPERATIONS_LINE2, AFTER_READ_BANK_OPERATIONS, READ_STATEMENT, READ_STATEMENT_LINE_2, AFTER_READ_STATEMENT, READ_BLOCKINGS, READ_BLOCKINGS_LINE_2, AFTER_READ_BLOCKINGS

    }

    public static RTFDocument parse(String text) {
        RTFDocument document = new RTFDocument();
        LinkedList<Payment> payments = new LinkedList<Payment>();
        document.setPayments(payments);
        LinkedList<Payment> blockings = new LinkedList<Payment>();
        document.setBlockings(blockings);

        Place place = Place.BEFORE_ALL;
        for (String line : text.split("\n")) {
            try {
                switch (place) {
                case BEFORE_ALL:
                    if (tryReadStartBalance(document, line)) {
                        place = Place.READED_START_BALANCE;
                    }
                    break;

                case READED_START_BALANCE:
                    if (tryReadBankOperation(document, line)) {
                        place = Place.READ_BANK_OPERATIONS_LINE2;
                    }
                    break;
                case READ_BANK_OPERATIONS:
                    if (tryReadBankOperation(document, line)) {
                        place = Place.READ_BANK_OPERATIONS_LINE2;
                    } else {
                        place = Place.AFTER_READ_BANK_OPERATIONS;
                    }
                    break;
                case READ_BANK_OPERATIONS_LINE2:
                    readBankOperationLine2(document, line);
                    place = Place.READ_BANK_OPERATIONS;
                    break;
                case AFTER_READ_BANK_OPERATIONS:
                    if (tryReadBankOperation(document, line)) {
                        place = Place.READ_STATEMENT_LINE_2;
                    }
                    break;
                case READ_STATEMENT_LINE_2:
                    readBankOperationLine2(document, line);
                    place = Place.READ_STATEMENT;
                    break;
                case READ_STATEMENT:
                    if (tryReadBankOperation(document, line)) {
                        place = Place.READ_STATEMENT_LINE_2;
                    } else {
                        place = Place.AFTER_READ_STATEMENT;
                    }
                    break;
                default:
                    log.trace("line not read" + line);
                    break;
                }
            } catch (ParseException e) {
                log.warn("not parsed: " + e);
            }
        }
        return document;
    }

    private static void readBankOperationLine2(RTFDocument document, String line) {
        Payment p = document.getPayments().getLast();
        if (line.length() > 71){
        p.setDescription(p.getDescription() + " " + line.substring(20, 71).trim());
        if (!line.substring(71).trim().isEmpty()) {
            p.setCurrecy(line.substring(71, 75).trim());
        }
        if (!line.substring(75).trim().isEmpty()) {
            p.setBillingAmount(Double.valueOf(line.substring(75, 85).trim().replace(',', '.')));
        }
        if (!line.substring(91).trim().isEmpty()) {
            p.setProcessingAmount(Double.valueOf(line.substring(91).trim().replace(',', '.')));
        }
        }
        log.trace("line parsed to " + p);
    }

    private static boolean tryReadBankOperation(RTFDocument document, String line) throws ParseException {
        if (line.length() > 18 && line.substring(2, 10).matches("[0-3][0-9].[0-1][0-9].[0-9][0-9]")) {
            Payment p = new Payment();
            p.setHoldingData(new SimpleDateFormat("dd.MM.yy").parse(line.substring(2, 10)));
            p.setProcessingData(new SimpleDateFormat("dd.MM.yy").parse(line.substring(11, 19)));
            if (line.trim().length() > 20 && line.trim().length() <= 71){
                p.setDescription(line.substring(20).trim());
            } else 
            if (line.trim().length() > 71) {
                p.setDescription(line.substring(20, 71).trim());
                p.setCurrecy(line.substring(71, 75).trim());
                p.setBillingAmount(Double.valueOf(line.substring(75, 85).trim().replace(',', '.')));
                if (!line.substring(91).trim().isEmpty()) {
                    p.setProcessingAmount(Double.valueOf(line.substring(91).trim().replace(',', '.')));
                }
            }
            document.getPayments().add(p);
            return true;
        }
        log.trace("line NOT parsed:" + line + "$");
        return false;
    }

    private static boolean tryReadStartBalance(RTFDocument document, String line) {
        if (line.contains(":") && line.contains(",")) {
            try {
                document.setStartBalanse(Double.parseDouble(line.substring(28).trim().replace(',', '.')));
                log.debug("parsed start balanse:" + document.getStartBalanse());
                return true;
            } catch (NumberFormatException e) {
                log.warn(e + " on line: $" + line + "$");
            }
        }
        log.trace("line NOT parsed:" + line + "$");
        return false;
    }

}
