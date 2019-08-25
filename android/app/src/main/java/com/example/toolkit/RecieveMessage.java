package com.example.toolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class RecieveMessage extends AppCompatActivity {

    TableLayout tb;
    TableRow tr1, tr2;
    Button back;
    TextView fullname, email_id, reciever_message,tx1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_message);

        tb = findViewById(R.id.table);
        new MyTask().execute();

        back = findViewById(R.id.backtoprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),Myprofile.class);
                startActivity(bridge);

            }
        });
    }





    private class MyTask extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname;

        @SuppressLint("WrongThread")


        DataInfo datainfo =DataInfo.getInstance();
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/recievemessage&"+ datainfo.getUser_id());

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
                if(obj.getString("Status").equals("OK")){

                final JSONArray postarray = obj.getJSONArray("DATA");
                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {


                        tr1 = new TableRow(getApplicationContext());

                        tr2 = new TableRow(getApplicationContext());
                        fullname = new TextView(getApplicationContext());
                        fullname.setText(" FullName ");
                        fullname.setTextSize(20);
                        fullname.setAlpha(1);
                        fullname.setTextColor(Color.BLACK);
                        //email.setTextColor(R.color.colorPrimaryDark);
                        tr1.addView(fullname);


                        email_id = new TextView(getApplicationContext());
                        email_id.setText(" email_id ");
                        email_id.setAlpha(1);
                        email_id.setTextSize(20);

                        email_id.setTextColor(Color.BLACK);
                        //  firstName.setTextColor(Color.Black);

                        //tr1.addView(email_id);

                        tx1 = new TextView(getApplicationContext());


                        reciever_message = new TextView(getApplicationContext());
                        reciever_message.setText("    " + "reciever_message ");
                        reciever_message.setTextSize(20);
                        reciever_message.setAlpha(1);
                        reciever_message.setTextColor(Color.BLACK
                        );
                        email_id.setTypeface(null, Typeface.BOLD_ITALIC);

                        fullname.setTypeface(null, Typeface.BOLD_ITALIC);
                        reciever_message.setTypeface(null, Typeface.BOLD_ITALIC);


                        tr1.addView(reciever_message);

                        tr2.addView(tx1);
                        tb.addView(tr1);

                        tb.addView(tr2);
                        String fn = null, ln = null, emailid = null, recimessage = null;

                        int message_id = 0;


                        JSONObject arrayobj = null;

                        for (int i = 0; i < postarray.length(); i++) {
                            tr1 = new TableRow(getApplicationContext());
                            tr2 = new TableRow(getApplicationContext());
                            fullname = new TextView(getApplicationContext());
                            email_id = new TextView(getApplicationContext());
                            reciever_message = new TextView(getApplicationContext());
                            tx1 = new TextView(getApplicationContext());
                            reciever_message.setTextColor(Color.BLACK);

                            email_id.setTextColor(Color.BLACK);

                            fullname.setTextColor(Color.BLACK);
                            try {
                                arrayobj = postarray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                fn = arrayobj.getString("FIRSTNAME");

                                ln = arrayobj.getString("LASTNAME");


                                fullname.setTextSize(20);
                                fullname.setAlpha(1);
                                email_id.setTextSize(20);
                                email_id.setAlpha(1);
                                reciever_message.setTextSize(20);
                                reciever_message.setAlpha(0.99f);

                                fullname.setText(fn + " " + ln);
                                tr1.addView(fullname);


                                emailid = arrayobj.getString("EMAIL_ID");
                                email_id.setText(emailid + "  ");
                                // tr1.addView(email_id);
                                recimessage = arrayobj.getString("Message");
                                reciever_message.setText("    " + recimessage);
                                tr1.addView(reciever_message);
                                //tr2.addView(tx1);

                                message_id = arrayobj.getInt("MESSAGE_ID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            tr1.setAlpha(1);
                            tr2.setAlpha(1);

                            tb.addView(tr1);
                            tb.addView(tr2);
                            //tablelayout.addView(tr2);


                            final int finalMessage_id = message_id;
                            tr1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent bridge = new Intent(getApplicationContext(), Singlemessage.class);
                                    bridge.putExtra("messageid", finalMessage_id);
                                    startActivity(bridge);
                                }
                            });


                            }


                    }
                });
                }
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
