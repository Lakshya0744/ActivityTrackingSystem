package edu.asu.activitytracker.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;
import edu.asu.activitytracker.R;

public class BodyFatActivity extends Activity {

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat);

        // Init. variables
        Activity activity = this;
        SharedPrefsHelper sharedPrefs = new SharedPrefsHelper(this);
        TextView tv_body_fat_val = (findViewById(R.id.body_fat_val));

        // Calculate BMI values, by calling the BMI function
        double bmi = BMIActivity.calcBMI(getApplicationContext(), activity);
        double bodyFat;

        // Get user gender and age
        boolean male = sharedPrefs.getString(SharedPrefsHelper.USER_GENDER, SharedPrefsHelper.USER_GENDER_MALE).equals(SharedPrefsHelper.USER_GENDER_MALE);
        String age = sharedPrefs.getString(SharedPrefsHelper.USER_AGE, "");

        // Check user gender, to determine body fat calculation
        if (male) {
            bodyFat = (1.20 * bmi) + (0.23 * Long.valueOf(age)) - 16.2;
        } else {
            bodyFat = (1.20 * bmi) + (0.23 * Long.valueOf(age)) - 5.4;
        }

        // Display the calculated body fat percentage
        tv_body_fat_val.setText(String.format("%.1f", bodyFat) + "%");
    }
}
