package com.codamasters.ryp.UI.professor;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Professor;
import com.codamasters.ryp.model.Rating;
import com.codamasters.ryp.model.RatingValue;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfessorStatsActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef;
    private ChildEventListener listener;
    private Professor professor;
    private String professor_key;
    private Query query;

    private ArrayList<Float> ratings;
    private ArrayList<Rating> rating_info;
    private ArrayList<Long> timestamps;
    private RatingValue ratingValue;

    private LineChart lineChart;
    private PieChart pieChart;
    private TextView textTime;

    private LinearLayout chartsLayout;
    private SpinKitView spinKitView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_stats);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadProfessor();
        loadStats();

    }


    private void loadProfessor(){
        Gson gson = new Gson();
        String json = getIntent().getExtras().getString("professor", null);
        Type type = new TypeToken<Professor>(){}.getType();
        professor = gson.fromJson(json, type);
        professor_key = getIntent().getExtras().getString("professor_key", null);
    }

    private void loadStats(){
        chartsLayout = (LinearLayout) findViewById(R.id.chartsLayout);
        spinKitView = (SpinKitView) findViewById(R.id.spinKit);
        spinKitView.setVisibility(SpinKitView.VISIBLE);

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor_rating").child(professor_key);
        //query = firebaseRef.orderByChild("timestamp").limitToFirst(100);
        query = firebaseRef.orderByChild("timestamp");
        rating_info = new ArrayList<>();
        ratings = new ArrayList<>();
        timestamps = new ArrayList<>();
        textTime = (TextView) findViewById(R.id.timeText);
        ratingValue = new RatingValue("All stats");

        createLineChart();
        createPieChart();

        listener = query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("Firebase", "Child ADDED");

                Rating r = dataSnapshot.getValue(Rating.class);
                rating_info.add(r);

                float rating = r.getSkillRating1()+r.getSkillRating2()+r.getSkillRating3()+r.getSkillRating4()+r.getSkillRating5();
                rating = rating / 5;
                ratings.add(rating);
                timestamps.add(r.getTimestamp());

                if(timestamps.size()==1){
                    String elapsed_time_string = (String) DateUtils.getRelativeTimeSpanString(getApplication(), r.getTimestamp());
                    textTime.setText("Last rating is from : " + elapsed_time_string);
                }

                // Update LineChart
                setLineChartData(ratings);
                lineChart.invalidate();
                lineChart.setVisibleXRangeMaximum(10);
                lineChart.moveViewToX(ratings.size());

                // Update PieChart
                updateSkill(r.getSkillRating1(), ratingValue);
                updateSkill(r.getSkillRating2(), ratingValue);
                updateSkill(r.getSkillRating3(), ratingValue);
                updateSkill(r.getSkillRating4(), ratingValue);
                updateSkill(r.getSkillRating5(), ratingValue);
                setPieChartData(ratingValue);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void createPieChart(){

        pieChart = (PieChart) findViewById(R.id.pieChart);

        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

    }

    private void setPieChartData(RatingValue ratingValue){

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(ratingValue.getOneStar(),"1 ★"));
        entries.add(new PieEntry(ratingValue.getTwoStars(),"2 ★"));
        entries.add(new PieEntry(ratingValue.getThreeStars(),"3 ★"));
        entries.add(new PieEntry(ratingValue.getFourStars(),"4 ★"));
        entries.add(new PieEntry(ratingValue.getFiveStars(),"5 ★"));


        PieDataSet dataSet = new PieDataSet(entries, "Rating Values");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();


        //android:endColor="#09B300"
        //android:centerColor="#E6E600"
        //android:startColor="#E30000"

        colors.add(ColorTemplate.getColorWithAlphaComponent(0xE30000, 255));
        colors.add(ColorTemplate.getColorWithAlphaComponent(0xFFA500, 255));
        colors.add(ColorTemplate.getColorWithAlphaComponent(0xE5E500, 255));
        colors.add(ColorTemplate.getColorWithAlphaComponent(0x66B266, 255));
        colors.add(ColorTemplate.getColorWithAlphaComponent(0x09B300, 255));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }


    private void createLineChart(){

        lineChart = (LineChart) findViewById(R.id.lineChart);
        lineChart.setDrawGridBackground(true);

        lineChart.setDescription("");

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setScaleXEnabled(true);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinValue(0f); // start at zero
        yAxis.setAxisMaxValue(5f); // the axis maximum is 5
        yAxis.setGranularity(1f); // interval 1
        yAxis.setLabelCount(6, true); // force 6 labels

        lineChart.getAxisRight().setEnabled(false);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Display info over rating
                Toast.makeText(getApplicationContext(), String.valueOf(rating_info.get(((int) e.getX())).getSkillRating1()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    private void setLineChartData(ArrayList<Float> data){

        // Because they are data with a timestamp, we have to reversse the order
        // so the last one is always the first in time order in the graph

        ArrayList<Entry> values = new ArrayList<Entry>();
        int j = data.size()-1;
        for (int i = 0; i < data.size(); i++) {
            values.add(new Entry(i, data.get(j)));
            j--;
        }

        //Collections.reverse(values);

        LineDataSet set1;

        if (lineChart.getData() != null &&
                    lineChart.getData().getDataSetCount() > 0) {


                Log.d("GRAPH", "ADDING POINT");

                Log.d("VALUES", values.toString());

                set1 = (LineDataSet)lineChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();

        } else {

            // NEW SET
            Log.d("GRAPH", "INIT SET");

            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient_stats);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData lineData = new LineData(dataSets);

            // set data
            lineChart.setData(lineData);
            lineChart.animateX(2500, Easing.EasingOption.EaseInOutBounce);

            // Graph created, show layout and hide loadView
            if(chartsLayout.getVisibility() == View.GONE) {
                spinKitView.setVisibility(SpinKitView.GONE);
                chartsLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        query.removeEventListener(listener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        query.removeEventListener(listener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //query.addChildEventListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.openStats:
                ProfessorAdvancedStatsActivity.setRatings(rating_info);
                Intent intent = new Intent(this, ProfessorAdvancedStatsActivity.class);

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.transition.animation_in_1,R.transition.animation_in_2).toBundle();
                startActivity(intent, bndlanimation);
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

}
