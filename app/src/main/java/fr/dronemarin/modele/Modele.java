package fr.dronemarin.modele;

/**
 * Created by joshu on 09/03/2018.
 */

class Modele {
    private static final Modele ourInstance = new Modele();

    static Modele getInstance() {
        return ourInstance;
    }

    private Modele() {
    }
}
