package com.codamasters.ryp.model;

/**
 * Created by Juan on 30/07/2016.
 */
public class University {

    private String name;
    private String acronym;
    private Location location;
    private int numVotes;
    private double sumRating;
    private double elo;

    public University(){

    }

    public University(String name, String acronym, Location location){
        this.name = name;
        this.acronym = acronym;
        this.numVotes = 0;
        this.sumRating = 0;
        this.elo = 0;
        this.location = location;
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

    public double getElo() {
        return elo;
    }

    public void setElo(double elo) {
        this.elo = elo;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
