package com.vsecure.android.vsecure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,pasword;
    TextView tvsingnUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        progressDialog = new ProgressDialog(this);
        login = (Button)findViewById(R.id.btnlogin);
        email = (EditText)findViewById(R.id.edemail);
        pasword=(EditText)findViewById(R.id.edpassword);
        tvsingnUp=(TextView)findViewById(R.id.tvlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stremail = email.getText().toString().trim();
                String strpwd = pasword.getText().toString().trim();
                /*if (TextUtils.isEmpty(stremail)){
                    Toast.makeText(MainActivity.this,"Please enter Email First",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!validateEmail(stremail)){
                    email.setError("Not a Valid Email Id");
                }
                    else if (strpwd.length()<6){
                    pasword.setError("Password too short");
                }
                if (TextUtils.isEmpty(strpwd)){
                    Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                }*/
                progressDialog.setMessage("You are being registered please wait");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(stremail,strpwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Your are Registered Successfully",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),HomePage.class));
                        }else {
                            Toast.makeText(MainActivity.this,"Login failed, Please try again later",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
             }
        });

        tvsingnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}
