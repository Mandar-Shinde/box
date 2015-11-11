package com.elekso.potfix.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by mandar on 25-Oct-15.
 */


public class Config {
    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }
    public static Config getInstance(Context c,File cache) {
        context = c;
        if(!cache.exists())
            cache.mkdirs();
        configurationFile = new String(cache.getPath()+"/"+"config.ini");
        return ourInstance;
    }

    private static Properties configuration;
    private static String configurationFile = "config.ini";
    private static String configName;
    private static String configEmail;
    public static Context context;

    private Config() {
        configuration = new Properties();
        configurationFile = "config.ini";
        configName="name";
        configEmail="email";
    }

    public  void load() {
        try {
            configuration.clear();
            try {
                configuration.load(new FileInputStream(configurationFile));
            } catch (FileNotFoundException e) {

                FileWriter writer=null;
                try {
                    writer = new FileWriter(configurationFile);
                    writer.write("");
                    writer.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }

    public void store() {
        try {
            configuration.store(new FileOutputStream(configurationFile), null);
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

    }

    public  String getProfileName()
    {
        load();
        String data=configuration.getProperty(configName);
        return  data;
    }
    public  String getProfileEmail()
    {
        load();
        String data=configuration.getProperty(configEmail);
        return  data;
    }

    public  boolean setProfile(String name,String email)
    {
        try {
            configuration.setProperty(configName, name);
            configuration.setProperty(configEmail, email);
            store();
        } catch (Exception e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

        return true;
    }

    public  boolean delProfile(int id,String name,String email)
    {
        return true;
    }

}
