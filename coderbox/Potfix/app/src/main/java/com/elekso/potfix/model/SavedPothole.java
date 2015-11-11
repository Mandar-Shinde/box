package com.elekso.potfix.model;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by mandar on 07-Oct-15.
 */
public class SavedPothole {

    public class potloc
    {
        double la;
        double lo;

        public potloc(double la, double lo) {
            this.la = la;
            this.lo = lo;
        }
    }

    private static Deque deque;

    private static SavedPothole ourInstance = new SavedPothole();

    public static SavedPothole getInstance() {
        return ourInstance;
    }

    private SavedPothole() {
        deque = new LinkedBlockingDeque<potloc>(50000);
    }

    public void addpothole(double pla,double plo)
    {
        if(this.deque.size()>50000)
        {
            this.deque.removeLast();
        }
        this.deque.add(new potloc(pla,plo));
    }

    public  long getSize()
    {
        return this.deque.size();
    }

}
