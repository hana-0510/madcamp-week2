package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText repasswordText;
    private Button signupButton;
    private TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        ButterKnife.inject(this);

        nameText = (EditText) findViewById(R.id.et_name);
        emailText = (EditText) findViewById(R.id.et_email);
        passwordText = (EditText) findViewById(R.id.et_password);
        repasswordText = (EditText) findViewById(R.id.et_repassword);
        signupButton = (Button) findViewById(R.id.btn_register);
        loginLink = (TextView) findViewById(R.id.go_log_in);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            return;
        }

        signupButton.setEnabled(false);

        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", name);
                params.put("id", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
//                try {
//                    //converting response to json object
//                    JSONObject obj = new JSONObject(s);
//                    //if no error in response
//                    if (!obj.getBoolean("error")) {
//                        //getting the user from the response
////                        JSONObject userJson = obj.getJSONObject("user");
//                        //creating a new user object
//                        User user = new User(
//                                obj.getString("username"),
//                                obj.getString("email"),
//                                obj.getString("password")
//                        );
//                        if(obj.getInt("result")==1) {
//                            onSignupSuccess();
//                        } else { onSignupFailed(); }
//                        //storing the user in shared preferences
//                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                        progressDialog.dismiss();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
                int a = s.indexOf("result");
                final String result = s.substring(a + 8, a + 9);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                if (result.equals("1")) {
                                    onSignupSuccess();
                                } else {
                                    onSignupFailed();
                                }
                                progressDialog.dismiss();
                            }
                        }, 2500);
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }
    //executing the async task

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Sign up successful", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Email already exists", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String repassword = repasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (!password.equals(repassword)) {
            passwordText.setError("Password does not equal Re-password");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}
