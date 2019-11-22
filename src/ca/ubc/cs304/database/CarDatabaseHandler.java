package ca.ubc.cs304.database;

import ca.ubc.cs304.model.RentReceipt;
import ca.ubc.cs304.model.Reservations;
import ca.ubc.cs304.model.Vehicles;

import java.sql.*;
import java.util.ArrayList;

public class CarDatabaseHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public CarDatabaseHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    /**
     * View all the vehicles
     * Customers are able to choose: vtname, location, timeInterval[*PIAZZA*]
     *           if not inputs view all
     */
    public Vehicles[] getAllVehicles(String vtname, String location) {
        ArrayList<Vehicles> result = new ArrayList<Vehicles>();
        ResultSet rs;

        try {
            Statement stmt = connection.createStatement();
            if (vtname == null && location == null) {
                rs = stmt.executeQuery("SELECT * FROM vehicle ORDER BY "); //>> no input from customer
            } else if (vtname != null && location != null) {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE vtname = ? AND location = ?");
                    ps.setString(1, vtname);
                    ps.setString(2, location);
                    rs = ps.executeQuery();

            } else if (vtname == null) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE location = ?");
                ps.setString(1, location);
                rs = ps.executeQuery();
            } else {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE vtname = ?");
                ps.setString(1, vtname);
                rs = ps.executeQuery();
            }

            while(rs.next()) {
                Vehicles v = new Vehicles(rs.getString("vlicense"),
                        rs.getString("make"),
                        rs.getString("model"), //model
                        rs.getInt("year"), //year
                        rs.getString("color"), //color
                        rs.getInt("odometer"), //odometer
                        rs.getString("status"), //status
                        rs.getString("vtname"), //vtname
                        rs.getString("location"), //location
                        rs.getString("city")); //city
                result.add(v);
            }


            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Vehicles[result.size()]);
    }

    /**
     * Reserving && Renting && Returning ================================================
     ***
     ***
     ***
     ***
     * Making a Reservation
     * Inputs:
     * Output a confNo for the customer
     */
    public Integer makeReservation(String vtname, String dlicense, Date fromDate, Time fromTime,
           Date toDate, Time toTime){
        Integer result = -1;

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservations VALUES (reserveConfNo.nextval,?,?,?,?,?,?)");
            ps.setString(2, vtname);
            ps.setString(3, dlicense);
            ps.setDate(4, fromDate);
            ps.setTime(5, fromTime);
            ps.setDate(6, toDate);
            ps.setTime(7, toTime);

            ps.executeUpdate();
            connection.commit();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT confNo FROM reservations WHERE confNo = reserveConfNo.currval");
            result = rs.getInt("confNo"); // the confNo to return

            ps.close();
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return result;

    }


    /**
     * Renting a vehicle
     * If reservation no made -> insert into reservation, customer provides confNo & dlicense
     * find the rest from reservation
     * to complete : CardNo + ExpDate
     * return a receipt displaying: confNo, date of reservation, vtname, location, rental lasts for etc.
     */
    public RentReceipt rentVehicle(String vtname, String location, String cardName, Integer cardNo, Date expDate, int confNo) {
        RentReceipt result = null;
        ResultSet rs = null;

        try {
            //if reservation was made
            Statement stmt = connection.createStatement();
            if (confNo != 0) { //confNo was not empty
                // get vtname from reservations with confNo
                // find a car with that vtname
                // that car will have location 
                PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals VALUES(rentID.nextval,?,?,?,?,?,?,?,?,?,?,?");
                ps.setString(8, cardName);
                ps.setInt(9, cardNo);
                ps.setDate(10, expDate);
                ps.setInt(11, confNo);

                PreparedStatement getReservation = connection.prepareStatement("SELECT vtname, dlicense, reserveFromDate, reserveFromTime, reserveToDate, reserveToTime FROM reservations WHERE confNo = ?");
                getReservation.setInt(1, confNo);
                PreparedStatement getVehicles = connection.prepareStatement("SELECT vlicense, odometer FROM vehicle WHERE vtname = ?");

                ResultSet reserveSet = getReservation.executeQuery();
                while(reserveSet.next()) {
                    getVehicles.setString(1, reserveSet.getString("vtname"));
                    ps.setInt(1, reserveSet.getInt("dlicense"));
                    ps.setDate(3, reserveSet.getDate("reserveFromDate"));
                    ps.setTime(4, reserveSet.getTime("reserveFromTime"));
                    ps.setDate(5, reserveSet.getDate("reserveToDate"));
                    ps.setTime(6, reserveSet.getTime("reserveToTime"));
                }
                
                ResultSet vehicleSet = getVehicles.executeQuery();
                while (vehicleSet.first()) {
                    ps.setInt(2, vehicleSet.getInt("vlicense"));
                    ps.setInt(7, vehicleSet.getInt("odometer"));
                }

                ps.executeUpdate();
                connection.commit();

                //need to return confNo, rental lasts for can just be fromDate and toDate
                //return vtname, location <- this comes from the vehicle relation....using rentals foreign key vlicense
            }
            if (vtname == null && location == null) {
                rs = stmt.executeQuery("SELECT * FROM vehicle ORDER BY "); //>> no input from customer
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        // stub
        return result;
    }


    /**
     * Generate Reports ================================================
     */

    /**
     * Generate Daily Rental Report
     * TODO!!!!!
     * TODO: need to construct another class to store report info ???
     */
    public ResultSet getDailyRentals() {
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("stub");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return rs; //TODO need to save it locally
    }


    // not sure what it does here just put it first
    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }





}
