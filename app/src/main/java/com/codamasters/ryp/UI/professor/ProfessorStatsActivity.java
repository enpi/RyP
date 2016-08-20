package com.codamasters.ryp.UI.professor;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Rating;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ProfessorStatsActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef;
    private ChildEventListener listener;
    private String professor_key;
    private Query query;

    private ArrayList<Float> ratings;
    private ArrayList<Rating> rating_info;
    private ArrayList<Long> timestamps;


    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView textTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //professor_key = getIntent().getExtras().getString("professor_key", null);
        professor_key = "-KO5yEtWyjv9R4DL_ncC";
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor_rating").child(professor_key);
        query = firebaseRef.orderByChild("timestamp").limitToFirst(20);
        rating_info = new ArrayList<>();
        ratings = new ArrayList<>();
        timestamps = new ArrayList<>();
        textTime = (TextView) findViewById(R.id.timeText);

        createGraph(ratings);

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

                setPingData(ratings);
                mChart.invalidate();
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


    private void createGraph(ArrayList<Float> values){

        Log.d("GRAPH", "GRAPH CREATED");

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mSeekBarX.setProgress(45);
        mSeekBarY.setProgress(100);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("Error loading data.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        mChart.getAxisRight().setEnabled(false);

        setPingData(values);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutBounce);
    }

    private void setPingData(ArrayList<Float> data){

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

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {


            Log.d("GRAPH", "ADDING POINT");

            Log.d("VALUES", values.toString());

            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.red_button_background);
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
            mChart.setData(lineData);
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

}
