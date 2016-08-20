package com.codamasters.ryp.model;

/**
 * Created by Juan on 30/07/2016.
 */
public class University {

    private String name;
    private Location location;
    private int numVotes;
    private double sumRating;
    private double sumElo;

    public University(){

    }

    public University(String name){
        this.name = name;
        this.numVotes = 0;
        this.sumRating = 0;
        this.sumElo = 0;
        this.location = new Location(13.1321, 35.31231);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public double getSumRating() {
        return sumRating;
    }

    public double getSumElo() {
        return sumElo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public void setSumRating(double sumRating) {
        this.sumRating = sumRating;
    }

    public void setSumElo(double sumElo) {
        this.sumElo = sumElo;
    }
}
