package com.codamasters.ryp.UI.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.model.Professor;
import com.codamasters.ryp.model.University;
import com.codamasters.ryp.utils.adapter.list.CustomListAdapter;
import com.codamasters.ryp.utils.adapter.list.search.SearchDegreeRankingListAdapter;
import com.codamasters.ryp.utils.adapter.list.search.SearchProfessorRankingListAdapter;
import com.codamasters.ryp.utils.adapter.list.search.SearchUniversityRankingListAdapter;

import java.util.ArrayList;

/**
 * Created by Juan on 28/07/2016.
 */
public class SearchListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_REFERENCE = "section_reference";

    private CustomListAdapter listAdapter;

    public static ArrayList<University> universities;
    public static ArrayList<Degree> degrees;
    public static ArrayList<Professor> professors;

    public SearchListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SearchListFragment newInstance(int sectionNumber, String reference) {
        SearchListFragment fragment = new SearchListFragment();
        Log.d("NEW FRAGMENT", "CREATING FRAGMENT " + reference);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_REFERENCE, reference);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.ranking_lv);

        switch (getArguments().getString(ARG_SECTION_REFERENCE)) {
            case "university":
                listAdapter = new SearchUniversityRankingListAdapter(getActivity(), universities, null, getActivity());
                //listAdapter = new ProfessorRankingListAdapter(getActivity(), query, getActivity(), R.layout.ranking_list_row);
                break;
            case "degree":
                listAdapter = new SearchDegreeRankingListAdapter(getActivity(), degrees, null, getActivity());
                break;
            case "professor":
                listAdapter = new SearchProfessorRankingListAdapter(getActivity(), professors, null, getActivity());
                break;
        }

        listView.setAdapter(listAdapter);

        return rootView;
    }

    public static void setUniversities(ArrayList<University> universities) {
        SearchListFragment.universities = universities;
    }

    public static void setDegrees(ArrayList<Degree> degrees) {
        SearchListFragment.degrees = degrees;
    }

    public static void setProfessors(ArrayList<Professor> professors) {
        SearchListFragment.professors = professors;
    }
}
