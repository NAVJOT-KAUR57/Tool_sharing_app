package com.example.toolkit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Myprofile extends AppCompatActivity {

    EditText user_fn,user_ln,user_contactnumber;
    Button change_password,my_recievemessage,btn_home2,btn_update;
    String stn_fn,stn_ln,stn_pass="jk",stn_cn;
    TextView user_id,user_password,user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        btn_update=findViewById(R.id.button2);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stn_fn=user_fn.getText().toString();
                stn_ln=user_ln.getText().toString();
                stn_cn=user_contactnumber.getText().toString();
               // stn_pass=user_password.getText().toString();
                new MyTask2().execute();
            }
        });
        user_fn=findViewById(R.id.firstname);
        user_ln=findViewById(R.id.lastname);
        user_email=findViewById(R.id.email);

        user_contactnumber=findViewById(R.id.contactnumber);
        change_password =findViewById(R.id.changepassword);
        user_id=findViewById(R.id.userId);
        Button user_signout=findViewById(R.id.signout);
        my_recievemessage=findViewById(R.id.recieve_btn);
        my_recievemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),RecieveMessage.class);
                startActivity(bridge);
            }
        });

        user_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(bridge);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(bridge);
            }
        });
        new MyTask().execute();

        btn_home2=findViewById(R.id.home2);
        btn_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),ListofPost.class);
                startActivity(bridge);
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
        String fn,ln,emailid,cnumber,pass;

        @Override

        protected Void doInBackground(Void... params) {



            DataInfo datainfo=DataInfo.getInstance();


            fulluid=datainfo.getUser_id();

            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/myprofile&"+ fulluid);

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

                fn=obj.getString("firstname");
                ln=obj.getString("lastname");
                emailid=obj.getString("email");
                cnumber=obj.getString("contactnumber");
                pass=obj.getString("Password");

                datainfo.setFirst_name(fn);
                datainfo.setLast_name(ln);
                datainfo.setContact_number(cnumber);
                datainfo.setEmail_id(emailid);
                datainfo.setPassword(pass);



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
            System.out.println(fn+ln+emailid+cnumber+pass);
            user_fn.setText(fn);
            user_ln.setText(ln);
            user_email.setText(emailid);
            user_contactnumber.setText(cnumber);

            user_id.setText(fulluid+"");

        }

    }


    private class MyTask2 extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname,user_status;

        DataInfo data=DataInfo.getInstance();


        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL(data.getIpaddress()+"/Toolkit/mobile/app/update&"
                        + stn_fn + "&" + stn_ln + "&" + stn_cn  + "&" + data.getUser_id());

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
                if(obj.getString("STATUS").equals("WRONG")) {
                    Toast.makeText(getApplicationContext(),"EMAIL is Already Register",Toast.LENGTH_LONG).show();
                    Intent bridge=new Intent(getApplicationContext(),Registration.class);
                    startActivity(bridge);
                }else {
                    userID=obj.getInt("User_id");
                }


                System.out.println(userID);
                DataInfo datainfo=DataInfo.getInstance();
                datainfo.setUser_id(userID);


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
            Intent bridge=new Intent(getApplicationContext(),ListofPost.class);
            startActivity(bridge);
            Toast.makeText(getApplicationContext(),"YOU ARE SUCCESSFULLY UPDATED..THANKS",Toast.LENGTH_LONG).show();
        }

    }
}
