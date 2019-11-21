package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a single Customer
 */

/**
 * A customer is identified by its dlicense
 *
 */
public class Rentals {
    private final int rid;
    private final String  vlicense; // vehicle is now identified by vlicense -> old: vid
    private final String dlicense; // customer is now identified by dlicense -> old: cellphone
    private final Date fromDate;
    private final Time fromTime;
    private final Date toDate;
    private final Time toTime;
    private final int odometer;
    private final String cardName;
    private final int cardNo;
    private final Date expDate;
    private final int confNo;


    public Rentals(int rid, String vlicense, String dlicense, Date fromDate, Time fromTime,
                   Date toDate, Time toTime, int odometer, String cardName, int cardNo, Date expDate, int confNo) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public Integer getRid() {
        return rid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getDlicense() {
        return dlicense;
    }

    public Date getToDate() {
        return toDate;
    }

    public Time getToTime() {
        return toTime;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Time getFromTime() {
        return fromTime;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public String getCardName() {
        return cardName;
    }

    public Integer getCardNo() {
        return cardNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public Integer getConfNo() {
        return confNo;
    }

}
