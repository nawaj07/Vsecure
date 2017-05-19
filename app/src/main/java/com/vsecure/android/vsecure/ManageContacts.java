package com.vsecure.android.vsecure;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ManageContacts extends Fragment implements View.OnClickListener {

    EditText edName,edRelation,edPhone,edId;
    TextView tvuser1,tvuser2,tvuser3;
    Button btnAdd;
    Cursor cr;
    private DatabaseReference databaseReference;

    public SQLiteDatabase db;
    private LoginDatabaseAdapter dbadapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.manage_contact, container, false);

        edId = (EditText)view.findViewById(R.id.contactId);
        edName = (EditText)view.findViewById(R.id.contactName);
        edRelation = (EditText)view.findViewById(R.id.contactRelation);
        edPhone = (EditText)view.findViewById(R.id.contactPhone);
        tvuser1 = (TextView)view.findViewById(R.id.user1);
        tvuser2 = (TextView)view.findViewById(R.id.user2);
        tvuser3 = (TextView)view.findViewById(R.id.user3);
        btnAdd = (Button)view.findViewById(R.id.btnaddcontact);

        dbadapter = new LoginDatabaseAdapter(this.getContext());
        List<UserInformation> userInformations = dbadapter.getAllContacts();
        for (UserInformation us : userInformations){
            String str = "Name"+us.getName()+"\nPhone:"+us.getPhone()+"\nRelation:"+us.getRelation();
            tvuser1.setText(str);
        }
        UserInformation use = dbadapter.getContact(2);
        String usr = use.getName();
        tvuser3.setText(usr);

        // getFirebaseData();

        btnAdd.setOnClickListener(this);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Manage Contacts");
    }

    public void addContact(){
      // String stringid = edId.getText().toString();
        //int id = Integer.parseInt(stringid);
        String name = edName.getText().toString();
        String phone = edPhone.getText().toString();
        String relation = edRelation.getText().toString();


        if (TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"Enter Name",Toast.LENGTH_LONG).show();
            return;
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(getContext(),"Enter Phone",Toast.LENGTH_LONG).show();
            return;
        }else if (TextUtils.isEmpty(relation)){
            Toast.makeText(getContext(),"Enter relation",Toast.LENGTH_LONG).show();
            return;
        }

        else {

        dbadapter.addContact(new UserInformation(name,phone,relation));
            Toast.makeText(getContext(),"Data added",Toast.LENGTH_LONG).show();
            //tvuser1.setText(dbadapter.getData().toString());
    }}

    @Override
    public void onClick(View v) {
        if (v==btnAdd){
            addContact();
        }
    }


    public void getFirebaseData(){
        final String[] data = new String[3];
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://b-secure.firebaseio.com");
        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserInformation userInformation = dataSnapshot1.getValue(UserInformation.class);
                    data[0] = "Welcome " + userInformation.getName()+ "\tRelation:" + userInformation.getRelation()+ "\tPhone:" + userInformation.getPhone();
                    ;
                    //data[1] = "Relation" + userInformation.getRelation();
                    //data[2] = "Phone"+userInformation.getPhone();
                    tvuser1.setText(data[0]);
                    //tvuser2.setText(data[1]);
                    //tvuser3.setText(data[2]);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed"+databaseError.getMessage());
            }
        });
        //return data[0];


    }
}