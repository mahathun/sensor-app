package com.mahathun.sensorapp;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class LoadMap extends Activity {
    TextView text;
    ListView list;
    String urlMain = "http://mahathun.owncloud.arvixe.com/androidapp/loadMap.php";
    String urlSub = "http://mahathun.owncloud.arvixe.com/androidapp/drawMap.php";
    String userName,customerName,customerAddress,customerBudget,mpName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httpclient);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Bundle b=this.getIntent().getExtras();

        userName = b.getString("username");

        Log.d("-----------------------LoadMAP", userName);

        list=(ListView)findViewById(R.id.listView1);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String)list.getItemAtPosition(i);
                    Toast.makeText(getBaseContext(), selectedItem + " is selected",0).show();
                mpName = selectedItem;
                loadData(selectedItem);




            }
        });

        connect(urlMain, userName);
    }
    private void connect(String url, String user) {
        String data;
        List<String> r = new ArrayList<String>();
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.customlayout,r);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url+"?username="+user);
            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data=EntityUtils.toString(entity);
            Log.e("STRING", data);
            try {

                JSONArray json=new JSONArray(data);
                for(int i=0;i<json.length(); i++)
                {
                    JSONObject obj=json.getJSONObject(i);
                    String mapName=obj.getString("mapName");

                    Log.e("STRING", mapName);
                    r.add(mapName);

                    list.setAdapter(adapter);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }




    }

    private void loadData(String mapName) {
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
        String parameters = "mapname="+mapName;// String parameters = "type="+type+"&x="+X+"&y="+Y;
        Log.d("debug","first OK");
        try
        {
            url = new URL(urlSub);//url = new URL("http://"+ipAdd+"/androidapp/index.php");
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

            double [] angle = new double[10];
            double [] distance = new double[10];
            float [] obj_y = new float[20];
            float [] obj_x = new float[20];
            String [] obj_type = new String[20];


            try {

                JSONArray json=new JSONArray(response);

                    JSONObject obj=json.getJSONObject(0);

                    //populating the angle array
                    for(int i=0;i<10;i++){
                        String a=obj.getString("a"+(i+1));
                        angle[i] = Double.parseDouble(a);
                        Log.e("a1", a);
                    }

                //populating the distance array
                for(int i=0;i<10;i++){
                    String d=obj.getString("d"+(i+1));
                    distance[i] = Double.parseDouble(d);
                    Log.e("a1", d);
                }

                //populating the obj type array
                for(int i=0;i<20;i++){
                    String d=obj.getString("obj"+(i+1));
                    obj_type[i] = d;

                }
                //populating the obj x array
                for(int i=0;i<20;i++){
                    String d=obj.getString("objx"+(i+1));
                    obj_x[i] = Float.parseFloat(d);

                }
                //populating the obj y array
                for(int i=0;i<20;i++){
                    String d=obj.getString("objy"+(i+1));
                    obj_y[i] = Float.parseFloat(d);

                }

                customerName = String.valueOf(obj.get("csName"));
                customerAddress = String.valueOf(obj.get("csAddress"));
                customerBudget= String.valueOf(obj.get("csBudget"));





                //changing the activity(loading the map)

                Intent intent = new Intent(this, CanvasActivity.class);


                Bundle b=new Bundle();
                b.putDoubleArray("angle_array", angle); // passing the angles
                b.putDoubleArray("distance_array", distance); // passing the distances
                b.putStringArray("obj_type", obj_type); // passing the obj type
                b.putFloatArray("objx", obj_x); // passing the object x
               b.putFloatArray("objy", obj_y); // passing the object y

                b.putString("username",userName);
                b.putString("csName",customerName);
                b.putString("csAddress",customerAddress);
                b.putString("csBudget",customerBudget);
                b.putString("mapName",mpName);

                intent.putExtras(b);
                startActivity(intent);








            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            // UI operations
            if (response != "" ){
                //Toast.makeText(this, "Welcome " + user, 0).show();

                //changeActivity(user);

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



}