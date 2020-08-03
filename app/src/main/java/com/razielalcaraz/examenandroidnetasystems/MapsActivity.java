package com.razielalcaraz.examenandroidnetasystems;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
String TAG = "Mapador";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int este = MisColaboradores.idEste;
        String idEste = String.valueOf(MisColaboradores.ids.get(String.valueOf(este)));
    Log.v(TAG,"Id este: "+idEste);
    Log.v(TAG,"Abrirtodos?"+MisColaboradores.abrirTodos);
        // Add a marker in Sydney and move the camera
        if(MisColaboradores.abrirTodos){
            for ( String key : MisColaboradores.todos.keySet() ) {
                Log.v(TAG,"Llave::: "+ key );
                if(key.contains("loc")){
                   Log.v(TAG, "Key :  :::"+  MisColaboradores.todos.get(key));
                    Map lugar= (Map) MisColaboradores.todos.get(key);

                    LatLng sydney = new LatLng(Double.valueOf((String) lugar.get("lat")), Double.valueOf((String) lugar.get("log")));
                    Log.v(TAG,"Punto: "+sydney.toString());
                   String nombrezote = (String) MisColaboradores.todos.get(key.replace("loc","nam"));
                    String detalles = (String) MisColaboradores.todos.get(key.replace("loc","det"));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombrezote).snippet(detalles));
                    Log.v(TAG, "Nombrezote :::"+  nombrezote);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        }else {
            for ( String key : MisColaboradores.todos.keySet() ) {
                Log.v(TAG,key+"=="+String.valueOf(idEste) + "-loc");
                if (key.equals(idEste + "-loc")) {
                    Log.v(TAG,"SI");
                    Log.v(TAG,key + ":" + MisColaboradores.todos.get(key));
                    Map lugar = (Map) MisColaboradores.todos.get(key);

                    LatLng sydney = new LatLng(Double.valueOf((String) lugar.get("lat")), Double.valueOf((String) lugar.get("log")));
                    Log.v(TAG,"Punto: "+sydney.toString());
                    String nombrezote = (String) MisColaboradores.todos.get(idEste.concat("-nam"));
                    Log.v(TAG, "Nombrezote :::"+  nombrezote);
                    String detalles = (String) MisColaboradores.todos.get(key.replace("loc","det"));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombrezote).snippet(detalles));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
                }else{
                    Log.v(TAG,"NO");
                }
            }
/*
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */
        }

    }
}