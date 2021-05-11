package edu.asu.activitytracker.Activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;
import java.util.ArrayList;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;

public class MainMenuActivity extends WearableActivity {

    // Permissions that need user approval
    private final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BODY_SENSORS,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        // Create curved list/main menu view
        WearableRecyclerView recyclerView = findViewById(R.id.main_menu_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        recyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        final SharedPrefsHelper sharedPrefs = new SharedPrefsHelper(this);

        // Prompts to ask user for all required permissions
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.heart_rate, " Heart Rate"));
        menuItems.add(new MenuItem(R.drawable.steps, " Steps"));
        menuItems.add(new MenuItem(R.drawable.bmi, " BMI"));
        menuItems.add(new MenuItem(R.drawable.body_fat, " Body Fat %"));
        menuItems.add(new MenuItem(R.drawable.user_info, " User Info"));

        recyclerView.setAdapter(new MainMenuAdapter(this, menuItems, new MainMenuAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(final Integer menuPosition) {
                // Get saved values to check which ones are empty and prevent user from opening activity, if required inputs are missing
                boolean metric = sharedPrefs.getString(SharedPrefsHelper.USER_UNIT, SharedPrefsHelper.USER_UNIT_METRIC).equals(SharedPrefsHelper.USER_UNIT_METRIC);
                String age = sharedPrefs.getString(SharedPrefsHelper.USER_AGE, "");
                String height = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_METRIC, "");
                String weight = sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_METRIC, "");
                String heightFt = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, "");
                String heightIn = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, "");
                String weightImperial = sharedPrefs.getString(SharedPrefsHelper.USER_WEIGHT_IMPERIAL, "");
                String minHR = sharedPrefs.getString(SharedPrefsHelper.USER_MIN_HEART_RATE, "");
                String maxHR = sharedPrefs.getString(SharedPrefsHelper.USER_MAX_HEART_RATE, "");
                boolean metricCheck = "".equals(height) || "".equals(weight);
                boolean imperialCheck = "".equals(heightFt) || "".equals(heightIn) || "".equals(weightImperial);

                // Open activities based on items in list
                switch (menuPosition) {
                    case 0: // Open heart rate activity
                        if ("".equals(minHR) || "".equals(maxHR)) {
                            Utility.toast(getApplicationContext(), "Fill User Info!");
                        } else {
                            try {
                                startActivity(new Intent(MainMenuActivity.this, HeartRateActivity.class));
                            } catch (ActivityNotFoundException e){
                                Utility.toast(getApplicationContext(), "Error!");
                            }
                        }
                        break;
                    case 1: // Open steps activity
                        try {
                            startActivity(new Intent(MainMenuActivity.this, StepsActivity.class));
                        } catch (ActivityNotFoundException e){
                            Utility.toast(getApplicationContext(), "Error!");
                        }
                        break;
                    case 2: // Open BMI activity
                        if (metric) {
                            if (metricCheck) {
                                Utility.toast(getApplicationContext(), "Fill Metric Info!");
                            } else {
                                launchBMI();
                            }
                        } else {
                            if (imperialCheck) {
                                Utility.toast(getApplicationContext(), "Fill Imperial Info!");
                            } else {
                                launchBMI();
                            }
                        }
                        break;
                    case 3: // Open Body Fat activity
                        if ("".equals(age)) {
                            Utility.toast(getApplicationContext(), "Fill Age!");
                        } else {
                            if (metric) {
                                if (metricCheck) {
                                    Utility.toast(getApplicationContext(), "Fill Metric Info!");
                                } else {
                                    launchBodyFat();
                                }
                            } else {
                                if (imperialCheck) {
                                    Utility.toast(getApplicationContext(), "Fill Imperial Info!");
                                } else {
                                    launchBodyFat();
                                }
                            }
                        }
                        break;
                    case 4: // Open User Info activity
                        try {
                            startActivity(new Intent(MainMenuActivity.this, UserInfoActivity.class));
                        } catch (ActivityNotFoundException e){
                            Utility.toast(getApplicationContext(), "Error!");
                        }
                        break;
                    default:
                }
            }
        }));
    }

    // Loop for showing all required activities, one after another
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    // Callback handling when user rejects permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Do nothing
            } else {
                Utility.toast(getApplicationContext(), "Required!");
                if(!hasPermissions(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        }
    }

    private void launchBMI() {
        try {
            startActivity(new Intent(MainMenuActivity.this, BMIActivity.class));
        } catch (ActivityNotFoundException e){
            Utility.toast(getApplicationContext(), "Error!");
        }
    }

    private void launchBodyFat() {
        try {
            startActivity(new Intent(MainMenuActivity.this, BodyFatActivity.class));
        } catch (ActivityNotFoundException e){
            Utility.toast(getApplicationContext(), "Error!");
        }
    }
}
