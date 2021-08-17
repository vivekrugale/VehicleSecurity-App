package com.example.edi6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView textViewTilt, textViewStatus, textViewSpeed, textViewDirection, textViewSatellites, textViewMotor;
    Button btnOn, btnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textViewTilt = findViewById(R.id.textViewTilt);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewDirection = findViewById(R.id.textViewDirection);
        textViewSatellites = findViewById(R.id.textViewSatellites);
        textViewMotor = findViewById(R.id.textViewMotor);
        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("actuation").setValue("ON");
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("actuation").setValue("OFF");
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference();

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tilt = snapshot.child("tilt").getValue().toString();
                String speed = snapshot.child("speed").getValue().toString();
                String direction = snapshot.child("direction").getValue().toString();
                String satellites = snapshot.child("satellites").getValue().toString();
                String motor = snapshot.child("actuation").getValue().toString();

                textViewTilt.setText(tilt);
                textViewSpeed.setText(speed);
                textViewDirection.setText(direction);
                textViewSatellites.setText(satellites);
                textViewMotor.setText(motor);

                if (Float.parseFloat(tilt) < -45.0){
                    textViewStatus.setText("Bike seems fallen on the ground !");
                }
                else if (Float.parseFloat(tilt) < 8.0 && Float.parseFloat(tilt) > -8.0){
                    textViewStatus.setText("Bike seems standing vertically");
                }
                else if (Float.parseFloat(tilt) < 50.0 && Float.parseFloat(tilt) > 40.0){
                    textViewStatus.setText("Bike seems to be on stand");
                }
                else if (Float.parseFloat(tilt) > 80.0){
                    textViewStatus.setText("Bike seems fallen on the ground");
                }
                else{
                    textViewStatus.setText("Bike is tilted");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initMap();
    }

    private void initMap()
    {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference();

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String latitude = snapshot.child("latitude").getValue().toString();
                String longitude = snapshot.child("longitude").getValue().toString();

                float lat = Float.parseFloat(latitude);
                float lan = Float.parseFloat(longitude);

                LatLng location = new LatLng(lat,lan);
                mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Add a marker in Sydney and move the camera
//        float lat = (float) 19.451324;
//        float lan = (float) 76.443228;
//        LatLng sydney = new LatLng(lat,lan);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}