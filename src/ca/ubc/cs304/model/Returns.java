package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a single Return
 */

public class Returns {
    private final int rid;
    private final Date date;
    private final int odometer;
    private final boolean fulltank;
    private final int value;


    public Returns(int rid, Date date,  int odometer, boolean fulltank, int value) {
        this.rid = rid;
        this.date = date;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public Integer getRid() {
        return rid;
    }

    public Date getDate() {
        return date;
    }


    public Integer getOdometer() {
        return odometer;
    }

    public Boolean getFulltank() {
        return fulltank;
    }

    public Integer getValue() {
        return value;
    }

}
