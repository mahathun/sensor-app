package com.mahathun.sensorapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via username/password.

 */
public class LoginActivity extends Activity {


    private String myServerUrl = "http://mahathun.owncloud.arvixe.com/androidapp/login.php";

    // UI references.
    private EditText username,password;
    private Button login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //strict mode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll().build());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);


        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());

                login(user,pass);
            }
        });

        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //loading the signup
                Intent i=new Intent(getBaseContext(),Signup.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        username.setText("");
        password.setText("");
    }

    private void login(String user, String pass) {
        //Toast.makeText(getApplicationContext(), "Your map \""+name +"\" has been saved", Toast.LENGTH_SHORT).show();

        String ipAdd=null;

        String param = null;



        //ipAdd = ip.getText().toString();
        if(ipAdd=="" || ipAdd ==null){
            ipAdd = "198.252.76.128";
        }
        Log.d("ip", ipAdd);
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "username="+user+"&password="+pass;// String parameters = "type="+type+"&x="+X+"&y="+Y;
        Log.d("debug","first OK");
        try
        {
            url = new URL(myServerUrl);//url = new URL("http://"+ipAdd+"/androidapp/index.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            //Log.d("debug","second OK");

            request = new OutputStreamWriter(connection.getOutputStream());
            //Log.d("debug","third OK");
            request.write(parameters);

            request.flush();

            request.close();

            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            // Response from server
            response = sb.toString();
            // UI operations
            if (response != "" ){
               Toast.makeText(this, "Welcome " + user, 0).show();

                changeActivity(user);

            }else{
                Toast.makeText(this,"Please use a valid username and a password", 0).show();
               // Log.d("dd",response);
            }

            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            Toast.makeText(this,"Please check your internet connection", 0).show();
        }

    }

    public void changeActivity( String user){
        Intent intent1 = new Intent(this, HomeActivity.class);


        Bundle b1=new Bundle();
       b1.putString("username",user);

       intent1.putExtras(b1);
        startActivity(intent1);
    }
}



