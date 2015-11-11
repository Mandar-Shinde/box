package com.elekso.potfix.model;

import java.io.Serializable;

/**
 * Created by mandar on 02-Oct-15.
 */
public class PotfixModel implements Serializable {

    private static final long serialVersionUID = 3359152015L;


    // class member
    private long rid;
    private String androidid;
    private String timestamp;
    private double latitude;
    private double longitude;
    private double gforce;
    private double accuracy;
    private float[] sensordata={0,0,0,0};
    boolean isBump=false;

    public long getRid() {
        return rid;
    }
    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getAndroidid() {
        return androidid;
    }
    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getGforce() {
        return gforce;
    }
    public void setGforce(double gforce) {
        this.gforce = gforce;
    }

    public double getaccuracy() {
        return accuracy;
    }
    public void setaccuracy(double acc) {
        this.accuracy = acc;
    }


    public float[] getSensor() {
        return sensordata;
    }
    public void setSensor(float x,float y,float z) {
        this.sensordata[0] = x;
        this.sensordata[1] = y;
        this.sensordata[2] = z;
        this.isBump=false;
    }
    public boolean isBump() {
        return isBump;
    }

    public void setIsBump(boolean isBump) {
        this.isBump = isBump;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof PotfixModel)) {
            return false;

        }
        PotfixModel model = (PotfixModel)o;
        return ((float)latitude == (float)model.latitude) ? ((float)longitude == (float)model.longitude) : false;
    }
}
