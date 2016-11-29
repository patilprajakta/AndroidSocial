package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.os.Bundle;
import android.preference.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button btnFb;
    Button btnWhatsApp, btnTwitter, btnGoogle, btnGCM, btnAquery, btnPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFb = (Button) findViewById(R.id.buttonFb);
        btnWhatsApp = (Button) findViewById(R.id.buttonWhtsapp);
        btnTwitter = (Button) findViewById(R.id.buttonTwitter);
        btnGoogle = (Button) findViewById(R.id.buttonGoogle);
        btnGCM = (Button) findViewById(R.id.buttonGCM);
        btnAquery = (Button) findViewById(R.id.buttonAquery);
        btnPreferences = (Button) findViewById(R.id.buttonPref);

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
                startActivity(intent);
            }
        });


        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WhatsAppActivity.class);
                startActivity(intent);
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwitterActivity.class);
                startActivity(intent);
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoogleActivity.class);
                startActivity(intent);
            }
        });

        btnGCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GCMActivity.class);
                startActivity(intent);
            }
        });

        btnAquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AueryAndGoogleActivity.class);
                startActivity(intent);
            }
        });

        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrefTasksActivity.class);
                startActivity(intent);
            }
        });
    }
}