package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single Customer
 */

/**
 * A customer is identified by its dlicense
 *  cellphone is not used
 */
public class Customer {
    private final String dlicense;
    private final String name;
    private final String address;
    // private final int cellphone; //do we still need this??


    public Customer(String dlicense, String name, String address) {
        this.dlicense = dlicense;
        this.name = name;
        this.address = address;
        // this.cellphone = cellphone;
    }

    public String getDlicense() {
        return dlicense;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // public Integer getCellphone() {
    // 	return cellphone;
    // }

}
