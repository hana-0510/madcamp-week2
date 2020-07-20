package com.example.secondproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class fragment_2 extends Fragment {

    public fragment_2() {
        // Required empty public constructor
    }


    public static fragment_2 newInstance() {
        fragment_2 fragment = new fragment_2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    ImageView IDProf;
    Button Upload_Btn;
    ProgressDialog progressDialog ;
    boolean check = true;
    String GetImageNameFromEditText;
    String ImageNameFieldOnServer = "image_name" ;
    String ImagePathFieldOnServer = "image_path" ;
    String ImageUploadPathOnSever ="https://androidjsonblog.000webhostapp.com/capture_img_upload_to_server.php" ;
    String ConvertImage;
    Context context;
    private String Document_img1="";


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_2, container, false);
        context = getActivity();
        if(context != null) {
            IDProf= (ImageView) view.findViewById(R.id.IdProf);
            Upload_Btn= (Button) view.findViewById(R.id.UploadBtn);

            IDProf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
        }
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
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    Uri uri = FileProvider.getUriForFile(getContext(), "com.bignerdranch.android.test.fileprovider", f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    getActivity().startActivityForResult(intent, 1);
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
                /*
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                 */

                /*
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    bitmap=getResizedBitmap(bitmap, 400);
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment.getExternalStorageDirectory()
                            + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 */
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


                /*
                Uri uri = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(uri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                thumbnail=getResizedBitmap(thumbnail, 400);
                Log.w("path of image", picturePath+"");
                IDProf.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);


                 */

            }
        }
    }


    public void upload_Image(final Bitmap bit_img){
        Toast.makeText(getContext(), "upload 234234", Toast.LENGTH_SHORT).show();

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
                    IDProf.setImageBitmap(bitmap_r);




                    /*
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Log.d("d", "log"+jo.toString());
                        String id = jo.getString("Oid");
                        String name= jo.getString("name");
                        String number= jo.getString("phone_no");
                        mMyData.add(new ContactData(name,  number, id));
//                        Log.d("d", "log"+mMyData.toString());
                    }

                    when_nothing.setVisibility(View.GONE);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new ContactAdapter(mMyData);
                    mRecyclerView.setAdapter(mAdapter);
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        Upload_I up = new Upload_I();
        up.execute();
    }
}