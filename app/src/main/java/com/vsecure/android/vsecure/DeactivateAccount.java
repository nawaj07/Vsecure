package com.vsecure.android.vsecure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DeactivateAccount extends Fragment implements View.OnClickListener {

    EditText deleteEmail,deletePwd;
    Button btnDeactivate;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.deactivate_account, container, false);

        deleteEmail = (EditText)view.findViewById(R.id.dmail);
        deletePwd = (EditText)view.findViewById(R.id.dpwd);
        btnDeactivate = (Button)view.findViewById(R.id.btnDeactivate);
        pd = new ProgressDialog(getContext());
        btnDeactivate.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Deactivate Account");
    }

    public void deactivateAccount(){
        String Emaildelete = deleteEmail.getText().toString().trim();
        String Pseddelete = deletePwd.getText().toString().trim();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        pd.setMessage("Your account is being deleted...please wait");
        pd.show();
        if (Emaildelete.equals(userEmail)){
            AuthCredential credential = EmailAuthProvider.getCredential(userEmail,Pseddelete);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                pd.dismiss();
                                Toast.makeText(getContext(),"Account deleted successfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getContext(),MainActivity.class));
                            }
                        }
                    });

                }
            });
        }else {
            pd.dismiss();
            Toast.makeText(getContext(),"Authentication Failed. Input correct info...",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onClick(View v) {

        if (v==btnDeactivate){
            deactivateAccount();
        }
    }
}
