package com.vsecure.android.vsecure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PoliceHelp extends Fragment{

    TextView location, msg, num;
    Button sms, call;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.policehelp, container, false);

        location = (TextView)view.findViewById(R.id.tvfhloc);
        msg  = (TextView)view.findViewById(R.id.policehelpsms);
        //num = (TextView)view.findViewById(R.id.policehelpcall);
        sms = (Button) view.findViewById(R.id.policehelpsmsbtn);
        call = (Button)view.findViewById(R.id.policehelpcallbtn);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Police Help");

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }
    public void sendSms(){

        String number = "100";
        String message = msg.getText().toString();
        //String message = "Hello this is demo Message on Power Click service running in background";
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,message,null,null);
            Toast.makeText(this.getContext(),"SMS send successfully",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this.getContext(),"SMS sending failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void makeCall(){
        String number = "100";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }



}
