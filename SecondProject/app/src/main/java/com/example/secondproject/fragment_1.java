package com.example.secondproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_1 extends Fragment {

    public static RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContactData> mMyData;
    private TextView when_nothing;

    public fragment_1() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_1 newInstance() {
        fragment_1 fragment = new fragment_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final int REQUEST_ADDCONT = 0;
    private Context context;
    private Button add_contact_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_1, container, false);
        context = getActivity();
        if(context != null) {
            add_contact_button = view.findViewById(R.id.add_contact);
            addNewContact();
            when_nothing = view.findViewById(R.id.nocont);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            getContacts();
        }
        return view;
    }

    private void addNewContact() {
        add_contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), add_contact.class);
                startActivityForResult(intent, REQUEST_ADDCONT);
            }
        });
    }



    public void getContacts(){
        class GetAllContact extends AsyncTask<Void, Void, String> {

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
                return requestHandler.sendPostRequest(URLs.URL_GET_CONTACT, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                mMyData = new ArrayList<ContactData>();
                super.onPostExecute(s);
                try {

                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length()==0){
                        when_nothing.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Log.d("d", "log"+jo.toString());
                        String id = jo.getString("Oid");
                        String name= jo.getString("name");
                        String number= jo.getString("phone_no");
                        String prof_img= jo.getString("prof_img");
                        byte[] encodeByte = Base64.decode(prof_img, Base64.DEFAULT);
                        Bitmap bitmap_c = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        System.out.println("bibibibibibibibibbiib" + bitmap_c);
                        System.out.println("prprprprprprprprprpr" + prof_img);
                        mMyData.add(new ContactData(name,  number, id, bitmap_c));
                    }

                    Collections.sort(mMyData, new Comparator<ContactData>() {
                        @Override
                        public int compare(ContactData a, ContactData b) {
                            return a.getName().compareTo(b.getName());
                        }
                    });

                   if (jsonArray.length()!=0) {
                       when_nothing.setVisibility(View.GONE);
                       mLayoutManager = new LinearLayoutManager(getActivity());
                       mRecyclerView.setLayoutManager(mLayoutManager);
                       mRecyclerView.setHasFixedSize(true);
                       mAdapter = new ContactAdapter(mMyData);
                       mRecyclerView.setAdapter(mAdapter);
                   }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAllContact gac = new GetAllContact();
        gac.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADDCONT) {
            if (resultCode == Activity.RESULT_OK) {
                getContacts();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}