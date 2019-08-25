package com.example.toolkit;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import java.util.logging.Formatter;

public class MainActivity extends AppCompatActivity {
    EditText userEmail,userPassword;
    Button userLogin,singUp,guest,admin;

    String email,password;
    DataInfo dataInfo=DataInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataInfo.setIpaddress("http://192.168.2.57:8080");

        Toast.makeText(getApplicationContext(), "PLEASE ENTER AND PASSWORD FOR LOGIN", Toast.LENGTH_LONG).show();

        guest=findViewById(R.id.forget_Password);


        admin=findViewById(R.id.admin_login);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Adminlogin.class);
                startActivity(intent);
            }
        });
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.send_message);
        userLogin = findViewById(R.id.login);
        singUp = findViewById(R.id.registration);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(bridge
                );
            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge = new Intent(getApplicationContext(),Registration.class);

                startActivity(bridge);
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = userEmail.getText().toString();
                password = userPassword.getText().toString();
                MyTask my = new MyTask();
                my.execute();

                // Toast.makeText(getApplicationContext(), "You are successfully login", Toast.LENGTH_SHORT).show();
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

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/login&" + email  + "&" + password );

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


            System.out.println("hyyyyyyyyyyyyyyyyyyyyyyyyyy"+getUser_status()+email);


                if (getUser_status().equals("OK")) {
                    Intent bridge = new Intent(getApplicationContext(), ListofPost.class);

                    Toast.makeText(getApplicationContext(), "You are successfully login", Toast.LENGTH_SHORT).show();
                    startActivity(bridge);
                } else {
                    Toast.makeText(getApplicationContext(), "PLEASE RECHECK YOUR EMAIL AND PASSWORD", Toast.LENGTH_LONG).show();
                }
            }

        }

    }




