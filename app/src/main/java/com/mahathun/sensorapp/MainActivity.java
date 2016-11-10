package com.mahathun.sensorapp;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener, AdapterView.OnItemSelectedListener {
    private static final String[]obj = {"Add an object", "Chair","Table"};
    private static final String EXTRA_MESSAGE = "com.mahathun.sensorapp.key1";
    private double angle;
    private double distance;
    private String objType;

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    //a TextView
    private TextView tv, tv2;
    //the Sensor Manager
    private SensorManager sManager;
    private Button mark, finish;

    private int counter,counter2 = 0;
    private double[] angle_array = new double[10];
    private double[] distance_array = new double[10];

    private String[] obj_type = new String[20];
    private float[] obj_x = new float[20];
    private float[] obj_y = new float[20];

    String userName,customerName,customerAddress, customerBudget;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        Bundle b=this.getIntent().getExtras();

        userName = b.getString("username");
        customerName = b.getString("csName");
        customerAddress = b.getString("csAddress");
        customerBudget = b.getString("csBudget");

        //strict mode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll().build());


        //fullscreeen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //wake lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,obj);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        //get the TextView from the layout file
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);


        Button objMark = (Button) findViewById(R.id.objMark);

        objMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter2<10) {
                    if(objType=="C"){
                        //obj_type[counter2] = "C";
                        //obj_angle[counter2] = angle;
                        //obj_distance[counter2] = distance;
                        counter2++;
                    }else if (objType=="T"){
                        //obj_type[counter2] = "T";
                        //obj_angle[counter2] = angle;
                        //obj_distance[counter2] = distance;
                        counter2++;
                    }

                }else{
                    //error msg when more than 10 corners
                    Log.d("error1", "Error : You are not allowed to enter more than 10 objects");

                    counter2=0;//emptying the counter

                    obj_type = new String[20];//emptying the arrays
                    obj_x= new float[20];//emptying the arrays
                    obj_y= new float[20];//emptying the arrays
                }
            }
        });

        mark = (Button) findViewById(R.id.button_mark);

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv2.setText((new StringBuilder()).append(Double.toString(distance)).append("\n\n").append(Double.toString(angle)).toString());

            if(counter<10) {
                angle_array[counter] = angle;
                distance_array[counter] = distance;



                Log.d("error1", "angle_array["+counter+"]="+angle_array[counter]+"\n"+"distance_array["+counter+"]="+distance_array[counter]+"\n");
                counter++;
             }else{
                //error msg when more than 10 corners
                Log.d("error1", "Error : You are not allowed to enter more than 10 corners");

                counter=0;//emptying the counter
                angle_array= new double[10];//emptying the arrays
                distance_array = new double[10];//emptying the arrays
            }

            }
        });

        finish = (Button) findViewById(R.id.button_finish);





        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        //setting the orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void changeActivity( View view){
        Intent intent = new Intent(this, CanvasActivity.class);


        Bundle b=new Bundle();
        b.putDoubleArray("angle_array", angle_array); // passing the angles
        b.putDoubleArray("distance_array", distance_array); // passing the distances
        b.putStringArray("obj_type", obj_type); // passing the angles
        b.putFloatArray("objx", obj_x); // passing the object angles
        b.putFloatArray("objy", obj_y); // passing the object distance

        b.putString("username",userName);
        b.putString("csName", customerName);
        b.putString("csAddress", customerAddress);
        b.putString("csBudget", customerBudget);


        intent.putExtras(b);
        startActivity(intent);
    }

    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
		/*register the sensor listener to listen to the gyroscope sensor, use the
		 * callbacks defined in this class, and gather the sensor information as
		 * quick as possible*/

         sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);

        Log.d("onResueme", "start");
        Log.d("onResueme", "1");
        Log.d("onResueme", "2");
        if (mCamera == null)
        {
            mCamera = getCameraInstance();
            Log.d("onResueme", "3-null");
        }
        Log.d("onResueme", "3");
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();

        Log.d("onStop", "2");
        if (mCamera != null)
        {
            mCamera.release();
            Log.d("onStop", "3-null");
        }
        Log.d("onStop", "3");
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        double d = event.values[2];
        double theta = (90D - d) / 57.295000000000002D;
        float f = event.values[0];

        //double Y = ((double) event.values[2]);

        //double theta = (90-Y)/57.295;
        //float angle = event.values[0];

        if(angle<0){
            angle = 360 + angle;
        }


        tv.setText("Y :"+ d +"\n"+

                "original angle :"+ event.values[0] +"\n\n"+
                "angle :"+ angle +"\n\n"+
                "Distance = "+  1.4/Math.tan(theta));

        angle = Math.toRadians(f);
        distance = 1.4 / Math.tan(theta);
    }

    private Camera getCameraInstance() {
        Log.d("myapp-camerainstant", "start");
        Camera camera = null;
        Log.d("myapp-camerainstant", "1");
        try
        {
            Log.d("myapp-camerainstant", "2");
            camera = Camera.open();
            Log.d("myapp-camerainstant", "3");
        }
        catch (Exception exception)
        {
            Log.d("myapp-camerainstant", "4");
            return camera;
        }
        return camera;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0:
                //  item 1 selected
                Log.d("1", "Add an object");
                objType = null;
                break;
            case 1:
                //  chair selected
                objType = "C";

                break;
            case 2:
                //  table selected
                objType = "T";

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}