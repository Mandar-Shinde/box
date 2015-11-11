package com.elekso.potfix;

import android.hardware.SensorEvent;

import java.util.ArrayList;
import java.util.List;

enum Pothole{ TINY,SMALL,MEDIUM,BIG,HUGE };

enum FilterType{RAW,X_AXIS,Y_AXIS,Z_AXIS};

public class Filter {

    static float x,y,z;         //raw
    static float xo,yo,zo;      //dx difference values

    static int zdiffcount=0;    //count
    static int zdifflimit=0;    //threshold limit

    static int stddevcount=0;
    static int stddevlimit=30;

    static List<Float> stdvalues = new ArrayList<Float>();

    private static Filter fob = new Filter();

    public static Filter getInstance() {
        return fob;
    }

    private Filter()
    {
        if(fob==null)
            getInstance();
    }

    public static boolean Normalize()
    {
        //todo: if required
        return true;
    }

    public static float Thresh(SensorEvent e,FilterType type)
    {
        float ret=0;
        x=e.values[0];
        y=e.values[1];
        z=e.values[2];

        Normalize();

        ret=(float)(Math.sqrt(((x) * (x)) + ((y) * (y)) + ((z) * (z)))/9.81);

        return ret;
    }

    public static float Diff(float[] e,FilterType type)
    {
        float ret=0;
        x=e[0];
        y=e[1];
        z=e[2];

        Normalize();

        if(xo==0 && yo==0 && zo==0)  // reset if required
        {
            xo=x;
            yo=y;
            zo=z;
        }

        if(zdiffcount<zdifflimit)
        {
            zdiffcount++;
            return 0;
        }
        else
        {
            zdiffcount=0;
        }

        switch(type)
        {
            case X_AXIS:
                ret= Math.abs(x - xo);
                break;
            case Y_AXIS:
                ret= Math.abs(y - yo);
                break;
            case Z_AXIS:
                ret= Math.abs(z - zo);
                break;
        }
        xo=x;
        yo=y;
        zo=z;
        return ret;
    }

    public static float Stddev(SensorEvent e,FilterType type)
    {
        float ret=0;
        float var;
        x=e.values[0];
        y=e.values[1];
        z=e.values[2];

        Normalize();

        if(stdvalues.size()>stddevlimit)
        {
            stdvalues.remove(0);
        }

        switch(type)
        {
            case X_AXIS:
                stdvalues.add(x);
                break;
            case Y_AXIS:
                stdvalues.add(y);
                break;
            case Z_AXIS:
                stdvalues.add(z);
                break;
        }

        float sum=0;            //Sum Xbar
        float average=0;        //Avg Xbar
        for(Float value:stdvalues )
        {
            sum+=value;
        }
        average=sum/stddevlimit;

        float sumXbar=0;            //Sum Xn-Xbar^2
        float averageXvbar=0;        //Avg Xn-Xbar^2
        for(Float value:stdvalues )
        {
            sumXbar+=  Math.abs(value - average)* Math.abs(value - average);
        }
        averageXvbar=sumXbar/stddevlimit;

        ret=(float) Math.sqrt(averageXvbar);

        return ret;
    }


}



