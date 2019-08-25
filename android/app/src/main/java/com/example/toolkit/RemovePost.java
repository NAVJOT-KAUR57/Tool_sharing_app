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

public class RemovePost extends AppCompatActivity {

    TextView toolName, categoryName, startTime,tx1;
    TableLayout tablelayout;
    TableRow tr1,tr2;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_post);

        tablelayout=findViewById(R.id.tbtb);

        new MyTask().execute();
        btn_home=findViewById(R.id.home7);
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

            final DataInfo datainfo=DataInfo.getInstance();
            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/recievedeletepost");

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


                        tablelayout.removeAllViews();
                        tr1 = new TableRow(getApplicationContext());

                        tr2 = new TableRow(getApplicationContext());
                        toolName = new TextView(getApplicationContext());
                        toolName.setText(" ToolName");
                        toolName.setTextSize(18);
                        toolName.setAlpha(1);
                        toolName.setTextColor(Color.BLACK);
                        //email.setTextColor(R.color.colorPrimaryDark);
                        tr1.addView(toolName);


                        categoryName = new TextView(getApplicationContext());
                        categoryName.setText(" CategoryName ");
                        categoryName.setAlpha(1);
                        categoryName.setTextSize(18);

                        categoryName.setTextColor(Color.BLACK);


                        tr1.addView(categoryName);

                        tx1 = new TextView(getApplicationContext());


                        startTime = new TextView(getApplicationContext());
                        startTime.setText(" StartTime ");
                        startTime.setTextSize(18);
                        startTime.setAlpha(1);
                        startTime.setTextColor(Color.BLACK);
                        categoryName.setTypeface(null, Typeface.BOLD_ITALIC);

                        toolName.setTypeface(null, Typeface.BOLD_ITALIC);
                        startTime.setTypeface(null, Typeface.BOLD_ITALIC);


                        tr1.addView(startTime);

                        tr2.addView(tx1);
                        tablelayout.addView(tr1);

                        tablelayout.addView(tr2);

                        String tool_name = null, category_name = null, startdate = null, enddate;
                        int post_id = 0,tool_id=0,category_id=0;


                        JSONObject arrayobj = null;

                        for (int i = 0; i < postarray.length(); i++) {
                            tr1 = new TableRow(getApplicationContext());
                            tr2 = new TableRow(getApplicationContext());
                            toolName = new TextView(getApplicationContext());
                            startTime = new TextView(getApplicationContext());
                            categoryName=new TextView(getApplicationContext());
                            tx1 = new TextView(getApplicationContext());
                            startTime.setTextColor(Color.BLACK);
                            categoryName.setTextColor(Color.BLACK);
                            toolName.setTextColor(Color.BLACK);

                            try {
                                arrayobj = postarray.getJSONObject(i);

                                toolName.setTextSize(20);
                                toolName.setAlpha(1);
                                tool_name = arrayobj.getString("TOOL_NAME");
                                toolName.setText(tool_name + "  ");
                                tr1.addView(toolName);

                                categoryName.setTextSize(20);
                                categoryName.setAlpha(1);
                                category_name = arrayobj.getString("CATEGORY_NAME");
                                categoryName.setText(category_name + "  ");
                                tr1.addView(categoryName);

                                startTime.setTextSize(20);
                                startTime.setAlpha(0.99f);
                                startdate = arrayobj.getString("STARTTIME");
                                startTime.setText(startdate + "  ");
                                tr1.addView(startTime);

                                post_id = arrayobj.getInt("POST_ID");
                                category_id=arrayobj.getInt("CATEGORY_ID");
                                tool_id=arrayobj.getInt("TOOL_ID");

                                tr2.addView(tx1);

                                tr1.setAlpha(1);
                                tr2.setAlpha(1);

                                tablelayout.addView(tr1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //                          tablelayout.addView(tr2);
                            //tablelayout.addView(tr2);

                            final int finalPost_id = post_id;
                            final int finalTool_id = tool_id;
                            final int finalCategory_id = category_id;
                            tr1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    datainfo.setPost_id(finalPost_id);
                                    datainfo.setTool_id(finalTool_id);
                                    datainfo.setCategory_id(finalCategory_id);
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

                url = new URL(dataInfo.getIpaddress()+"/Toolkit/mobile/app/deletepost&"+dataInfo.getPost_id()+"&"+dataInfo.getTool_id()+"&"+dataInfo.getCategory_id());

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
