package com.vsecure.android.vsecure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FireHelp extends Fragment {

    TextView location, msg, num;
    Button firesms, firecall;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.firehelp, container, false);

        location = (TextView)view.findViewById(R.id.tvfhloc);
        msg  = (TextView)view.findViewById(R.id.tvfirehelpmsg);
        num = (TextView)view.findViewById(R.id.tvfirehelpnum);
        firesms = (Button) view.findViewById(R.id.firehelpsms);
        firecall = (Button)view.findViewById(R.id.firehelpcall);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Manage Acounts");

        firesms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        firecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

    }
    public void sendSms(){

        String number = num.getText().toString();
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
        String number = "101";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }



}
