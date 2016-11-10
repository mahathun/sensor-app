package com.mahathun.sensorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahathun.sensorapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup extends Activity {

    private String myServerUrl = "http://mahathun.owncloud.arvixe.com/androidapp/signup.php";

    EditText username1,email,pass1,pass2;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //strict mode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll().build());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username1 = (EditText) findViewById(R.id.txtName);
        email = (EditText) findViewById(R.id.txtEmail);
        pass1 = (EditText) findViewById(R.id.txtPass1);
        pass2 = (EditText) findViewById(R.id.txtPass2);

        signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdd = String.valueOf(email.getText());
                String p1 = String.valueOf(pass1.getText());
                String p2 = String.valueOf(pass2.getText());

                String uname = String.valueOf(username1.getText());

                if(p1.equals(p2) ){
                       if(!uname.isEmpty() && !p1.isEmpty() && !p2.isEmpty()){
                           //signup
                           signupMtd(uname, p1, String.valueOf(email.getText()));
                        ;
                       }else{
                           Toast.makeText(getBaseContext(),"You can't leave these fields blank", 0).show();;
                       }
                }else{
                    //Toast.makeText(this,"Your passowrds doesn't match",0).show();
                    Toast.makeText(getBaseContext(),"Passwords does not match", 0).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void signupMtd(String user, String pass,String txt_email) {


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
        String parameters = "username="+user+"&password="+pass+"&email="+txt_email;// String parameters = "type="+type+"&x="+X+"&y="+Y;
        Log.d("debug","first OK");
        try
        {
            url = new URL(myServerUrl);//url = new URL("http://"+ipAdd+"/androidapp/index.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            Log.d("debug","second OK");

            request = new OutputStreamWriter(connection.getOutputStream());
            Log.d("debug","third OK");
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
                Toast.makeText(this,"That username is already been taken", 0).show();

            }else{
                Toast.makeText(this, "Successfully Created the user "+user, 0).show();

                changeActivity(user);

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
        Intent intent1 = new Intent(this, LoginActivity.class);


        // Bundle b1=new Bundle();
        //b1.putString("username",user);

        // intent1.putExtras(b1);
        startActivity(intent1);
    }
}
