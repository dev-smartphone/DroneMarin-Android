package fr.dronemarin.controleur;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import fr.dronemarin.R;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

public class Vue2Activity extends FragmentActivity implements Serializable, OnMapReadyCallback, WaypointDialog.WaypointsDialogListener {

    private GoogleMap mMap;
    private LatLng current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* findViewById(R.id.buttonWay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vue2Activity.this, WaypointManagerActivity.class);
                startActivityForResult(intent,1);
            }
        });*/





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
                WaypointDialog currentDialog= new WaypointDialog();
                currentDialog.show(getSupportFragmentManager(),"waypoint");

            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                WaypointDialog currentDialog = new WaypointDialog();
                Bundle b = new Bundle();
                b.putSerializable("waypoint", Modele.getInstance().getWaypoints().get(Integer.parseInt(marker.getTitle())));
                b.putSerializable("activity",Vue2Activity.this);
                currentDialog.setArguments(b);
                currentDialog.show(getSupportFragmentManager(),"waypoint");
                return false;
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

        mMap.addMarker(new MarkerOptions().snippet("Vitesse : " + speedInt + ", Photo : " + (picture.isChecked() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (stat.isChecked() ? "Oui" : "Non")).position(current).title( ""+Modele.getInstance().getWaypoints().size())).showInfoWindow();
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
            mMap.addMarker(new MarkerOptions().snippet("Vitesse : " + existingWaypoints.get(i).getVitesse() + ", Photo : " + (existingWaypoints.get(i).isPriseImage() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (existingWaypoints.get(i).isPointStationnaire() ? "Oui" : "Non")).position(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).title(""+ i)).showInfoWindow();
            if(i>0)
            {
                mMap.addPolyline(new PolylineOptions().add(new LatLng(existingWaypoints.get(i-1).getLocation().getLatitude(),existingWaypoints.get(i-1).getLocation().getLongitude()))
                        .add(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).width(5).color(Color.RED));
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mMap.clear();
        refreshWithExisting();
        super.onActivityResult(requestCode, resultCode, data);
    }


    public double[] DecimalToNMEAConverter(double lat, double lng) {
        String dcmLatString;
        int latDegree;
        double latMinutes, dcmLat, absLat;

        String dcmLngString;
        int lngDegree;
        double lngMinutes, dcmLng, absLng;

        Log.i("Decimal to NMEA", "Decimal To NMEA: " + lat + " Longitude: "  + lng);
        if (lat < 0) {
            absLat = lat * -1;
        } else {
            absLat = lat;
        }

        latDegree = (int) absLat;
        latMinutes = (absLat - latDegree) * 60;
        dcmLatString = String.valueOf(latDegree)
                + String.valueOf(latMinutes);
        dcmLat = Double.parseDouble(dcmLatString);
        dcmLat = Math.round(dcmLat * 10000.0) / 10000.0;

        if (lat < 0) {
            dcmLat *= -1;
        }
        // ************************************************//
        if (lng < 0) {
            absLng = lng * -1;
        } else {
            absLng = lng;
        }

        lngDegree = (int) absLng;
        lngMinutes = (absLng - lngDegree) * 60;
        dcmLngString = String.valueOf(lngDegree)
                + String.valueOf(lngMinutes);
        dcmLng = Double.parseDouble(dcmLngString);
        dcmLng = Math.round(dcmLng * 10000.0) / 10000.0;

        if (lng < 0) {
            dcmLng *= -1;
        }
        Log.i("Decimal to NMEA", "NMEA Lat: " + dcmLat + " Lng: " + dcmLng);
        return (new double[] { dcmLat, dcmLng });
    }
    public void parseJson() {

        JSONObject json = new JSONObject ( );
        JSONArray jsonWay = new JSONArray ( );
        List<Waypoint> waypoints = Modele.getInstance ( ).getWaypoints ( );
        int cpt = 1;
        for (Waypoint w : waypoints) {
            Log.i ("mdr", Double.toString (DecimalToNMEAConverter (w.getLocation ( ).getLatitude ( ), w.getLocation ( ).getLongitude ( ))[0]));
            JSONObject json1 = new JSONObject ( );
            try {
                json1.put ("latitude", w.getLocation ( ).getLatitude ( ));
                json1.put ("longitude", w.getLocation ( ).getLongitude ( ));
                json1.put ("vitesse", w.getVitesse ( ));
                json1.put ("point Stationnaire", w.isPointStationnaire ( ));
                json1.put ("prise image", w.isPriseImage ( ));
                jsonWay.put (json1);
            } catch (JSONException e) {
                e.printStackTrace ( );
            }
            cpt++;
        }
        try {
            json.put ("waypoints", jsonWay);
        } catch (JSONException e) {
            e.printStackTrace ( );
        }
        // Log.i("test",json.toString ());
    }

    public GoogleMap getmMap() {
        return mMap;
    }
}
