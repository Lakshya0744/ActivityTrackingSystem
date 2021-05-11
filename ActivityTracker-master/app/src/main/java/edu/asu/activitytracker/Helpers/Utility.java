package edu.asu.activitytracker.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class Utility {
    public interface DelayCallback{
        void afterDelay();
    }

    // Delay mechanism
    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    // Unified toast messages. Placed here to simplify changing toast options (colors, background, etc.)
    public static void toast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }
}