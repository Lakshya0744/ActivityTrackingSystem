package edu.asu.activitytracker.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;

public class StepsActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    int SENSOR_SPEED;
    Sensor steps_sensor;
    SharedPrefsHelper sharedPrefs;
    TextView tv_steps_val, tv_distance_val, tv_distance_unit;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Init variables and sensor
        sharedPrefs = new SharedPrefsHelper(this);
        tv_steps_val = (findViewById(R.id.steps_val));
        tv_distance_val = (findViewById(R.id.distance_val));
        tv_distance_unit = (findViewById(R.id.distance_unit));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        steps_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Fastest sensor speed since we need to show steps rate as soon as possible
        SENSOR_SPEED = SensorManager.SENSOR_DELAY_FASTEST;

        // Register sensor
        registerSensor();
    }

    @SuppressLint("SetTextI18n")
    private void registerSensor() {
        // Show dummy steps number when sensor isn't available (for demo purposes)
        if (steps_sensor == null) {
            tv_steps_val.setText("598");
            calcDistance(598);
        } else {
            sensorManager.registerListener(this, steps_sensor, SENSOR_SPEED);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Only act when sensor type is step counter
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Get raw sensor values
            float[] hr = event.values;
            for (float v : hr) {
                // Show values on UI
                int steps = (int) v;
                tv_steps_val.setText(String.valueOf(steps));

                // Calculate distance based on number of steps
                calcDistance(steps);
            }
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void calcDistance(int steps) {
        boolean metric = sharedPrefs.getString(SharedPrefsHelper.USER_UNIT, SharedPrefsHelper.USER_UNIT_METRIC).equals(SharedPrefsHelper.USER_UNIT_METRIC);
        String height = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_METRIC, "");
        String heightFt = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, "");
        String heightIn = sharedPrefs.getString(SharedPrefsHelper.USER_HEIGHT_IMPERIAL_FT, "");
        double heightInInches = 0.0;

        // Calculate number of steps based on unit and height (stride based)
        if (metric) {
            if ("".equals(height)) {
                Utility.toast(getApplicationContext(), "Fill Height!");
                this.finish();
            } else {
                heightInInches = Long.valueOf(height)/2.54;
            }
        } else {
            if ("".equals(heightFt) || "".equals(heightIn)) {
                Utility.toast(getApplicationContext(), "Fill Height!");
                this.finish();
            } else {
                heightInInches = (Long.valueOf(heightFt) * 12) + Long.valueOf(heightIn);
            }
        }
        double stride = heightInInches * .43;
        double distanceInInch = stride * steps;
        double distanceInFt = distanceInInch / 12;
        double distanceInMiles = distanceInFt / 5280;

        if (metric) {
            double distanceInKm = distanceInMiles * 1.609;
            tv_distance_val.setText(String.format("%.2f", distanceInKm));
            tv_distance_unit.setText("estimated km");
        } else {
            tv_distance_val.setText(String.format("%.2f", distanceInMiles));
            tv_distance_unit.setText("estimated mi");
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
