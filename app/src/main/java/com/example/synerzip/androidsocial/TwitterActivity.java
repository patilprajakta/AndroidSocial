package com.example.synerzip.androidsocial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterActivity extends AppCompatActivity {

    String aouthVerifier, callbackUrl, consumerKey, consumerSecret;
    EditText text, username;
    Button btnShare, btnLogin;
    SharedPreferences sharedPreferences;
    private AccessToken accessToken;

    RequestToken requestToken;

    Twitter twitter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        text = (EditText) findViewById(R.id.twitterText);
        username = (EditText) findViewById(R.id.twitterName);

        btnShare = (Button) findViewById(R.id.btnTwitShare);
        btnLogin = (Button) findViewById(R.id.btnTwitLogin);
        initializeTwitter();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLogin(v);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterShare(v);
            }
        });

        //getPreferences is d file dat contains info about user for future references
        //you can also give custom file name
        sharedPreferences = getPreferences(0);// '0' is the access code which alloes to access the preference file


        //check login status using sharedpreferences
        boolean isLoggedIn = sharedPreferences.getBoolean("login_status", false); //false as a  default value

        if (isLoggedIn) {
            String user_name = sharedPreferences.getString("username", "");
            username.setText(user_name);
            btnShare.setEnabled(true);
            btnLogin.setEnabled(false);
        } else {
            Toast.makeText(this, "NOT logged IN", Toast.LENGTH_LONG).show();
        }

    }

    //share status on twitter
    public void twitterShare(View view) {
        String tweet = text.getText().toString();

        new shareStatus().execute(tweet);
    }

    //share status async class
    class shareStatus extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String tweet = params[0];
            try {


                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                String access_token = sharedPreferences.getString("access_token", "");
                String token_secret = sharedPreferences.getString("token_secret", "");

                AccessToken token = new AccessToken(access_token, token_secret);
                Twitter twit = new TwitterFactory(builder.build()).getInstance(token);

                StatusUpdate statusUpdate = new StatusUpdate("Hello, its my first tweet");
                InputStream inputStream = getResources().openRawResource(R.raw.gtm_analytics);

                statusUpdate.setMedia("twitterImage.jpg", inputStream);

                twitter4j.Status response = twit.updateStatus(statusUpdate);
                Log.v("tag", response.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            text.setText("tweet posted");
        }

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TwitterActivity.this);
            progressDialog.setTitle("Status");
            progressDialog.setMessage("uploading....");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

    }

    //initialize twitter
    public void initializeTwitter() {
        aouthVerifier = getString(R.string.oauth_verifier);
        callbackUrl = getString(R.string.callback_url);
        consumerKey = getString(R.string.consumer_key);
        consumerSecret = getString(R.string.consumer_secret);
    }

    //twitter login function

    public void twitterLogin(View view) {
        boolean isLoggedIn = sharedPreferences.getBoolean("login_status", false);
        if (!isLoggedIn) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerSecret(consumerSecret);
            builder.setOAuthConsumerKey(consumerKey);

            twitter4j.conf.Configuration configuration = builder.build();
            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            //function to get the request token

            getAccessToken();

        } else {
            btnShare.setEnabled(true);
            btnLogin.setEnabled(false);
            Toast.makeText(this, "LOGGED IN", Toast.LENGTH_LONG).show();
        }
    }


    private void getAccessToken() {

        new requestToken().execute();
    }

    private class requestToken extends AsyncTask<Void, Void, String> {

        String oauth_url = "";

        @Override
        protected String doInBackground(Void... params) {

            try {
                requestToken = twitter.getOAuthRequestToken();

                if (requestToken != null)
                    oauth_url = requestToken.getAuthenticationURL();

            } catch (TwitterException e) {
                e.printStackTrace();

                Log.v("TAG", e.toString());
            }


            return oauth_url;
        }

        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            Toast.makeText(TwitterActivity.this, "Twitter Webview", Toast.LENGTH_LONG).show();
            Log.v("TAG", "url is" + url);
            final Intent intent = new Intent(TwitterActivity.this, TwitterWebView.class);
            intent.putExtra(TwitterWebView.EXTRA_URL, url);
            startActivityForResult(intent, 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(TwitterActivity.this, "in activity result method" + requestCode, Toast.LENGTH_LONG).show();

        if (requestCode == Activity.RESULT_OK) {
            String verifier = data.getStringExtra("oauth_verifier");
            Log.v("tag", "verifier" + verifier);

            new getUserInfo().execute(verifier);
        }
    }

    //async task for userinfo class

    class getUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = "";

            try {
                //to have further access to twitter
                accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                username = saveTwitterInfo(accessToken);


            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return username;
        }


        //onPostExecute() method
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            btnShare.setEnabled(true);
            btnLogin.setEnabled(false);

            if (s.equals("")) {
                username.setText("user not found");
            } else {
                username.setText(s);
            }
        }

    }

    //save user info function

    private String saveTwitterInfo(AccessToken accessToken) {
        long userId = accessToken.getUserId();
        String username = "";
        User user;

        try {

            user = twitter.showUser(userId);

            if (user != null) {

                username = user.getName();

                //save user data to share preference file using editor ; to write to shared preference file u need editor
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("access_token", accessToken.getToken());
                editor.putString("token_secret", accessToken.getTokenSecret());
                editor.putBoolean("login_status", true);
                editor.putString("user_name", username);
                editor.commit();
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return username;
    }

}

