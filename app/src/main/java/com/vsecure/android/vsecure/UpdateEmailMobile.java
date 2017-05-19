package com.vsecure.android.vsecure;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UpdateEmailMobile extends Fragment implements View.OnClickListener {

    EditText oldemailid,newEmailId,pswd;
    Button updateEmail;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.update_email, container, false);

        oldemailid = (EditText)view.findViewById(R.id.oldEmail);
        newEmailId = (EditText)view.findViewById(R.id.newEmail);
        updateEmail = (Button)view.findViewById(R.id.updateEmail);
        pswd = (EditText)view.findViewById(R.id.pwd);
        pd = new ProgressDialog(getContext());
            updateEmail.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Manage Acounts");
    }


    public void updateEmail(){
        String email = oldemailid.getText().toString().trim();
        final String newEmail = newEmailId.getText().toString().trim();
        String password = pswd.getText().toString().trim();
        user= FirebaseAuth.getInstance().getCurrentUser();
        pd.setMessage("Your Email is being updated\nPlease Wait...");
        pd.show();
        if (email.equals(user.getEmail())){
            final String getemail = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(getemail,password);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "Email Id changed", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getContext(),LoginActivity.class));
                            } else {
                                pd.dismiss();
                                Toast.makeText(getContext(), "Email Update failed...try again later", Toast.LENGTH_LONG).show();
                                oldemailid.setText("");
                                newEmailId.setText("");
                                pswd.setText("");
                            }
                    }
            });
                }
            });
        }else {
            pd.dismiss();
            Toast.makeText(getContext(),"Wrong email id..please enter correct one",Toast.LENGTH_LONG).show();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

    }
    @Override
    public void onClick(View v) {

        if (v==updateEmail){
            updateEmail();
        }

    }
}
