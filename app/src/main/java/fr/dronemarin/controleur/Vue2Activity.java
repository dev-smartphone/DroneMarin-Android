package fr.dronemarin.controleur;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.dronemarin.R;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

public class Vue2Activity extends FragmentActivity implements Serializable, OnMapReadyCallback, WaypointDialog.WaypointsDialogListener {

    private GoogleMap mMap;
    private LatLng current;
    private Polyline currentRoad;
    private Map<Waypoint,Marker> markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        markers = new HashMap<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       findViewById(R.id.buttonJson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    parseJson ();
                } catch (IOException e) {
                    e.printStackTrace ( );
                }
            }
        });

        findViewById(R.id.buttonNmea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lireJSON ();
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

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                try{
                    Waypoint w = Modele.getInstance().getWaypoints().get(Integer.parseInt(marker.getTitle()));
                    Location loc = new Location("DroneMarinProvider");
                    loc.setLatitude(marker.getPosition().latitude);
                    loc.setLongitude(marker.getPosition().longitude);
                    w.setLocation(loc);
                    refreshWithExisting();

                }
                catch (Exception ignored){}

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

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

        Marker m = mMap.addMarker(new MarkerOptions().draggable(true).snippet("Vitesse : " + speedInt + ", Photo : " + (picture.isChecked() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (stat.isChecked() ? "Oui" : "Non")).position(current).title( ""+Modele.getInstance().getWaypoints().size()));
        Waypoint current = new Waypoint(speedInt,picture.isChecked(),stat.isChecked(),loc);
        markers.put(current,m);
        Waypoint previous = Modele.getInstance().addWaypoint(current);
        refreshWithExisting();
        /*if(previous!=null){
            mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLocation().getLatitude(),previous.getLocation().getLongitude()))
                    .add(new LatLng(current.getLocation().getLatitude(),current.getLocation().getLongitude())).width(5).color(Color.RED));
        }*/
    }

    @Override
    public void onDialogNegativeClick(DialogInterface dialog) {

    }

    public void refreshWithExisting(){
        if(current!=null){
            currentRoad.remove();
        }

        List<Waypoint> existingWaypoints = Modele.getInstance().getWaypoints();
        List<LatLng> coords = new ArrayList<>();
        for (int i =0; i<Modele.getInstance().getWaypoints().size();i++){

            //mMap.addMarker(new MarkerOptions().draggable(true).snippet("Vitesse : " + existingWaypoints.get(i).getVitesse() + ", Photo : " + (existingWaypoints.get(i).isPriseImage() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (existingWaypoints.get(i).isPointStationnaire() ? "Oui" : "Non")).position(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).title(""+ i));
            coords.add(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude()));
            if(markers.get(existingWaypoints.get(i)) == null){
                Marker m = mMap.addMarker(new MarkerOptions().draggable(true).snippet("Vitesse : " + existingWaypoints.get(i).getVitesse() + ", Photo : " + (existingWaypoints.get(i).isPriseImage() ? "Oui" : "Non") + ", " + "Point stationnaire : " + (existingWaypoints.get(i).isPriseImage() ? "Oui" : "Non")).position(new LatLng(existingWaypoints.get(i).getLocation().getLatitude(),existingWaypoints.get(i).getLocation().getLongitude())).title( ""+Modele.getInstance().getWaypoints().size()));
                markers.put(existingWaypoints.get(i),m);
            }
            markers.get(existingWaypoints.get(i)).setTitle(i+"");
        }


        currentRoad = mMap.addPolyline(new PolylineOptions().addAll(coords).width(5).color(Color.RED));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    @SuppressLint("WorldWriteableFiles")
    public void parseJson() throws IOException {

        JSONObject json = new JSONObject ( );
        JSONArray jsonWay = new JSONArray ( );
        List<Waypoint> waypoints = Modele.getInstance ( ).getWaypoints ( );
        int cpt = 1;
        for (Waypoint w : waypoints) {
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
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Android/data/"+getPackageName ()+"/file");
        dir.mkdirs();
        File file = new File(dir, "waypoints.json");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write (json.toString ());
        writer.flush ();
        if(writer!= null)
            writer.close();
    }

    public Map<Waypoint, Marker> getMarkers() {
        return markers;
    }


    public void lireJSON()
    {
        String ret="";
        try {
            InputStream inputStream = openFileInput("waypoints.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        Log.i("test",ret);
    }
    public GoogleMap getmMap() {
        return mMap;
    }
}
