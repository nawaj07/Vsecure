package com.vsecure.android.vsecure;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


public class Settings extends ListFragment implements AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsatanceState){
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInsatanceState){
        super.onActivityCreated(savedInsatanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.SettingsMenu,android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fragment f= null;
        if (position == 0) {
            f= new EditProfile();
        }
        if (position == 1) {
            f = new ChangPassword();}
        if (position == 2) {
            f = new UpdateEmailMobile();
        }
        if (position == 3) {
            f= new DeactivateAccount();
        }

        if (f!=null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame1,f).commit();
        }


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        drawar = (DrawerLayout) drawar.findViewById(R.id.drawer_layout);
        drawar.closeDrawer(GravityCompat.START);
        return false;
    }
}
