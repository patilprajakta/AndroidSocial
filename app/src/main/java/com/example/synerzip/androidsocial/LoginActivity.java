package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by Prajakta Patil on 11/11/16.
 */

public class LoginActivity extends AppCompatActivity {
    public TextView textView;

    public CallbackManager mCallbackManager;

    // public LoginManager loginManager;

    public LoginButton mLoginButton;
    public ProfileTracker mProfilrTracker;
    public AccessTokenTracker mAccesstokenTracker;
    public ProfilePictureView profilePictureView;

    ImageView imageView;
    TextView txtFirstName;
    Button btnLogin;
    Button btnShareLink;
    Button btnShareImage;

    public ShareDialog shareDialog;

    //to load imageview using 3rd party library
    public AQuery aquery;

    public FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("Facebook", "Success");
            final AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            getUserProfileInfo(profile);

            /*if(accessToken==null){
                txtFirstName.setText("You have logged OUT");
            }else{
                btnShareLink.setVisibility(View.VISIBLE);
                txtFirstName.setText("You have logged IN");
            }*/
            if (accessToken == null) {
                btnLogin.setText("Login");
            }

            if (accessToken != null) {
                btnLogin.setText("Logout");
                btnShareLink.setVisibility(View.GONE);

            }/*else {
                btnLogin.setText("Login To FB");
            }*/

            //Uri uri = profile.getLinkUri();
            //imageUrl = profile.getProfilePictureUri(100, 100);

            //shareToFacebook();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Application code
                            String id = "";
                            try {
                                id = object.getString("id");
                            } catch (JSONException exe) {
                            }
                            String name = "";
                            try {
                                name = object.getString("name");
                                Log.d("name", name);
                            } catch (JSONException exe) {
                            }
                            String email = "";
                            try {
                                email = object.getString("email");
                            } catch (JSONException exe) {
                            }
                            String gender = "";
                            try {
                                gender = object.getString("gender");
                            } catch (JSONException exe) {
                            }

                            Log.v("LoginActivity", response.toString());
                        }
                    });

            request.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {


                @Override
                public void onCompleted(JSONArray objects, GraphResponse response) {

                    Log.d("MyJson", objects + "");

                    Log.d("MyResponse", response.getJSONArray() + "");
                    //    Log.d(response.getJSONArray(),"jsonarray");

                }

            });
        }

        @Override
        public void onCancel() {
            Log.e("Facebook", "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("Facebook", "onError");
        }
    };

/*    public void share(View view){

        if(view==btnShareLink){
            shareToFacebook();
        }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        aquery = new AQuery(this);
        shareDialog = new ShareDialog(this);

        textView = (TextView) findViewById(R.id.text_user_info);
        mLoginButton = (LoginButton) findViewById(R.id.button_log_in);
        profilePictureView = (ProfilePictureView) findViewById(R.id.imgProPic);
        imageView = (ImageView) findViewById(R.id.profileImage);
        txtFirstName = (TextView) findViewById(R.id.txtfirstname);
        btnLogin = (Button) findViewById(R.id.btn);
        btnShareLink = (Button) findViewById(R.id.shareLink);
        btnShareImage = (Button) findViewById(R.id.shareImage);

        mLoginButton.setCompoundDrawables(null, null, null, null);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        mLoginButton.registerCallback(mCallbackManager, mFacebookCallback);

        setupTokenTracker();
        setupProfileTracker();

        mAccesstokenTracker.startTracking();
        mProfilrTracker.startTracking();

        // new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute();

        /**
         * share on facebook
         */
        ///// SHARE DIALOG needs and fb app to be installed on device
        ///// WEB FEED DIALOG does not need fb app to be installed on device

        btnShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //share the link on facebook

                if (shareDialog.canShow(ShareLinkContent.class)) {

                    ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()

                            .setContentTitle("Share link and image on FB")
                            .setImageUrl(Uri.parse("https://graph.facebook.com/532119700319799/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D"))
                            .setContentDescription("simple facebook integration")
                            .setContentUrl(Uri.parse("https://www.numetriclabz.com/android-linkedin-integration-login-tutorial"))
                            .build();
                    shareDialog.show(shareLinkContent);

                } else {
                }

                //share the image on facebook
                btnShareImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*    Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(image)
                                    .setCaption("Share Image")
                                    .build();

                            SharePhotoContent content = new SharePhotoContent.Builder()
                                    .addPhoto(photo)
                                    .build();
                            ShareApi.share(content, null);*/

                        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(image)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();

                        ShareApi.share(content,null);

                    }
                });
            }
        });
    }//oncreate

    public void login(View view) {
        if (view == btnLogin) {
            mLoginButton.performClick();

            mLoginButton.performClick();

            //login manager manages login and permissions for facebook
            // LoginManager.getInstance().logInWithReadPermissions(Arrays.asList("public_profile", "email","user_friends"));
        }
    }

    // get user profile url
    public Uri getUserProfileUri(Profile currentProfile) {
        Uri uri = null;
        if (currentProfile != null) {
            uri = currentProfile.getProfilePictureUri(100, 100);
        }
        return uri;
    }

    private void setupProfileTracker() {
        mProfilrTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //  Uri uri=null;
                Uri prouri = getUserProfileUri(currentProfile);
                Log.d("###", prouri + "");
                Log.d("CurrentProfile", String.valueOf(currentProfile));
                // uri = currentProfile.getProfilePictureUri(200, 200);
                if (currentProfile != null) {


                    String uri = String.valueOf(getUserProfileUri(currentProfile));
                    textView.setText(getUserProfileInfo(currentProfile));

                    if (uri != null) {
                        //   Picasso.with(getApplicationContext()).load(uri).into(imageView);

                        aquery.id(imageView).image(uri);
                    } else {
                        return;
                    }

                } else {
                    textView.setText(getUserProfileInfo(currentProfile));

                    String uri = String.valueOf(getUserProfileUri(currentProfile));

                    if (uri != null) {
                        //   Picasso.with(getApplicationContext()).load(uri).into(imageView);

                        aquery.id(imageView).image(uri);
                    } else {
                        return;
                    }

                }
                //profilePictureView.setProfileId(String.valueOf(currentProfile.getProfilePictureUri(100,100)));
//                profilePictureView.setProfileId(String.valueOf(currentProfile.getProfilePictureUri(100,100)));
            }
        };
    }

    private void setupTokenTracker() {
        mAccesstokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("CurrentToken", String.valueOf(currentAccessToken));
                if (currentAccessToken == null) {
                    btnLogin.setText("Login To FB");
                    btnShareLink.setVisibility(View.INVISIBLE);
                    txtFirstName.setText("You have logged OUT");
                }

                if (currentAccessToken != null) {
                    btnLogin.setText("Logout ");
                    btnShareLink.setVisibility(View.VISIBLE);
                    txtFirstName.setText("You have logged IN");


                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        textView.setText(getUserProfileInfo(profile));
    }

    private String getUserProfileInfo(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();

        if (profile != null) {
            profile = Profile.getCurrentProfile().getCurrentProfile();
            if (profile != null) {
                //txtFirstName.setText("You have logged IN");
                btnShareLink.setVisibility(View.VISIBLE);

                btnShareLink.setVisibility(View.VISIBLE);

            } else {
                // txtFirstName.setText("You have logged OUT");

                //    btnShareLink.setVisibility(View.INVISIBLE);
                // btnLogin.setText("Logout");

            }
        }

       /* String url=String.valueOf(profile.getProfilePictureUri(100, 100));

        if(url!=null) {
            aquery.id(imageView).image(url);
        }else{
            return url;
        }*/

        if (profile != null) {

            stringBuffer.append("Welcome " + profile.getName());

            //   stringBuffer.append("profilepicture" + profile.getProfilePictureUri(100, 100));

            //   new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(String.valueOf(profile.getProfilePictureUri(100, 100)));

     /*       Uri uri = null;
            if (profile!=null) {
                uri = profile.getProfilePictureUri(100, 100);
            }
            return uri;*/
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void onStop() {
        super.onStop();
        mAccesstokenTracker.stopTracking();
        mProfilrTracker.stopTracking();
        profilePictureView.clearFocus();
    }
}

