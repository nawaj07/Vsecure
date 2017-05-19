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


public class AddContact extends Fragment {
    RegistrationAdapter adapter;
    RegistrationOpenHelper helper;
    EditText fnameEdit, relationedit, phoneedit ;
    Button submitBtn, resetBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.contact, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        //getActivity().setTitle("Add contact");
        fnameEdit = (EditText) view.findViewById(R.id.ctName);
        relationedit = (EditText) view.findViewById(R.id.ct_Relation);
        phoneedit = (EditText) view.findViewById(R.id.ctPhone);
        submitBtn = (Button) view.findViewById(R.id.ctBtnAdd);
         adapter = new RegistrationAdapter(this.getContext());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fnameValue = fnameEdit.getText().toString();
                String relationValue = relationedit.getText().toString();
                String phoneValue = phoneedit.getText().toString();
                if (TextUtils.isEmpty(fnameValue)){
                    Toast.makeText(getContext(),"Enter Name", Toast.LENGTH_LONG).show();
                    return;
                }
                long val = adapter.insertDetails(fnameValue, relationValue);
                if (val<=3){
                    Toast.makeText(getContext(),"Maximum 3 contacts can be added",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), Long.toString(val)+"contact added",Toast.LENGTH_LONG).show();
                //finish();
                fnameEdit.setText("");
                relationedit.setText("");
                phoneedit.setText("");
            }
        });




    }



}
