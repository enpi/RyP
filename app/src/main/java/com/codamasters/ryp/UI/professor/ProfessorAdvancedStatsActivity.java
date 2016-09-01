package com.codamasters.ryp.UI.professor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Rating;
import com.codamasters.ryp.model.RatingValue;
import com.codamasters.ryp.utils.adapter.pager.StatsPagerAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;

public class ProfessorAdvancedStatsActivity extends AppCompatActivity {


    public static ArrayList<Rating> ratings;
    private RatingValue skillOne, skillTwo, skillThree, skillFour, skillFive;
    private HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_advanced_stats);

        getGraphData();
        initViewPager();

    }

    private void initViewPager(){
        horizontalInfiniteCycleViewPager = (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);
        horizontalInfiniteCycleViewPager.setAdapter(new StatsPagerAdapter(this, skillOne, skillTwo, skillThree, skillFour, skillFive));
    }

    private void getGraphData(){

        skillOne = new RatingValue(getString(R.string.title_aptitude_1));
        skillTwo = new RatingValue(getString(R.string.title_aptitude_2));
        skillThree = new RatingValue(getString(R.string.title_aptitude_3));
        skillFour = new RatingValue(getString(R.string.title_aptitude_4));
        skillFive = new RatingValue(getString(R.string.title_aptitude_5));

        for(Rating rating : ratings){
            updateSkill(rating.getSkillRating1(), skillOne);
            updateSkill(rating.getSkillRating2(), skillTwo);
            updateSkill(rating.getSkillRating3(), skillThree);
            updateSkill(rating.getSkillRating4(), skillFour);
            updateSkill(rating.getSkillRating5(), skillFive);
        }
    }

    private void updateSkill(long value, RatingValue ratingValue){
        switch ((int) value){
            case 1 : ratingValue.addOneStar();
                break;
            case 2 : ratingValue.addTwoStar();
                break;
            case 3: ratingValue.addThreeStar();
                break;
            case 4: ratingValue.addFourStar();
                break;
            case 5: ratingValue.addFiveStar();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adv_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.animation_out_1, R.transition.animation_out_2);
    }

    public static void setRatings(ArrayList<Rating> ratings) {
        ProfessorAdvancedStatsActivity.ratings = ratings;
    }
}
