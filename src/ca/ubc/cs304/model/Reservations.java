package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a single Reservation
 */

/**
 * A vehicle is identified by its vlicense
 * A customer is identified by its dlicense
 */
public class Reservations {
    private final int confNo;
    private final String vlicense;  // vtname -> vlicense
    private final String dlicense;  // cellphone -> dlicense
    private final Date fromDate;
    private final Date toDate;
    private final String vtname;



    public Reservations(int confNo, String vlicense, String dlicense, Date fromDate,
                        Date toDate, String vtname) {
        this.confNo = confNo;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.vtname = vtname;
    }

    public Integer getconfNo() {
        return confNo;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getDlicense() {
        return dlicense;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getVtname() {
        return vtname;
    }

}
