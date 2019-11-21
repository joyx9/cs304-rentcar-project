package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single VehicleType
 */

/**
 * A vehicle is identified by its vlicense
 *
 */
public class VehicleTypes {
    private final String vlicense;  // vtname -> vlicense instead??
    private final String features;
    private final int wrate;
    private final int drate;
    private final int hrate;
    private final int wirate;
    private final int dirate;
    private final int hirate;
    private final int kirate;


    public VehicleTypes(String vlicense, String features, int wrate, int drate, int hrate, int wirate,
                        int dirate, int hirate, int kirate) {
        this.vlicense = vlicense;
        this.features = features;
        this.wrate = wrate;
        this.drate = drate;
        this.hrate = hrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.hirate = hirate;
        this.kirate = kirate;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getFeatures() {
        return features;
    }

    public Integer getWrate() {
        return wrate;
    }

    public Integer getDrate() {
        return drate;
    }

    public Integer getHrate() {
        return hrate;
    }

    public Integer getWirate() {
        return wirate;
    }

    public Integer getDirate() {
        return dirate;
    }


    public Integer getHirate() {
        return hirate;
    }

    public Integer getKirate() {
        return kirate;
    }

}
