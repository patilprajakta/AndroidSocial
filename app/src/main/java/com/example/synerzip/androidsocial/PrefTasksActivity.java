package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrefTasksActivity extends AppCompatActivity {

    Button btnSavePref, btnEditPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_tasks);

        btnSavePref = (Button) findViewById(R.id.buttonSavePref);
        btnEditPref = (Button) findViewById(R.id.buttonEditPref);

        btnSavePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrefTasksActivity.this, PreferenceActivity.class));
            }
        });


        btnEditPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrefTasksActivity.this, PreferenceActivity.class));
            }
        });
    }
}
