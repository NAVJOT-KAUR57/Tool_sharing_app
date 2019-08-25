package com.example.toolkit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AddTool extends AppCompatActivity {

    DataInfo datainfo=DataInfo.getInstance();

    Button tool,category, back;
    EditText edt_tool,edt_cat;
    String stn_tool,stn_category=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tool);

        edt_cat=findViewById(R.id.editText_category);
        edt_tool=findViewById(R.id.editText_tool);
        tool=findViewById(R.id.btn_tool);
        category=findViewById(R.id.btn_category);
        back=findViewById(R.id.backtosignin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Postadd.class);
                startActivity(bridge);

            }
        });



        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stn_tool=edt_tool.getText().toString();
                new MyTask().execute();
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stn_category=edt_cat.getText().toString();
                new MyTask2().execute();
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname,user_status;

        public int getFulluid() {
            return fulluid;
        }

        public void setFulluid(int fulluid) {
            this.fulluid = fulluid;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        @Override

        protected Void doInBackground(Void... params) {

            DataInfo data=DataInfo.getInstance();

            URL url = null;

            try {


                url = new URL(data.getIpaddress()+"/Toolkit/mobile/app/addtools&"
                        + stn_tool+ "&" + stn_category + "&" + datainfo.getPost_id() );

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

                user_status=obj.getString("STATUS");
                datainfo.setTool_id(obj.getInt("tool_id"));

                datainfo.setStatus(user_status);




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
            if(datainfo.getStatus().equals("OK")){
                edt_tool.setText(stn_tool);
                Toast.makeText(getApplicationContext(),"YOU ARE SUCCESSFULLY ENTERED..THANKS",Toast.LENGTH_LONG).show();
            }else {

                edt_tool.setText("");
            }
        }

    }


    private class MyTask2 extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname,user_status;

        public int getFulluid() {
            return fulluid;
        }

        public void setFulluid(int fulluid) {
            this.fulluid = fulluid;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {


                url = new URL("http://192.168.2.57:8080/Toolkit/mobile/app/addcategory&"
                        + stn_category+ "&" +datainfo.getTool_id());

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

                user_status=obj.getString("STATUS");

                datainfo.setStatus(user_status);




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
            if(datainfo.getStatus().equals("OK")){
                Intent bridge=new Intent(getApplicationContext(),ListofPost.class);
                startActivity(bridge);
                Toast.makeText(getApplicationContext(),"YOU ARE SUCCESSFULLY ENTERED..THANKS",Toast.LENGTH_LONG).show();
            }else {

                edt_cat.setText("");


            }
        }

    }
}
