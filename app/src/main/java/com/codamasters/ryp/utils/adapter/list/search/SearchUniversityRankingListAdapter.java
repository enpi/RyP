package com.codamasters.ryp.utils.adapter.list.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.ranking.RankingUniversityActivity;
import com.codamasters.ryp.model.University;
import com.codamasters.ryp.utils.adapter.list.CustomListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class SearchUniversityRankingListAdapter extends CustomListAdapter<University> {

    private Context context;

    public SearchUniversityRankingListAdapter(Context context, ArrayList<University> mModels, ArrayList<String> mKeys, Activity activity) {
        super(University.class, mModels, mKeys, activity);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final University university = mModels.get(i);
        //final String key = mKeys.get(i);

        if (view==null) {
            view = mInflater.inflate(R.layout.ranking_list_row, viewGroup, false);
        }

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
                //intent.putExtra("university_key", key);
                context.startActivity(intent);
            }
        });

        return view;

    }



}
