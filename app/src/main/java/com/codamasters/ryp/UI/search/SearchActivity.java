package com.codamasters.ryp.UI.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codamasters.ryp.R;
import com.codamasters.ryp.tasks.CustomJSONObjectRequest;
import com.codamasters.ryp.tasks.CustomVolleyRequestQueue;
import com.codamasters.ryp.utils.adapter.pager.SearchSectionsPagerAdapter;
import com.codamasters.ryp.utils.parser.CustomJsonParser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener, MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {

    public static final String REQUEST_TAG = "SearchActivity";
    private final static String PREF_TAG = "RYP";

    private RequestQueue mQueue;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;

    private SearchSectionsPagerAdapter searchSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tl;

    private SpinKitView spinKitView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpSearchBar();

        spinKitView = (SpinKitView) findViewById(R.id.spinKit);

    }

    private void setUpViewPager(){
        searchSectionsPagerAdapter = new SearchSectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(searchSectionsPagerAdapter);

        tl = (TabLayout) findViewById(R.id.tl_activity_main);
        tl.setupWithViewPager(mViewPager);
        tl.setTabTextColors(-1,-256);

    }

    private void setUpSearchBar(){
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Buscar en RyP");
        //searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
        searchBar.setNavButtonEnabled(true);
        searchBar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        //restore last queries from disk
        lastSearches = loadSearchSuggestions();
        if(lastSearches!=null) {
            searchBar.setLastSuggestions(lastSearches);
            Log.d("SEARCH", "ADDED SUGGESTIONS");
            Log.d("Suggestions", lastSearches.toString());
        }
        //Inflate menu and setup OnMenuItemClickListener
        //searchBar.inflateMenu(R.menu.main);
        //searchBar.getMenu().setOnMenuItemClickListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveSearchSuggestions(searchBar.getLastSuggestions());
    }

    private List<String> loadSearchSuggestions(){
        SharedPreferences prefs = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        String json = prefs.getString("suggestions", null);

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> list = gson.fromJson(json, type);

        return list;
    }

    private void saveSearchSuggestions(List<String> list){
        SharedPreferences.Editor editor = getSharedPreferences(PREF_TAG, MODE_PRIVATE).edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString("suggestions", json);
        editor.commit();

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

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                onBackPressed();
                break;
        }
    }


    private void doSearch(String text){
        spinKitView.setVisibility(spinKitView.VISIBLE);

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
        spinKitView.setVisibility(spinKitView.GONE);

        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        Log.d("SUCCESS", ((JSONObject) response).toString());

        spinKitView.setVisibility(spinKitView.GONE);

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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.animation_out_1, R.transition.animation_out_2);
    }
}
