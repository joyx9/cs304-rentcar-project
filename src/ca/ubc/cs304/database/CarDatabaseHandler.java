package ca.ubc.cs304.database;

import ca.ubc.cs304.model.RentReceipt;
import ca.ubc.cs304.model.Reservations;
import ca.ubc.cs304.model.Vehicles;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
     * Check if the confNo exists
     */
    public boolean confNoExist(Integer confNo) {
        ResultSet rs;
        boolean ret = false;

        try {
            if (confNo != null) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE confNo = ? ");
                ps.setInt(1, confNo);
                rs = ps.executeQuery();
                ret = rs.next();
                rs.close();
                ps.close();
            } else {
                ret = false;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return ret;
    }

    /**
     * Check if the rid exists in rentals
     */
    public boolean ridExist(Integer rid) {
        ResultSet rs;
        boolean ret = false;

        try {
            if (rid != null) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM rentals WHERE rid = ? ");
                ps.setInt(1, rid);
                rs = ps.executeQuery();
                ret = rs.next();
                rs.close();
                ps.close();
            } else {
                ret = false;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return ret;
    }

    /**
     * Check if the dlicense exists in reservations
     */
    public boolean dlicenseExistInResv(String dlicense) {
        ResultSet rs;
        boolean ret = false;

        try {
            if (dlicense.equals("")) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE dlicense = ? ");
                ps.setString(1, dlicense);
                rs = ps.executeQuery();
                ret = rs.next();
                rs.close();
                ps.close();
            } else {
                ret = false;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return ret;
    }

    /**
     * Check if the dlicense exists in reservations
     */
    public boolean dlicenseExistInCustomer(String dlicense) {
        ResultSet rs;
        boolean ret = false;

        try {
            if (!dlicense.equals("")) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM customer WHERE dlicense = ? ");
                ps.setString(1, dlicense);
                rs = ps.executeQuery();
                ret = rs.next();
                rs.close();
                ps.close();
            } else {
                ret = false;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return ret;
    }

    /**
     * View all the vehicles Customers are able to choose: vtname, location,
     * timeInterval[*PIAZZA*] if not inputs view all
     */
    public Vehicles[] getAllVehicles(String vtname, String location) {
        ArrayList<Vehicles> result = new ArrayList<>();
        ResultSet rs;

        try {
            Statement stmt = connection.createStatement();
            if (vtname.equals("") && location.equals("")) {
                rs = stmt.executeQuery("SELECT * FROM vehicle ORDER BY location"); // >> no input from customer
            } else if (!vtname.equals("") && !location.equals("")) {
                PreparedStatement ps = connection
                        .prepareStatement("SELECT * FROM vehicle WHERE vtname = ? AND location = ?");
                ps.setString(1, vtname);
                ps.setString(2, location);
                rs = ps.executeQuery();

            } else if (vtname.equals("")) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE location = ?");
                ps.setString(1, location);
                rs = ps.executeQuery();
            } else {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE vtname = ?");
                ps.setString(1, vtname);
                rs = ps.executeQuery();
            }

            while (rs.next()) {
                Vehicles v = new Vehicles(rs.getString("vlicense"), rs.getString("make"), rs.getString("model"), // model
                        Integer.toString(rs.getInt("year")), // year
                        rs.getString("color"), // color
                        Integer.toString(rs.getInt("odometer")), // odometer
                        rs.getString("status"), // status
                        rs.getString("vtname"), // vtname
                        rs.getString("location"), // location
                        rs.getString("city")); // city
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
     * Reserving && Renting && Returning
     * ================================================
     ***
     ***
     ***
     ***
     * Making a Reservation Inputs: vtname, dlicense, fromdate, todate Output a
     * confNo for the customer
     */
    public String makeReservation(String vtname, String dlicense, String fromDate, String toDate) {

        String result = "";

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO reservations VALUES (reserveConfNo.nextval,?,?,?,?)");
            ps.setString(1, vtname);
            ps.setString(2, dlicense);
            ps.setString(3, fromDate);
            // ps.setTime(5, fromTime);
            ps.setString(4, toDate);
            // ps.setTime(6, toTime);

            System.out.println("reservation query= " + ps);

            ps.executeUpdate();
            connection.commit();

            System.out.println("sequence check 1");

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT confNo FROM reservations WHERE confNo = (SELECT max(confNo) FROM reservations)");
            // rs.next();
            System.out.println(rs.next());
            result = Integer.toString(rs.getInt("confNo")); // the confNo to return
            System.out.println(result);
            ps.close();
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("ORA-02291")) {
                result = "ERROR: the car type does not exist";
            } else {
                result = "ERROR: your inputs are invalid";
            }
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return result;

    }

    /**
     * add a new Customer to SQL Inputs: name,addr,delicense Output a confNo for the
     * customer
     */
    public void addCustomer(String name, String address, String dlicense) {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?)");
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, dlicense);

            ps.executeUpdate();
            connection.commit();

            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    /**
     * Renting a vehicle If reservation no made -> insert into reservation, customer
     * provides confNo & dlicense find the rest from reservation to complete :
     * CardNo + ExpDate return a receipt displaying: confNo, date of reservation,
     * vtname, location, rental lasts for etc.
     */
    // public RentReceipt rentVehicle(String vtname, String location, String
    // cardName, Integer cardNo, String expDate, int confNo) {
    // RentReceipt result = null;
    // ResultSet rs = null;

    // try {
    // //if reservation was made
    // Statement stmt = connection.createStatement();
    // if (confNo != 0) { //confNo was not empty
    // // get vtname from reservations with confNo
    // // find a car with that vtname
    // // that car will have location
    // PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals
    // VALUES(rentID.nextval,?,?,?,?,?,?,?,?,?");
    // ps.setString(6, cardName);
    // ps.setInt(7, cardNo);
    // ps.setString(8, expDate);
    // ps.setInt(9, confNo);

    // PreparedStatement getReservation = connection.prepareStatement("SELECT
    // vtname, dlicense, reserveFromDate, reserveFromTime, reserveToDate,
    // reserveToTime FROM reservations WHERE confNo = ?");
    // getReservation.setInt(1, confNo);
    // PreparedStatement getVehicles = connection.prepareStatement("SELECT vlicense,
    // odometer FROM vehicle WHERE vtname = ?");

    // ResultSet reserveSet = getReservation.executeQuery();
    // while(reserveSet.next()) {
    // getVehicles.setString(1, reserveSet.getString("vtname"));
    // ps.setInt(1, reserveSet.getInt("dlicense"));
    // ps.setString(3, reserveSet.getString("reserveFromDate"));
    // //ps.setTime(4, reserveSet.getTime("reserveFromTime"));
    // ps.setString(4, reserveSet.getString("reserveToDate"));
    // //ps.setTime(6, reserveSet.getTime("reserveToTime"));
    // }

    // ResultSet vehicleSet = getVehicles.executeQuery();
    // while (vehicleSet.first()) {
    // ps.setInt(2, vehicleSet.getInt("vlicense"));
    // ps.setInt(5, vehicleSet.getInt("odometer"));
    // }

    // ps.executeUpdate();
    // connection.commit();

    // //need to return confNo, rental lasts for can just be fromDate and toDate
    // //return vtname, location <- this comes from the vehicle relation....using
    // rentals foreign key vlicense
    // }
    // if (vtname == null && location == null) {
    // rs = stmt.executeQuery("SELECT * FROM vehicle ORDER BY "); //>> no input from
    // customer
    // }

    // rs.close();
    // stmt.close();
    // } catch (SQLException e) {
    // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
    // }

    // // stub
    // return result;
    // }

    public String rentVehicle(String cardName, Integer cardNo, String expDate, int confNo, String fromDate,
            String toDate) {
        String rentReceipt = "";
        ResultSet rs = null;

        try {
            // if reservation was made
            Statement stmt = connection.createStatement();
            if (confNo != 0) { // confNo was not empty
                // get vtname from reservations with confNo
                // find a car with that vtname
                // that car will have location
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO rentals VALUES(rentID.nextval,?,?,?,?,?,?,?,?,?)");
                ps.setString(3, fromDate);
                ps.setString(4, toDate);
                ps.setString(6, cardName);
                ps.setInt(7, cardNo);
                ps.setString(8, expDate);
                ps.setInt(9, confNo);

                PreparedStatement getReservation = connection
                        .prepareStatement("SELECT vtname, dlicense FROM reservations WHERE confNo = ?");
                getReservation.setInt(1, confNo);
                PreparedStatement getVehicles = connection
                        .prepareStatement("SELECT vlicense, odometer FROM vehicle WHERE vtname = ?");

                ResultSet reserveSet = getReservation.executeQuery();
                boolean query = reserveSet.next();
                System.out.println(query);
                if (query) {
                    ps.setString(1, reserveSet.getString("dlicense")); // rent.dlicense from reservations
                    getVehicles.setString(1, reserveSet.getString("vtname")); // vehicle.vtname from reservations
                }
                ResultSet vehicleSet = getVehicles.executeQuery();
                if (vehicleSet.next()) {
                    ps.setString(2, vehicleSet.getString("vlicense"));
                    ps.setString(5, vehicleSet.getString("odometer")); // get rent vlicense and odometer from that
                                                                       // specific vehicle
                }

                ps.executeUpdate();
                connection.commit();

                Statement findRID = connection.createStatement();
                ResultSet rentID = findRID
                        .executeQuery("SELECT rid FROM rentals WHERE rid = (SELECT max(rid) FROM rentals)");
                // rs.next();
                System.out.println(rentID.next());
                int rid = rentID.getInt("rid"); // the confNo to return

                PreparedStatement getReceipt = connection
                        .prepareStatement("SELECT rid, vlicense, rentFromDate, rentToDate FROM rentals WHERE rid = ?");
                getReceipt.setInt(1, rid);
                rs = getReceipt.executeQuery();

                // while(rs.next()) {
                rs.next();
                rentReceipt = ("Your rental confirmaton number is: " + Integer.toString(rs.getInt("rid"))
                        + "\n Vehicle License: " + (rs.getString("vlicense")) + "\n Starting Date : "
                        + (rs.getString("rentFromDate")) + "\n End Date: " + (rs.getString("rentToDate")));
                // }

                getReservation.close();
                getVehicles.close();
                ps.close();
                reserveSet.close();
                vehicleSet.close();

                // need to return confNo, rental lasts for can just be fromDate and toDate
                // return vtname, location <- this comes from the vehicle relation....using
                // rentals foreign key vlicense
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rentReceipt = "Ooops... Somthing Wrong happened.";
        }

        return rentReceipt;
    }

    /**
     * Returning a vehicle When returning a vehicle, the system will display a
     * receipt with the necessary details (e.g., reservation confirmation number,
     * date of return, how the total was calculated etc.) if the vehicle is not
     * rented throw error message Clerk enters:date, the time, the odometer reading,
     * and gas tank is full? //TODO ->> LOOK AT ME the totalcost is a make up number
     * !!!!!!
     */
    public String returnVehicle(int rid, String returnDate, int odometer, String gasTankFull) {
        String returnRecipt = "";

        try {
            if (!ridExist(rid)) {
                returnRecipt = ("Sorry this rental id does not exist.");
            } else {

                PreparedStatement ps = connection.prepareStatement("INSERT INTO returns VALUES (?,?,?,?,?)");
                ps.setInt(1, rid);
                ps.setString(2, returnDate);
                ps.setInt(3, odometer);
                ps.setString(4, gasTankFull);
                ps.setInt(5, 180);

                ps.executeUpdate();
                connection.commit();
                ps.close();

                // return array: [rid, returnDate, totalCost]
                returnRecipt = ("Your return Id is: " + Integer.toString(rid) + "\nReturn Date: " + returnDate
                        + "\nYour total cost is: 180 ");

            }

        } catch (SQLException e) {

            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            returnRecipt = ("Sorry, an error occurred, please contact help desk.");
        }

        return returnRecipt;
    }

    /**
     * Generate Reports ================================================
     */

    /**
     * Generate Daily Rental Report TODO!!!!! TODO: need to construct another class
     * to store report info ???
     */
    public ArrayList<String[]> getDailyRentals() {
        ArrayList<String[]> report = new ArrayList<>();
        ResultSet rs = null;

        try {
            String todaysDate = getDate();
            System.out.println(todaysDate);
            PreparedStatement createView = connection.prepareStatement(
                    "create or replace view dailyrent as select v.location, v.vtname from vehicle v, rentals r where r.rentFromDate = ? and v.vlicense = r.vlicense");
            createView.setString(1, todaysDate);

            ResultSet dailyRentView = createView.executeQuery();
            connection.commit();

            Statement getReport = connection.createStatement();
            rs = getReport.executeQuery("select location, vtname, count(*) from dailyrent group by location, vtname");

            while (rs.next()) {
                String[] reportRow = new String[] { rs.getString("location"), rs.getString("vtname"),
                        Integer.toString(rs.getInt(3)) };
                report.add(reportRow);
            }

            Statement getTotal = connection.createStatement();
            ResultSet total = getTotal.executeQuery("select count(*) FROM dailyrent");

            if (total.next()) {
                String[] reportTotal = new String[] { Integer.toString(total.getInt(1)) };
                report.add(reportTotal);
            }

            createView.close();
            dailyRentView.close();
            rs.close();
            total.close();
            getReport.close();
            getTotal.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Error("Something went wrong!!");
        }
        return report;
    }

    /**
     * Generate Daily Rental Report by Branch TODO!!!!! TODO: need to construct
     * another class to store report info ???
     */
    public ArrayList<String> getDailyRentalsByBranch() {
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("stub");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null; // TODO need to save it locally
    }

    /**
     * Generate Daily Return Report TODO!!!!! TODO: need to construct another class
     * to store report info ???
     */
    public ArrayList<String> getDailyReturns() {
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("stub");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        // stub
        return null;
    }

    /**
     * Generate Daily Return Report by Branch TODO!!!!! TODO: need to construct
     * another class to store report info ???
     */
    public ArrayList<String> getDailyReturnsByBranch() {
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("stub");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        // stub
        return null; // TODO need to save it locally
    }

    /**
     * Get current date as a STRING METHOD
     * ================================================
     */

    // private String getDateTime() {
    //
    // DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
    // Locale.getDefault());
    //
    // Date date = new Date();
    //
    // return dateFormat.format(date);
    // }

    private String getDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }

    /**
     * Weird Stuff Ignore ================================================
     */

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, "a" + password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    // not sure what it does here just put it first
    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

}
