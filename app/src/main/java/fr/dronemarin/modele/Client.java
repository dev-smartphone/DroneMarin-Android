package fr.dronemarin.modele;

/**
 * Created by fatima/Caroline on 26/03/2018.
 */


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    private Socket socket;
    private Drone drone;

    public Client(String adresse, int port) throws IOException {
        this.socket = new Socket();
        this.socket.setSoTimeout(2000);
        this.socket.connect(new InetSocketAddress(adresse, port),2000);
        this.drone = new Drone();
    }

    public void start() throws IOException {
        String ligne;
        while (true) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!in.ready()) {}
            ligne = in.readLine();
            Log.i("ligne", "ligne: "+ligne);
            PositionGPS pos = new PositionGPS(ligne);
            if(pos.getTrameGPS())
                Log.d("","latitude: " + pos.getLatitude() + " longitude: " +pos.getLongitude());

            drone.addPositionGPS(pos);
        }
    }

    public Socket getSocket()
    {
        return  this.socket;
    }

    }
