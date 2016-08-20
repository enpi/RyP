package com.codamasters.ryp.UI.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codamasters.ryp.R;
import com.codamasters.ryp.tasks.CustomJSONObjectRequest;
import com.codamasters.ryp.tasks.CustomVolleyRequestQueue;
import com.codamasters.ryp.utils.adapter.pager.SearchSectionsPagerAdapter;
import com.codamasters.ryp.utils.parser.CustomJsonParser;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener, MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {

    public static final String REQUEST_TAG = "SearchActivity";
    private RequestQueue mQueue;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;

    private SearchSectionsPagerAdapter searchSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpSearchBar();
    }

    private void setUpViewPager(){
        searchSectionsPagerAdapter = new SearchSectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(searchSectionsPagerAdapter);

        tl = (TabLayout) findViewById(R.id.tl_activity_main);
        tl.setupWithViewPager(mViewPager);
    }

    private void setUpSearchBar(){
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
        //restore last queries from disk
        //lastSearches = loadSearchSuggestionFromDiks();
        //searchBar.setLastSuggestions(lastSearches);
        //Inflate menu and setup OnMenuItemClickListener
        //searchBar.inflateMenu(R.menu.main);
        //searchBar.getMenu().setOnMenuItemClickListener(this);
    }

    //called when searchbar enabled or disabled
    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
    }

    //called when user confirms request
    @Override
    public void onSearchConfirmed(CharSequence text) {
        //startSearch(text.toString(), true, null, true);
        doSearch(text.toString());
    }

    //called when microphone icon clicked
    @Override
    public void onSpeechIconSelected() {
        //openVoiceRecognizer();
    }


    private void doSearch(String text){

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url ="https://bifur-eu-west-1.searchly.com/firebase/_search?q=name:*"+text+"*";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);

        mQueue.add(jsonRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ERROR", error.toString());
    }

    @Override
    public void onResponse(Object response) {
        Log.d("SUCCESS", ((JSONObject) response).toString());

        CustomJsonParser parser = new CustomJsonParser((JSONObject) response);
        try {
            parser.parse();
            SearchListFragment.setUniversities(parser.getUniversities());
            SearchListFragment.setDegrees(parser.getDegrees());
            SearchListFragment.setProfessors(parser.getProfessors());
            setUpViewPager();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
