package ua.pp.bizon.persentcounter.controller.rtf;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.pp.bizon.persentcounter.controller.Payment;

public class RTFDocument {

    static Logger log = LoggerFactory.getLogger(RTFDocument.class);

    private LinkedList<Payment> payments;
    private double startBalanse;
    private LinkedList<Payment> blockings;

    public LinkedList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(LinkedList<Payment> payments) {
        this.payments = payments;
    }

    public double getStartBalanse() {
        return startBalanse;
    }

    public void setStartBalanse(double startBalanse) {
        this.startBalanse = startBalanse;
    }

    public LinkedList<Payment> getBlockings() {
        return blockings;
    }

    public void setBlockings(LinkedList<Payment> blockings) {
        this.blockings = blockings;
    }

}
