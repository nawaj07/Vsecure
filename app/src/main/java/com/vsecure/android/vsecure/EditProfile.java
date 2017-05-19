package com.vsecure.android.vsecure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditProfile extends Fragment{

    ProfileAdapter adapter;
    ProfileOpenHelper helper;
    EditText pfname, plname, pdob, pemail, pphone;
    Button psubmitbtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_profile, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        //getActivity().setTitle("Add contact");
        pfname = (EditText) view.findViewById(R.id.profilefname);
        plname = (EditText) view.findViewById(R.id.profilelname);
        pdob = (EditText) view.findViewById(R.id.profiledob);
        pemail = (EditText) view.findViewById(R.id.profileemail);
        pphone = (EditText) view.findViewById(R.id.profilephn);
        psubmitbtn = (Button) view.findViewById(R.id.pbtnsubmit);
        adapter = new ProfileAdapter(this.getContext());

        psubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fnameValue = pfname.getText().toString();
                String lnameValue = plname.getText().toString();
                String dobValue = pdob.getText().toString();
                String emailValue = pemail.getText().toString();
                String phoneValue = pphone.getText().toString();
                if (TextUtils.isEmpty(fnameValue)){
                    Toast.makeText(getContext(),"Enter First Name", Toast.LENGTH_LONG).show();
                    return;
                }if (TextUtils.isEmpty(lnameValue)){
                    Toast.makeText(getContext(),"Enter Last Name", Toast.LENGTH_LONG).show();
                    return;
                }if (TextUtils.isEmpty(dobValue)){
                    Toast.makeText(getContext(),"Enter DOB", Toast.LENGTH_LONG).show();
                    return;
                }if (TextUtils.isEmpty(emailValue)){
                    Toast.makeText(getContext(),"Enter EMail Address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneValue)){
                    Toast.makeText(getContext(),"Enter Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }
                long val1 = adapter.insertProfileDetails(fnameValue,lnameValue,dobValue,emailValue,phoneValue);

                Toast.makeText(getContext(), Long.toString(val1)+"Details added",Toast.LENGTH_LONG).show();
                //finish();
                pfname.setText("");
                plname.setText("");
                pdob.setText("");
                pemail.setText("");
                pphone.setText("");
            }
        });




    }



}
