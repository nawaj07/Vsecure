package com.vsecure.android.vsecure;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vsecure.android.vsecure.RegistrationAdapter;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.R.attr.fragment;

public class MapsActivity extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private static final String TAG = "Location Address";
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    Fragment fragment;
    double latitude, longitude;
    Marker marker;
    LocationRequest locationRequest;
    Button sendfiresms, policehelp, ambulancehelp;
    TextView tvtopaddress;
    String local = "";
    SupportMapFragment supportMapFragment;
    RegistrationOpenHelper registrationOpenHelper;
    RegistrationAdapter registrationAdapter;
    public String number;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_home_page, null, false);
        view.setClickable(true);
        sendfiresms = (Button) view.findViewById(R.id.btnfire);
        policehelp = (Button) view.findViewById(R.id.btnhelp);
        ambulancehelp = (Button) view.findViewById(R.id.btnambulance);
        tvtopaddress = (TextView) view.findViewById(R.id.tvtop);

        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapview);
        supportMapFragment.getMapAsync(this);

        sendfiresms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                makeCall();
                /*    Fragment f = new FireHelp();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame1,f).commit();
                */
            }
        });
        policehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                makeCall();

           /*  Fragment f = new PoliceHelp();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame1,f).commit();
*/
            }
        });
        ambulancehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                makeCall();
            }
        });
//        onMapReady(mMap);
        return view;
    }
    public void sendSms(){
        registrationAdapter = new RegistrationAdapter(this.getContext());
        Cursor cr = registrationAdapter.queryName();
        if (cr!=null && cr.getCount()>0){
            cr.moveToFirst();
            do {
                String number = cr.getString(cr.getColumnIndex(registrationOpenHelper.LNAME));
                String message = "My Locatio : "+local +"\n"+"Please Help me";

                //String message = "Hello this is demo Message on Power Click service running in background";
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Toast.makeText(this.getContext(), "SMS send successfully to  :   " + number, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this.getContext(), "SMS sending failed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            while (cr.moveToNext());
        }
    }
    public void makeCall(){
        number = null;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }


    public void getLocal() {
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addressList = gc.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                    local = sb.toString();
                    tvtopaddress.setText(local);
                }
            /*    sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName()).append("\n");
                local = sb.toString();
                tvaddress.setText(local);   */
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to connect to Geocoder", e);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onLocationChanged(Location location) {

        mLocation = location;
        if (marker != null) {
            marker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your are Here");

        marker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        getLocal();
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    public void onRequestPermissionResult(int requestcode, String permissions[], int[] grantresults) {
        switch (requestcode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantresults.length > 0 && grantresults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getLocation();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    public void onDestroy(){

        try {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(supportMapFragment).commit();

        }catch (Exception e){

        }
        super.onDestroy();
    }
}