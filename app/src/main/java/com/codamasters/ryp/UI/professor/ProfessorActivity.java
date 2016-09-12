package com.codamasters.ryp.UI.professor;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Professor;
import com.codamasters.ryp.model.Rating;
import com.codamasters.ryp.utils.other.EloCalculator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfessorActivity extends AppCompatActivity {

    private static final String PREFS = "RYP";


    private static TextView tvName, tvTotalRating;
    private TextView tvSkill1, tvSkill2, tvSkill3, tvSkill4, tvSkill5;
    private RatingBar rbSkill1, rbSkill2, rbSkill3, rbSkill4, rbSkill5;
    private Context mContext;

    private ImageView icon_teacher;
    private static TextView tvTotalSkillRating1, tvTotalSkillRating2, tvTotalSkillRating3,tvTotalSkillRating4, tvTotalSkillRating5;

    private Professor professor;
    private String professor_key;
    private String user_id;
    private ArrayList<String> degree_keys;
    private String university_key;

    private DatabaseReference firebaseRef;
    private boolean update=false;
    private Rating lastRating;
    private boolean anonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        loadUser();
        initView();

        // SEND BUTTON
        // TODO : loadProfessor error after coming back from Comment Activity
        loadProfessor();
        //initProfessor();
        initListeners();

        Log.d("PROFESSOR KEY", professor_key);

    }

    private void initView(){
        tvName = (TextView) findViewById(R.id.tv_teacher_name);
        tvTotalRating = (TextView) findViewById(R.id.tv_teacher_total_rating);
        tvSkill1 = (TextView) findViewById(R.id.tv_aptitude_1);
        tvSkill2 = (TextView) findViewById(R.id.tv_aptitude_2);
        tvSkill3 = (TextView) findViewById(R.id.tv_aptitude_3);
        tvSkill4 = (TextView) findViewById(R.id.tv_aptitude_4);
        tvSkill5 = (TextView) findViewById(R.id.tv_aptitude_5);


        tvTotalSkillRating1  = (TextView) findViewById(R.id.total_aptitude_1_rating);
        tvTotalSkillRating2  = (TextView) findViewById(R.id.total_aptitude_2_rating);
        tvTotalSkillRating3  = (TextView) findViewById(R.id.total_aptitude_3_rating);
        tvTotalSkillRating4  = (TextView) findViewById(R.id.total_aptitude_4_rating);
        tvTotalSkillRating5  = (TextView) findViewById(R.id.total_aptitude_5_rating);


        rbSkill1 = (RatingBar) findViewById(R.id.rating_aptitude1);
        rbSkill2 = (RatingBar) findViewById(R.id.rating_aptitude2);
        rbSkill3 = (RatingBar) findViewById(R.id.rating_aptitude3);
        rbSkill4 = (RatingBar) findViewById(R.id.rating_aptitude4);
        rbSkill5 = (RatingBar) findViewById(R.id.rating_aptitude5);


        tvName.setTextColor(Color.parseColor("#FFFFFF"));
        tvTotalRating.setTextColor(Color.parseColor("#FFFFFF"));
        tvSkill1.setTextColor(Color.parseColor("#FFFFFF"));
        tvSkill2.setTextColor(Color.parseColor("#FFFFFF"));
        tvSkill3.setTextColor(Color.parseColor("#FFFFFF"));
        tvSkill4.setTextColor(Color.parseColor("#FFFFFF"));
        tvSkill5.setTextColor(Color.parseColor("#FFFFFF"));


        tvTotalSkillRating1.setTextColor(Color.parseColor("#FFFFFF"));
        tvTotalSkillRating2.setTextColor(Color.parseColor("#FFFFFF"));
        tvTotalSkillRating3.setTextColor(Color.parseColor("#FFFFFF"));
        tvTotalSkillRating4.setTextColor(Color.parseColor("#FFFFFF"));
        tvTotalSkillRating5.setTextColor(Color.parseColor("#FFFFFF"));

        icon_teacher = (ImageView) findViewById(R.id.icon_teacher);

    }

    private void loadUser(){
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        user_id = prefs.getString("user_id", null);
        anonymous = prefs.getBoolean("anonymous", true);
    }

    private void loadProfessor(){
        Gson gson = new Gson();
        String json = getIntent().getExtras().getString("professor", null);
        Type type = new TypeToken<Professor>(){}.getType();
        professor = gson.fromJson(json, type);
        professor_key = getIntent().getExtras().getString("professor_key", null);
        university_key = professor.getUniversityID();
        degree_keys = (ArrayList<String>) professor.getDegreeIDs();

        initProfessor();

        // Check if prof got already voted from user

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("user_rating").child(user_id).child(professor_key);
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("CHILDREN", "YES");
                    update = true;
                    lastRating = dataSnapshot.getValue(Rating.class);
                    loadRating(lastRating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Watch profs values to update the UI (happens after votes get updated)

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor").child(professor_key);
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA", dataSnapshot.toString());
                professor = dataSnapshot.getValue(Professor.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadRating(Rating rating){
            rbSkill1.setRating(rating.getSkillRating1());
            rbSkill2.setRating(rating.getSkillRating2());
            rbSkill3.setRating(rating.getSkillRating3());
            rbSkill4.setRating(rating.getSkillRating4());
            rbSkill5.setRating(rating.getSkillRating5());
    }

    public void initProfessor(){

        Log.d("Update", "Updating PROF");

        int total = professor.getTotalSkillRating1() + professor.getTotalSkillRating2() + professor.getTotalSkillRating3() + professor.getTotalSkillRating4() + professor.getTotalSkillRating5();
        float rating = 0;
        float aptitude_1 = 0;
        float aptitude_2 = 0;
        float aptitude_3 = 0;
        float aptitude_4 = 0;
        float aptitude_5 = 0;

        if(professor.getNumVotes()!=0) {
            rating = (float) total / (5 * professor.getNumVotes());

            aptitude_1 = (float) professor.getTotalSkillRating1() / professor.getNumVotes();
            aptitude_2 = (float) professor.getTotalSkillRating2() / professor.getNumVotes();
            aptitude_3 = (float) professor.getTotalSkillRating3() / professor.getNumVotes();
            aptitude_4 = (float) professor.getTotalSkillRating4() / professor.getNumVotes();
            aptitude_5 = (float) professor.getTotalSkillRating5() / professor.getNumVotes();
        }


        tvName.setText(professor.getName());
        tvTotalRating.setText(String.format("%.1f", rating) + "  (" + professor.getNumVotes() + ")");

        tvTotalSkillRating1.setText(String.format("%.1f", aptitude_1));
        tvTotalSkillRating2.setText(String.format("%.1f", aptitude_2));
        tvTotalSkillRating3.setText(String.format("%.1f", aptitude_3));
        tvTotalSkillRating4.setText(String.format("%.1f", aptitude_4));
        tvTotalSkillRating5.setText(String.format("%.1f", aptitude_5));

    }

    public void sendRating(){

        // TODO : ADD RATING UPDATE
        long skillRating[] = {(int) rbSkill1.getRating(),(int) rbSkill2.getRating(), (int) rbSkill3.getRating(), (int) rbSkill4.getRating(), (int) rbSkill5.getRating()};
        float skillRatingValue = (float) ( skillRating[0] + skillRating[1] + skillRating[2] + skillRating[3] + skillRating[4] ) / 5;
        Rating rating = new Rating(skillRating[0], skillRating[1], skillRating[2], skillRating[3], skillRating[4], System.currentTimeMillis());

        // must be sync, more users are accessing the same db location

        // The profesor is updated
        // The degree is updated
        // The university is updated

        // If its an update (already voted prof) we have to do it correctly

        EloCalculator eloCalculator = new EloCalculator();
        double elo = eloCalculator.getNewElo(- professor.getElo(), skillRatingValue);

        if(lastRating!=null){
            skillRating[0] = skillRating[0] - lastRating.getSkillRating1();
            skillRating[1] = skillRating[1] - lastRating.getSkillRating2();
            skillRating[2] = skillRating[2] - lastRating.getSkillRating3();
            skillRating[3] = skillRating[3] - lastRating.getSkillRating4();
            skillRating[4] = skillRating[4] - lastRating.getSkillRating5();

            skillRatingValue = (float) ( skillRating[0] + skillRating[1] + skillRating[2] + skillRating[3] + skillRating[4] ) / 5;
            double lastSkillRatingValue = (double) (lastRating.getSkillRating1() + lastRating.getSkillRating2() + lastRating.getSkillRating3() + lastRating.getSkillRating4() + lastRating.getSkillRating5()) / 5;
            elo -= eloCalculator.getNewElo(- professor.getElo(), lastSkillRatingValue);
        }

        lastRating = rating;

        updateProfessor(skillRating, elo);
        for(String degree : degree_keys) {
            updateDegree(degree, skillRatingValue, elo);
        }
        updateUniversity(skillRatingValue, elo);

        // it's uniquer per user, sync isn't necessary

        // The ratings list from the user is updated
        // The rating list of the professor is updated

        // Every db location is denormalized for faster read operations
        // but write operations are more complex

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("user_rating").child(user_id).child(professor_key);
        firebaseRef.setValue(rating);

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor_rating").child(professor_key).child(user_id);
        firebaseRef.setValue(rating);

        // Generate lots of random Ratings
        /*
        for(int i=0; i<500; i++) {
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor_rating").child(professor_key).child(user_id+i);
            Random ran = new Random();
            int x1 = ran.nextInt(5) + 1; // Randoms entre 1-5
            int x2 = ran.nextInt(5) + 1; // Randoms entre 1-5
            int x3 = ran.nextInt(5) + 1; // Randoms entre 1-5
            int x4 = ran.nextInt(5) + 1; // Randoms entre 1-5
            int x5 = ran.nextInt(5) + 1; // Randoms entre 1-5

            Rating rating1 = new Rating(x1, x2, x3, x4,x5, System.currentTimeMillis());
            firebaseRef.setValue(rating1);
        }
        */
    }

    private void updateProfessor(final long skillRating[], final double elo){
        // runTransactions for sync update
        for(int i=0; i<5; i++) {
            int j = i+1;
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor").child(professor_key).child("totalSkillRating"+j);

            final int finalI = i;
            firebaseRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    mutableData.setValue((Long) mutableData.getValue() + skillRating[finalI]);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        System.out.println("Firebase counter decrement failed.");
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor").child(professor_key).child("elo");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(mutableData.getValue(Double.class) - elo);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("ERROR UPDATING ELO.");
                }
            }
        });


        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professor").child(professor_key).child("numVotes");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if(!update)
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase counter increment failed.");
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    // UI Update
                    // After last update, prof gets updated with the value listener and we can update the UI
                    initProfessor();

                    // Show messages

                    if(update){
                        Toast.makeText(getApplicationContext(), "Puntuación actualizada.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Puntuación enviada.", Toast.LENGTH_SHORT).show();
                        update=true;
                    }
                }
            }
        });

    }


    private void updateDegree(String degree, final float skill, final double elo){

        Log.d("UPDATE", "DEGREE");


        firebaseRef = FirebaseDatabase.getInstance().getReference().child("degree").child(degree).child("sumRating");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Log.d("UPDATE", "ADDING SKILL !!!!" + String.valueOf(skill));
                mutableData.setValue(mutableData.getValue(Float.class) +  skill);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase counter decrement failed.");
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("degree").child(degree).child("elo");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(mutableData.getValue(Double.class) - elo);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });


        firebaseRef = FirebaseDatabase.getInstance().getReference().child("degree").child(degree).child("numVotes");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if(!update)
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase counter decrement failed.");
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUniversity(final float skill, final double elo){

        Log.d("UPDATE", "UNIVERSITY");


        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university").child(university_key).child("sumRating");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(mutableData.getValue(Float.class) +  skill);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase counter decrement failed.");
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university").child(university_key).child("elo");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(mutableData.getValue(Double.class) - elo);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });


        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university").child(university_key).child("numVotes");

        firebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if(!update)
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase counter decrement failed.");
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void initListeners(){
        tvSkill1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext)
                        .setTitleText(getString(R.string.title_aptitude_1))
                        .setContentText(getString(R.string.info_aptitude_1))
                        .show();
            }
        });
        tvSkill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext)
                        .setTitleText(getString(R.string.title_aptitude_2))
                        .setContentText(getString(R.string.info_aptitude_2))
                        .show();
            }
        });
        tvSkill3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext)
                        .setTitleText(getString(R.string.title_aptitude_3))
                        .setContentText(getString(R.string.info_aptitude_3))
                        .show();
            }
        });
        tvSkill4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext)
                        .setTitleText(getString(R.string.title_aptitude_4))
                        .setContentText(getString(R.string.info_aptitude_4))
                        .show();
            }
        });
        tvSkill5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext)
                        .setTitleText(getString(R.string.title_aptitude_5))
                        .setContentText(getString(R.string.info_aptitude_5))
                        .show();
            }
        });

        icon_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorActivity.this, ProfessorWebViewActivity.class);
                intent.putExtra("web_url", professor.getWebUrl());

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.transition.animation_in_1,R.transition.animation_in_2).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorActivity.this, ProfessorWebViewActivity.class);
                intent.putExtra("web_url", professor.getWebUrl());

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.transition.animation_in_1,R.transition.animation_in_2).toBundle();
                startActivity(intent, bndlanimation);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.transition.animation_in_1,R.transition.animation_in_2).toBundle();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.openChat:
                Intent intentComment = new Intent(ProfessorActivity.this, ProfessorCommentActivity.class);
                intentComment.putExtra("professor_key", professor_key);
                intentComment.putExtra("professor_name", professor.getName());

                startActivity(intentComment, bndlanimation);
                return true;
            case R.id.openStats:
                Intent intentStats = new Intent(ProfessorActivity.this, ProfessorStatsActivity.class);
                intentStats.putExtra("professor_key", professor_key);
                intentStats.putExtra("professor_name", professor.getName());

                startActivity(intentStats, bndlanimation);
                return true;
            case R.id.sendRating:
                if(!anonymous)
                    sendRating();
                else
                    Toast.makeText(this, "You can't rate a professor being anonymous.", Toast.LENGTH_SHORT).show();
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
}
