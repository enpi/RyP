package com.codamasters.ryp.UI.ranking;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.utils.adapter.list.primary.DegreeRankingListAdapter;
import com.codamasters.ryp.utils.adapter.list.FirebaseListAdapter;
import com.codamasters.ryp.utils.adapter.list.primary.ProfessorRankingListAdapter;
import com.codamasters.ryp.utils.adapter.list.primary.UniversityRankingListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Juan on 28/07/2016.
 */
public class RankingListFragment  extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_REFERENCE = "section_reference";
    private static final String ARG_SECTION_KEY = "section_key";


    private FirebaseListAdapter listAdapter;
    private DatabaseReference firebaseRef;

    public RankingListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RankingListFragment newInstance(int sectionNumber, String reference, String key) {
        RankingListFragment fragment = new RankingListFragment();
        Log.d("NEW FRAGMENT", "CREATING FRAGMENT " + reference);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_REFERENCE, reference);
        args.putString(ARG_SECTION_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ranking_primary, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.ranking_lv);
        // Tell our list adapter that we only want 50 messages at a time

        Log.d("VIEW CREATED", getArguments().getString(ARG_SECTION_REFERENCE));

        firebaseRef = FirebaseDatabase.getInstance().getReference().child(getArguments().getString(ARG_SECTION_REFERENCE));
        Query query;

        switch (getArguments().getString(ARG_SECTION_REFERENCE)){
            case "university":
                listAdapter = new UniversityRankingListAdapter(getActivity(), firebaseRef.orderByChild("sumElo").limitToFirst(10), getActivity(), R.layout.ranking_list_row);
                break;
            case "degree":
                listAdapter = new DegreeRankingListAdapter(getActivity(), firebaseRef.orderByChild("sumElo").limitToFirst(10), getActivity(), R.layout.ranking_list_row);
                break;
            case "professor":
                listAdapter = new ProfessorRankingListAdapter(getActivity(), firebaseRef.orderByChild("elo").limitToFirst(10), getActivity(), R.layout.ranking_list_row);
                break;
            case "university_degree":
                query = firebaseRef.child(getArguments().getString(ARG_SECTION_KEY)).orderByChild("sumElo").limitToFirst(10);
                listAdapter = new DegreeRankingListAdapter(getActivity(), query, getActivity(), R.layout.ranking_list_row);
                break;
            case "university_professor":
                query = firebaseRef.child(getArguments().getString(ARG_SECTION_KEY)).orderByChild("elo").limitToFirst(10);
                listAdapter = new ProfessorRankingListAdapter(getActivity(), query, getActivity(), R.layout.ranking_list_row);
                break;
            case "university_degree_professor":
                query = firebaseRef.child(getArguments().getString(ARG_SECTION_KEY)).orderByChild("elo").limitToFirst(10);
                listAdapter = new ProfessorRankingListAdapter(getActivity(), query, getActivity(), R.layout.ranking_list_row);
                break;
        }

        listView.setAdapter(listAdapter);
        listAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(listAdapter.getCount() - 1);
            }
        });

        return rootView;
    }
}
