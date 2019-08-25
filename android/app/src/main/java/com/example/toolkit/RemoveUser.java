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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class RemoveUser extends AppCompatActivity {


    TextView userName, userEmail, contactNumber, tx1;
    TableLayout tablelayout;
    TableRow tr1, tr2;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);
        tablelayout = findViewById(R.id.tbLayout);

        new MyTask().execute();

        btn_home=findViewById(R.id.home6);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Admin.class);
                startActivity(bridge);
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {


        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            final DataInfo datainfo = DataInfo.getInstance();
            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/userList");

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

                final JSONArray postarray = obj.getJSONArray("DATA");
                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {


                        tr1 = new TableRow(getApplicationContext());

                        tr2 = new TableRow(getApplicationContext());


                        tablelayout.removeAllViews();

                        userName = new TextView(getApplicationContext());
                        userName.setText(" userName");
                        userName.setTextSize(18);
                        userName.setAlpha(1);
                        userName.setTextColor(Color.BLACK);
                        //email.setTextColor(R.color.colorPrimaryDark);
                        tr1.addView(userName);


                        userEmail = new TextView(getApplicationContext());
                        userEmail.setText(" userEmail ");
                        userEmail.setAlpha(1);
                        userEmail.setTextSize(18);

                        userEmail.setTextColor(Color.BLACK);


                        tr1.addView(userEmail);

                        tx1 = new TextView(getApplicationContext());


                        contactNumber = new TextView(getApplicationContext());
                        contactNumber.setText(" contactNumber ");
                        contactNumber.setTextSize(18);
                        contactNumber.setAlpha(1);
                        contactNumber.setTextColor(Color.BLACK);
                        userEmail.setTypeface(null, Typeface.BOLD_ITALIC);

                        userName.setTypeface(null, Typeface.BOLD_ITALIC);
                        contactNumber.setTypeface(null, Typeface.BOLD_ITALIC);


                        tr1.addView(contactNumber);

                        tr2.addView(tx1);
                        tablelayout.addView(tr1);

                        tablelayout.addView(tr2);

                        String uName = null, cName = null, cNUmber = null, enddate;
                        int post_id = 0, user_id = 0;


                        JSONObject arrayobj = null;

                        for (int i = 0; i < postarray.length(); i++) {
                            tr1 = new TableRow(getApplicationContext());
                            tr2 = new TableRow(getApplicationContext());
                            userName = new TextView(getApplicationContext());
                            contactNumber = new TextView(getApplicationContext());
                            userEmail = new TextView(getApplicationContext());
                            tx1 = new TextView(getApplicationContext());
                            contactNumber.setTextColor(Color.BLACK);
                            userEmail.setTextColor(Color.BLACK);
                            userName.setTextColor(Color.BLACK);

                            try {
                                arrayobj = postarray.getJSONObject(i);

                                userName.setTextSize(20);
                                userName.setAlpha(1);
                                uName = arrayobj.getString("USERNAME");
                                userName.setText(uName + "  ");
                                tr1.addView(userName);

                                userEmail.setTextSize(20);
                                userEmail.setAlpha(1);
                                cName = arrayobj.getString("EMAIL");
                                userEmail.setText(cName + "  ");
                                tr1.addView(userEmail);

                                contactNumber.setTextSize(20);
                                contactNumber.setAlpha(0.99f);
                                cNUmber = arrayobj.getString("CONTACTNUMBER");
                                contactNumber.setText(cNUmber + "  ");
                                tr1.addView(contactNumber);

                             //   post_id = arrayobj.getInt("POST_ID");

                                user_id = arrayobj.getInt("USER_ID");

                                tr2.addView(tx1);

                                tr1.setAlpha(1);
                                tr2.setAlpha(1);

                                tablelayout.addView(tr1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //final int finalPost_id = post_id;
                            final int finaluser_id = user_id;

                            tr1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //datainfo.setPost_id(finalPost_id);
                                    datainfo.setUser_id(finaluser_id);

                                    new MyTask2().execute();
                                }
                            });


                        }


                    }
                });
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

        }
    }


    private class MyTask2 extends AsyncTask<Void, Void, Void> {


        String fn,ln,email,datetime,cn,desc,loc,price,toolname,catname;

        double the_price;

        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            DataInfo dataInfo=DataInfo.getInstance();


            URL url = null;

            try {

                url = new URL(dataInfo.getIpaddress()+"/Toolkit/mobile/app/deleteuser&"+dataInfo.getUser_id());

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

            new MyTask().execute();
        }

    }
}