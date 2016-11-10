package com.mahathun.sensorapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mahathun.sensorapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CanvasActivity extends Activity {
    private String myServerUrl = "http://mahathun.owncloud.arvixe.com/androidapp/save.php";
    private EditText ip;

    private double[] angle_array,distance_array;

    String userName,customerName,customerAddress, customerBudget,mapName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b=this.getIntent().getExtras();

       angle_array= b.getDoubleArray("angle_array");
       distance_array= b.getDoubleArray("distance_array");


        String[] obj_type = b.getStringArray("obj_type");
        float[] obj_x = b.getFloatArray("objx");
        float[] obj_y = b.getFloatArray("objy");

        userName = b.getString("username");
        customerName = b.getString("csName");
        customerAddress = b.getString("csAddress");
        customerBudget = b.getString("csBudget");
        mapName = b.getString("mapName");


        //setting the orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        CanvasSurfaceView canvassurfacview = new CanvasSurfaceView(this,angle_array, distance_array, obj_type, obj_x, obj_y);


//----------------------------------
        //SurfaceView myview = (SurfaceView) findViewById(R.id.mysurfaceView);


              //  myview = canvassurfacview;


        //addLayouts(canvassurfacview);
        setContentView(canvassurfacview);


//-------------------------------------



        Log.d("val", "angle val 1: " + angle_array[0]);
        Log.d("val", "distance val 1: " + distance_array[0]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.canvas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.save) {



            AlertDialog.Builder dialogBuilder;

            dialogBuilder = new AlertDialog.Builder(this);
            final EditText txtInput = new EditText(this);



            dialogBuilder.setTitle("Save");
            dialogBuilder.setMessage("Enter a name for the map");
            dialogBuilder.setView(txtInput);
            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String name = txtInput.getText().toString();
                    mapName = name;
                    saveData(name);

                }
            });

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Canceled", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog saveDialog = dialogBuilder.create();

            saveDialog.show();


            return true;
        }else if(id == R.id.viewDetails){


            AlertDialog.Builder dialogBuilder;

            dialogBuilder = new AlertDialog.Builder(this);




            dialogBuilder.setTitle("View Customer Details");
            dialogBuilder.setMessage("Name : "+ customerName + "\nAddress : "+customerAddress + "\nBudget : " + customerBudget);

            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   ;

                }
            });

            AlertDialog saveDialog = dialogBuilder.create();

            saveDialog.show();
        }else if(id== R.id.shareImage){

            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/SPEIN";
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "map.png");


            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share the MAP"));

        }else if(id== R.id.edit){
            changeActivity(userName,EditActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData(String name) {
        //Toast.makeText(getApplicationContext(), "Your map \""+name +"\" has been saved", Toast.LENGTH_SHORT).show();

        String ipAdd=null;

        String param ="";

        for(int i=0;i<angle_array.length;i++){
            param+= "&a"+(i+1)+"="+angle_array[i];

        }

        for(int i=0;i<distance_array.length;i++){
            param+= "&d"+(i+1)+"="+distance_array[i];
        }


        //ipAdd = ip.getText().toString();
        if(ipAdd=="" || ipAdd ==null){
            ipAdd = "198.252.76.128";
        }
        Log.d("ip",ipAdd);
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "name="+name+"&username="+userName+"&csName="+customerName+"&csAddress="+customerAddress+"&csBudget="+customerBudget+param;// String parameters = "type="+type+"&x="+X+"&y="+Y;
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
                sb.append(line + "\n");
            }
            // Response from server
            response = sb.toString();
            // UI operations
            Toast.makeText(this,"Message from Server: \n"+ response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            Toast.makeText(this,"Please enter a valid server IP", 0).show();
        }

    }

    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Do you want to Go back?");
        // alert.setMessage("Message");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Your action here
                changeActivity(userName, HomeActivity.class);

            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();

    }
    public void changeActivity( String user, Class activity){
        Intent intent1 = new Intent(this, activity);


         Bundle b1=new Bundle();
        b1.putString("username",user);
        b1.putString("mapName",mapName);


        intent1.putExtras(b1);
        startActivity(intent1);
    }



}
