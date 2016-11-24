package com.example.synerzip.androidsocial;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Prajakta Patil on 15/11/16.
 */
public class WhatsAppActivity extends AppCompatActivity{

    EditText editText;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_whatsapp);

        button=(Button)findViewById(R.id.btnSend);
        editText=(EditText)findViewById(R.id.editTxt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatsapp(view);
            }
        });

    }

    public void whatsapp(View view){
        PackageManager packageManager=getPackageManager();
        try {


            PackageInfo packageInfo=packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_META_DATA);

            Toast.makeText(this,"WhatsApp Installed",Toast.LENGTH_LONG).show();

            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.whatsappimage);

            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);


            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"whatsapp.jpeg");

            try {
            file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file);

                fos.write(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error",e.toString());
            }

            //broadcast to all n den choose one too send
            Intent intent=new Intent(Intent.ACTION_SEND);

            //send to particular number
           //Intent intent=new Intent(Intent.ACTION_SENDTO,Uri.parse(new StringBuilder().append("+919421528289").toString()));

//Intent intent = new Intent("android.intent.action.MAIN");
// intent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));

            //intent.putExtra(Intent.EXTRA_TEXT, PhoneNumberUtils.stripSeparators("919421528289")+"@s.whatsapp.net");

            //intent.putExtra("sms_body",PhoneNumberUtils.stripSeparators("919421528289")+"@s.whatsapp.net");
        //    intent.putExtra(Intent.EXTRA_TEXT,"hiiii");
            intent.putExtra(Intent.EXTRA_TEXT,editText.getText().toString());
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpeg");
            intent.setPackage("com.whatsapp");
            startActivity(intent);

         /*   Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("content://com.android.contacts/data/" + .getString(0)));
            i.setType("text/plain");
            i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
            i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            i.putExtra(Intent.EXTRA_TEXT, "I'm the body.");
            startActivity(i);*/


        } catch (PackageManager.NameNotFoundException e) {

            Toast.makeText(this,"WhatsApp NOT Installed",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
