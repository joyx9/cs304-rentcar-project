package ca.ubc.cs304.database;

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
        if (isDateBeforeToday(fromDate) || isDateBeforeToday(toDate) || isToDateBefore(fromDate, toDate)) {
            result = "ERROR: Invalid date was inputted";
        } else {
            try {
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO reservations VALUES (reserveConfNo.nextval,?,?,?,?)");
                ps.setString(1, vtname);
                ps.setString(2, dlicense);
                ps.setString(3, fromDate);
                ps.setString(4, toDate);

                ps.executeUpdate();
                connection.commit();

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT confNo FROM reservations WHERE confNo = (SELECT max(confNo) FROM reservations)");
                rs.next();
                result = Integer.toString(rs.getInt("confNo")); // the confNo to return
                ps.close();
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                if (e.getMessage().contains("ORA-02291")) {
                    result = "ERROR: your desired car type is not available";
                } else if (nullError(e)) {
                    result = "ERROR: inputs cannot be null";
                } else {
                    result = "ERROR: your inputs are invalid";
                }
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
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

    public String rentVehicle(String cardName, Integer cardNo, String expDate, int confNo, String fromDate,
            String toDate) {
        String rentReceipt = "";
        ResultSet rs = null;

        if (isDateBeforeToday(fromDate) || isDateBeforeToday(toDate) || isToDateBefore(fromDate, toDate)
                || isDateBeforeToday(expDate)) {
            rentReceipt = "ERROR: Invalid date was inputted";
        } else {
            try {
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
                    rentID.next();
                    int rid = rentID.getInt("rid"); // the confNo to return

                    PreparedStatement getReceipt = connection.prepareStatement(
                            "SELECT rid, vlicense, rentFromDate, rentToDate FROM rentals WHERE rid = ?");
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
                if (nullError(e)) {
                    rentReceipt = "ERROR: inputs cannot be empty";
                } else {
                    rentReceipt = "Ooops... Somthing Wrong happened.";
                }
            }
        }
        return rentReceipt;
    }

    /**
     * Returning a vehicle When returning a vehicle, the system will display a
     * receipt with the necessary details (e.g., reservation confirmation number,
     * date of return, how the total was calculated etc.) if the vehicle is not
     * rented throw error message Clerk enters:date, the time, the odometer reading,
     * and gas tank is full !!!!!!
     */
    public String returnVehicle(int rid, String returnDate, int odometer, String gasTankFull) {
        String returnRecipt = "";

        if (!ridExist(rid)) {
            returnRecipt = ("Sorry this rental id does not exist.");
        }
        if (isDateBeforeToday(returnDate)) {
            returnRecipt = "ERROR: Invalid date was inputted.";
        } else {
            try {
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
                        + "\nYour total cost is: $180 ");
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                if (nullError(e)) {
                    returnRecipt = "ERROR: inputs cannot be empty";
                } else {
                    returnRecipt = ("Sorry, an error occurred, please contact help desk.");
                }
            }
        }

        return returnRecipt;
    }

    /**
     * Generate Reports ================================================
     */

    /**
     * Generate Daily Rental Report to store report info ???
     */
    public ArrayList<String[]> getDailyRentals() {
        ArrayList<String[]> report = new ArrayList<>();
        ResultSet rs = null;

        try {
            String todaysDate = getDate();
            PreparedStatement createView = connection.prepareStatement(
                    "create or replace view dailyrent as select v.location, v.vtname, r.rentFromDate from vehicle v, rentals r where v.vlicense = r.vlicense");
            ResultSet dailyRentView = createView.executeQuery();

            PreparedStatement getReport = connection.prepareStatement(
                    "select location, vtname, count(*) from dailyrent where rentFromDate = ? group by location, vtname");
            getReport.setString(1, todaysDate);
            rs = getReport.executeQuery();

            while (rs.next()) {
                String[] reportRow = new String[] { rs.getString("location"), rs.getString("vtname"),
                        Integer.toString(rs.getInt(3)) };
                report.add(reportRow);
            }

            PreparedStatement getTotal = connection
                    .prepareStatement("select count(*) FROM dailyrent where rentFromDate = ?");
            getTotal.setString(1, todaysDate);
            ResultSet total = getTotal.executeQuery();

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
            // throw new Exception("Something went wrong!!");
        }
        return report;
    }

    /**
     * Generate Daily Rental Report by Branch
     */
    public ArrayList<String[]> getDailyRentalsByBranch(String location) {
        ArrayList<String[]> report = new ArrayList<>();
        ResultSet rs = null;

        try {
            String todaysDate = getDate();
            PreparedStatement createView = connection.prepareStatement(
                    "create or replace view dailyrent as select v.location, v.vtname, r.rentFromDate from vehicle v, rentals r where v.vlicense = r.vlicense");
            ResultSet dailyRentView = createView.executeQuery();

            PreparedStatement getReport = connection.prepareStatement(
                    "select location, vtname, count(*) from dailyrent where rentFromDate = ? and location = ? group by location, vtname");
            getReport.setString(1, todaysDate);
            getReport.setString(2, location);
            rs = getReport.executeQuery();

            while (rs.next()) {
                String[] reportRow = new String[] { rs.getString("location"), rs.getString("vtname"),
                        Integer.toString(rs.getInt(3)) };
                report.add(reportRow);
            }

            PreparedStatement getTotal = connection
                    .prepareStatement("select count(*) FROM dailyrent where rentFromDate = ? and location = ?");
            getTotal.setString(1, todaysDate);
            getTotal.setString(2, location);
            ResultSet total = getTotal.executeQuery();

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
            // throw new Exception("Something went wrong!!");
        }
        return report;
    }

    public ArrayList<String[]> getDailyReturns() {
        ArrayList<String[]> report = new ArrayList<>();
        ResultSet rs = null;

        try {
            String todaysDate = getDate();
            PreparedStatement createView = connection.prepareStatement(
                    "create or replace view dailyreturn as select v.location, v.vtname, ret.dateReturned, ret.value from vehicle v, rentals r, returns ret where v.vlicense = r.vlicense and r.rid = ret.rid");

            ResultSet dailyReturnView = createView.executeQuery();
            PreparedStatement getReport = connection.prepareStatement(
                    "select location, vtname, count(*), sum(value) from dailyreturn where dateReturned = ? group by location, vtname");
            getReport.setString(1, todaysDate);
            rs = getReport.executeQuery();
            while (rs.next()) {
                String[] reportRow = new String[] { rs.getString("location"), rs.getString("vtname"),
                        Integer.toString(rs.getInt(3)), Integer.toString(rs.getInt(4)) };
                report.add(reportRow);
            }
            PreparedStatement getTotal = connection
                    .prepareStatement("select count(*), sum(value) FROM dailyreturn where dateReturned = ?");
            getTotal.setString(1, todaysDate);
            ResultSet total = getTotal.executeQuery();
            if (total.next()) {
                String[] reportTotal = new String[] { Integer.toString(total.getInt(1)) + " and total cost is: $"
                        + Integer.toString(total.getInt(2)) };
                report.add(reportTotal);
            }

            createView.close();
            dailyReturnView.close();
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

    public ArrayList<String[]> getDailyReturnsByBranch(String location) {
        ArrayList<String[]> report = new ArrayList<>();
        ResultSet rs = null;

        try {
            String todaysDate = getDate();
            PreparedStatement createView = connection.prepareStatement(
                    "create or replace view dailyreturn as select v.location, v.vtname, ret.dateReturned, ret.value from vehicle v, rentals r, returns ret where v.vlicense = r.vlicense and r.rid = ret.rid");

            ResultSet dailyReturnView = createView.executeQuery();

            PreparedStatement getReport = connection.prepareStatement(
                    "select location, vtname, count(*), sum(value) from dailyreturn where dateReturned = ? and location = ? group by location, vtname");
            getReport.setString(1, todaysDate);
            getReport.setString(2, location);
            rs = getReport.executeQuery();

            while (rs.next()) {
                String[] reportRow = new String[] { rs.getString("location"), rs.getString("vtname"),
                        Integer.toString(rs.getInt(3)), Integer.toString(rs.getInt(4)) };
                report.add(reportRow);
            }

            PreparedStatement getTotal = connection.prepareStatement(
                    "select count(*), sum(value) FROM dailyreturn where dateReturned = ? and location = ?");
            getTotal.setString(1, todaysDate);
            getTotal.setString(2, location);
            ResultSet total = getTotal.executeQuery();

            if (total.next()) {
                String[] reportTotal = new String[] { Integer.toString(total.getInt(1)) + " and total cost is: $"
                        + Integer.toString(total.getInt(2)) };
                report.add(reportTotal);
            }

            createView.close();
            dailyReturnView.close();
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
     * Get current date as a STRING METHOD
     * ================================================
     */

    private String getDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return formatter.format(date);
    }

    private boolean nullError(SQLException e) {
        return (e.getMessage().contains("ORA-02291"));
    }

    private boolean isDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        try {
            LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isDateBeforeToday(String date) {
        String todaysDate = getDate();
        if (isDate(date)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate today = LocalDate.parse(todaysDate, formatter);
            LocalDate input = LocalDate.parse(date, formatter);
            return input.isBefore(today);
        }
        return true;
    }

    private boolean isToDateBefore(String fromDate, String toDate) {
        if (isDate(fromDate) && isDate(toDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate from = LocalDate.parse(fromDate, formatter);
            LocalDate to = LocalDate.parse(toDate, formatter);
            return to.isBefore(from);
        }
        return true;
    }

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

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

}
