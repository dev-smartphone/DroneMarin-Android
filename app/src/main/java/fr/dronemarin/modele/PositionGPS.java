
package fr.dronemarin.modele;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class PositionGPS {
    private float longitude;
    private float latitude;

    private boolean trameGPS;
    private String tr;

    public PositionGPS(String trame) {
        tr = trame;
        String[] parts = tr.split(",");

        if (parts[0].compareTo("$GPGLL") == 0) {
            trameGPS = true;
            String lati = parts[1];
            String lat = lati.substring(0, lati.length() - 5);
            String minLati = lati.substring(lati.length() - 5);
            float minLat = Float.parseFloat(minLati) / 60;
            latitude = Float.parseFloat(lat) + minLat;


            String lon = parts[3];
            String longi = lon.substring(0, lon.length() - 5);
            String minLongi = lon.substring(lon.length() - 5);
            float minLon = Float.parseFloat(minLongi) / 60;

            longitude = Float.parseFloat(longi) + minLon;

            Log.d("position", "Lat: " + this.latitude + " Lon: " + this.longitude);
            String dirNS = parts[2];

            String dirEO = parts[4];

            if (dirNS.equals("S")) {
                latitude = -latitude;
            }
            if (dirEO.equals("W")) {
                longitude = -longitude;
            }

        } else {
            trameGPS = false;
            latitude = 0;
            longitude = 0;

        }

    }

    public float getLongitude()

    {
        return this.longitude;
    }


    public float getLatitude()

    {
        return this.latitude;
    }


    public Boolean getTrameGPS()

    {
        return this.trameGPS;
    }
}

