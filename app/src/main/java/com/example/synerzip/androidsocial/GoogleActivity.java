package com.example.synerzip.androidsocial;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    TextView txtLocation;

    private static final int PLAY_SERVICES_CODE=1000;
    private static final String TAG="location";

    GoogleApiClient googleApiClient;

    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);


        txtLocation=(TextView)findViewById(R.id.idLocation);
        if(checkGoogleServices()){
            initializeGoogleApiClient();
            createLocationRequest();
        }

        if(checkGoogleServices()){
            createLocationRequest();
        }
    }

    private void initializeGoogleApiClient() {

    googleApiClient=new GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
    }

    /**
     * check google services are available or not
     */
    private boolean checkGoogleServices(){
        //int result= GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        int result=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(result!= ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(result)) {

                GooglePlayServicesUtil.getErrorDialog(result, this, PLAY_SERVICES_CODE).show();
            }

            return false;
        }
        return true;
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10); // 10 meters
    }

    //ask to connect to location
    protected void onStart(){
        super.onStart();

        googleApiClient.connect();
    }

    //if already connect then disconnect it in onStop()
    protected void onStop(){
        super.onStop();
        if(googleApiClient.isConnected())
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkGoogleServices();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        displayLocation();
    }
    //display location
    private void displayLocation() {

        Log.v(TAG,"connection has been ESTABLISHED");

        //check permission problem
        //try out different api than using fusedlocationapi

        Location location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location!=null){

            double lattitude=location.getLatitude();
            double longitude=location.getLongitude();

            txtLocation.setText("lattitude"+lattitude+"longitude"+longitude);
        }else {
            txtLocation.setText("Location not available");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG,"connection has been SUSPENDED");

        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG,"connection has been FAILED"+connectionResult.getErrorCode());



    }
}
