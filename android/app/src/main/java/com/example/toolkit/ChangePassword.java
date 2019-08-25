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

public class ChangePassword extends AppCompatActivity {

    EditText oldpass,newpass,confirmpass;
    Button setpass,btn_home;
    String newpassword="";
    DataInfo datainfo=DataInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass=findViewById(R.id.oldpassword);
        newpass=findViewById(R.id.newpassword);
        confirmpass=findViewById(R.id.confirmpassword);
        setpass=findViewById(R.id.password);
        btn_home=findViewById(R.id.backtoprofile);
        setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datainfo.getPass().equals(oldpass.getText().toString()))
                {
                Toast.makeText(getApplicationContext(),
                        "Your old password is correct please enter new password",Toast.LENGTH_LONG).show();
                newpassword=newpass.getText().toString();
                if(newpassword.equals(confirmpass.getText().toString()))
                {

                    datainfo.setPass(newpassword);
                    new MyTask().execute();
                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"YOUR password didnot match ",Toast.LENGTH_LONG).show();
                        oldpass.setText("");
                        newpass.setText("");
                        confirmpass.setText("");
                    }
                }else
                    {
                        Toast.makeText(getApplicationContext(),"YOUR password didnot match ",Toast.LENGTH_LONG).show();}
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Myprofile.class);
                startActivity(bridge);
            }
        });
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        DataInfo datainfo = DataInfo.getInstance();
        String user;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/changepassword&" +
                        datainfo.getUser_id() + "&" + datainfo.getPass());

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
                setUser(obj.getString("STATUS"));

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
            if(getUser().equals("OK")){
            Intent bridge = new Intent(getApplicationContext(), Myprofile.class);
            startActivity(bridge);
            Toast.makeText(getApplicationContext(), "YOUR PASSWORD IS SUCCESSFULLY CHANGED", Toast.LENGTH_LONG).show();
        }else{
                Intent bridge = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(bridge);
                Toast.makeText(getApplicationContext(), "YOU SELECTED THE OLD PASSWORD", Toast.LENGTH_LONG).show();
            }
        }
    }
    }

