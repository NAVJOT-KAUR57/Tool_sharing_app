package com.example.toolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Singlepost extends AppCompatActivity {


    TextView tv_fn,tv_ln,tv_email,tv_des,tv_loc,tv_toolname,tv_catname,tv_price,tv_starttime,tv_cn;
    int post_id;
    Button lis,mess,btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepost);
        tv_fn=findViewById(R.id.edt_fn);
        tv_ln=findViewById(R.id.edt_ln);
        tv_email=findViewById(R.id.edt_email);
        tv_catname=findViewById(R.id.edt_cat);
        tv_toolname=findViewById(R.id.edt_tool);
        tv_des=findViewById(R.id.edt_des);
        tv_loc=findViewById(R.id.edt_loc);
        tv_price=findViewById(R.id.edt_price);
        tv_starttime=findViewById(R.id.edt_date);
        tv_cn=findViewById(R.id.edt_cn);
        btn_home=findViewById(R.id.home9);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),ListofPost.class);
                startActivity(bridge);
            }
        });

        lis=findViewById(R.id.btn_list);
        lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ListofPost.class);
                startActivity(intent);
            }
        });
        mess=findViewById(R.id.btn_message);
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Message.class);
                startActivity(intent);
            }
        });



        new MyTask().execute();

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {


        String fn,ln,email,datetime,cn,desc,loc,price,toolname,catname;

        double the_price;

        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            DataInfo dataInfo=DataInfo.getInstance();


            URL url = null;

            try {

                url = new URL(dataInfo.getIpaddress()+"/Toolkit/mobile/app/recievepost&"+dataInfo.getPost_id());

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

                fn=obj.getString("FIRSTNAME").trim();
                ln=obj.getString("LASTNAME").trim();
                email=obj.getString("EMAIL");
                toolname=obj.getString("TOOL_NAME");
                catname=obj.getString("CATEGORY_NAME");
                desc=obj.getString("DESCRIPTION");
                loc=obj.getString("LOCATION");
                cn=obj.getString("CONTACTNUMBER");
                datetime=obj.getString("STARTTIME");
                the_price=obj.getDouble("PRICE");



                dataInfo.setEmail_id(email);

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

            tv_fn.setText(fn);
            tv_ln.setText(ln);
            tv_email.setText(email);
            tv_catname.setText(catname);
            tv_toolname.setText(toolname);
            tv_des.setText(desc);
            tv_loc.setText(loc);
            tv_starttime.setText(datetime);
            tv_cn.setText(cn);
            tv_price.setText(the_price+"");
        }

    }
}
