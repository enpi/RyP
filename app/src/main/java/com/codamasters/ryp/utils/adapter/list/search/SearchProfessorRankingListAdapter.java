package com.codamasters.ryp.utils.adapter.list.search;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.professor.ProfessorActivity;
import com.codamasters.ryp.model.Professor;
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
public class SearchProfessorRankingListAdapter extends CustomListAdapter<Professor> {

    private Context context;

    public SearchProfessorRankingListAdapter(Context context, ArrayList<Professor> mModels, ArrayList<String> mKeys, Activity activity) {
        super(Professor.class, mModels, mKeys, activity);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Professor professor = mModels.get(i);
        final String key = mKeys.get(i);

        if (view==null) {
            view = mInflater.inflate(R.layout.ranking_list_row, viewGroup, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.rank);
        textView.setText(String.valueOf(i+1));

        textView = (TextView) view.findViewById(R.id.name);
        textView.setText(professor.getName());

        textView = (TextView) view.findViewById(R.id.num_votes);

        int total = professor.getTotalSkillRating1() + professor.getTotalSkillRating2() + professor.getTotalSkillRating3() + professor.getTotalSkillRating4() + professor.getTotalSkillRating5();

        double rating = 0;

        if(professor.getNumVotes() != 0){
            rating = (float) total / ( 5 * professor.getNumVotes() );
        }

        textView.setText(String.format("%.1f", rating) + "  (" + professor.getNumVotes() + ")");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfessorActivity.class);

                Gson gson = new Gson();
                String json = gson.toJson(professor);
                intent.putExtra("professor", json);

                intent.putExtra("professor_key", key);

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.transition.animation_in_1,R.transition.animation_in_2).toBundle();
                context.startActivity(intent, bndlanimation);
            }
        });

        return view;

    }
}
