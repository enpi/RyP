package com.codamasters.ryp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.codamasters.ryp.UI.MainActivity;

/**
 * Created by Juan on 30/03/2016.
 */

public class LoginAsynkTask extends AsyncTask<Context, Void, String> {

    private Context context;
    private String fullName = "Prueba";
    private String email = "Prueba";

    public LoginAsynkTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Context... params) {
        saveUser(email, fullName);
        return "Success";
    }

    @Override
    protected void onPostExecute(String result) {
        context.startActivity(new Intent(context, MainActivity.class));
        Log.d("user", fullName);
    }

    public void saveUser(String email, String name){
        SharedPreferences prefs = context.getSharedPreferences("RYT", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("user_email", email);
        editor.putString("user_name", name);
        editor.commit();
    }


}
