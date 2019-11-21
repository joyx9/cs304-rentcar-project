package ca.ubc.cs304.database;

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
     * Generate Daily Rental Report
     * TODO!!!!!
     */
    public ResultSet getDailyRentals() {
        ResultSet rs;

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






}
