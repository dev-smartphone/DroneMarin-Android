package fr.dronemarin.modele;

import java.util.ArrayList;

/**
 * Created by fatima/caroline on 26/03/2018.
 */

public class Drone {

    private ArrayList<PositionGPS> positionGPS;

    public Drone() {
        positionGPS = new ArrayList<>();
    }

    public void addPositionGPS(PositionGPS p) {
        if(this.positionGPS.get(this.positionGPS.size()-1) != p )
            positionGPS.add(p);
    }
}