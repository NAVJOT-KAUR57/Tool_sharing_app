package com.example.toolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Singlemessage extends AppCompatActivity {

    TextView fn,date,email,cn,occ,ln,mess;
    EditText edt_reply;
    Button list,reply;
    String stn_reply;
    int reciever_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessage);
        new MyTask().execute();
        fn=findViewById(R.id.tv_fn);
        date=findViewById(R.id.tv_date);
        email=findViewById(R.id.tv_email);
        cn=findViewById(R.id.tv_contactnumber);
        mess=findViewById(R.id.tv_message);
        list=findViewById(R.id.backtosignin);
        ln=findViewById(R.id.tv_ln);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),RecieveMessage.class);
                startActivity(bridge);

            }
        });



        reply=findViewById(R.id.button);
        edt_reply=findViewById(R.id.editText);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stn_reply=edt_reply.getText().toString();
                new MyTask2().execute();
            }
        });

    }


    String stn_email;

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String user_status;
        int message_id;
        String stn_mess,stn_fn,stn_ln,stn_date,stn_cn;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }




        @SuppressLint("WrongThread")


        DataInfo datainfo =DataInfo.getInstance();
        protected Void doInBackground(Void... params) {

            Intent bridge=getIntent();
            Bundle bundle=bridge.getExtras();
            message_id=bundle.getInt("messageid");

            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/message&"+ message_id);

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
                setUser_status(obj.getString("Status"));
                stn_cn=obj.getString("CONTACTNUMBER");
                stn_fn=obj.getString("FIRSTNAME");
                stn_ln=obj.getString("LASTNAME");
                stn_email=obj.getString("EMAIL_ID");
                stn_date=obj.getString("date");
                stn_mess=obj.getString("Message");
                reciever_id=obj.getInt("SENDER_ID");

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

            if(getUser_status().equals("OK")){
            fn.setText("FIRSTNAME "+stn_fn);
            email.setText("EMAIL "+stn_email);
            date.setText("DATE "+stn_date);
            cn.setText("CN "+stn_cn);
            mess.setText("MESSAGE "+stn_mess);
            ln.setText("LASTNAME "+stn_ln);
        }

        }

    }


    private class MyTask2 extends AsyncTask<Void, Void, Void> {

        String user_status=null;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        @Override

        protected Void doInBackground(Void... params) {


            DataInfo datainfo=DataInfo.getInstance();
            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/message&" + stn_reply+ "&"+datainfo.getUser_id()+
                        "&"+stn_email);

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
                setUser_status(obj.getString("STATUS"));


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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(getUser_status().equals("OK")){
                Intent intent=new Intent(getApplicationContext(),Myprofile.class);
                startActivity(intent);
            }

            else{


                edt_reply.setText("");
                Toast.makeText(getApplicationContext(), "Please put a valid Email_id", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
