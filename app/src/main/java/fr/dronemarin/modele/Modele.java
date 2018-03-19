package fr.dronemarin.modele;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshu on 09/03/2018.
 */

public class Modele {
    private static final Modele ourInstance = new Modele();
    private List<Waypoint> waypoints = new ArrayList<>();
    static  public Modele getInstance() {
        return ourInstance;
    }

    private Modele() {
        Location l = new Location ("test");
        l.setLatitude (37.31917);
        l.setLongitude (-122.04511);
       // waypoints.add (new Waypoint (40,false,false,l));
       // waypoints.add (new Waypoint (60,true,false,l));
       // waypoints.add (new Waypoint (40,false,true,l));
       // waypoints.add (new Waypoint (80,true,true,l));
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Waypoint addWaypoint(Waypoint waypoint){
        this.waypoints.add(waypoint);
        if(waypoints.size() == 1){
            return null;
        }
        else{
            return this.waypoints.get(this.waypoints.size()-2);
        }


    }

}
