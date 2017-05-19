package com.vsecure.android.vsecure;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class ServiceClass extends Service {

    private static final int INTERVAL = 3000; // poll every 3 secs
    private static final String YOUR_APP_PACKAGE_NAME = "com.example.ashutosh.backgroundsms";

    private static boolean stopTask;
    //private PowerManager.WakeLock mWakeLock;
   public static String TAG="";
   
    Timer timer;
    @Override
    public void onCreate(){
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "service running in background");
            }
        },1000,1000);

    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            //sendSms();
        } else {
            Toast.makeText(this,"Service working",Toast.LENGTH_LONG).show();
            // YOUR CODE
        }
        return super.onStartCommand(intent,flags,startId);
    }


    public void sendSms(){

        String num = "7978175168";
        String message = "Hello this is demo Message on Power Click service running in background";
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num,null,message,null,null);
            Toast.makeText(this,"SMS send successfully",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this,"SMS sending failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy(){
        stopTask = true;
        timer.cancel();
        Log.d(TAG, "onDestroy() called with: Service " + "");
        super.onDestroy();
    }
}

