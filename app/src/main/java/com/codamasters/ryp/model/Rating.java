package com.codamasters.ryp.model;

/**
 * Created by Juan on 30/07/2016.
 */
public class Rating {


    private long skillRating1;
    private long skillRating2;
    private long skillRating3;
    private long skillRating4;
    private long skillRating5;

    private Long timestamp;

    public Rating(){

    }

    public Rating(long skillRating1, long skillRating2, long skillRating3, long skillRating4, long skillRating5, Long timestamp){
        this.skillRating1 = skillRating1;
        this.skillRating2 = skillRating2;
        this.skillRating3 = skillRating3;
        this.skillRating4 = skillRating4;
        this.skillRating5 = skillRating5;
        this.timestamp = timestamp;
    }

    public long getSkillRating1() {
        return skillRating1;
    }

    public long getSkillRating2() {
        return skillRating2;
    }

    public long getSkillRating3() {
        return skillRating3;
    }

    public long getSkillRating4() {
        return skillRating4;
    }

    public long getSkillRating5() {
        return skillRating5;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
