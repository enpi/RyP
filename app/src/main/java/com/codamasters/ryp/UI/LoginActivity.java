package com.codamasters.ryp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.codamasters.ryp.R;
import com.codamasters.ryp.tasks.LoginAsynkTask;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;


/**
 * Created by Juan on 06/04/2016.
 */

public class LoginActivity extends AuthActivity {

    private final static String TAG = "LoginActivity";

    private final static int RC_SIGN_IN = 1;


    private Button tw_button, fb_button, gp_button;
    private Context context;

    // Google Login
    // https://firebase.google.com/docs/auth/android/google-signin
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient mGoogleApiClient;

    // Facebook Login
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        initView();
        initListeners();
        context = this;

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("846239848996-m1ogkn6apntb44g155m4b6tigpoflilu.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        socialSignIn(credential);
    }

    private void twitterLogin(AccessToken token, String secret) {
        AuthCredential credential = TwitterAuthProvider.getCredential(token.getToken(), secret);
        socialSignIn(credential);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        socialSignIn(credential);

    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // TODO: Google Sign In failed, update UI appropriately
            }
        }
    }

    private void initView() {
        tw_button = (Button) findViewById(R.id.twitter_button);
        fb_button = (Button) findViewById(R.id.facebook_button);
        gp_button = (Button) findViewById(R.id.google_plus_button);
    }

    private void initListeners() {

        tw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Profile profile = new Twitter(context, "TE9ZI2XjvASkSiujiwss8hdmq", "WIqpDcGFpsPsAiAsQ21TTKaeWs6tfdLXezNryfFDoPv0xwcltX");
                //profile.login();

                new LoginAsynkTask(context).execute();
                //new LoginAsynkTask(null, context).execute();
            }
        });

        fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Profile profile = new Facebook(context, "1729489167324954", "fc016e80666319698ea6250bee4fea67");
                new LoginAsynkTask( context).execute();
                //new LoginAsynkTask(null, context).execute();
            }
        });

        gp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Profile profile = new GooglePlus(context, "846239848996-m1ogkn6apntb44g155m4b6tigpoflilu.apps.googleusercontent.com", "LHSsBnCzxta2Y4N1B2BpHU4e");
                new LoginAsynkTask( context).execute();
                //new LoginAsynkTask(null, context).execute();
            }
        });
    }

}
