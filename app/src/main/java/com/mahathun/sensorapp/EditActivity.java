package com.mahathun.sensorapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.mahathun.sensorapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditActivity extends Activity implements View.OnTouchListener, View.OnDragListener{
    View []  chairs = new View[10];
    View []  tables = new View[10];
    private int counter_ch,counter_tbl;
    private String myServerUrl = "http://mahathun.owncloud.arvixe.com/androidapp/update.php";
    private String userName,mapName;

    String obj_Type[] = new String[20];
    float obj_x[] = new float[20];
    float obj_y[] = new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Bundle b=this.getIntent().getExtras();



        userName = b.getString("username");

        mapName = b.getString("mapName");


        //setting the orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ImageView map_bg = (ImageView) findViewById(R.id.imgMap);




        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/SPEIN";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "map.png");


        Uri uri = Uri.fromFile(file);




        map_bg.setImageURI(uri);



        //objCode chair1 ;

        findViewById(R.id.chair1).setOnTouchListener(this);
        findViewById(R.id.chair2).setOnTouchListener(this);
        findViewById(R.id.chair3).setOnTouchListener(this);
        findViewById(R.id.chair4).setOnTouchListener(this);
        findViewById(R.id.chair5).setOnTouchListener(this);
        findViewById(R.id.chair6).setOnTouchListener(this);
        findViewById(R.id.chair7).setOnTouchListener(this);
        findViewById(R.id.chair8).setOnTouchListener(this);
        findViewById(R.id.chair9).setOnTouchListener(this);
        findViewById(R.id.chair10).setOnTouchListener(this);

        findViewById(R.id.table1).setOnTouchListener(this);
        findViewById(R.id.table2).setOnTouchListener(this);
        findViewById(R.id.table3).setOnTouchListener(this);
        findViewById(R.id.table4).setOnTouchListener(this);
        findViewById(R.id.table5).setOnTouchListener(this);
        findViewById(R.id.table6).setOnTouchListener(this);
        findViewById(R.id.table7).setOnTouchListener(this);
        findViewById(R.id.table8).setOnTouchListener(this);
        findViewById(R.id.table9).setOnTouchListener(this);
        findViewById(R.id.table10).setOnTouchListener(this);





        findViewById(R.id.top_container).setOnDragListener(this);




       // ImageView chair1 = (ImageView) findViewById(R.id.chair1);

        chairs[0] = findViewById(R.id.chair1);
        chairs[1] = findViewById(R.id.chair2);
        chairs[2] = findViewById(R.id.chair3);
        chairs[3] = findViewById(R.id.chair4);
        chairs[4] = findViewById(R.id.chair5);
        chairs[5] = findViewById(R.id.chair6);
        chairs[6] = findViewById(R.id.chair7);
        chairs[7] = findViewById(R.id.chair8);
        chairs[8] = findViewById(R.id.chair9);
        chairs[9] = findViewById(R.id.chair10);




        tables[0] = findViewById(R.id.table1);
        tables[1] = findViewById(R.id.table2);
        tables[2] = findViewById(R.id.table3);
        tables[3] = findViewById(R.id.table4);
        tables[4] = findViewById(R.id.table5);
        tables[5] = findViewById(R.id.table6);
        tables[6] = findViewById(R.id.table7);
        tables[7] = findViewById(R.id.table8);
        tables[8] = findViewById(R.id.table9);
        tables[9] = findViewById(R.id.table10);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addChair) {
            chairs[counter_ch].setVisibility(View.VISIBLE);
            chairs[counter_ch].setX(0);
            chairs[counter_ch].setY(0);
            counter_ch++;

            return true;


        }else if (id == R.id.addTable){

            tables[counter_tbl].setVisibility(View.VISIBLE);
            tables[counter_tbl].setX(0);
            tables[counter_tbl].setY(0);

            counter_tbl++;

        }else if (id == R.id.saveChanges){



            int i;
            //converting the chair data into object data
            for(i=0;i<counter_ch; i++){
                obj_Type[i] = "C";

                obj_x[i] = chairs[i].getX();
                obj_y[i] = chairs[i].getY();
            }


            //converting the table data into object data
            int temp=0;
            for(int j = i; j<i+counter_tbl; j++){
                obj_Type[j] = "T";

                obj_x[j] = tables[temp].getX();
                obj_y[j] = tables[temp].getY();
                temp++;
            }



            for(int x = 0; x<obj_Type.length;x++) {
                Log.d("Object Type : ","object - "+obj_Type[x] +" ; x - "+String.valueOf(obj_x[x])+" ; y - "+String.valueOf(obj_y[x]));
            }


            saveData(mapName);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        if (e.getAction()==DragEvent.ACTION_DROP) {
            View view = (View) e.getLocalState();

            view.setVisibility(View.VISIBLE);

            float x = e.getX();
            float y = e.getY();

            Log.d("TEST", "X:-"+ x);
            Log.d("TEST", "Y:-"+ y);

            int widthOffset = view.getWidth()/2;
            int heightOffset = view.getHeight()/2;

            view.setX(x-widthOffset);
            view.setY(y-heightOffset);





        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            float x = e.getX();
            float y = e.getY();
            Log.d("TEST", "X:-"+ x);
            Log.d("TEST", "Y:-"+ y);
            return true;
        } else {
            return false;
        }
    }



    private void saveData(String name) {
        //Toast.makeText(getApplicationContext(), "Your map \""+name +"\" has been saved", Toast.LENGTH_SHORT).show();

        String ipAdd=null;

        String param ="";

        for(int i=0;i<obj_Type.length;i++){
            param+= "&obj"+(i+1)+"="+obj_Type[i];

        }

        for(int i=0;i<obj_Type.length;i++){
            param+= "&objx"+(i+1)+"="+obj_x[i];
        }

        for(int i=0;i<obj_Type.length;i++){
            param+= "&objy"+(i+1)+"="+obj_y[i];
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
        String parameters = "name="+name+"&username="+userName+param;// String parameters = "type="+type+"&x="+X+"&y="+Y;
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
            Toast.makeText(this, "Message from Server: \n" + response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            Toast.makeText(this,"Please enter a valid server IP", 0).show();
        }

    }
}
