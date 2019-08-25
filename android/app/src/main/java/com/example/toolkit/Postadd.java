package com.example.toolkit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;

public class Postadd extends AppCompatActivity {

    DataInfo datainfo=DataInfo.getInstance();
    EditText description, category, startdate, location, price, start_time,toolname;
    Button post, user_signout, home;
    String post_description, post_category, stn_tool,post_startdate,post_location;
    int  stTime;
    double post_price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postadd);
        home = findViewById(R.id.home);
        toolname=findViewById(R.id.edt_tool);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge = new Intent(getApplicationContext(), ListofPost.class);
                startActivity(bridge);
            }
        });


        description = findViewById(R.id.desc_editText);
        category = findViewById(R.id.category_edittext);
        startdate = findViewById(R.id.startdate);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int  mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(Postadd.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                startdate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth   );

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });
        location = findViewById(R.id.loc_editText);
        price = findViewById(R.id.price);
        post = findViewById(R.id.post);
        start_time = findViewById(R.id.starttime_editText);


        Toast.makeText(getApplicationContext(), "PLEASE ENTER thE FOllowing DETAILSTO POST ADD", Toast.LENGTH_LONG).show();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                post_description = description.getText().toString().toUpperCase().toUpperCase();
                post_category = category.getText().toString().toUpperCase().toUpperCase();
                post_startdate = startdate.getText().toString();
                String[] startdatesplit = post_startdate.split("-");
                int year = Integer.parseInt(startdatesplit[0]);
                int month = Integer.parseInt(startdatesplit[1]);
                int date = Integer.parseInt(startdatesplit[2]);



                post_location = location.getText().toString();
                post_price = Double.parseDouble(price.getText().toString());
                stTime = Integer.parseInt(start_time.getText().toString());

                post_startdate = post_startdate +" "+ stTime;
                    stn_tool=toolname.getText().toString();
                        new MyTask().execute();
            }
        });



    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        int uid;

        String post_status;

        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            DataInfo datainfo = DataInfo.getInstance();
            try {
                uid = datainfo.getUser_id();

                url = new URL(datainfo.getIpaddress()+"/Toolkit/mobile/app/addpost&"
                        + post_description + "&" + post_location +
                        "&" + post_price + "&" + post_category + "&" + post_startdate  + "&" + uid+"&"+stn_tool);

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
                post_status = obj.getString("STATUS");
                datainfo.setPost_id(obj.getInt("Post_id"));


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
            if (post_status.equals("OK")) {
                Intent bridge = new Intent(getApplicationContext(), ListofPost.class);
                Toast.makeText(getApplicationContext(), "Your post is succesfully added", Toast.LENGTH_LONG).show();
                startActivity(bridge);
            } else {

                description.setText("");
                category.setText("");
                startdate.setText("");
                location.setText("");
                price.setText("");
                post.setText("");
                start_time.setText("");



                Toast.makeText(getApplicationContext(), "You are adding some information wrong please RE-Enter", Toast.LENGTH_LONG).show();
            }
        }

    }
}
