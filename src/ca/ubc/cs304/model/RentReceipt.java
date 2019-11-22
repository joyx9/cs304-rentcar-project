package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a Renting Receipt
 */

/**
 * a receipt with the necessary details (e.g.,confirmation number,date of reservation,
 *   type of car, location, how long the rental period lasts for, etc.)
 *
 */
public class RentReceipt {
    // confNo, date of reservation, vtname, location, rental lasts for etc.
    private final int confNo;
    private final Date resDate;
    private final String vtname;
    private final String location;
    private final Date period;



    public RentReceipt(int confNo, Date resDate, String vtname, String location, Date period) {
        this.confNo = confNo;
        this.resDate = resDate;
        this.vtname = vtname;
        this.location = location;
        this.period = period;
    }

    public Integer getConfNo(){
        return confNo;
    }

    public Date getResDate(){
        return resDate;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public Date getPeriod() {
        return period;
    }
}
