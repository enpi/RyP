package com.codamasters.ryp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 28/07/2016.
 */
public class Professor {

    // Profile info
    private String name;
    private String webUrl;

    // Rating info
    private int totalSkillRating1;
    private int totalSkillRating2;
    private int totalSkillRating3;
    private int totalSkillRating4;
    private int totalSkillRating5;
    private int numVotes;
    private int elo;

    // Profile and rating related info
    private String universityID;
    private List<String> degreeIDs;

    public Professor(){

    }

    public Professor(String name, String universityID){
        this.name = name;
        this.webUrl = "google.es";
        this.totalSkillRating1 = 0;
        this.totalSkillRating2 = 0;
        this.totalSkillRating3 = 0;
        this.totalSkillRating4 = 0;
        this.totalSkillRating5 = 0;
        this.numVotes = 0;
        this.degreeIDs = new ArrayList<>();
        this.universityID = universityID;
        this.degreeIDs.add("-KPSPpKYZUCS3yYMbbI0");
    }

    public String getName() {
        return name;
    }

    public String getWebUrl(){
        return webUrl;
    }

    public int getTotalSkillRating1() {
        return totalSkillRating1;
    }

    public int getTotalSkillRating2() {
        return totalSkillRating2;
    }

    public int getTotalSkillRating3() {
        return totalSkillRating3;
    }

    public int getTotalSkillRating4() {
        return totalSkillRating4;
    }

    public int getTotalSkillRating5() {
        return totalSkillRating5;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public int getElo() {

        return elo;
    }

    public String getUniversityID() {
        return universityID;
    }

    public List<String> getDegreeIDs() {
        return degreeIDs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setTotalSkillRating1(int totalSkillRating1) {
        this.totalSkillRating1 = totalSkillRating1;
    }

    public void setTotalSkillRating2(int totalSkillRating2) {
        this.totalSkillRating2 = totalSkillRating2;
    }

    public void setTotalSkillRating3(int totalSkillRating3) {
        this.totalSkillRating3 = totalSkillRating3;
    }

    public void setTotalSkillRating4(int totalSkillRating4) {
        this.totalSkillRating4 = totalSkillRating4;
    }

    public void setTotalSkillRating5(int totalSkillRating5) {
        this.totalSkillRating5 = totalSkillRating5;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public void setUniversityID(String universityID) {
        this.universityID = universityID;
    }

    public void setDegreeIDs(List<String> degreeIDs) {
        this.degreeIDs = degreeIDs;
    }
}
