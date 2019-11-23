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
     * Check if the confNo exists
     */
    public boolean confNoExist(Integer confNo){
        ResultSet rs;
        boolean ret = false;

        try {
            if (confNo != null ) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE confNo = ? ");
                ps.setInt(1, confNo);
                rs = ps.executeQuery();
                ret = !rs.wasNull();
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
    public boolean ridExist(Integer rid){
        ResultSet rs;
        boolean ret = false;

        try {
            if (rid != null ) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM rentals WHERE rid = ? ");
                ps.setInt(1, rid);
                rs = ps.executeQuery();
                ret = !rs.wasNull();
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
    public boolean dlicenseExistInResv(String dlicense){
        ResultSet rs;
        boolean ret = false;

        try {
            if (dlicense.equals("") ) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE dlicense = ? ");
                ps.setString(1, dlicense);
                rs = ps.executeQuery();
                ret = !rs.wasNull();
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
    public boolean dlicenseExistInCustomer(String dlicense){
        ResultSet rs;
        boolean ret = false;

        try {
            if (!dlicense.equals("")) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM customer WHERE dlicense = ? ");
                ps.setString(1, dlicense);
                rs = ps.executeQuery();
                ret = !rs.wasNull();
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
     * View all the vehicles
     * Customers are able to choose: vtname, location, timeInterval[*PIAZZA*]
     *           if not inputs view all
     */
    public Vehicles[] getAllVehicles(String vtname, String location) {
        ArrayList<Vehicles> result = new ArrayList<>();
        ResultSet rs;

        try {
            Statement stmt = connection.createStatement();
            if (vtname.equals("") && location.equals("")) {
                rs = stmt.executeQuery("SELECT * FROM vehicle ORDER BY location"); //>> no input from customer
            } else if (!vtname.equals("") && !location.equals("")) {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM vehicle WHERE vtname = ? AND location = ?");
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

            while(rs.next()) {
                Vehicles v = new Vehicles(rs.getString("vlicense"),
                        rs.getString("make"),
                        rs.getString("model"), //model
                        Integer.toString(rs.getInt("year")), //year
                        rs.getString("color"), //color
                        Integer.toString(rs.getInt("odometer")), //odometer
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
     * Inputs: vtname, dlicense, fromdate, todate
     * Output a confNo for the customer
     */
    public String makeReservation(String vtname, String dlicense, String fromDate, String toDate){

        String result = "";

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservations VALUES (reserveConfNo.nextval,?,?,?,?)");
            ps.setString(2, vtname);
            ps.setString(3, dlicense);
            ps.setString(4, fromDate);
            //ps.setTime(5, fromTime);
            ps.setString(5, toDate);
            //ps.setTime(6, toTime);

            ps.executeUpdate();
            connection.commit();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT confNo FROM reservations WHERE confNo = reserveConfNo.currval");
            result = Integer.toString(rs.getInt("confNo")); // the confNo to return

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
     * add a new Customer to SQL
     * Inputs: name,addr,delicense
     * Output a confNo for the customer
     */
    public void addCustomer(String name, String address, String dlicense){

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
     * Renting a vehicle
     * If reservation no made -> insert into reservation, customer provides confNo & dlicense
     * find the rest from reservation
     * to complete : CardNo + ExpDate
     * return a receipt displaying: confNo, date of reservation, vtname, location, rental lasts for etc.
     */
    public RentReceipt rentVehicle(String vtname, String location, String cardName, Integer cardNo, String expDate, int confNo) {
        RentReceipt result = null;
        ResultSet rs = null;

        try {
            //if reservation was made
            Statement stmt = connection.createStatement();
            if (confNo != 0) { //confNo was not empty
                // get vtname from reservations with confNo
                // find a car with that vtname
                // that car will have location 
                PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals VALUES(rentID.nextval,?,?,?,?,?,?,?,?,?");
                ps.setString(6, cardName);
                ps.setInt(7, cardNo);
                ps.setString(8, expDate);
                ps.setInt(9, confNo);

                PreparedStatement getReservation = connection.prepareStatement("SELECT vtname, dlicense, reserveFromDate, reserveFromTime, reserveToDate, reserveToTime FROM reservations WHERE confNo = ?");
                getReservation.setInt(1, confNo);
                PreparedStatement getVehicles = connection.prepareStatement("SELECT vlicense, odometer FROM vehicle WHERE vtname = ?");

                ResultSet reserveSet = getReservation.executeQuery();
                while(reserveSet.next()) {
                    getVehicles.setString(1, reserveSet.getString("vtname"));
                    ps.setInt(1, reserveSet.getInt("dlicense"));
                    ps.setString(3, reserveSet.getString("reserveFromDate"));
                    //ps.setTime(4, reserveSet.getTime("reserveFromTime"));
                    ps.setString(4, reserveSet.getString("reserveToDate"));
                    //ps.setTime(6, reserveSet.getTime("reserveToTime"));
                }
                
                ResultSet vehicleSet = getVehicles.executeQuery();
                while (vehicleSet.first()) {
                    ps.setInt(2, vehicleSet.getInt("vlicense"));
                    ps.setInt(5, vehicleSet.getInt("odometer"));
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
     *  Returning a vehicle
     *  When returning a vehicle, the system will display a receipt with the necessary details
     *  (e.g., reservation confirmation number, date of return, how the total was calculated etc.)
     *  if the vehicle is not rented throw error message
     *  Clerk enters:date, the time, the odometer reading, and  gas tank is full?
     * //TODO TODO TODO TODO TODO LOOK AT ME NOT DONE YET TODO TODO TODO TODO TODO
     */
    public ArrayList<String> returnVehicle(int rid, String returnDate, int odometer, String gasTankFull){
        ArrayList<String> returnRecipt = new ArrayList<>();

        try {
            //if rid does not exist return empty string
            if (!ridExist(rid)){
                returnRecipt.add("Sorry this rental id does not exist.");
            } else {

                Integer totalCost = -1;
                PreparedStatement ps = connection.prepareStatement("INSERT INTO returns VALUES (?,?,?,?)");
                ps.setInt(1, rid);
                ps.setString(2, returnDate);
                ps.setInt(3, odometer);
                ps.setString(4, gasTankFull);

                ps.executeUpdate();
                connection.commit();

                PreparedStatement cs = connection.prepareStatement("SELECT odometer FROM rentals" +
                        "WHERE rid = ?");
                cs.setInt(1, rid);
                ResultSet rs = cs.executeQuery();

//
//
//                Statement stmt = connection.createStatement(); //TODO
//                ResultSet rs = stmt.executeQuery("SELECT confNo FROM reservations WHERE confNo = reserveConfNo.currval");
//                result = Integer.toString(rs.getInt("confNo")); // the confNo to return

                ps.close();
                rs.close();
                //stmt.close();










            }


                //need to return confNo, rental lasts for can just be fromDate and toDate
                //return vtname, location <- this comes from the vehicle relation....using rentals foreign key vlicense

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }


        return returnRecipt;
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
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }





}
