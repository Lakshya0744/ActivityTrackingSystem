package edu.asu.activitytracker.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;

public class HeartRateActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    int SENSOR_SPEED;
    Sensor heart_rate_sensor;
    SharedPrefsHelper sharedPrefs;
    TextView tv_hr_val1, tv_bmp;
    private long MIN_TIME_BETWEEN_EVENTS = 10000; // To prevent multiple emergency events
    private long lastEmergencyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        // Init variables and sensor
        sharedPrefs = new SharedPrefsHelper(this);
        tv_hr_val1 = (findViewById(R.id.hr_val1));
        tv_bmp = (findViewById(R.id.bmp));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heart_rate_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        lastEmergencyTime = sharedPrefs.getLong(SharedPrefsHelper.LAST_EMERGENCY_TIME, 0);

        // Fastest sensor speed since we need to show heart rate as soon as possible
        SENSOR_SPEED = SensorManager.SENSOR_DELAY_FASTEST;

        registerSensor();
    }

    @SuppressLint("SetTextI18n")
    private void registerSensor() {
        // Invoke emergency mode if in simulator, based on sensor availability (for demo purposes)
        if (heart_rate_sensor == null) {
            tv_bmp.setText("Sensor NULL!");
            Utility.delay(2, new Utility.DelayCallback() {
                @Override
                public void afterDelay() {
                    lastEmergencyTime = sharedPrefs.getLong(SharedPrefsHelper.LAST_EMERGENCY_TIME, 0);
                    if ((System.currentTimeMillis() - lastEmergencyTime) > MIN_TIME_BETWEEN_EVENTS) {
                        sharedPrefs.putLong(SharedPrefsHelper.LAST_EMERGENCY_TIME, System.currentTimeMillis());
                        startEmergency();
                    }
                }
            });
        } else { // Otherwise, register sensor
            sensorManager.registerListener(this, heart_rate_sensor, SENSOR_SPEED);
        }
    }

    @SuppressLint("SetTextI18n")
    private void startEmergency() {
        // Start emergency activity, if heart rate reflects that
        tv_bmp.setText("EMERGENCY!");
        try {
            startActivity(new Intent(HeartRateActivity.this, EmergencyActivity.class));
            tv_bmp.setText("");
        } catch (ActivityNotFoundException e){
            Utility.toast(getApplicationContext(), "Error!");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // onSensorChanged responds to all sensors, but we only care about heart rate here
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            // Get min and max heart to determine emergency range, and set colors
            float min = Float.valueOf(sharedPrefs.getString(SharedPrefsHelper.USER_MIN_HEART_RATE, "0.0"));
            float max = Float.valueOf(sharedPrefs.getString(SharedPrefsHelper.USER_MAX_HEART_RATE, "0.0"));

            // Get last recorded emergency event
            lastEmergencyTime = sharedPrefs.getLong(SharedPrefsHelper.LAST_EMERGENCY_TIME, 0);

            // Get sensor readings
            float[] hr = event.values;
            for (float v : hr) {

                // Display sensor readings
                tv_hr_val1.setText(String.valueOf(v));

                // Take action if heart rate is below min or above max
                if (v != 0.0 && (v < min || v > max) && (System.currentTimeMillis() - lastEmergencyTime) > MIN_TIME_BETWEEN_EVENTS) {
                    sharedPrefs.putLong(SharedPrefsHelper.LAST_EMERGENCY_TIME, System.currentTimeMillis());
                    startEmergency();
                } else {
                    tv_bmp.setText("bpm");
                }
                setColors(min, max, v);
            }
        }
    }

    // Set heart rate colors based on 5 ranges:
    // Below min
    // Above min, lower 1/3
    // Middle range (middle 1/3)
    // Below max, upper 1/3
    // Above max
    private void setColors(float min, float max, float v) {
        float diff = (max - min)/3;

        if (v < min) { // Below lower bound
            tv_hr_val1.setTextColor(Color.rgb(219, 68, 55));
            tv_bmp.setTextColor(Color.rgb(219, 68, 55));
        } else if (v > min && v < min+diff) { // Bottom third
            tv_hr_val1.setTextColor(Color.rgb(244,180,0));
            tv_bmp.setTextColor(Color.rgb(244,180,0));
        } else if (v > min+diff && v < min+diff+diff) { // Middle
            tv_hr_val1.setTextColor(Color.rgb(15, 157, 88));
            tv_bmp.setTextColor(Color.rgb(15, 157, 88));
        } else if (v < max && v > max-diff) { // Upper third
            tv_hr_val1.setTextColor(Color.rgb(244,180,0));
            tv_bmp.setTextColor(Color.rgb(244,180,0));
        } else if (v > max) { // Above upper bound
            tv_hr_val1.setTextColor(Color.rgb(219, 68, 55));
            tv_bmp.setTextColor(Color.rgb(219, 68, 55));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
