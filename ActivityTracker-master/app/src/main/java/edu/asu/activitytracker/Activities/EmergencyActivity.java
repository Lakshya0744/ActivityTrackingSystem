package edu.asu.activitytracker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.WindowManager;
import edu.asu.activitytracker.Helpers.SharedPrefsHelper;
import edu.asu.activitytracker.Helpers.Utility;
import edu.asu.activitytracker.R;

public class EmergencyActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener{

    // Duration of countdown prompt
    public static long COUNTDOWN = 10000; // 10000 milliseconds = 10 seconds
    Vibrator vibrator;
    Thread vibrate;
    DelayedConfirmationView mDelayedConfirmationView;
    private boolean CANCEL_TIMER = false;
    SharedPrefsHelper sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.emergency_countdown);
        sharedPrefs = new SharedPrefsHelper(this);

        // Create the confirmation/prompt view and its listener
        vibrate = createVibrateThread();
        mDelayedConfirmationView = findViewById(R.id.delayed_confirm);
        mDelayedConfirmationView.setListener(this);
        mDelayedConfirmationView.setTotalTimeMs(COUNTDOWN);
        mDelayedConfirmationView.start();
        vibrate.start();
    }

    @Override
    public void onTimerFinished(View view){
        // User didn't cancel prompt, call emergency number
        if (!CANCEL_TIMER) {
            String emergencyNumber = sharedPrefs.getString(SharedPrefsHelper.USER_EMERGENCY_NUMBER, "");
            Intent intent;
            if (this.checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                intent = new Intent(Intent.ACTION_CALL);
            } else {
                intent = new Intent(Intent.ACTION_DIAL);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:" + emergencyNumber));

            if (!PhoneNumberUtils.isGlobalPhoneNumber(emergencyNumber) || "".equals(emergencyNumber.trim())) {
                Utility.toast(getApplicationContext(), "Invalid #!");
                this.finish();
            } else {
                this.startActivity(intent);
            }
        }
    }

    @Override
    public void onTimerSelected(View view){
        CANCEL_TIMER = true;
        // User canceled, abort the calling emergency number and display cancelled animation
        Intent intent = new Intent(this, CustomConfirmationActivity.class);
        intent.putExtra(CustomConfirmationActivity.EXTRA_ANIMATION_TYPE, CustomConfirmationActivity.CANCEL_ANIMATION);
        intent.putExtra(CustomConfirmationActivity.EXTRA_MESSAGE, "Canceled!");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(vibrator != null){
            vibrator.cancel();
        }
        CANCEL_TIMER = true;
        finish();
    }

    private Thread createVibrateThread() {
        // Vibration pattern during prompt
        return new Thread(new Runnable() {
            @Override
            public void run() {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 100, 100, 100, 700,
                        0, 0, 0, 1000,
                        100, 100, 100, 700,
                        0, 0, 0, 1000,
                        100, 100, 100, 700,
                        0, 0, 0, 1000,
                        100, 100, 100, 700,
                        0, 0, 0, 1000,
                        100, 100, 100, 700};

                //-1 means don't repeat
                final int indexInPatternToRepeat = -1;
                vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
            }
        });
    }
}


