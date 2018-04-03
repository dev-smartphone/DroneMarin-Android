package fr.dronemarin.controleur;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import fr.dronemarin.R;
import fr.dronemarin.modele.Client;
import fr.dronemarin.modele.PositionGPS;

public class Vue1Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Client("172.20.10.5", 55555);
                    Log.d("", "Connection au serveur:" + client.getSocket().getInetAddress());
                    client.start(mMap);




                } catch (Exception e) {
                    Log.d("", "Erreur");

                    e.printStackTrace();
                }
            }
        }).start();




/*

        Socket socket;
        PrintStream theOutputStream;


        //new PositionGPS().execute();

        try {
            InetAddress serveur = InetAddress.getByName("127.0.0.1");
            socket = new Socket(serveur, 8080);

            Log.d("test","test1");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Log.d("Verif", "in: "+in.readLine());
            PrintStream out = new PrintStream(socket.getOutputStream());
            PositionGPS test;

            int i=0;
            while(true)
            {
                if(i!=0)
                {
                    test=new PositionGPS(in.readLine());
                    Log.d("GetTest", "lat = "+test.getLatitude());
                    if(test.getTrameGPS())
                    {

                        float lon=test.getLongitude();
                        float lat=test.getLatitude();

                        Log.i("test", "Lat: "+lat+" Lon: "+lon);
                    }
                }
                i++;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
*/




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

        for(int i=0;i<client.getDrone().getPosition().size();i++)
        {
            // Add a marker in Sydney and move the camera
            LatLng l = new LatLng(client.getDrone().getPosition().get(i).getLatitude(), client.getDrone().getPosition().get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(l).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        }


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }




}
