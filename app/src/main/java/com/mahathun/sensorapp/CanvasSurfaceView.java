package com.mahathun.sensorapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 7/29/2014.
 */
public class CanvasSurfaceView extends SurfaceView
        implements SurfaceHolder.Callback {
    private SurfaceHolder sh;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private double[] angle,distance;
    private float [] obj_x,obj_y;
    private String[] obj_type;
    private float constant=100;//for the scaling purposes
    private int device_width=7;//device width
    private int device_height=10;//device height

    Bitmap bitmap = Bitmap.createBitmap(720, 1042, Bitmap.Config.ARGB_8888);

    public CanvasSurfaceView(Context context, double[] angle_array, double[] distance_array, String[] obj_type_array, float[] objx, float[] objy) {
        super(context);
        sh = getHolder();
        sh.addCallback(this);
        paint.setColor(Color.BLACK);
        //paint.setStyle(Paint.Style.FILL);

        angle = angle_array;
        distance = distance_array;

        obj_type = obj_type_array;
        obj_x = objx;
        obj_y = objy;





    }

    public void surfaceCreated(SurfaceHolder holder) {
       // Canvas canvas = new Canvas(bitmap);
        Canvas canvas = sh.lockCanvas();

        final Canvas c = new Canvas (bitmap);



        canvas.drawColor(Color.WHITE);
        c.drawColor(Color.LTGRAY);

        float[] x =  new float[10];
        float[] y =  new float[10];
        float[] d = new float[10];



        for(int i=0;i<angle.length;i++){
            float dx,dy;
           if(angle[i+1]!=0) {
                dx = (float) Math.abs((Math.cos(angle[i]) * distance[i]) - ((Math.cos(angle[i + 1]) * distance[i + 1])));//x difference
                dy = (float) Math.abs(((Math.sin(angle[i]) * distance[i])) - ((Math.sin(angle[i + 1]) * distance[i + 1])));//x differenceg
               d[i] = (float) Math.pow((dx * dx + dy * dy), 0.5);
           }else{
                dx = (float) Math.abs(((Math.cos(angle[i]) * distance[i])) - ((Math.cos(angle[0]) * distance[0])));//x difference
                dy = (float) Math.abs(((Math.sin(angle[i]) * distance[i])) - ((Math.sin(angle[0]) * distance[0])));//x difference
               d[i] = (float) Math.pow((dx * dx + dy * dy), 0.5);
               break;
           }

        }




        int counter=1;

        x[0] = (float) (Math.cos(angle[0]) * distance[0]);
        y[0] = (float) (Math.sin(angle[0]) * distance[0]);

        while(angle[counter]!=0){
            x[counter] = (float) (x[counter-1] + (Math.cos(angle[counter]) * distance[counter]));
            y[counter] = (float) (y[counter-1] + (Math.sin(angle[counter]) * distance[counter]));

            counter++;
        }



        float min = min(x);
       // Log.d("minimum of x : ", String.valueOf(min));

        if(min<0){
            x = add(x,min);


        }

        float miny = min(y);
        //Log.d("minimum of y : ", String.valueOf(miny));

        if(miny<0){
            y = add(y,miny);


        }

        // printing the array
        for(int i=0;i<x.length;i++){
        // Log.d("x[" + i + "] : ", String.valueOf(x[i]));
          //  Log.d("y[" + i + "] : ", String.valueOf(y[i]));

         //   Log.d("obx[" + i + "] : ", String.valueOf(obx[i]));
         //   Log.d("oby[" + i + "] : ", String.valueOf(oby[i]));

        }


       double max_x = max(x);
       double max_y = max(y);



        float k;

        if(max_x>device_width){
        //    Log.d("max_x>device_width", "true");
            k = (float) (device_width/max_x);
           // Log.d("max_x : k :---", String.valueOf(k));
            x = replace(x,k);


        }



        if(max_y>device_height){
         //   Log.d("max_y>device_height", "true");
            k = (float) (device_height/max_y);
        //    Log.d("max_y : k :---", String.valueOf(k));
            y= replace(y,k);


        }






        x = replace(x,constant);
        y = replace(y,constant);



        // printing the array
        for(int i=0;i<x.length;i++){
            Log.d("x[" + i + "] : ", String.valueOf(x[i]));
            Log.d("y[" + i + "] : ", String.valueOf(y[i]));


        }






        Paint wallpaint = new Paint();
        wallpaint.setColor(Color.GRAY);
        wallpaint.setStrokeWidth(10);
        wallpaint.setStyle(Paint.Style.STROKE);

        Path wallpath = new Path();
        wallpath.reset(); // only needed when reusing this path for a new build
        //wallpath.moveTo(0,0); // used for first point
        wallpath.moveTo(x[0],y[0]);


       // drawing the points on the canvas
        int j=0;
        while(x[j]!=0){
            wallpath.lineTo(x[j], y[j]);


            j++;
        }



        wallpath.lineTo(x[0], y[0]); // there is a setLastPoint action but i found it not to work as expected

        canvas.drawPath(wallpath, wallpaint);
        c.drawPath(wallpath,wallpaint); // for snapshot


        //drawing the text
        j=0;
        while(x[j] != 0){
            paint.setColor(Color.BLUE);
            paint.setTextSize(30);
            canvas.drawText(String.valueOf(j+1), x[j]+5, y[j]+5, paint);
            c.drawText(String.valueOf(j+1), x[j]+5, y[j]+5, paint);

            double roundOff = Math.round(d[j] * 100.0) / 100.0;

            if(x[j+1]==0){
                paint.setColor(Color.RED);
                paint.setTextSize(30);

                canvas.drawText(String.valueOf(roundOff)+" m ", ((x[j] + x[0]) / 2) + 5, ((y[j] + y[0]) / 2) + 5, paint);
                c.drawText(String.valueOf(roundOff)+" m ", ((x[j] + x[0]) / 2) + 5, ((y[j] + y[0]) / 2) + 5, paint);
            }else {
                paint.setColor(Color.RED);
                paint.setTextSize(30);
                canvas.drawText(String.valueOf(roundOff)+" m ", ((x[j] + x[j + 1]) / 2) + 5, ((y[j] + y[j + 1]) / 2) + 5, paint);
                c.drawText(String.valueOf(roundOff)+" m ", ((x[j] + x[j + 1]) / 2) + 5, ((y[j] + y[j + 1]) / 2) + 5, paint);
            }
            j++;
        }



        //drawing the canvas
        //Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        //canvas.drawBitmap(myBitmap, obx[0], oby[0], null);

        //canvas.drawBitmap(myBitmap, obx[1], oby[1], null);

    //drawing the objects
        int l=0;
        while(obj_x[l] != 0) {
            paint.setColor(Color.GREEN);
            paint.setTextSize(30);

                canvas.drawText(String.valueOf(obj_type[l]), obj_x[l], obj_y[l], paint);
                c.drawText(String.valueOf(obj_type[l]), obj_x[l], obj_y[l], paint);


            l++;
        }






        sh.unlockCanvasAndPost(canvas);



        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/SPEIN";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "map.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    public static float[] replace(float[] array, float k){
        float[] temp=new float[10];
        for(int i=0; i<array.length;i++){
        //    Log.d("replace: b4: " , String.valueOf(array[i]));
            temp[i] =  (array[i]* k);
        //    Log.d("replace: after: " , String.valueOf(temp[i]));

        }

        return temp;

    }

    public static float[] add(float[] array, float k){
        float[] temp=new float[10];
        for(int i=0; i<array.length;i++){
         //   Log.d("add: b4: " , String.valueOf(array[i]));
            if(array[i]!=0) {
                temp[i] = (array[i] - k*2);//k is negative
            }else{
                temp[i] = 0;
            }

         //   Log.d("add: after: " , String.valueOf(temp[i]));

        }

        return temp;

    }

    public static float max(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }


    public static float min(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns min
        float min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] < min) {
                min = array[j];

            }
        }

        return min;
    }
}