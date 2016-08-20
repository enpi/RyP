package com.codamasters.ryp.utils.adapter.list.primary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.ranking.RankingUniversityActivity;
import com.codamasters.ryp.model.University;
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
public class UniversityRankingListAdapter extends FirebaseListAdapter<University> {

    private Context context;
    private int rank = 1;

    public UniversityRankingListAdapter(Context context, Query ref, Activity activity, int layout) {
        super(ref, University.class, layout, activity);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        University model = mModels.get(i);
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
     * @param university An instance representing the current state of a chat message
     * @param key An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, final University university, final String key, int i) {
        // Map a Chat object to an entry in our listview

        TextView textView = (TextView) view.findViewById(R.id.rank);
        textView.setText(String.valueOf(i+1));

        textView = (TextView) view.findViewById(R.id.name);
        textView.setText(university.getName());

        textView = (TextView) view.findViewById(R.id.num_votes);
        textView.setText(String.valueOf(university.getNumVotes()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingUniversityActivity.class);

                Gson gson = new Gson();
                String json = gson.toJson(university);
                intent.putExtra("university", json);
                intent.putExtra("university_key", key);
                context.startActivity(intent);
            }
        });

    }

}
