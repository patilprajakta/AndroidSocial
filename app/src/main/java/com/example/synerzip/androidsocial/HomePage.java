package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.login.LoginManager;

/**
 * Created by Prajakta Patil on 10/11/16.
 */

public class HomePage extends AppCompatActivity {
        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.homepage);
                (findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         LoginManager.getInstance().logOut();
                        Intent intent = new Intent(HomePage.this,MainActivity.class);
                        startActivity(intent);
            }
        });
    }
}
