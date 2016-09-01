package com.codamasters.ryp.UI.ranking;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.degree.DegreeCommentActivity;
import com.codamasters.ryp.UI.map.MapActivity;
import com.codamasters.ryp.UI.search.SearchListFragment;
import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.tasks.CustomJSONObjectRequest;
import com.codamasters.ryp.tasks.CustomVolleyRequestQueue;
import com.codamasters.ryp.utils.adapter.pager.DegreeSectionsPagerAdapter;
import com.codamasters.ryp.utils.parser.CustomJsonParser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class RankingDegreeActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener{


    public static final String REQUEST_TAG = "RankingDegreeActivity";
    private RequestQueue mQueue;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DegreeSectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tl;
    private Toolbar toolbar;

    private Degree degree;
    private String degree_key;

    private SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_degree);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDegree();
        initView();

        doSearch(degree_key);
    }

    private void setUpViewPager(){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DegreeSectionsPagerAdapter(getSupportFragmentManager(), degree.getUniversityID()+"_"+degree_key);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tl = (TabLayout) findViewById(R.id.tl_activity_main);
        tl.setupWithViewPager(mViewPager);
        tl.setTabTextColors(-1,-256);

    }

    private void initView(){
        double skillRating = 0;

        if(degree.getNumVotes() != 0){
            skillRating = degree.getSumRating() / degree.getNumVotes();
        }

        ((TextView) findViewById(R.id.degreeRating)).setText(String.format("%.1f", skillRating) + " (" + degree.getNumVotes() + ")");
        ((TextView) findViewById(R.id.degreeName)).setText(degree.getName() + " (" + degree.getUniversityName() + ")");


        spinKitView = (SpinKitView) findViewById(R.id.spin_kit_web);
        Sprite drawable = new ThreeBounce();
        spinKitView.setIndeterminateDrawable(drawable);

    }

    private void doSearch(String text){

        spinKitView.setVisibility(spinKitView.VISIBLE);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url ="https://bifur-eu-west-1.searchly.com/firebase/_search?q=degreeIDs:'"+text+"'"+"&sort=elo:asc";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);

        mQueue.add(jsonRequest);

    }

    private void loadDegree(){
        Gson gson = new Gson();
        String json = getIntent().getExtras().getString("degree", null);
        Type type = new TypeToken<Degree>(){}.getType();
        degree = gson.fromJson(json, type);
        degree_key = getIntent().getExtras().getString("degree_key", null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ranking_degree, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent intent;
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.transition.animation_in_1,R.transition.animation_in_2).toBundle();


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.openChat:
                intent = new Intent(this, DegreeCommentActivity.class);
                intent.putExtra("degree_id", degree_key);
                intent.putExtra("degree_name", degree.getName());

                startActivity(intent, bndlanimation);
                return true;
            case R.id.openMap:

                intent = new Intent(this, MapActivity.class);
                intent.putExtra("name", degree.getName());
                intent.putExtra("lat", degree.getLocation().getLatitude());
                intent.putExtra("lng", degree.getLocation().getLongitude());

                startActivity(intent, bndlanimation);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.animation_out_1, R.transition.animation_out_2);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        spinKitView.setVisibility(spinKitView.GONE);

        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
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
}
