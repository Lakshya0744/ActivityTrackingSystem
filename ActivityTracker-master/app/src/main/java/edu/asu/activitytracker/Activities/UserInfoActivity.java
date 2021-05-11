package edu.asu.activitytracker.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import com.google.android.material.textfield.TextInputLayout;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;

public class UserInfoActivity extends Activity implements View.OnClickListener{

    private SharedPrefsHelper sharedPrefs;
    private Switch s_units, s_gender;
    private EditText et_age, et_height_m, et_height_i_ft, et_height_i_in, et_weight_m, et_weight_i, et_min_hr, et_max_hr, et_emergency;
    private TextInputLayout ti_height_m, ti_height_i_ft, ti_height_i_in, ti_weight_m, ti_weight_i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initializeUserInfo();
    }

    private void initializeUserInfo() {
        // Init. shared prefs.
        sharedPrefs = new SharedPrefsHelper(this);

        // Init. Switches/Buttons and add listeners
        s_units = (findViewById(R.id.user_units));
        s_units.setOnClickListener(this);
        s_gender = (findViewById(R.id.user_gender));
        s_gender.setOnClickListener(this);
        Button b_save = (findViewById(R.id.save_info));
        b_save.setOnClickListener(this);

        // Init. EditText fields
        et_age = (findViewById(R.id.user_age));
        et_height_m = (findViewById(R.id.user_height_metric));
        et_height_i_ft = (findViewById(R.id.user_height_imperial_ft));
        et_height_i_in = (findViewById(R.id.user_height_imperial_in));
        et_weight_m = (findViewById(R.id.user_weight_metric));
        et_weight_i = (findViewById(R.id.user_weight_imperial));
        et_min_hr = (findViewById(R.id.user_min_heart_rate));
        et_max_hr = (findViewById(R.id.user_max_heart_rate));
        et_emergency = (findViewById(R.id.user_emergency_number));

        // Init. TextInputLayout
        ti_height_m = (findViewById(R.id.user_height_metric_layout));
        ti_height_i_ft = (findViewById(R.id.user_height_imperial_ft_layout));
        ti_height_i_in = (findViewById(R.id.user_height_imperial_in_layout));
        ti_weight_m = (findViewById(R.id.user_weight_metric_layout));
        ti_weight_i = (findViewById(R.id.user_weight_imperial_layout));

        // Retrieve and populate saved values, or set their default
        s_units.setChecked(sharedPrefs.getString(SharedPrefsHelper.USER_UNIT, SharedPrefsHelper.USER_UNIT_METRIC).equals(SharedPrefsHelper.USER_UNIT_METRIC));
        s_gender.setChecked(sharedPrefs.getString(SharedPrefsHelper.USER_GENDER, SharedPrefsHelper.USER_GENDER_MALE).equals(SharedPrefsHelper.USER_GENDER_MALE));
        et_age.setText(sharedPrefs.getString(SharedPrefsHelper.USER_AGE, ""));
        et_height_m.setText(sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_METRIC, ""));
        et_height_i_ft.setText(sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, ""));
        et_height_i_in.setText(sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_IN, ""));
        et_weight_m.setText(sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_METRIC, ""));
        et_weight_i.setText(sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_IMPERIAL, ""));
        et_min_hr.setText(sharedPrefs.getString(SharedPrefsHelper.USER_MIN_HEART_RATE, ""));
        et_max_hr.setText(sharedPrefs.getString(SharedPrefsHelper.USER_MAX_HEART_RATE, ""));
        et_emergency.setText(sharedPrefs.getString(SharedPrefsHelper.USER_EMERGENCY_NUMBER, ""));

        // Hide fields depending on unit type
        if (s_units.isChecked()) {
            s_units.setText(SharedPrefsHelper.USER_UNIT_METRIC);
            ti_height_i_ft.setVisibility(View.GONE);
            ti_height_i_in.setVisibility(View.GONE);
            ti_weight_i.setVisibility(View.GONE);
        } else {
            s_units.setText(SharedPrefsHelper.USER_UNIT_IMPERIAL);
            ti_height_m.setVisibility(View.GONE);
            ti_weight_m.setVisibility(View.GONE);
        }

        // Set gender field text based on toggle value
        if (s_gender.isChecked()) {
            s_gender.setText(SharedPrefsHelper.USER_GENDER_MALE);
        } else {
            s_gender.setText(SharedPrefsHelper.USER_GENDER_FEMALE);
        }

        // Set max heart rate (if empty) based on age
        et_age.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    String maxHRStr = sharedPrefs.getString(SharedPrefsHelper.USER_MAX_HEART_RATE, "");
                    String age = et_age.getText().toString();
                    int maxHR = "".equals(maxHRStr) ? 0 : Integer.parseInt(maxHRStr);
                    if (!"".equals(age) && maxHR == 0) {
                        maxHR = 220 - Integer.valueOf(age);
                        et_max_hr.setText(String.valueOf(maxHR));
                    }
                }
            }
        });
    }

    // Handle clicks on Switches/Buttons
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.user_units:
                // Show/Hide fields depending on the unit type
                if (s_units.isChecked()) {
                    s_units.setText(SharedPrefsHelper.USER_UNIT_METRIC);
                    ti_height_m.setVisibility(View.VISIBLE);
                    ti_weight_m.setVisibility(View.VISIBLE);
                    ti_height_i_ft.setVisibility(View.GONE);
                    ti_height_i_in.setVisibility(View.GONE);
                    ti_weight_i.setVisibility(View.GONE);
                } else {
                    s_units.setText(SharedPrefsHelper.USER_UNIT_IMPERIAL);
                    ti_height_m.setVisibility(View.GONE);
                    ti_weight_m.setVisibility(View.GONE);
                    ti_height_i_ft.setVisibility(View.VISIBLE);
                    ti_height_i_in.setVisibility(View.VISIBLE);
                    ti_weight_i.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.user_gender:
                // Update Switch text based on value
                if (s_gender.isChecked()) {
                    s_gender.setText(SharedPrefsHelper.USER_GENDER_MALE);
                } else {
                    s_gender.setText(SharedPrefsHelper.USER_GENDER_FEMALE);
                }
                break;
            case R.id.save_info:
                // Save user info
                sharedPrefs.putString(SharedPrefsHelper.USER_UNIT, s_units.isChecked() ? SharedPrefsHelper.USER_UNIT_METRIC : SharedPrefsHelper.USER_UNIT_IMPERIAL);
                sharedPrefs.putString(SharedPrefsHelper.USER_GENDER, s_gender.isChecked() ? SharedPrefsHelper.USER_GENDER_MALE : SharedPrefsHelper.USER_GENDER_FEMALE);
                sharedPrefs.putString(SharedPrefsHelper.USER_AGE, et_age.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_HEIGHT_METRIC, et_height_m.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, et_height_i_ft.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_IN, et_height_i_in.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_WEIGHT_METRIC, et_weight_m.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_WEIGHT_IMPERIAL, et_weight_i.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_MIN_HEART_RATE, et_min_hr.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_MAX_HEART_RATE, et_max_hr.getText().toString());
                sharedPrefs.putString(SharedPrefsHelper.USER_EMERGENCY_NUMBER, et_emergency.getText().toString());
                Utility.toast(getApplicationContext(), "Saved!");
                this.finish();
                break;
        }
    }
}
