package com.example.mymaptracklocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    double LO;
    double LA;
    private GoogleMap mMap;
    public static String TAG = "Location-Services-App-Tag";
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 2 sec */

    String locationName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("address");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        LO = bundle.getDouble("LON");
        LA = bundle.getDouble("LAT");

        //Toast.makeText(MapActivity.this,"welcome to myMap ",Toast.LENGTH_SHORT).show();

        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addInfoWindowToMap();

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                updateLocation();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                displayPermissionDeniedToast();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
            }
        }).check();

        mMap.setOnInfoWindowClickListener(this);

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        createLocationRequest();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallbackObject(), Looper.myLooper());
    }

    private LocationCallback locationCallbackObject() {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                MapActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Making sure updating the map on main thread.
                        updateMap(constructLatLngObject(locationResult));
                    }
                });
            }
        };

        return locationCallback;
    }

    private void createLocationRequest() {
        // Create the location request to start receiving updates
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    private LatLng constructLatLngObject(LocationResult locationResult) {

        double latitude = LA;
        double longitude = LO;
        return new LatLng(longitude, latitude); //******************
    }

    private void updateMap(final LatLng location) {
        mMap.clear();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                locationName = listAddresses.get(0).getAddressLine(0);

                mMap.addMarker(new MarkerOptions().position(location).title("Your Current location").snippet("latitude : " + LA + "  longitude : " + LO + "\n" + locationName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                //ขนาดการ Zoom
                float zoomLevel = 17.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayPermissionDeniedToast() {
        Toast toast = Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT);
        toast.show();
    }


    private void addInfoWindowToMap() {

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("lati", LA);
        i.putExtra("longti", LO);
        startActivity(i);

    }


}
