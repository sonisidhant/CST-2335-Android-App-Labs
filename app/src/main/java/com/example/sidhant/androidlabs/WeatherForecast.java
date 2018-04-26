package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.os.AsyncTask;


import android.os.Bundle;

import android.util.Log;

import android.util.Xml;

import android.view.View;

import android.widget.Button;

import android.widget.ImageView;

import android.widget.ProgressBar;

import android.widget.TextView;



import org.xmlpull.v1.XmlPullParser;

import org.xmlpull.v1.XmlPullParserException;



import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.ProtocolException;

import java.net.URL;

public class WeatherForecast extends Activity {

    String ACTIVITY_NAME="";
    Button rouiBt;

    Button pttBt;
    Button siBt;
    Button saBt;

    ProgressBar progressBr;
    TextView currenttempView;
    TextView mintempView;
    TextView maxtempView;

    String urlW="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    ImageView outsidePic;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME,"ACTIVITY_NAME onCreate() works"); //activity name works here

        setContentView(R.layout.activity_weather_forecast);
        currenttempView = (TextView) findViewById(R.id.Current);

        mintempView = (TextView) findViewById(R.id.Minimum);

        maxtempView = (TextView) findViewById(R.id.Maximum);

        progressBr=(ProgressBar) findViewById(R.id.ProgressBar);

        outsidePic = (ImageView) findViewById(R.id.WImage);//we need to update
        ForecastQuery letsgo = new ForecastQuery();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        letsgo.execute(url);
    }//onCreate

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        String minTempS="0";

        String maxTempS="0";

        String currentTempS="0";

        String parselatch="";//should we use String builder?

        String iconTaglatch="";

        Bitmap weatherPic;

        @Override

        protected void onPreExecute() {

            progressBr.setVisibility(View.VISIBLE);

        }
        @Override

        protected void onPostExecute(String result) {//on UI

            currenttempView.setText(currenttempView.getText() + "Current Temp = " + currentTempS + " C*");

            mintempView.setText(mintempView.getText() + "Minimum Temp = " + minTempS + " C*");

            maxtempView.setText(maxtempView.getText() + "Maximum Temp = " + maxTempS + " C*");
            outsidePic.setImageBitmap(weatherPic);
            progressBr.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... value) {

            progressBr.setVisibility(View.VISIBLE);//is now Vis
            progressBr.setProgress(value[0]);
            Log.i(ACTIVITY_NAME, "We have updated");
        }

        @Override

        protected String doInBackground(String... params) {

            try {
                String urlString="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
                URL url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000 );

                conn.setConnectTimeout(15000 );

                conn.setRequestMethod("GET");

                conn.setDoInput(true);
                conn.connect();
                InputStream stream = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();

                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature") ) {

                        currentTempS = parser.getAttributeValue(null, "value");
                        publishProgress(25);

                        minTempS = parser.getAttributeValue(null, "min");
                        publishProgress(50);

                        maxTempS = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }

                    if (parser.getName().equals("weather")) {
                        iconTaglatch = parser.getAttributeValue(null, "icon");
                        String imageF = iconTaglatch+".png";

                        if(fileExistance(imageF))   {
                            FileInputStream fis = null;

                            try {
                                fis = new FileInputStream(getBaseContext().getFileStreamPath(imageF));   }
                            catch (FileNotFoundException e) {    e.printStackTrace();  }
                            weatherPic = BitmapFactory.decodeStream(fis);
                            Log.i( ACTIVITY_NAME,"Hey already have image. in: "+ getBaseContext().getFileStreamPath(imageF));
                        }

                        else{
                            iconTaglatch = parser.getAttributeValue(null, "icon");

                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconTaglatch + ".png");
                            weatherPic = getImage(iconUrl);

                            FileOutputStream outputStream = openFileOutput(iconTaglatch + ".png",
                                    Context.MODE_PRIVATE);
                            weatherPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i( ACTIVITY_NAME,"else doens't exist getting a picture and saving bitmap");
                        }}
                    publishProgress(100); //publishProgress() with 100
                }
            }
            catch  (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e){    e.printStackTrace();
            }
            {}
            return null;
        }
    }



    public boolean fileExistance(String fName) {

        Log.i(ACTIVITY_NAME, "In fileExistance");
        Log.i(ACTIVITY_NAME, getBaseContext().getFileStreamPath(fName).toString());
        File file = getBaseContext().getFileStreamPath(fName);//existing sun/rain image
        return file.exists();
    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {//why 200?
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) { e.printStackTrace();//in get image
            return  null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }}
}//App
