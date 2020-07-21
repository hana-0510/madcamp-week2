package com.example.secondproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;

public class add_contact extends AppCompatActivity {

    private EditText name;
    private EditText phone_num;
    private Button add_to_server;
    private TextView go_back;
    private ImageView IDProf;
    private Bitmap img_bit;
    private Boolean is_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        name = (EditText) findViewById(R.id.add_name);
        phone_num = (EditText) findViewById(R.id.add_phone_num);
        add_to_server = (Button) findViewById(R.id.add_button);
        go_back = (TextView) findViewById(R.id.back_to_contacts);

        add_to_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToServer();
            }
        });

        IDProf= (ImageView) findViewById(R.id.image_insert);
        is_profile = false;
        IDProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Log.d("d", "log3");
                finish();
            }
        });
    }

    private Boolean is_image(){return this.is_profile;}
    private void set_image(){this.is_profile = true;}

    private Bitmap getImg_bit() {return this.img_bit;}

    public void addContactToServer(){
//        Log.d("d", "log");
        add_to_server.setEnabled(false);

        final String nameString = name.getText().toString();
        final String numString = phone_num.getText().toString();



        class ContactInformation extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                params.put("name", nameString);
                params.put("phone_no", numString);

                //returing the response
                if (is_profile) {
                    return requestHandler.sendPostFile(URLs.URL_ADD_CONTACT, params, getImg_bit());
                }
                Context context = getApplicationContext();
                Drawable drawable = getResources().getDrawable(R.drawable.img_df_1);
                Bitmap bitmap_df = ((BitmapDrawable)drawable).getBitmap();
                return requestHandler.sendPostFile(URLs.URL_ADD_CONTACT, params, bitmap_df);
            }

            final ProgressDialog progressDialog = new ProgressDialog(add_contact.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Adding Contact Info...");
                progressDialog.show();
            }
            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                int a = s.indexOf("result");
                final String result = s.substring(a+8, a+9);
                new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                        if (result.equals("1")) {
                            onContactAddSuccess();
                        }else { onContactAddFailed(); }
                        progressDialog.dismiss();
                        }
                    }, 2500);
            }
        }
        //executing the async task
        ContactInformation ci = new ContactInformation();
        ci.execute();
    }

    public void onContactAddSuccess(){
        add_to_server.setEnabled(true);
        Toast.makeText(getBaseContext(), "Contact Added Successfully", Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    public void onContactAddFailed(){
        add_to_server.setEnabled(true);
        Toast.makeText(getBaseContext(), "Contact Not Added. Try again", Toast.LENGTH_LONG).show();
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                /*
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.bignerdranch.android.test.fileprovider", f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);
                }
                else */
                if (options[item].equals("Choose from Gallery"))
                {
                    set_image();
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
            /*
            if (requestCode == 1) {
                Bitmap img_bit_c = null;
                Bitmap img_bit_r = null;
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    img_bit_c = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    ExifInterface ei = new ExifInterface(f.getAbsolutePath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            img_bit_r = rotateImage(img_bit_c, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            img_bit_r = rotateImage(img_bit_c, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            img_bit_r = rotateImage(img_bit_c, 270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            img_bit_r = img_bit_c;
                    }
                    String path = android.os.Environment.getExternalStorageDirectory()
                            + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.img_bit = img_bit_r;
                IDProf.setImageBitmap(img_bit_r);


            } else
            */
            if (requestCode == 2) {
                Bitmap img_bit = null;
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    this.img_bit = BitmapFactory.decodeStream(in);
                    //System.out.println(img_bit.toString());
                    //IDProf.setImageBitmap(img_bit);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                IDProf.setImageBitmap(this.img_bit);
            }
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}