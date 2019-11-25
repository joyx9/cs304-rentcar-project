package ca.ubc.cs304.delegates;

import java.util.ArrayList;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
	public ArrayList<String[]> getAllVehicles(String vtname, String location);
	public String makeReservation(String name, String address, String dlicense,
                                  String vtname, String fromDate, String toDate);
	public String rentVehicle(String cardName, Integer cardNo, String expDate, int confNo, String fromDate, String toDate);
	public String returnVehicle(int rid, String returnDate, int odometer, String gasTankFull);
	public ArrayList<String[]>getDailyRentals();
	public ArrayList<String[]>getDailyBranchRentals(String branchloc);
    public ArrayList<String[]>getDailyReturns();
    public ArrayList<String[]>getDailyBranchReturns(String branchloc);
	public boolean reservationExists(int confno);
	public void terminalTransactionsFinished();
}
