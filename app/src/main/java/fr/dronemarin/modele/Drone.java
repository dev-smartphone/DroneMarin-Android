package fr.dronemarin.modele;

import java.util.ArrayList;


/**
 * Created by fatima/caroline on 26/03/2018.
 */

public class Drone {

    private ArrayList<PositionGPS> position;

    public Drone() {
        position = new ArrayList<>();
    }

    public void addPositionGPS(PositionGPS p) {
        if(this.position.get(this.position.size()-1) != p )
        {
            position.add(p);

        }
    }

    public ArrayList getPosition()
    {
        return this.position;
    }
}