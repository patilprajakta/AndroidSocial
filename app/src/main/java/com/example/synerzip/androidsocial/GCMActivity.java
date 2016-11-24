package com.example.synerzip.androidsocial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GCMActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_CODE=90;
    private static final String TAG="gcm";
    private static final String REG_ID="reg_id";
    private static final String APP_VERSION="app_version";
    private static String SENDER_ID;

    String regId;
    GoogleCloudMessaging  cloudMessaging;
    SharedPreferences preferences;
    Button btnReg;
    EditText txtReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        btnReg=(Button)findViewById(R.id.idBtnRegister0);
        txtReg=(EditText)findViewById(R.id.idTxtRegister);
        preferences=getGcmPreferences(getApplicationContext());
        SENDER_ID=getString(R.string.gcm_sender_key);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMyMobile();
            }
        });

        //check if google services are available or not

        if(checkPlayServices()){

            regId=getRegistrationId();

            if(regId.isEmpty()){

              btnReg.setEnabled(true);

            }else {
                txtReg.setText("Registration Id is Already there"+regId);
            }

        }else{
            Log.v(TAG,"Play services are not available");
        }

    }

    //if play services are available but no reg id then call this method

    private void registerMyMobile() {

       new AsyncTask<Void,Void,String>(){


           @Override
           protected String doInBackground(Void... params) {

               String msg="";

               if(cloudMessaging==null){

                   cloudMessaging=GoogleCloudMessaging.getInstance(GCMActivity.this);
               }

               try {
                   regId=cloudMessaging.register(SENDER_ID);

                   //after getting reg id u need to send it to your server

                   storeUserId();

                   //save all settings to preferences file
                   saveAllSettings();

                   msg="Registered device with ID"+regId;
                   Log.v(TAG,"Got ID"+regId);

               } catch (IOException e) {
                   e.printStackTrace();
                   msg="Error"+e.toString();
               }

               return msg;
           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);

               if(!s.contains("Error")){
                   btnReg.setEnabled(false);
                   txtReg.setText(s);
               }

           }
       }.execute();

    }

    //save settings to preferences file
    private void saveAllSettings() {

       SharedPreferences.Editor  editor=preferences.edit();
        editor.putString(REG_ID,regId);
        editor.putInt(APP_VERSION,getAppVersion(getApplicationContext()));
        editor.commit();

    }

    //store the user id in server
    private void storeUserId() {
        Log.v(TAG,"Stored to server");

    }

    //obtain the new registration id
    private String getRegistrationId() {

        String registration=preferences.getString(REG_ID,"");

        if(registration.isEmpty()){
Log.v(TAG,"Device not registered");
            return "";
        }

        int version=preferences.getInt(APP_VERSION,Integer.MIN_VALUE);
        int current_version=getAppVersion(getApplicationContext());

        if(version!=current_version){
            Log.v(TAG,"app version dont match");
            return "";
        }
        return registration;
    }

    //method to obatain the app version

    private int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo=context.getPackageManager().getPackageInfo(getPackageName(),0);
        return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return Integer.MIN_VALUE;

    }

    //method to check google play services
private boolean checkPlayServices(){
    int result=GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

    if(result!= ConnectionResult.SUCCESS){

        if(GooglePlayServicesUtil.isUserRecoverableError(result)){

            GooglePlayServicesUtil.getErrorDialog(result,this,PLAY_SERVICES_CODE).show();
        }
        return false;
    }
    return true;
}

    private SharedPreferences getGcmPreferences(Context applicationContext) {

        return applicationContext.getSharedPreferences("MyPrefs",MODE_PRIVATE); //only this app can able to read MyPrefs
    }
}
