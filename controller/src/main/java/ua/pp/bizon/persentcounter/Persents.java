package ua.pp.bizon.persentcounter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Persents {

    private Date from;
    private Date to;
    private Double value;

    public Persents(Date from, Date to) {
        super();
        this.from = from;
        this.to = to;
    }

    /**
     * date in format dd/MM/yyyy
     * @param from
     * @param to
     * @throws ParseException
     */
    public Persents(String from, String to) throws ParseException {
        this.from = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        this.to = new SimpleDateFormat("dd/MM/yyyy").parse(to);
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Persents [from=" + from + ", to=" + to + ", value=" + value + "]";
    }

}
