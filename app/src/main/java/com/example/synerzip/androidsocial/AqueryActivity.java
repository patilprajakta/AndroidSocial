package com.example.synerzip.androidsocial;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.io.File;
import java.io.FileOutputStream;

public class AqueryActivity extends AppCompatActivity {

    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquery);

        aQuery=new AQuery(this);
//if view ids are wrong it ll juz ignore without giving error
        aQuery.id(R.id.btnText).clicked(this,"clickhandle");
    }

    public void clickhandle(View view){

        //download my facebook profile pic
        String url="https://graph.facebook.com/532119700319799/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D";

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Downloading Image...");
        aQuery.id(R.id.btnText).text("Aquery Text").backgroundColor(Color.CYAN).textColor(Color.RED);
        aQuery.id(R.id.imageview).progress(progressDialog).image(url,true,true,0,0,null,AQuery.FADE_IN);

        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"myFiles/myImage.jpeg");

        if(!file.exists()){
            file.mkdir();

            aQuery.download(url,file,new AjaxCallback<File>()
            {
                @Override
                public void callback(String url, File object, AjaxStatus status) {
                    super.callback(url, object, status);

                    handleFile(object);
                }
            });
        }

    }

    private void handleFile(File object) {

        Toast.makeText(this,"Got File :"+object.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();

    }

}
