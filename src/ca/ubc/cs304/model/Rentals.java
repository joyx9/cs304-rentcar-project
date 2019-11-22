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
    private final String fromDate;
    private final String toDate;
    private final int odometer;
    private final String cardName;
    private final int cardNo;
    private final String expDate;
    private final int confNo;


    public Rentals(int rid, String vlicense, String dlicense, String fromDate,
                   String toDate, int odometer, String cardName, int cardNo, String expDate, int confNo) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    public String getToDate() {
        return toDate;
    }

    public String getFromDate() {
        return fromDate;
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

    public String getExpDate() {
        return expDate;
    }

    public Integer getConfNo() {
        return confNo;
    }

}
