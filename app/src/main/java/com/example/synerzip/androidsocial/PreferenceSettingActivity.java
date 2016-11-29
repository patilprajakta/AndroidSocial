package com.example.synerzip.androidsocial;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Prajakta Patil on 25/11/16.
 */

public class PreferenceSettingActivity extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //all activities are stored for this app
        //adding preferences to tha app using below method
        addPreferencesFromResource(R.xml.settings);
    }
}
