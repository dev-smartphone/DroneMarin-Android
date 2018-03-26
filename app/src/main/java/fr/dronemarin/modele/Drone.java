package fr.dronemarin.modele;

import java.util.ArrayList;

/**
 * Created by fatima/caroline on 26/03/2018.
 */

public class Drone {

    private ArrayList<PositionGPS> trajectoire;

    public Drone() {
        trajectoire = new ArrayList<>();
    }

    public void addPositionGPS(PositionGPS p) {
        //A modifier -> Ajouter seulement si diffÃ©rent de la derniÃ¨re position
        trajectoire.add(p);
    }
}