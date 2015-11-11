package com.elekso.potfix.utils;

/**
 * Created by mandar on 28-Oct-15.
 */
public class Globals {
    private static Globals ourInstance = new Globals();

    public static Globals getInstance() {
        return ourInstance;
    }

    private Globals() {
    }

    boolean flexiblemap;
    int IgnoreDiameter;

    public boolean getFlexiblemap()
    {
        return flexiblemap;
    }

    public void setFlexiblemap(boolean v)
    {
        flexiblemap=v;
    }


    public int getIgnoreDiameter()
    {
        if(IgnoreDiameter==0)
            IgnoreDiameter=2;
        return IgnoreDiameter;
    }

    public void setIgnoreDiameter(int v)
    {
        IgnoreDiameter=v;
    }
}
