package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.CarDatabaseHandler;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.Vehicles;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainDisplay;
import ca.ubc.cs304.ui.TerminalTransactions;

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

	public ArrayList<String> getAllVehicles(String vtname, String location) {
		Vehicles[] vehicles = carHandler.getAllVehicles(vtname, location);
		ArrayList<String> resultStr = new ArrayList<>();
        for (int i = 0; i < vehicles.length; i++) {
            Vehicles vehicle = vehicles[i];
            // simplified output formatting; truncation may occur
            String str = vehicle.getCity() + vehicle.getColor() + vehicle.getLocation()
                    + vehicle.getMake() + vehicle.getModel() + vehicle.getStatus() + vehicle.getVlicense();

            resultStr.add(str);
        }
        return resultStr;
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
