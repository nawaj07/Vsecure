package com.vsecure.android.vsecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ScreenReceiver extends BroadcastReceiver {

    private boolean wasscreenOn;

    final String[] data = new String[3];

    TextView tv;
    int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        for (int i = 0; i <= 2; i++) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("Check", "Screen went ON");

                if (i == 2) {
                    sendSms();
                   // Toast.makeText(context, "Power butoon is pressed", Toast.LENGTH_LONG).show();
                }



        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            //sendSms();
            wasscreenOn = true;
        }
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra("screen_state", wasscreenOn);
        context.startService(intent1);
    }}

    public void getFirebaseData() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://b-secure.firebaseio.com");
        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserInformation userInformation = dataSnapshot1.getValue(UserInformation.class);
                    //data[2] = "Welcome " + userInformation.getName() + "\tRelation:" + userInformation.getRelation() + "\tPhone:" + userInformation.getPhone();
                    ;
                    //data[1] = "Relation" + userInformation.getRelation();
                    data[2] = userInformation.getPhone();

                    //tvuser2.setText(data[1]);
                    //tvuser3.setText(data[2]);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendSms() {

        String num = "7725817823";
        String message = "Hello this is demo Message on Power Click Broadcast running in background";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, message, null, null);
            //Toast.makeText(,"SMS send successfully",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(this,"SMS sending failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
