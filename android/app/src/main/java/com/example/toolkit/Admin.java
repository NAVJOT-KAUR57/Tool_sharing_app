package com.example.toolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    Button add_admin,remove_post,remove_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        add_admin=findViewById(R.id.addadmin);
        remove_user=findViewById(R.id.removeuser2);
        remove_post=findViewById(R.id.removepost);
        add_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Addadmin.class);
                startActivity(intent);
            }
        });


        Button admin_signout=findViewById(R.id.signout);
        admin_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bridge=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(bridge);
            }
        });


                remove_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),RemovePost.class);
                        startActivity(intent);

                    }
                });

                remove_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),RemoveUser.class);
                        startActivity(intent);

                    }
                });
    }
}
