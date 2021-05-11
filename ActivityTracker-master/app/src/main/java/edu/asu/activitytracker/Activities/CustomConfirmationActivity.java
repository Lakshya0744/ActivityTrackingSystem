package edu.asu.activitytracker.Activities;

import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.ActionLabel;
import android.support.wearable.view.ActionPage;
import android.util.Log;
import edu.asu.activitytracker.R;

public class CustomConfirmationActivity extends Activity {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ANIMATION_TYPE = "animation_type";
    public static final int SUCCESS_ANIMATION = 1;
    public static final int OPEN_ON_PHONE_ANIMATION = 2;
    public static final int CANCEL_ANIMATION = 3;
    public static final int FAILURE_ANIMATION = 4;
    private final Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();

        // Setting up animation types and messages
        int animationType = intent.getIntExtra("animation_type", 1);
        String message = intent.getStringExtra("message");
        ActionPage mActionPage = new ActionPage(this);
        long displayDurationMs;
        if(animationType == FAILURE_ANIMATION) {
            this.setContentView(R.layout.emergency_countdown);
            displayDurationMs = 2000L;
        } else {
            mActionPage.setColor(0);
            mActionPage.setStateListAnimator(new StateListAnimator());
            mActionPage.setImageScaleMode(ActionPage.SCALE_MODE_CENTER);
            this.setContentView(mActionPage);
            if(message != null) {
                mActionPage.setText(message);
            }

            Drawable animatedDrawable1;

            // Show animation based on intent call
            switch(animationType) {
                // Shows a check mark to display success
                case SUCCESS_ANIMATION:
                    animatedDrawable1 = this.getDrawable(android.support.wearable.R.drawable.generic_confirmation_animation);
                    displayDurationMs = 1666L;
                    Log.e("CONFIRMED", "CONFIRMED");
                    break;
                // Shows an arrow going to phone
                case OPEN_ON_PHONE_ANIMATION:
                    animatedDrawable1 = this.getDrawable(android.support.wearable.R.drawable.open_on_phone_animation);
                    displayDurationMs = 1666L;
                    Log.e("OPEN_ON_PHONE", "OPEN_ON_PHONE");
                    break;
                // Shows a red box confirming cancellation
                case CANCEL_ANIMATION:
                    animatedDrawable1 = this.getDrawable(R.drawable.cancel_animation);
                    Log.e("CANCELLED", "CANCELLED");
                    displayDurationMs = 1666L;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type of animation: " + animationType);
            }

            // Setting various animation variables
            mActionPage.setImageDrawable(animatedDrawable1);
            final ActionLabel label = mActionPage.getLabel();
            long fadeDuration = label.animate().getDuration();
            final long fadeOutDelay = Math.max(0L, displayDurationMs - 2L * (50L + fadeDuration));
            ((Animatable)animatedDrawable1).start();
            label.setAlpha(0.0F);
            label.animate().alpha(1.0F).setStartDelay(50L).withEndAction(new Runnable() {
                public void run() {
                    label.animate().alpha(0.0F).setStartDelay(fadeOutDelay);
                }
            });
        }

        // Make sure screen stays on during animation
        mActionPage.setKeepScreenOn(true);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                CustomConfirmationActivity.this.finish();
                CustomConfirmationActivity.this.overridePendingTransition(0, 17432577);
            }
        }, displayDurationMs);
    }
}
