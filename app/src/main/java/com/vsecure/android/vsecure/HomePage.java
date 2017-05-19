package com.vsecure.android.vsecure;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.support.v7.util.ThreadUtil;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView headerName,headerEmail;
    private NavigationView navigationView;
    private View navHeader;
    private FirebaseAuth firebaseAuth;
    DrawerLayout drawer;
    public static int navItemIndex=0;
    public boolean shouldLoadHomeonBackPress = true;
    private FragmentManager manager;
    LoginDatabaseAdapter dbadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver receiver = new ScreenReceiver();
        registerReceiver(receiver, intentFilter);
        Toast.makeText(this, "Receiver is registered", Toast.LENGTH_LONG).show();

        headerEmail = (TextView)findViewById(R.id.textViewEmail);
        headerName =(TextView)findViewById(R.id.textViewName);


        startService(new Intent(this,ServiceClass.class));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreeen(R.id.nav_home);
    }

    private static Boolean IS_MAIN_SHOWN = false;

    @Override
    public void onBackPressed() {
       if (manager.getBackStackEntryCount() !=0 ) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
            if (IS_MAIN_SHOWN){
                finish();
            }else {
                displaySelectedScreeen(0);
            }

        } else {

            super.onBackPressed();
        }
 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreeen(item.getItemId());

        /*        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
  */
        return true;
    }
    private void displaySelectedScreeen(int itemId){
        Fragment f= null;
        switch (itemId){
            case R.id.nav_home:
                f = new MapsActivity();
                break;
            case R.id.nav_myProfile:
                f = new DisplayProfile();
                break;
            case R.id.nav_editProfile:
                f = new EditProfile();
                break;
            case R.id.nav_contactList:
                f= new ContactList();
                break;
            case R.id.nav_manageContact:
                f = new AddContact();
                break;
            case R.id.nav_setting:
                f = new Settings();
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                drawer.closeDrawers();
                return;
            default:
                navItemIndex=0;
        }
        if (f!=null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame1, f).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void getFirebaseData() {
        DatabaseReference databaseReference;
        final String[] data = new String[2];
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://b-secure.firebaseio.com");
        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserInformation userInformation = dataSnapshot1.getValue(UserInformation.class);
                    data[0] = "Welcome " + userInformation.getName();
                    // + "\tRelation:" + userInformation.getRelation() + "\tPhone:" + userInformation.getPhone();

                    //data[1] = "Relation" + userInformation.getRelation();
                   // data[0] = userInformation.getPhone();

                    headerName.setText(data[1]);
                    //tvuser3.setText(data[2]);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
