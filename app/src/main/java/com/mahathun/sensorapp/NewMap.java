package com.mahathun.sensorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mahathun.sensorapp.R;

public class NewMap extends Activity {

    Button next;
    EditText customerName,customerAddress, customerBudget;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b=this.getIntent().getExtras();

        userName = b.getString("username");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);


        customerName = (EditText) findViewById(R.id.csName);
        customerAddress = (EditText) findViewById(R.id.csAddress);
        customerBudget = (EditText) findViewById(R.id.csBudget);

        next = (Button) findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getBaseContext(), MainActivity.class);


                Bundle b1=new Bundle();
                b1.putString("username",userName);
                b1.putString("csName", String.valueOf(customerName.getText()));
                b1.putString("csAddress", String.valueOf(customerAddress.getText()));
                b1.putString("csBudget", String.valueOf(customerBudget.getText()));


                intent1.putExtras(b1);
                startActivity(intent1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_map, menu);
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
}
