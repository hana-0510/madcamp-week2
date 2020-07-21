package com.example.secondproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class fragment_2 extends Fragment implements Serializable

{

    Button Upload_Btn;
    ProgressDialog progressDialog ;

    Context context;

    private TextView when_nothing;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private GalleryAdapter mAdapter;
    public ArrayList<GalleryData> mGalleryData;
    public ArrayList<Bitmap> mBitmapData;

    private static final int REQUEST_ADDIMG = 0;

    public fragment_2() {
        // Required empty public constructor
    }


    public static fragment_2 newInstance() {
        fragment_2 fragment = new fragment_2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //ImageView IDProf;



    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        //mGalleryData = new ArrayList<GalleryData>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_2, container, false);
        context = getActivity();
        if(context != null) {
            //IDProf= (ImageView) view.findViewById(R.id.IdProf);
            Upload_Btn= (Button) view.findViewById(R.id.add_button);

            Upload_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            mRecyclerView = (RecyclerView) view.findViewById(R.id.Gallery_View);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            getGallery();
        }
/*
        mAdapter = new GalleryAdapter(mGalleryData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListner(new GalleryAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(View v, int pos) {
                GalleryData item = mAdapter.getItem(pos);
                Toast.makeText(getActivity(), "click", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Image_Clicked.class);
                intent.putExtra("img_num",pos);
                intent.putExtra("Oid", item.getOid());
                intent.putExtra("Bitmap", item.getBitmap());
                startActivity(intent);
            }
        }
        );
*/
        return view;
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp_gal.jpg");
                    Uri uri = FileProvider.getUriForFile(context, "com.bignerdranch.android.test.fileprovider", f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bitmap img_bit = null;
                Bitmap img_bit_r = null;
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp_gal.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    img_bit = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    ExifInterface ei = new ExifInterface(f.getAbsolutePath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            img_bit_r = rotateImage(img_bit, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            img_bit_r = rotateImage(img_bit, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            img_bit_r = rotateImage(img_bit, 270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            img_bit_r = img_bit;
                    }
                    String path = android.os.Environment.getExternalStorageDirectory()
                            + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("7818781878187818");
                upload_Image(img_bit_r);

            } else if (requestCode == 2) {
                Bitmap img_bit = null;
                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    img_bit = BitmapFactory.decodeStream(in);
                    //System.out.println(img_bit.toString());
                    //IDProf.setImageBitmap(img_bit);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                upload_Image(img_bit);
            }
            getGallery();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public void upload_Image(final Bitmap bit_img){

        class Upload_I extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                User user = SharedPrefManager.getInstance(getContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                //returing the response
                return requestHandler.sendPostFile(URLs.URL_ADD_IMAGE, params, bit_img);
            }

            @Override
            protected void onPostExecute(final String s) {
                Toast.makeText(getContext(), "upload finish", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                super.onPostExecute(s);
                try {
                    JSONObject jsonO = new JSONObject(s);
                    String bit_st = jsonO.getString("encoded");
                    System.out.println("qwerqwerqwerqwer: "+bit_st);

                    byte[] encodeByte = Base64.decode(bit_st, Base64.DEFAULT);
                    Bitmap bitmap_r = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    //IDProf.setImageBitmap(bitmap_r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        Upload_I up = new Upload_I();
        up.execute();
    }

    public void delete_image(final String Oid){
        class Delete_I extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context,"Deleting","Please Wait",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                User user = SharedPrefManager.getInstance(getContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                params.put("Oid", Oid);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_DEL_IMAGE, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                Toast.makeText(getContext(), "delete finish", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                super.onPostExecute(s);
            }
        }
        Delete_I del = new Delete_I();
        del.execute();

        getGallery();
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
                User user = SharedPrefManager.getInstance(getContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GET_IMAGE, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                mGalleryData = new ArrayList<GalleryData>();
                mBitmapData = new ArrayList<Bitmap>();
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
                        mGalleryData.add(new GalleryData(bitmap_r, Oid));
                        //mBitmapData.add(bitmap_r);
                    }


                    if (jsonArray.length()!=0) {
                        mLayoutManager = new GridLayoutManager(getActivity(),3);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new GalleryAdapter(mGalleryData);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListner(new GalleryAdapter.OnItemClickListener() {
                                                           @Override
                                                           public void onItemClick(View v, int pos) {
                                                               GalleryData item = mAdapter.getItem(pos);
                                                               Toast.makeText(getActivity(), "click", Toast.LENGTH_LONG).show();
                                                               Intent intent = new Intent(getActivity(), Image_Clicked.class);
                                                               Bundle b = new Bundle();
                                                               intent.putExtra("key", b);
                                                               intent.putExtra("img_num",pos);
                                                               intent.putExtra("Oid", item.getOid());
                                                               startActivity(intent);
                                                           }
                                                       }
                        );
                        mAdapter.setOnItemLongClickListener(new GalleryAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(View v, final int pos) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("이미지 삭제");
                                builder.setMessage("이미지를 삭제하시겠습니까?");
                                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        delete_image(mGalleryData.get(pos).getOid());
                                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT);
                                        System.out.println("deldeldeldeldledldleldeldledledl");
                                    }
                                });
                                builder.setNegativeButton("취소", null);
                                builder.show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAllImage gai = new GetAllImage();
        gai.execute();
    }
}