package com.example.secondproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Image_Clicked extends Activity {
    private ViewPager viewPager;
    private ImageViewPagerAdapter imageViewPagerAdapter;
    private static int img_num;
    private static String Oid;
    public static ArrayList<Bitmap> datas;
 //   private RecyclerView RecyclerView;
//    private ImgRecyclerAdapter imgRecyclerAdapter;
 //   private RecyclerView.LayoutManager imgLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_viewpager);

        getGallery();
/*
        Intent intent = getIntent();
        img_num = intent.getExtras().getInt("img_num");
        Oid = intent.getExtras().getString("Oid");

        viewPager=findViewById(R.id.galleryViewPager);
        imageViewPagerAdapter=new ImageViewPagerAdapter(this, datas);
        viewPager.setAdapter(imageViewPagerAdapter);
        viewPager.setCurrentItem(img_num);

 */
    }

    public void getGallery(){
        class GetAllImage extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GET_IMAGE, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                datas = new ArrayList<Bitmap>();
                super.onPostExecute(s);
                try {

                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length()==0){
                        //Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Log.d("d", "log"+jo.toString());
                        String Oid = jo.getString("file_id");
                        String content= jo.getString("content");
                        byte[] encodeByte = Base64.decode(content, Base64.DEFAULT);
                        Bitmap bitmap_r = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        datas.add(bitmap_r);
                    }
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println(datas);
                    Intent intent = getIntent();
                    img_num = intent.getExtras().getInt("img_num");
                    Oid = intent.getExtras().getString("Oid");

                    viewPager=findViewById(R.id.galleryViewPager);
                    imageViewPagerAdapter=new ImageViewPagerAdapter(getApplicationContext(), datas);
                    viewPager.setAdapter(imageViewPagerAdapter);
                    viewPager.setCurrentItem(img_num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAllImage gai = new GetAllImage();
        gai.execute();
    }

}