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

public class ForgetPassword extends AppCompatActivity {

    DataInfo dataInfo=DataInfo.getInstance();
    EditText email,userid,pass,confirmpassword;
    Button setpass,btn_home;
    String newpassword,email_id;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btn_home=findViewById(R.id.back1);
        email = findViewById(R.id.email);
        userid = findViewById(R.id.user_id);
        pass = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpass);
        setpass = findViewById(R.id.setpass);
        setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_id=Integer.parseInt(userid.getText().toString());
                email_id=email.getText().toString();
                newpassword=pass.getText().toString();
                if(newpassword.equals(confirmpassword.getText().toString()))
                {


                    new MyTask().execute();
                    dataInfo.setPass(newpassword);
                    dataInfo.setUser_id(user_id);
                    dataInfo.setEmail_id(email_id);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"YOUR password didnot match ",Toast.LENGTH_LONG).show();
                    userid.setText("");
                    email.setText("");
                    pass.setText("");
                    confirmpassword.setText("");
                }

            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(bridge);
            }
        });

    }
        private class MyTask extends AsyncTask<Void, Void, Void> {

        String status,message;
            DataInfo datainfo = DataInfo.getInstance();

            @Override

            protected Void doInBackground(Void... params) {


                URL url = null;

                try {

                    url = new URL(dataInfo.getIpaddress()+"/Toolkit/mobile/app/forgetpassword&" +
                            datainfo.getUser_id()+"&"+dataInfo.getEmail_id() + "&" + datainfo.getPass());

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

                    status=obj.getString("STATUS");
                    message=obj.getString("Message");
                    dataInfo.setStatus(status);
                    dataInfo.setMessage(message);

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
                if(dataInfo.getStatus().equals("OK")) {
                    Intent bridge = new Intent(getApplicationContext(), Myprofile.class);
                    startActivity(bridge);
                    Toast.makeText(getApplicationContext(), "YOUR PASSWORD IS SUCCESSFULLY SET", Toast.LENGTH_LONG).show();
                }else if(dataInfo.getStatus().equals("WRONG"))
                {
                    userid.setText("");
                    email.setText("");
                    pass.setText("");
                    confirmpassword.setText("");
                    Toast.makeText(getApplicationContext(), dataInfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }
        }
    }
