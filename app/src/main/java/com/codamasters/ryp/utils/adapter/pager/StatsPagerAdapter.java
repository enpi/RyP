package com.codamasters.ryp.utils.adapter.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.RatingValue;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Juan on 24/08/2016.
 */
public class StatsPagerAdapter extends PagerAdapter{


    private DatabaseReference firebaseRef;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RatingValue skillOne, skillTwo, skillThree, skillFour, skillFive;

    public StatsPagerAdapter(final Context context, RatingValue skillOne, RatingValue skillTwo, RatingValue skillThree, RatingValue skillFour, RatingValue skillFive ) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        this.skillOne = skillOne;
        this.skillTwo = skillTwo;
        this.skillThree = skillThree;
        this.skillFour = skillFour;
        this.skillFive = skillFive;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        view = mLayoutInflater.inflate(R.layout.item_stats, container, false);

        switch (position){
            case 0: createGraph(view, skillOne);
                break;
            case 1: createGraph(view, skillTwo);
                break;
            case 2: createGraph(view, skillThree);
                break;
            case 3: createGraph(view, skillFour);
                break;
            case 4: createGraph(view, skillFive);
                break;
        }

        container.addView(view);
        return view;
    }

    private void createGraph(View view, RatingValue ratingValue){

        ((TextView) view.findViewById(R.id.statTitle)).setText(ratingValue.getTitle());

        PieChart mChart = (PieChart) view.findViewById(R.id.chart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        setData(mChart, ratingValue);

    }

    private void setData(PieChart mChart, RatingValue ratingValue){

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
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

}
