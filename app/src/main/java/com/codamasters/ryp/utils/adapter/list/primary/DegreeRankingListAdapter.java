package com.codamasters.ryp.utils.adapter.list.primary;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.ranking.RankingDegreeActivity;
import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.utils.adapter.list.FirebaseListAdapter;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class DegreeRankingListAdapter extends FirebaseListAdapter<Degree> {

    private Context context;

    public DegreeRankingListAdapter(Context context, Query ref, Activity activity, int layout) {
        super(ref, Degree.class, layout, activity);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Degree model = mModels.get(i);
        String key = mKeys.get(i);

        if (view==null) {
            view = mInflater.inflate(R.layout.ranking_list_row, viewGroup, false);
        }

        // Call out to subclass to marshall this model into the provided view
        populateView(view, model, key, i);
        return view;

    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param degree An instance representing the current state of a chat message
     * @param key An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, final Degree degree, final String key, int i) {
        // Map a Chat object to an entry in our listview

        TextView textView = (TextView) view.findViewById(R.id.rank);
        textView.setText(String.valueOf(i+1));

        textView = (TextView) view.findViewById(R.id.name);
        textView.setText(degree.getName() + " (" + degree.getUniversityName() + ")");

        textView = (TextView) view.findViewById(R.id.num_votes);
        double skillRating = 0;

        if(degree.getNumVotes() != 0){
            skillRating = degree.getSumRating() / degree.getNumVotes();
        }

        textView.setText(String.format("%.1f", skillRating) + "  (" + degree.getNumVotes() + ")");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingDegreeActivity.class);

                Gson gson = new Gson();
                String json = gson.toJson(degree);
                intent.putExtra("degree", json);

                intent.putExtra("degree_key", key);

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.transition.animation_in_1,R.transition.animation_in_2).toBundle();
                context.startActivity(intent, bndlanimation);
            }
        });

    }
}
