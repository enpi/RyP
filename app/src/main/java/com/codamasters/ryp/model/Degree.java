package com.codamasters.ryp.model;

/**
 * Created by Juan on 30/07/2016.
 */
public class Degree {

    private String name;
    private String faculty;
    private Location location;
    private int numVotes;
    private double sumRating;
    private double elo;

    private String universityName;
    private String universityID;

    public Degree(){

    }

    public Degree(String name){
        this.name = name;
        this.faculty = "ETSIIT";
        this.location = new Location(13.1321, 35.31231);
        this.numVotes = 0;
        this.sumRating = 0;
        this.elo = 0;
        this.universityName = "UGR";
        this.universityID = "-KPSOVH-JIRhmwUgErmP";
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
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

    public void setFaculty(String faculty) {
        this.faculty = faculty;
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

    public String getUniversityID() {
        return universityID;
    }

    public void setUniversityID(String universityID) {
        this.universityID = universityID;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public double getElo() {
        return elo;
    }

    public void setElo(double elo) {
        this.elo = elo;
    }
}

