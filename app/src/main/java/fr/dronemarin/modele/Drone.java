package fr.dronemarin.modele;

import java.util.ArrayList;


/**
 * Created by fatima/caroline on 26/03/2018.
 */

public class Drone {

    private ArrayList<PositionGPS> position;

    public Drone() {
        position = new ArrayList<PositionGPS>();
    }

    public void addPositionGPS(PositionGPS p) {
         position.add(p);
    }
    public void removePositionGPS(PositionGPS p){
        position.remove(p);
    }

    public ArrayList<PositionGPS> getPosition()
    {
        return this.position;
    }
}