package fr.dronemarin.controleur;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import fr.dronemarin.R;
import fr.dronemarin.modele.Client;
import fr.dronemarin.modele.PositionGPS;

public class Vue1Activity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Client client;
    private  ArrayList<LatLng> points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.points=new ArrayList<>();

}

    @Override
    public void onMapReady(final GoogleMap googleMap) {

                try {
                    mMap = googleMap;
                    this.lancerServeur();

                } catch (Exception e) {
                    Log.d("", "Erreur");

                    e.printStackTrace();
                }

    }

    public void lancerServeur() throws IOException {
        this.client = new Client("172.20.10.5", 55555,this,mMap);

        client.execute();
        Log.d("", "Connection au serveur:" + client.getSocket().getInetAddress());


    }



    public void dessinerTrame()
    {

        Runnable r =new Runnable() {
            @Override
            public void run() {

                mMap.addMarker(new MarkerOptions().position(points.get(0)).title("Départ"));
              mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(points.size()-1)));
              // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(points.get(points.size()-1).latitude,points.get(points.size()-1).longitude),5));

                PolylineOptions trajectoire = new PolylineOptions()
                        .addAll(points)
                        .width(15).color(Color.RED);

                // Get back the mutable Polyline

                if(mMap != null && points.size() > 1){
                    Polyline polyline =  mMap.addPolyline(trajectoire);
                    Log.i("", "desssiner tram");
                }

                /*
            for(int i=0;i<points.size();i++)
            {

                mMap.addMarker(new MarkerOptions().position(points.get(i)).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(i)));
            }*/
            }
        };
        this.runOnUiThread(r);
    }

    public void ajouterPoint(LatLng p)
    {
        this.points.add(p);
    }

}
