package edu.asu.activitytracker.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    // Permission types
    public static int PRIVATE = 0;
    public static int WORLD_READABLE = 1;
    public static int WORLD_WRITABLE = 2;
    public static int MULTI_PROCESS = 3;

    // Activity tracker permission keys and some static values (for switches)
    public static String USER_UNIT= "user_units";
    public static String USER_UNIT_METRIC = "Metric Units";
    public static String USER_UNIT_IMPERIAL = "Imperial Units";

    public static String USER_GENDER = "user_gender";
    public static String USER_GENDER_MALE = "Male";
    public static String USER_GENDER_FEMALE = "Female";

    public static String USER_AGE = "user_age";
    public static String USER_HEIGHT_METRIC = "user_height_metric";
    public static String USER_HEIGHT_IMPERIAL_FT = "user_height_imperial_ft";
    public static String USER_HEIGHT_IMPERIAL_IN = "user_height_imperial_in";
    public static String USER_WEIGHT_METRIC = "user_weight_metric";
    public static String USER_WEIGHT_IMPERIAL = "user_weight_imperial";
    public static String USER_MAX_HEART_RATE = "user_max_heart_rate";
    public static String USER_MIN_HEART_RATE = "user_min_heart_rate";

    public static String USER_BMI_UNDERWEIGHT = "Underweight!";
    public static String USER_BMI_NORMAL = "Normal!";
    public static String USER_BMI_OVERWEIGHT = "Overweight!";
    public static String USER_BMI_OBESE = "Obese!";

    public static String USER_EMERGENCY_NUMBER = "user_emergency_number";

    public static String LAST_EMERGENCY_TIME = "last_emergency_time";

    public static final String SHARED_PREFS = "shared_prefs";   // File name
    SharedPreferences shared_preferences;
    SharedPreferences.Editor editor;

    // Context of Activity calling the prefs
    Context context;

    /*
    Description:    Constructor of the Shared Preferences Helper Class.
    Parameters:     Context of the Activity calling the operations.
    */
    public SharedPrefsHelper(Context context){
        this.context = context;
        shared_preferences = context.getSharedPreferences(SHARED_PREFS, PRIVATE);
    }

    /*
    Description:    Returns updated shared preferences object.
    */
    private SharedPreferences getSharedPreferences(){
        if(shared_preferences == null) {
            shared_preferences = context.getSharedPreferences(SHARED_PREFS, PRIVATE);
        }
        return shared_preferences;
    }

    /*
    Description:    Returns updated shared preferences editor object.
    */
    private SharedPreferences.Editor getEditor(){
        if(editor == null) {
            editor = getSharedPreferences().edit();
        }
        return editor;
    }

    /*
    Description:    Enters boolean into shared preferences.
    Parameters:     String key of entry,
                    boolean to enter.
    */
    public void putBoolean(String key, boolean bool){
        getEditor().putBoolean(key, bool).commit();
    }

    /*
    Description:    Returns boolean from shared preferences.
    Parameters:     String key of entry,
                    boolean default value if key doesn't exist.
    */
    public boolean getBoolean(String key, boolean defVal){
        return getSharedPreferences().getBoolean(key, defVal);
    }

    /*
    Description:    Enters long into shared preferences.
    Parameters:     String key of entry,
                    long to enter.
    */
    public void putLong(String key, long lon){
        getEditor().putLong(key, lon).commit();
    }

    /*
    Description:    Returns long from shared preferences
    Parameters:     String key of entry,
                    long default value if key doesn't exist.
    */
    public long getLong(String key, long defVal){
        return getSharedPreferences().getLong(key, defVal);
    }

    /*
    Description:    Enters int into shared preferences.
    Parameters:     String key of entry,
                    int to enter.
    */
    public void putInt(String key, int val){
        getEditor().putInt(key, val).commit();
    }

    /*
    Description:    Returns int from shared preferences
    Parameters:     String key of entry,
                    int default value if key doesn't exist.
    */
    public int getInt(String key, int defVal){
        return getSharedPreferences().getInt(key, defVal);
    }

    /*
    Description:    Enters double into shared preferences.
    Parameters:     String key of entry,
                    double to enter.
    */
    public void putDouble(String key, double val) {
        getEditor().putLong(key, Double.doubleToRawLongBits(val)).commit();
    }

    /*
    Description:    Returns double from shared preferences
    Parameters:     String key of entry,
                    double default value if key doesn't exist.
    */
    public double getDouble(String key, double defVal) {
        return Double.longBitsToDouble(getSharedPreferences().getLong(key, Double.doubleToLongBits(defVal)));
    }

    /*
    Description:    Enters String into shared preferences.
    Parameters:     String key of entry,
                    String to enter.
    */
    public void putString(String key, String val){
        getEditor().putString(key, val).commit();
    }

    /*
    Description:    Returns String from shared preferences
    Parameters:     String key of entry,
                    String default value if key doesn't exist.
    */
    public String getString(String key, String defVal){
        return getSharedPreferences().getString(key, defVal);
    }

    /*
    Description:    Enters float into shared preferences.
    Parameters:     String key of entry,
                    float to enter.
    */
    public void putFloat(String key, float val){
        getEditor().putFloat(key, val).commit();
    }

    /*
    Description:    Returns float from shared preferences
    Parameters:     String key of entry,
                    float default value if key doesn't exist.
    */
    public float getFloat(String key, float defVal){
        return getSharedPreferences().getFloat(key, defVal);
    }

    /*
    Description:    Removes key from shared preferences
    */
    public boolean removeKey(String key){
        getSharedPreferences().edit().remove(key).commit();
        return true;
    }
}
