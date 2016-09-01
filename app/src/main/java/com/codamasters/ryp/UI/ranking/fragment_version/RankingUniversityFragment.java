package com.codamasters.ryp.UI.ranking.fragment_version;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.search.SearchListFragment;
import com.codamasters.ryp.model.University;
import com.codamasters.ryp.tasks.CustomJSONObjectRequest;
import com.codamasters.ryp.tasks.CustomVolleyRequestQueue;
import com.codamasters.ryp.utils.adapter.pager.UniversitySectionsPagerAdapter;
import com.codamasters.ryp.utils.parser.CustomJsonParser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class RankingUniversityFragment extends Fragment implements Response.Listener, Response.ErrorListener{


    public static final String REQUEST_TAG = "RankingUniversityActivity";
    private RequestQueue mQueue;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private UniversitySectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tl;
    private Toolbar toolbar;

    private University university;
    private String university_key;

    private SpinKitView spinKitView;

    private CoordinatorLayout coordinatorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.coordinatorLayout    = (CoordinatorLayout)  inflater.inflate(R.layout.activity_ranking_university, container, false);


        toolbar = (Toolbar) coordinatorLayout.findViewById(R.id.toolbar);
        ((AppCompatActivity) super.getActivity()).setSupportActionBar(toolbar);

        this.setHasOptionsMenu(true);


        loadUniversity();
        initView();

        doSearch(university_key);

        return coordinatorLayout;

    }

    private void doSearch(String text){

        spinKitView.setVisibility(spinKitView.VISIBLE);

        mQueue = CustomVolleyRequestQueue.getInstance(super.getActivity().getApplicationContext())
                .getRequestQueue();
        String url ="https://bifur-eu-west-1.searchly.com/firebase/_search?q=universityID:'"+text+"'"+"&sort=elo:asc";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);

        mQueue.add(jsonRequest);

    }

    private void setUpViewPager(){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new UniversitySectionsPagerAdapter(super.getActivity().getSupportFragmentManager(), university_key);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) coordinatorLayout.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tl = (TabLayout) coordinatorLayout.findViewById(R.id.tl_activity_main);
        tl.setupWithViewPager(mViewPager);
    }

    private void loadUniversity(){
        Gson gson = new Gson();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            String json = bundle.getString("university", null);
            Type type = new TypeToken<University>() {}.getType();
            university = gson.fromJson(json, type);
            university_key = bundle.getString("university_key", null);
        }
    }

    private void initView(){
        double skillRating = 0;

        if(university.getNumVotes() != 0){
            skillRating = university.getSumRating() / university.getNumVotes();
        }

        ((TextView) coordinatorLayout.findViewById(R.id.universityRating)).setText(String.format("%.1f", skillRating) + " (" + university.getNumVotes() + ")");
        ((TextView) coordinatorLayout.findViewById(R.id.universityName)).setText(university.getName());


        spinKitView = (SpinKitView) coordinatorLayout.findViewById(R.id.spin_kit_web);
        Sprite drawable = new ThreeBounce();
        spinKitView.setIndeterminateDrawable(drawable);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_ranking_university, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.openMap) {
            //startActivity(new Intent(RankingUniversityActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        spinKitView.setVisibility(spinKitView.GONE);

        Toast.makeText(super.getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        spinKitView.setVisibility(spinKitView.GONE);

        Log.d("SUCCESS", ((JSONObject) response).toString());

        CustomJsonParser parser = new CustomJsonParser((JSONObject) response);
        try {
            parser.parse();

            SearchListFragment.setUniversities(parser.getUniversities());
            SearchListFragment.setUniversitiesKeys(parser.getUniversitiesKeys());

            SearchListFragment.setDegrees(parser.getDegrees());
            SearchListFragment.setDegreesKeys(parser.getDegreesKeys());

            SearchListFragment.setProfessors(parser.getProfessors());
            SearchListFragment.setProfessorsKeys(parser.getProfessorsKeys());

            setUpViewPager();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
