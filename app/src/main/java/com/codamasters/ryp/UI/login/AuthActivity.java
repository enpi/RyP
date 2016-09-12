package com.codamasters.ryp.UI.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.codamasters.ryp.R;
import com.codamasters.ryp.UI.ranking.RankingPrimaryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Juan on 26/07/2016.
 */
public class AuthActivity extends AppCompatActivity {

    private final static String TAG = "AuthActivity";
    private final static String PREF_TAG = "RYP";

    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    private String user_id, user_name, user_email, user_password;
    private boolean anonymous;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    user.getToken(true);


                    /*
                    user_id = user.getUid();
                    user_name = user.getDisplayName();
                    user_email = user.getEmail();

                    saveUser();

                    Intent intent = new Intent(AuthActivity.this, RankingPrimaryActivity.class);
                    finish();
                    startActivity(intent);
                    */

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        if(getUser()){
            doLogin();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signOut() {
        mAuth.signOut();

        user_name = null;
        user_email = null;
        user_id = null;
        user_password = null;

        saveUser();
    }

    public void socialSignIn(final AuthCredential credential){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        progressDialog.dismiss();

                        if (!task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "FRACASO LOGIN!!", Toast.LENGTH_SHORT).show();

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(getApplicationContext(), "EXITO LOGIN!!", Toast.LENGTH_SHORT).show();

                            user_id = task.getResult().getUser().getUid();
                            user_name = task.getResult().getUser().getDisplayName();
                            user_email = task.getResult().getUser().getEmail();
                            anonymous = false;
                            task.getResult().getUser().getPhotoUrl();

                            saveUser();

                            doLogin();

                        }
                    }
                });
    }

    public void anonymousSignIn(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        progressDialog.dismiss();

                        if (!task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "FRACASO LOGIN!!", Toast.LENGTH_SHORT).show();

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(getApplicationContext(), "EXITO LOGIN!!", Toast.LENGTH_SHORT).show();

                            user_id = task.getResult().getUser().getUid();
                            user_name = task.getResult().getUser().getDisplayName();
                            user_email = task.getResult().getUser().getEmail();
                            anonymous = true;
                            task.getResult().getUser().getPhotoUrl();

                            saveUser();

                            doLogin();
                        }
                    }
                });


    }


    private void doLogin(){
        Intent intent = new Intent(AuthActivity.this, RankingPrimaryActivity.class);
        finish();
        startActivity(intent);
    }

    private void saveUser(){
        SharedPreferences.Editor editor = getSharedPreferences(PREF_TAG, MODE_PRIVATE).edit();
        editor.putString("user_name", user_name);
        editor.putString("user_id", user_id);
        editor.putString("user_email", user_email);
        editor.putBoolean("anonymous", anonymous);
        //editor.putString("user_password", user_password);
        editor.commit();
    }

    private boolean getUser(){
        SharedPreferences prefs = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        user_name = prefs.getString("user_name", null);
        user_id = prefs.getString("user_id", null);
        user_email = prefs.getString("user_email", null);
        //user_password = prefs.getString("user_password", null);

        if(user_name!=null && user_id!=null && user_email!=null)
            return true;

        return false;
    }

}
