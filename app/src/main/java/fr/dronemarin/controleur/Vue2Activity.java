package fr.dronemarin.controleur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.dronemarin.R;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

public class Vue2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue2);
    }

    public double[] DecimalToNMEAConverter(double lat, double lng) {
        String dcmLatString = null;
        int latDegree = 0;
        double latMinutes = 0, dcmLat = 0, absLat = 0;

        String dcmLngString = null;
        int lngDegree = 0;
        double lngMinutes = 0, dcmLng = 0, absLng = 0;

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
}
