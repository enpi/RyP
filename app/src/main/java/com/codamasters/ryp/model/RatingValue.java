package com.codamasters.ryp.model;

/**
 * Created by Juan on 26/08/2016.
 */
public class RatingValue {

    private String title;
    private int oneStar, twoStars, threeStars, fourStars, fiveStars;

    public RatingValue(String title){
        this.title = title;
        this.oneStar = 0;
        this.twoStars = 0;
        this.threeStars = 0;
        this.fourStars = 0;
        this.fiveStars = 0;
    }

    public String getTitle() {
        return title;
    }

    public void addOneStar(){
        this.oneStar++;
    }

    public void addTwoStar(){
        this.twoStars++;
    }

    public void addThreeStar(){
        this.threeStars++;
    }

    public void addFourStar(){
        this.fourStars++;
    }

    public void addFiveStar(){
        this.fiveStars++;
    }

    public int getOneStar() {
        return oneStar;
    }

    public int getTwoStars() {
        return twoStars;
    }

    public int getThreeStars() {
        return threeStars;
    }

    public int getFourStars() {
        return fourStars;
    }

    public int getFiveStars() {
        return fiveStars;
    }
}
