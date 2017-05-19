package com.vsecure.android.vsecure;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


public class DisplayProfile extends ListFragment {

    ProfileAdapter adapter_ob;
    ProfileOpenHelper helper_ob;
    SQLiteDatabase db_ob;
    TextView tv;
    Button add;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsatanceState){
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        tv = (TextView)view.findViewById(R.id.profileTextview);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInsatanceState){
        super.onActivityCreated(savedInsatanceState);


        adapter_ob = new ProfileAdapter(this.getContext());

        String[] from = { helper_ob.FNAME, helper_ob.LNAME, helper_ob.DOB, helper_ob.EMAIL, helper_ob.PHONE };
        int[] to = { R.id.name, R.id.phone };
        cursor = adapter_ob.queryName();
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this.getContext(),R.layout.list_column,cursor,from,to);
        setListAdapter(cursorAdapter);
    }

}
