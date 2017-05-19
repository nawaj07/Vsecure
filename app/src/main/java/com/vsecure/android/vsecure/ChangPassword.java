package com.vsecure.android.vsecure;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangPassword extends Fragment implements View.OnClickListener {


    EditText oldPassword, newPassword, cnfrmNew;
    Button changePassword;
    LoginActivity loginActivity;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.change_password, container, false);

        pd = new ProgressDialog(getContext());
        oldPassword  = (EditText)view.findViewById(R.id.oldPwd);
        newPassword  = (EditText)view.findViewById(R.id.newPwd);
        cnfrmNew  = (EditText)view.findViewById(R.id.cnfrmNewPwd);
        changePassword=(Button)view.findViewById(R.id.changePwd);
        changePassword.setOnClickListener(this);

            return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Change Password");
    }

    public void changePassword(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String pwdOld = oldPassword.getText().toString();
        final String pwdNew = newPassword.getText().toString();
        String pwdCnfrm = cnfrmNew.getText().toString();

        pd.setMessage("Your password is being changed\nPlease wait...");
        pd.show();
        if (pwdCnfrm.equals(pwdNew)) {
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, pwdOld);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(pwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Password Changed Succesfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(),LoginActivity.class));
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Password change failed...try again", Toast.LENGTH_LONG).show();
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    cnfrmNew.setText("");
                                }
                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                        oldPassword.setText("");
                        newPassword.setText("");
                        cnfrmNew.setText("");

                    }
                }
            });
        }else {
            pd.dismiss();
            Toast.makeText(getContext(),"Password doesnot match",Toast.LENGTH_LONG).show();
        }

       /* if (pwdOld==loginActivity.editTextPwd.getText().toString()){
            if (pwdNew==pwdCnfrm){

                user.updatePassword(pwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"Password Changed succesfully",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }}*/
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();



    }
    @Override
    public void onClick(View v) {

        if (v==changePassword){
            changePassword();
        }
    }
}
