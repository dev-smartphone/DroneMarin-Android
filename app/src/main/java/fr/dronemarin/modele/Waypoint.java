package fr.dronemarin.modele;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by joshu on 09/03/2018.
 */

public class Waypoint implements Serializable{
    private double vitesse;
    private boolean priseImage;
    private boolean pointStationnaire;
    private Location location;

    public Waypoint(double vitesse, boolean priseImage, boolean pointStationnaire, Location location) {
        this.vitesse = vitesse;
        this.priseImage = priseImage;
        this.pointStationnaire = pointStationnaire;
        this.location = location;
    }

    public double getVitesse() {
        return vitesse;
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    public boolean isPriseImage() {
        return priseImage;
    }

    public void setPriseImage(boolean priseImage) {
        this.priseImage = priseImage;
    }

    public boolean isPointStationnaire() {
        return pointStationnaire;
    }

    public void setPointStationnaire(boolean pointStationnaire) {
        this.pointStationnaire = pointStationnaire;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
