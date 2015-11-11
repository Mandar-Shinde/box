package com.elekso.potfix.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mandar on 11-Oct-15.
 */
public class Logger {
    private static Logger ourInstance = new Logger();
    static File logFileFolder ;

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger()
    {
        logFileFolder = new File( "/sdcard/potfix/");
        logFileFolder.mkdirs();
    }


    public String getlogname()
    {

        int i=0;
        do {
            Date curDate = new Date();
            SimpleDateFormat formatfn = new SimpleDateFormat("dd-M-yyyy");
            String tmpnam= "potlog" + formatfn.format(curDate) + "-" + i++ + ".txt";
            File file = new File(logFileFolder,tmpnam);
            if(file.exists())
            {
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                if(file_size<5000)
                    return tmpnam;
            }
            else
            {
                return tmpnam;
            }
        }while(i<50);
        return "bumplog.txt";
    }


    public void appendLog(String text)
    {

        File file = new File(logFileFolder, getlogname());



        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            // Date dNow = new Date();
            // SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

            Date curDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            buf.append(format.format(curDate)+" "+text);

            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
