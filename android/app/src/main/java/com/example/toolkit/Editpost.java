package com.example.toolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Editpost extends AppCompatActivity {

    EditText edt_desc,edt_price,edt_cat,edt_loc,edt_time;
    Button editpostBtn, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpost);

        edt_desc=findViewById(R.id.desc_edt);
        edt_cat=findViewById(R.id.cat_edt);
        edt_loc=findViewById(R.id.loc_edt);
        edt_time=findViewById(R.id.starttime_edt);
        edt_price=findViewById(R.id.price_edt);

        back = findViewById(R.id.backtoviewpost);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Viewpost.class);
                startActivity(bridge);

            }
        });

        editpostBtn=findViewById(R.id.btn_edit);
        new MyTask().execute();
        editpostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stn_desc=edt_desc.getText().toString();
                stn_loc=edt_loc.getText().toString();
                stn_time=edt_time.getText().toString();
                doub_price=Double.parseDouble(edt_price.getText().toString());
                stn_cat=edt_cat.getText().toString();
                new MyTask2().execute();
            }
        });

    }
    String stn_desc;
    String stn_loc;
    String stn_cat;
    String stn_time;
    double doub_price;
    final DataInfo datainfo=DataInfo.getInstance();
    private class MyTask extends AsyncTask<Void, Void, Void> {




        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
        }

        String userstatus;

        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL("http://192.168.2.57:8080/Toolkit/mobile/app/viewpost&"+datainfo.getPost_id());

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                final StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                final JSONObject obj = new JSONObject(response.toString());

                setUserstatus(obj.getString("Status"));
                stn_desc=obj.getString("DESCRIPTION");
                stn_loc=obj.getString("LOCATION");
                stn_cat=obj.getString("CATEGORY_NAME");
                stn_time=obj.getString("STARTTIME");
                doub_price=obj.getDouble("PRICE");
                String subtime[]=stn_time.split(":");
                stn_time=subtime[0]+":"+subtime[1];



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getUserstatus().equals("OK")){
                edt_desc.setText(stn_desc);
                edt_loc.setText(stn_loc);
                edt_price.setText(doub_price+" ");
                edt_time.setText(stn_time);
                edt_cat.setText(stn_cat);

            }



        }

    }

    private class MyTask2 extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        String user_status;



        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/editposts&"
                        + stn_desc + "&" + stn_loc + "&" + doub_price + "&" + stn_cat + "&" + stn_time+"&"+datainfo.getPost_id());

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());


                JSONObject obj = new JSONObject(response.toString());
                int userID=0;
                // user_status=obj.getString("STATUS");
                setUser_status(obj.getString("Status"));




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(getUser_status().equals("OK")) {
                Toast.makeText(getApplicationContext(),"YOUR POST IS EDITED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                Intent bridge=new Intent(getApplicationContext(),Viewpost.class);
                startActivity(bridge);
            }

        }

    }
}
