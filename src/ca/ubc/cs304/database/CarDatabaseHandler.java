package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;
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
    public Vehicles[] getAllVehicles() {
        ArrayList<Vehicles> result = new ArrayList<Vehicles>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM vehicle");
            // TODO: executeQuery should be what the user input

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

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
}
