package com.codamasters.ryp.UI.university;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Comment;
import com.codamasters.ryp.utils.adapter.list.comments.CommentListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UniversityCommentActivity extends AppCompatActivity {

    private static final String PREFS = "RYP";

    private ValueEventListener mConnectedListener;
    private CommentListAdapter mCommentListAdapter;
    private String universityID;
    private String user_name;
    private String user_id;
    private String title;
    private boolean anonymous;

    private DatabaseReference firebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make sure we have a mUsername
        loadUser();

        title = getIntent().getExtras().getString("university_name", "Comments");
        universityID = getIntent().getExtras().getString("university_id", "Error");
        setTitle(title);


        // Setup our Firebase mFirebaseRef
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("university_comments").child(universityID);


        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!anonymous)
                    sendMessage();
                else
                    Toast.makeText(getApplicationContext(), "You can't comment an university being anonymous.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.chat_lv);
        // Tell our list adapter that we only want 50 messages at a time
        mCommentListAdapter = new CommentListAdapter(this, firebaseRef.limitToFirst(50), this, R.layout.own_chat_message, user_id);
        listView.setAdapter(mCommentListAdapter);
        mCommentListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mCommentListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = firebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(UniversityCommentActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UniversityCommentActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mCommentListAdapter.cleanup();
    }

    private void loadUser(){
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        user_name = prefs.getString("user_name", null);
        user_id = prefs.getString("user_id", null);
        anonymous = prefs.getBoolean("anonymous", true);
    }

    private void sendMessage() {

        if(user_name!=null && user_id!=null) {
            EditText inputText = (EditText) findViewById(R.id.messageInput);
            String input = inputText.getText().toString();
            if (!input.equals("")) {
                Comment comment = new Comment(input, user_id, user_name, System.currentTimeMillis());
                firebaseRef.push().setValue(comment);
                inputText.setText("");
            }
        }else{
            Toast.makeText(this, "Sorry, there was an error. Login again.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
