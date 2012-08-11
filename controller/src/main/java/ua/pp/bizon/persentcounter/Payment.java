package ua.pp.bizon.persentcounter;

import java.util.Date;

import static ua.pp.bizon.persentcounter.Utils.dateToString;

public class Payment {
    private Date holdingData;
    private Date processingData;
    private String description;
    private String currecy;
    private Double billingAmount;
    private Double processingAmount;
    
    public Payment() {
    }
    

    public Payment(Date holdingData, Date processingData, String description, String currecy, Double billingAmount, Double processingAmount) {
        super();
        this.holdingData = holdingData;
        this.processingData = processingData;
        this.description = description;
        this.currecy = currecy;
        this.billingAmount = billingAmount;
        this.processingAmount = processingAmount;
    }


    public Date getHoldingData() {
        return holdingData;
    }

    public void setHoldingData(Date holdingData) {
        this.holdingData = holdingData;
    }

    public Date getProcessingData() {
        return processingData;
    }

    public void setProcessingData(Date processingData) {
        this.processingData = processingData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrecy() {
        return currecy;
    }

    public void setCurrecy(String currecy) {
        this.currecy = currecy;
    }

    public Double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Double billingAmount) {
        this.billingAmount = billingAmount;
    }

    public Double getProcessingAmount() {
        return processingAmount;
    }

    public void setProcessingAmount(Double processingAmount) {
        this.processingAmount = processingAmount;
    }

    @Override
    public String toString() {
        /*
         * return "Payment [holdingData=" + holdingData + ", processingData=" +
         * processingData + ", description=" + description + ", currecy=" +
         * currecy + ", billingAmount=" + billingAmount + ", processingAmount="
         * + processingAmount + "]\n";
         */
        return dateToString(holdingData) + " " + dateToString(processingData) + " " + description + " " + billingAmount + " ";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((billingAmount == null) ? 0 : billingAmount.hashCode());
        result = prime * result + ((currecy == null) ? 0 : currecy.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((holdingData == null) ? 0 : holdingData.hashCode());
        result = prime * result + ((processingAmount == null) ? 0 : processingAmount.hashCode());
        result = prime * result + ((processingData == null) ? 0 : processingData.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Payment other = (Payment) obj;
        if (billingAmount == null) {
            if (other.billingAmount != null)
                return false;
        } else if (!billingAmount.equals(other.billingAmount))
            return false;
        if (currecy == null) {
            if (other.currecy != null)
                return false;
        } else if (!currecy.equals(other.currecy))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (holdingData == null) {
            if (other.holdingData != null)
                return false;
        } else if (!holdingData.equals(other.holdingData))
            return false;
        if (processingAmount == null) {
            if (other.processingAmount != null)
                return false;
        } else if (!processingAmount.equals(other.processingAmount))
            return false;
        if (processingData == null) {
            if (other.processingData != null)
                return false;
        } else if (!processingData.equals(other.processingData))
            return false;
        return true;
    }

}
