package fr.dronemarin.controleur;

import android.graphics.Color;
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
    private ArrayList<LatLng> points;
    private boolean reception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        reception=false;





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
    public void onMapReady(final GoogleMap googleMap) {

                try {
                    mMap = googleMap;
                    //client = new Client("172.20.10.5", 55555);
//                    Log.d("", "Connection au serveur:" + client.getSocket().getInetAddress());
  //                  client.execute(mMap);
                    this.lancerServeur();
                    dessinerTrame();

                    // client.start(mMap);
                    /*for(int i=0;i<client.getDrone().getPosition().size();i++)
                    {
                        // Add a marker in Sydney and move the camera
                        LatLng l = new LatLng(client.getDrone().getPosition().get(i).getLatitude(), client.getDrone().getPosition().get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(l).title("Marker"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
                    }*/

                } catch (Exception e) {
                    Log.d("", "Erreur");

                    e.printStackTrace();
                }





        /*while(true){
            if(client != null){
                if(client.getDrone()!=null){
                    if(!client.getDrone().getPosition().isEmpty()){
                        LatLng l = new LatLng(client.getDrone().getPosition().get(0).getLatitude(), client.getDrone().getPosition().get(0).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(l).title("Marker"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
                        client.getDrone().getPosition().remove(client.getDrone().getPosition().get(0));
                    }
                }
            }
        }*/




        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void lancerServeur() throws IOException {
        this.client = new Client("172.20.10.5", 55555,this);
        client.execute();
        Log.d("", "Connection au serveur:" + client.getSocket().getInetAddress());

    }



    private void dessinerTrame()
    {
        PolylineOptions trajectoire = new PolylineOptions()
                .addAll(this.points)
                .width(15).color(Color.RED);

        // Get back the mutable Polyline
        if(this.mMap != null && this.points.size() > 1){
            Polyline polyline =  this.mMap.addPolyline(trajectoire);
        }
    }


    public void actualiserDessin(LatLng pt) {
        Log.d("vals : ", pt.latitude+", "+pt.longitude+", size : "+this.points.size());
        this.points.add(pt);
        dessinerTrame();
    }

    public void ajouterPoint(LatLng p)
    {
        this.points.add(p);
    }
    public boolean getReception()
    {
        return  this.reception;
    }



}
