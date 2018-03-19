package fr.dronemarin;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import fr.dronemarin.controleur.WaypointDialog;
import fr.dronemarin.controleur.WaypointManagerActivity;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

public class WaypointActivity extends FragmentActivity implements OnMapReadyCallback, WaypointDialog.WaypointsDialogListener {

    private GoogleMap mMap;
    private LatLng current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.buttonWay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WaypointActivity.this, WaypointManagerActivity.class);
                startActivityForResult(intent,1);
            }
        });





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
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.15,-1.17),12));


        refreshWithExisting();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                current = latLng;
                WaypointDialog waypointDialog = new WaypointDialog();
                waypointDialog.show(getSupportFragmentManager(),"waypoint");


            }
        });
    }


    @Override
    public void onDialogPositiveClick(DialogInterface dialog) {
        Location loc = new Location("DroneMarinProvider");
        loc.setLatitude(current.latitude);
        loc.setLongitude(current.longitude);
        EditText speed = ((AlertDialog) dialog).findViewById(R.id.editText2);
        CheckBox picture = ((AlertDialog) dialog).findViewById(R.id.checkBox);
        CheckBox stat = ((AlertDialog) dialog).findViewById(R.id.checkBox2);

        double speedInt =0;
        try{
           speedInt =  Double.parseDouble(speed.getText().toString());
        }
        catch (Exception ignored){}

        mMap.addMarker(new MarkerOptions().snippet("Vitesse : " + speedInt + ", Photo : " + (picture.isChecked() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (stat.isChecked() ? "Oui" : "Non")).position(current).title("Waypoint " + (Modele.getInstance().getWaypoints().size()+1)));
        Waypoint current = new Waypoint(speedInt,picture.isChecked(),stat.isChecked(),loc);


        Waypoint previous = Modele.getInstance().addWaypoint(current);
        if(previous!=null){
            mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLocation().getLatitude(),previous.getLocation().getLongitude()))
                    .add(new LatLng(current.getLocation().getLatitude(),current.getLocation().getLongitude())).width(5).color(Color.RED));
        }
    }

    @Override
    public void onDialogNegativeClick(DialogInterface dialog) {

    }

    public void refreshWithExisting(){
        List<Waypoint> existingWaypoints = Modele.getInstance().getWaypoints();
        for (int i =0; i<Modele.getInstance().getWaypoints().size();i++){
            mMap.addMarker(new MarkerOptions().snippet("Vitesse : " + existingWaypoints.get(i).getVitesse() + ", Photo : " + (existingWaypoints.get(i).isPriseImage() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (existingWaypoints.get(i).isPointStationnaire() ? "Oui" : "Non")).position(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).title("Waypoint " + (i+1)));
            if(i>0)
            {
                mMap.addPolyline(new PolylineOptions().add(new LatLng(existingWaypoints.get(i-1).getLocation().getLatitude(),existingWaypoints.get(i-1).getLocation().getLongitude()))
                        .add(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).width(5).color(Color.RED));
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("INFO","Refrshing the map");
        mMap.clear();
        refreshWithExisting();
        super.onActivityResult(requestCode, resultCode, data);
    }


}
