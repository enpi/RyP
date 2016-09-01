package com.codamasters.ryp.utils.other;

/**
 * Created by Juan on 30/07/2016.
 */
public class EloCalculator {

    // Values are negative, because Firebase doenst handle descending order
    // with this trick IT'S POSBILE

    private static final Double INIT_ELO = 1200.0;
    private static final Double RATING_VALUE = 3.0;
    private static final Double MAX_POINTS = 10.0;


    private double elo;

    public EloCalculator(){
    }

    public double getNewElo(double elo, double rating){

        if(elo==0){
            elo = INIT_ELO + getPointsForRating(rating);
        }else{
            elo = getPointsForRating(rating);
        }

        return elo;
    }

    private double getPointsForRating(double rating){

        double result;

        if(rating < RATING_VALUE){
            result = - ( MAX_POINTS - ( rating - 1 ) / ( RATING_VALUE - 1 ) * MAX_POINTS );
        }else{
            result = ( rating - RATING_VALUE ) / ( RATING_VALUE - 1 ) * MAX_POINTS;
        }

        return result;
    }

}
