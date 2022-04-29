package com.example.sharif.onlinebooking;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constant {
    // italin language words
    static public String UPDATE = "AGGIORNARE";
    static public   String INSERT ="INSERIRE";
    static public   String DELETE ="ELIMINARE";
    static public  String USER ="UTENTE";
    static public  String ALARM ="Aggiungi allarme";
    static public  String ADMIN = "AMMINISTRATRICE";
    static public  String CONTACT ="CONTATTO VISIVO";


    // store selected language into sharedpreferances
    public static String getSelectedLanguage(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("name","English");
    }

    public static void setSelectedLanguage(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("name", s).commit();
    }


    public static boolean getUserLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("locked", false);
    }

    public static void setUserLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("locked", s).commit();
    }
    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("name","");
    }

    public static void setUsername(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("name", s).commit();
    }
    public static String getUsernumber(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("number","");
    }

    public static void setUsernumber(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("number", s).commit();
    }
    public static String getUseraddress(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("address","");
    }

    public static void setUseraddress(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("address", s).commit();
    }




}
