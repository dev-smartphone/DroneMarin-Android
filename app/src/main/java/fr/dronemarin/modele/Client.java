package fr.dronemarin.modele;

/**
 * Created by fatima/Caroline on 26/03/2018.
 */


import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client extends AsyncTask<GoogleMap,Void,Void>{
    private Socket socket;
    private Drone drone;
    private String adresse;
    private int port;

    public Client(String adresse, int port) throws IOException {
        this.drone = new Drone();
        this.adresse = adresse;
        this.port = port;
    }

    public void start(GoogleMap map) throws IOException {

    }

    public Socket getSocket()
    {
        return  this.socket;
    }



    public Drone getDrone()
    {
        return this.drone;
    }




    @Override
    protected Void doInBackground(GoogleMap... googleMaps) {

        String ligne;
        try{
            this.socket = new Socket();
            this.socket.setSoTimeout(2000);
            this.socket.connect(new InetSocketAddress(adresse, port),2000);
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (!in.ready()) {}
                ligne = in.readLine();
                ligne=in.readLine();
                //Log.i("ligne", "ligne: "+ ligne);
                PositionGPS pos = new PositionGPS(ligne);
                if(pos.getTrameGPS()) {
                    drone.addPositionGPS(pos);
                    addMarker(googleMaps[0],pos);
                    Log.i("", "latitude: " + pos.getLatitude() + " longitude: " + pos.getLongitude());

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void addMarker(GoogleMap googleMap, PositionGPS pos){
        LatLng l = new LatLng(pos.getLatitude(), pos.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(l).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(l));
    }
}