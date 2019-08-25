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

public class Addadmin extends AppCompatActivity {

    EditText firstName, lastName, emailID, userpassword, userconfirmpassword, usercontactnumber;
    Button usersingup, cancelsignup;
    String fName, lName, useremail, uPassword, cPassword, contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadmin);

        Toast.makeText(getApplicationContext(),"PLEASE ENTER THE FOLLOWING DETAILS TO GET REGISTER",Toast.LENGTH_LONG).show();
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        emailID = findViewById(R.id.email);
        userpassword = findViewById(R.id.send_message);
        userconfirmpassword = findViewById(R.id.confirmpassword);
        usercontactnumber = findViewById(R.id.contactnumber);
        usersingup = findViewById(R.id.signup);
        cancelsignup=findViewById(R.id.cancel);

        cancelsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Admin.class);
                startActivity(bridge);

            }
        });

        usersingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = firstName.getText().toString().toUpperCase();
                lName = lastName.getText().toString().toUpperCase();
                useremail = emailID.getText().toString().toLowerCase();
                uPassword = userpassword.getText().toString();
                cPassword = userconfirmpassword.getText().toString();
                contactNumber = usercontactnumber.getText().toString();

                if (uPassword.equalsIgnoreCase((cPassword))) {

                    new MyTask().execute();

                } else {
                    userpassword.setText("");
                    userpassword.setHint("PASSWORD");
                    userconfirmpassword.setText("");
                    userconfirmpassword.setHint("CONFIRMPASSWORD");
                    Toast.makeText(getApplicationContext(), "YOUR PASSWORD DOESNOT MATCH  ", Toast.LENGTH_SHORT).show();

                }
                if(useremail.contains("@")){

                }else{
                    emailID.setText("");

                    Toast.makeText(getApplicationContext(),"YOUR EMAIL SHOULD CONTAIN @ SYMBOL",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        String user_status;



        @Override

        protected Void doInBackground(Void... params) {



            DataInfo data=DataInfo.getInstance();
            URL url = null;

            String adminname=fName+" "+lName;
            try {


                url = new URL(data.getIpaddress()+"/Toolkit/mobile/app/addadmin&"
                        + adminname+ "&" + useremail + "&" + contactNumber + "&" + uPassword);

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getStatus().equals("OK")){
            Intent bridge=new Intent(getApplicationContext(),Admin.class);
            startActivity(bridge);
            Toast.makeText(getApplicationContext(),"YOU ARE SUCCESSFULLY REGISTER..THANKS",Toast.LENGTH_LONG).show();
        }else {

               emailID.setText("");
                Toast.makeText(getApplicationContext(),"email is already register",Toast.LENGTH_LONG).show();

            }
        }

    }
}
