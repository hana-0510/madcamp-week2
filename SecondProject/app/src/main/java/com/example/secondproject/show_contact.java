package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class show_contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        getContacts();
    }


    private void getContacts(){

        class GetAllContact extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() { super.onPreExecute(); }

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
                return requestHandler.sendPostRequest(URLs.URL_GET_CONTACT, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                Log.d("d", "log2");
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
//                    JSONObject jo = obj.getJSONObject("contact_list");
//                    String name = jo.getString("name");
//                    String id = jo.getString("Oid");
//                    String number= jo.getString("phone_no");
//                    mMyData.add(new ContactData(name,  number, id));
                    JSONArray jsonArray = obj.optJSONArray("contact_list");
                    if (jsonArray==null){ Log.d("d", "log7"); }
//                    else { Log.d("d", "log8"); }
//
//                    for(int i=0; i<jsonArray.length(); i++){
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        String name= jo.getString("name");
//                        String number= jo.getString("phone_no");
//                        String id = jo.getString("Oid");
//                        mMyData.add(new ContactData(name,  number, id));
//                    }

//                    if(obj.getInt("result")==2 ) {
//                        when_nothing.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
//                    }
                    //storing the user in shared preferences
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "No problem", Toast.LENGTH_SHORT).show();

            }
        }
        GetAllContact gac = new GetAllContact();
        gac.execute();
    }
}