package com.vsecure.android.vsecure;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.activity_list_item;

public class ContactList extends ListFragment {

    RegistrationAdapter adapter_ob;
    RegistrationOpenHelper helper_ob;
    SQLiteDatabase db_ob;
    ListView mylist;
    Button add;
    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsatanceState){
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInsatanceState){
        super.onActivityCreated(savedInsatanceState);

        adapter_ob = new RegistrationAdapter(this.getContext());
        String[] from = { helper_ob.FNAME, helper_ob.LNAME };
        int[] to = { R.id.name, R.id.phone };
        cursor = adapter_ob.queryName();
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this.getContext(),R.layout.list_column,cursor,from,to);
        setListAdapter(cursorAdapter);
    }
}
