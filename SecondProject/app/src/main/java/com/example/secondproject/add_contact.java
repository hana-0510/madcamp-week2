package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class add_contact extends AppCompatActivity {

    private EditText name;
    private EditText phone_num;
    private Button add_to_server;
    private TextView go_back;

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

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Log.d("d", "log3");
                finish();
            }
        });
    }

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
                return requestHandler.sendPostRequest(URLs.URL_ADD_CONTACT, params);
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

}