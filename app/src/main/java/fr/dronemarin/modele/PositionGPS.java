
package fr.dronemarin.modele;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class PositionGPS extends AsyncTask<String, String, String>{
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
            String minLati=lati.substring(lati.length()-5);
            float minLat=Float.parseFloat(minLati)/60;
            latitude = Float.parseFloat(lat)+minLat;



            String lon = parts[3];
            String longi = lon.substring(0, lon.length() - 5);
            String minLongi=lon.substring(lon.length()-5);
            float minLon=Float.parseFloat(minLongi)/60;

            longitude = Float.parseFloat(longi)+minLon;

            Log.d("position","Lat: "+this.latitude+" Lon: "+this.longitude);
            String dirNS = parts[2];

            String dirEO = parts[4];

            if(dirNS.equals("S") )
            {
                latitude=-latitude;
            }
            if(dirEO.equals("W"))
            {
                longitude=-longitude;
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
        return  this.latitude;
    }


    public Boolean getTrameGPS()

    {
        return this.trameGPS;
    }


    protected Object doInBackground(String objet) {
        Socket socket;
        PrintStream theOutputStream;

        try {

            InetAddress serveur = InetAddress.getByName("127.0.0.1");
            socket = new Socket(serveur, 1130);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            boolean ok=true;
            PositionGPS test;
            int i=0;
            while(ok)
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

        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
