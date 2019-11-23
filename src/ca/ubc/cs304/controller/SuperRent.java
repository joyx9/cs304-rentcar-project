package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.CarDatabaseHandler;
import ca.ubc.cs304.model.Vehicles;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainDisplay;
import java.util.ArrayList;

import ca.ubc.cs304.database.CarDatabaseHandler;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.RentReceipt;
import ca.ubc.cs304.model.Vehicles;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainDisplay;
import ca.ubc.cs304.ui.TerminalTransactions;
import java.sql.Date;
import java.util.ArrayList;


/**
 * This is the main controller class that will orchestrate everything.
 */
public class SuperRent implements LoginWindowDelegate, TerminalTransactionsDelegate {
	private CarDatabaseHandler carHandler = null;
//	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	public SuperRent() {
//		dbHandler = new DatabaseConnectionHandler();
		carHandler = new CarDatabaseHandler();
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
 @Override
	public void login(String username, String password) {
		boolean didConnect = carHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();
			// todo this is what we changed!!!!!!!!!!!!!!!!!!!!!!!!!
			// TerminalTransactions transaction = new TerminalTransactions();
			// transaction.showMainMenu(this);
            MainDisplay md = new MainDisplay();
            md.showDisplay(this);

		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

 @Override
	public ArrayList<String[]> getAllVehicles(String vtname, String location) {
		Vehicles[] vehicles = carHandler.getAllVehicles(vtname, location);
		ArrayList<String[]> resultStr = new ArrayList<>();
        for (int i = 0; i < vehicles.length; i++) {
            Vehicles vehicle = vehicles[i];
            String[] str = new String[] {vehicle.getLocation(), vehicle.getMake(),
                    vehicle.getModel(), vehicle.getVlicense(),
                    vehicle.getCity(), vehicle.getColor(), vehicle.getStatus()};

            resultStr.add(str);
        }
        return resultStr;
	}

    @Override
    public String makeReservation( String name, String address, String dlicense,
                                   String vtname, String fromDate, String toDate) {
	    // check if customer dlicense exists by calling database handler
		// if not, add customer
		System.out.println("initial reservation");
        if (! carHandler.dlicenseExistInCustomer(dlicense)) {
			carHandler.addCustomer(name, address, dlicense);
			System.out.println("new customer");
        }
		// make a reservation for them
		System.out.println("final");
        String str = carHandler.makeReservation(vtname, dlicense, fromDate, toDate);
	    return str;
    }

    @Override
    public String rentVehicle(String cardName, Integer cardNo, String expDate, int confNo, String fromDate, String toDate){
        String str = carHandler.rentVehicle(cardName, cardNo, expDate, confNo, fromDate, toDate);
	    str += ".      Thank you for renting from SuperRent!";
	    return str;
    }

    @Override
    public boolean reservationExists(int confNo){
        return carHandler.confNoExist(confNo);
    }

    @Override
    public String returnVehicle (int rid, String returnDate, int odometer, String gasTankFull){
        return carHandler.returnVehicle(rid, returnDate, odometer, gasTankFull);
    }

    @Override
    public ArrayList<String[]>getDailyRentals(){
     return carHandler.getDailyRentals();
    }
	// /**
	//  * TermainalTransactionsDelegate Implementation
	//  * 
	//  * Insert a branch with the given info
	//  */
    // public void insertBranch(BranchModel model) {
    // 	dbHandler.insertBranch(model);
    // }

    // /**
	//  * TermainalTransactionsDelegate Implementation
	//  * 
	//  * Delete branch with given branch ID.
	//  */ 
    // public void deleteBranch(int branchId) {
    // 	dbHandler.deleteBranch(branchId);
    // }
    
    // /**
	//  * TermainalTransactionsDelegate Implementation
	//  * 
	//  * Update the branch name for a specific ID
	//  */

    // public void updateBranch(int branchId, String name) {
    // 	dbHandler.updateBranch(branchId, name);
    // }

    // /**
	//  * TermainalTransactionsDelegate Implementation
	//  * 
	//  * Displays information about varies superRent branches.
	//  */
//     public void showBranch() {
//     	BranchModel[] models = dbHandler.getBranchInfo();
//
//     	for (int i = 0; i < models.length; i++) {
//     		BranchModel model = models[i];
//
//     		// simplified output formatting; truncation may occur
//     		System.out.printf("%-10.10s", model.getId());
//     		System.out.printf("%-20.20s", model.getName());
//     		if (model.getAddress() == null) {
//     			System.out.printf("%-20.20s", " ");
//     		} else {
//     			System.out.printf("%-20.20s", model.getAddress());
//     		}
//     		System.out.printf("%-15.15s", model.getCity());
//     		if (model.getPhoneNumber() == 0) {
//     			System.out.printf("%-15.15s", " ");
//     		} else {
//     			System.out.printf("%-15.15s", model.getPhoneNumber());
//     		}
//
//     		System.out.println();
//     	}
//     }
//
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that it is done with what it's 
     * doing so we are cleaning up the connection since it's no longer needed.
     */ 
    @Override
    public void terminalTransactionsFinished() {
    	carHandler.close();
    	carHandler = null;
    	
    	System.exit(0);
    }
    
	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		SuperRent superRent = new SuperRent();
		superRent.start();
	}
}
