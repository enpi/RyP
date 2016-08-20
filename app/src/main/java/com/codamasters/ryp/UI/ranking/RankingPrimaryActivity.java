package com.codamasters.ryp.UI.ranking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.settings.SettingsActivity;
import com.codamasters.ryp.utils.adapter.pager.PrimarySectionsPagerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class RankingPrimaryActivity extends AppCompatActivity {


    private final static String TAG = "RankingPrimaryActivity";
    private final static String PREF_TAG = "RYP";

    private String user_id, user_name, user_email, user_password;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PrimarySectionsPagerAdapter mPrimarySectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tl;
    private TabLayout.Tab[] mTabs;
    private Toolbar toolbar;

    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_primary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        saveUser();
        initDrawer();

        // Create teacher and send info

        /*firebaseRef = FirebaseDatabase.getInstance().getReference().child("university");
        University university = new University("UGR");
        firebaseRef.push().setValue(university);*/

        /*
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university_degree").child("-KPSOVH-JIRhmwUgErmP");
        Degree degree = new Degree("Informática");
        firebaseRef.push().setValue(degree);



        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university_professor").child("-KPSOVH-JIRhmwUgErmP");
        Professor teacher = new Professor("Juan", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan2", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan3", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan4", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan5", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);



        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university_degree_professor").child("-KPSOVH-JIRhmwUgErmP_-KPSPpKYZUCS3yYMbbI0");
        Professor teacher = new Professor("Juan", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan2", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan3", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan4", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);
        teacher = new Professor("Juan5", "-KPSOVH-JIRhmwUgErmP");
        firebaseRef.push().setValue(teacher);

        */

        /*

        Query query = firebaseRef.orderByChild("numVotes").limitToFirst(2);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Teacher user = dataSnapshot.getValue(Teacher.class);
                Log.d("teacher", String.valueOf(user.getNumVotes()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPrimarySectionsPagerAdapter = new PrimarySectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPrimarySectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tl = (TabLayout) findViewById(R.id.tl_activity_main);
        tl.setupWithViewPager(mViewPager);


    }

    public void saveUser(){
        SharedPreferences prefs = getSharedPreferences("RYP", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_key", "captain");
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ranking_primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(RankingPrimaryActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initDrawer(){
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Captain").withEmail("jacks@sparrow.com").withIcon(getResources().getDrawable(R.drawable.material_drawer_badge))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("primary"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("secondary")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        return true;
                    }
                })
                .build();
    }

    private boolean getUser(){
        SharedPreferences prefs = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        user_name = prefs.getString("user_name", null);
        user_id = prefs.getString("user_id", null);
        user_email = prefs.getString("user_email", null);
        user_password = prefs.getString("user_password", null);

        if(user_name!=null && user_id!=null && user_email!=null && user_password!=null)
            return true;

        return false;
    }
}
