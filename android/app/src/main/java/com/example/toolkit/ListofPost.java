package com.example.toolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ListofPost extends AppCompatActivity {

    TableLayout tablelayout;
    TableRow tr1, tr2;
    TextView toolName, categoryName, startTime,tx1;
    Button post, myprofile,edit_post;
    Spinner spinner;
    String thecat;
    Categoryadapter cat;


    public String getThecat() {
        return thecat;
    }

    public void setThecat(String thecat) {
        this.thecat = thecat;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_post);
        tablelayout = findViewById(R.id.tblayout);
        myprofile = findViewById(R.id.myprofile);
        edit_post=findViewById(R.id.editpost);

        Button user_signout=findViewById(R.id.signout);

//        Toast.makeText(getApplicationContext(), "PLEASE SELECT OF THE ITEM OF LIST VIEW TO EDIT YOUR POST", Toast.LENGTH_LONG).show();


        user_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(bridge);
            }
        });
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge = new Intent(getApplicationContext(), Myprofile.class);
                startActivity(bridge);
            }
        });

        post = findViewById(R.id.postadd);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge = new Intent(getApplicationContext(), Postadd.class);
                startActivity(bridge);
            }
        });
        edit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Viewpost.class);
                startActivity(bridge);
            }
        });


        spinner=findViewById(R.id.spinner);


        new MyTask2().execute();


    }


    private class MyTask extends AsyncTask<Void, Void, Void> {


        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {


            final DataInfo datainfo=DataInfo.getInstance();
            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/recieveposts&"+getThecat());

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
                        int post_id = 0;


                        JSONObject arrayobj = null;

                        for (int i = 0; i < postarray.length(); i++) {
                            tr1 = new TableRow(getApplicationContext());
                            tr2 = new TableRow(getApplicationContext());
                            toolName = new TextView(getApplicationContext());
                            startTime = new TextView(getApplicationContext());
                            categoryName = new TextView(getApplicationContext());
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
                            tr1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    datainfo.setPost_id(finalPost_id);
                                    Intent bridge = new Intent(getApplicationContext(), Singlepost.class);

                                    startActivity(bridge);
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
        String[] catarray;
        JSONArray jsonarr;
        String cat_name;JSONObject jsonObject;
        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            final DataInfo datainfo=DataInfo.getInstance();
            URL url = null;



            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/recievecategory");

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


                jsonarr=obj.getJSONArray("DATA");
                catarray=new String[jsonarr.length()+1];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                for(int i=0;i<jsonarr.length();i++){
                    try {
                        jsonObject=jsonarr.getJSONObject(i);
                        cat_name=jsonObject.getString("CATEGORY_NAME");

                     catarray[i+1]=cat_name;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                        System.out.println("khiii"+catarray.length);

                catarray[0]="PLEASE SELECT THE CATEGORY";
                cat=new Categoryadapter(getApplicationContext(),android.R.layout.simple_list_item_1,catarray);

                //cat.addAll(catarray);
                spinner.setAdapter(cat);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(spinner.getSelectedItem().equals("PLEASE SELECT THE CATEGORY")){

                        }else {
                            String temp=spinner.getSelectedItem().toString();
                            setThecat(temp);
                            new MyTask().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });

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
    }