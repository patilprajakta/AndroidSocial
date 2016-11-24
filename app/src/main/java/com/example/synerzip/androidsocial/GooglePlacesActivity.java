package com.example.synerzip.androidsocial;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 21/11/16.
 */

public class GooglePlacesActivity extends AppCompatActivity{

    private static final String TAG = "placesdemo";

    //change the lat and log to get area details
    final String latitude = "18.4993648";
    final String longtitude = "73.8189317";
    String google_key;
    ListView listView;
    AQuery aquery;
    String url;
    ArrayList<Places> placesArray;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placesdemo);

        listView = (ListView) findViewById(R.id.listView);

        google_key=getString(R.string.gcm_server_api_key);

        aquery=new AQuery(this);
        placesArray =new ArrayList<>();

        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

//url for json
///https://maps.googleapis.com/maps/api/place/search/json?location=&radius=100&sensor=true&key=AIzaSyDOE-_dus8yXmR_Qtb3q5qDCp3qP1D2CHQ
        url="https://maps.googleapis.com/maps/api/place/search/json?location="

                //chnage the radius to get more hotels
                + latitude + "," + longtitude + "&radius=200&sensor=true&key=" + google_key;

    /*url="https://itunes.apple.com/search?term=a+r+rehman";*/

    }

    //get data from url
    public void getData(View v)
    {
        aquery.progress(progressDialog).ajax(url,JSONObject.class,new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if(object!=null)
                    parseResults(object);
                else
                    Toast.makeText(GooglePlacesActivity.this,"Unable to fetch data",Toast.LENGTH_LONG).show();
            }
        });

    }

    //method to parse the url

    public void parseResults(JSONObject object)
    {
        placesArray =parsedata(object);
        //ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        MyAdapter myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);

    }

    /**
     * Custom Adapter
     */
    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return placesArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyViewHolder myViewHolder;
            if(convertView==null)
            {
                LayoutInflater lf=(LayoutInflater)parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=lf.inflate(R.layout.placesdemorow,parent,false);
                TextView txtTitle= (TextView) convertView.findViewById(R.id.title);
                TextView txtAddress= (TextView) convertView.findViewById(R.id.address);
                ImageView imageView=(ImageView)convertView.findViewById(R.id.imageViewstore);
                myViewHolder=new MyViewHolder(txtTitle,txtAddress,imageView);
                convertView.setTag(myViewHolder);
            }
            else
                myViewHolder=(MyViewHolder)convertView.getTag();

            myViewHolder.titlep.setText(placesArray.get(position).title);
            myViewHolder.descp.setText(placesArray.get(position).description);
            aquery.progress(progressDialog).id(myViewHolder.iv)

                    //get the image from json array
                    .image("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                            + placesArray.get(position).url+"&key="+google_key);

            return convertView;

        }
        //view holder class

        public class MyViewHolder
        {
            TextView titlep,descp;
            ImageView iv;
            MyViewHolder(TextView t, TextView v, ImageView i)
            {
                titlep=t;
                descp=v;
                iv=i;
            }
        }
    }
//parse data for arraylist
    public ArrayList parsedata(JSONObject data)
    {
        ArrayList<Places> temp=new ArrayList<>();
        if(data.has("results"))
        {
            try {
                JSONArray arr=data.getJSONArray("results");
                for(int i=0;i<arr.length();++i)
                {
                    String name=arr.getJSONObject(i).getString("name");
                    String area=arr.getJSONObject(i).getString("vicinity");
                    String lat=arr.getJSONObject(i).getJSONObject("geometry").getJSONObject("location")
                            .getString("lat");
                    String lng=arr.getJSONObject(i).getJSONObject("geometry").getJSONObject("location")
                            .getString("lng");

                    //not all array have photo thats why first get th ejson obj n check
                    JSONObject jsonObject=arr.getJSONObject(i);
                    String url="";
                    if(jsonObject.has("photos")) {
                        url = jsonObject.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                    }
                    Places places=new Places(name,area,url);
                    temp.add(places);
                }

            } catch (Exception e) {
                Log.v(TAG,e.toString());
                Toast.makeText(this,"error"+e.toString(),Toast.LENGTH_LONG).show();
            }//end of catch
        }//end of if
        return temp;
    }//end of parsedata

}

