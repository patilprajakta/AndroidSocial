package com.example.synerzip.androidsocial;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterWebView extends AppCompatActivity {


    public static final String EXTRA_URL = "extra_uri";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_web_view);
    webView=(WebView)findViewById(R.id.twitterWebView);
        setTitle("Login");

        final String url=this.getIntent().getStringExtra(EXTRA_URL);
        if(url==null){
            Log.e("twitter","URL cannot be null");
            finish();
        }

        webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);

    }

    class MyWebViewClient extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView webView,String url){

            if(url.contains(getString(R.string.callback_url))) {
                Uri uri = Uri.parse(url);
                Log.e("tag", "Url is present in webview" + uri.toString());
                String verifier = uri.getQueryParameter("oauth_verifier");

                Intent intent = new Intent();
                intent.putExtra("oauth_verifier", verifier);
                setResult(RESULT_OK);

                finish();
                return true;
            }
return false;
        }
    }
}
