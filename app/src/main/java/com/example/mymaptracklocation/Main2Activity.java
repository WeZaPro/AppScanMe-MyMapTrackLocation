package com.example.mymaptracklocation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class Main2Activity extends AppCompatActivity {

    double lati, longti;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = getIntent().getExtras();
        lati = bundle.getDouble("lati");
        longti = bundle.getDouble("longti");
        final LatLng currentLocation = new LatLng(longti, lati);

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);

        streetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                // Only set the panorama to SYDNEY on startup (when no panoramas have been
                // loaded which is when the savedInstanceState is null).
                if (savedInstanceState == null) {
                    panorama.setPosition(currentLocation);
                }
            }
        });
    }
}
