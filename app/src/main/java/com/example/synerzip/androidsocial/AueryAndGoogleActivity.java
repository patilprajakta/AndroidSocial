package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AueryAndGoogleActivity extends AppCompatActivity {

    Button buttonUpload;
    Button buttonGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auery_and_google);

        buttonGoogle=(Button)findViewById(R.id.btnGooglePlaces);
        buttonUpload=(Button)findViewById(R.id.btnUpload);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AueryAndGoogleActivity.this,AqueryGooglePlacesActivity.class);
            startActivity(intent);
            }

        });


        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AueryAndGoogleActivity.this,AqueryActivity.class);
                startActivity(intent);
            }

        });

    }
}
