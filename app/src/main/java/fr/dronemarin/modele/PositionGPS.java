
package fr.dronemarin.modele;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macos
 */

public class PositionGPS {
    //private PositionGPS util;
    //private SerialGPS gps;
    private float longitude;
    private float latitude;
    private float minLat;
    private float minLon;
    private String dirNS;
    private String dirEO;
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
            minLat=Float.parseFloat(minLati)/60;
            latitude = Float.parseFloat(lat)+minLat;



            String lon = parts[3];
            String longi = lon.substring(0, lon.length() - 5);
            String minLongi=lon.substring(lon.length()-5);
            minLon=Float.parseFloat(minLongi)/60;

            longitude = Float.parseFloat(longi)+minLon;


            dirNS = parts[2];

            dirEO = parts[4];

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
            minLat=0;
            minLon=0;
            dirNS = null;
            dirEO = null;
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

    public String getDirNS()
    {
        return this.dirNS;
    }

    public String getDirEO()
    {
        return this.dirEO;
    }

    public Boolean getTrameGPS()
    {
        return this.trameGPS;
    }

    public float getMinLat()
    {
        return this.minLat;
    }
    public float getMinLon()
    {
        return this.minLon;
    }
}

