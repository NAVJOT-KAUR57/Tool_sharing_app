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

public class Adminlogin extends AppCompatActivity {

    EditText add_email,add_password;
    String email_add,password_add;
    Button login, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        add_email=findViewById(R.id.adminemail);
        add_password=findViewById(R.id.adminpassword);
        login=findViewById(R.id.login);
        back = findViewById(R.id.backtosignin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(bridge);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email_add=add_email.getText().toString();
                password_add=add_password.getText().toString();
                new MyTask().execute();
            }
        });
    }



    private class MyTask extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String user_status;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public int getFulluid() {
            return fulluid;
        }

        public void setFulluid(int fulluid) {
            this.fulluid = fulluid;
        }


        @Override

        protected Void doInBackground(Void... params) {


            DataInfo datainfo=DataInfo.getInstance();


            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/adminlogin&" + email_add  + "&" + password_add );

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

                user_status=obj.getString("Status");
                setUser_status(user_status);
                int userId = obj.getInt("User_id");
                String pass=obj.getString("Password");

                //setFulluid(uid);
                System.out.println(userId+" and password "+pass);

                datainfo.setUser_id(userId);

                datainfo.setPass(pass);


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


            System.out.println(""+getUser_status()+add_email);


            if (getUser_status().equals("OK")) {
                Intent bridge = new Intent(getApplicationContext(), Admin.class);

                Toast.makeText(getApplicationContext(), "You are successfully login", Toast.LENGTH_SHORT).show();
                startActivity(bridge);
            } else {
                Toast.makeText(getApplicationContext(), "PLEASE RECHECK YOUR EMAIL AND PASSWORD", Toast.LENGTH_LONG).show();
            }
        }

    }

}






