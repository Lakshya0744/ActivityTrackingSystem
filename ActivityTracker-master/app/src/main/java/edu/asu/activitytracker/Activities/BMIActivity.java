package edu.asu.activitytracker.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;

public class BMIActivity extends Activity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        // Init. vars
        Activity activity = this;
        TextView tv_bmi_val = (findViewById(R.id.bmi_val));
        TextView tv_bmi_type = (findViewById(R.id.bmi_type));

        // Call the static calcBMI function (it's also used in BodyFat
        double bmi = calcBMI(getApplicationContext(), activity);

        // Show the BMI value on UI
        tv_bmi_val.setText(String.format("%.1f", bmi));

        // Color the UI based on BMI levels
        if (bmi < 18.5) {
            tv_bmi_type.setText(SharedPrefsHelper.USER_BMI_UNDERWEIGHT);
            tv_bmi_type.setTextColor(Color.rgb(244,180,0));
            tv_bmi_val.setTextColor(Color.rgb(244,180,0));
        } else if (bmi >= 18.5 && bmi < 25) {
            tv_bmi_type.setText(SharedPrefsHelper.USER_BMI_NORMAL);
            tv_bmi_type.setTextColor(Color.rgb(15, 157, 88));
            tv_bmi_val.setTextColor(Color.rgb(15, 157, 88));
        } else if (bmi >= 25 && bmi < 30) {
            tv_bmi_type.setText(SharedPrefsHelper.USER_BMI_OVERWEIGHT);
            tv_bmi_type.setTextColor(Color.rgb(244,180,0));
            tv_bmi_val.setTextColor(Color.rgb(244,180,0));
        } else if (bmi >= 30) {
            tv_bmi_type.setText(SharedPrefsHelper.USER_BMI_OBESE);
            tv_bmi_type.setTextColor(Color.rgb(219, 68, 55));
            tv_bmi_val.setTextColor(Color.rgb(219, 68, 55));
        }
    }

    // Calculate BMI based on unit type
    public static double calcBMI(Context context, Activity activity) {
        SharedPrefsHelper sharedPrefs = new SharedPrefsHelper(context);
        boolean metric = sharedPrefs.getString(SharedPrefsHelper.USER_UNIT, SharedPrefsHelper.USER_UNIT_METRIC).equals(SharedPrefsHelper.USER_UNIT_METRIC);
        String height = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_METRIC, "");
        String weight = sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_METRIC, "");
        String heightFt = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, "");
        String heightIn = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_IN, "");
        String weightImperial = sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_IMPERIAL, "");
        double bmi = 0.0;

        // Checking the unit type, and calculate BMI accordingly
        if (metric) {

            // Check if the required values to calculate BMI are filled out
            if ("".equals(height) || "".equals(weight)) {
                Utility.toast(context, "Fill Metric Info!");
                activity.finish();
            } else {
                bmi = Long.valueOf(weight) / Math.pow(Long.valueOf(height), 2);
                bmi = bmi * 10000.0;
            }
        } else {
            // Check if the required values to calculate BMI are filled out
            if ("".equals(heightFt) || "".equals(heightIn) || "".equals(weightImperial)) {
                Utility.toast(context, "Fill Imperial Info!");
                activity.finish();
            } else {
                double totalHeight = (Long.valueOf(heightFt) * 12) + Long.valueOf(heightIn);
                bmi = (Long.valueOf(weightImperial) / Math.pow(totalHeight, 2)) * 703;
            }
        }
        return bmi;
    }
}
