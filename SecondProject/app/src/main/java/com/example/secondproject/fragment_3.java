package com.example.secondproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


import static java.lang.Integer.parseInt;

//implements DatePickerDialog.OnDateSetListener
public class fragment_3 extends Fragment {

    private FloatingActionButton add_todo_button;
    String toDo;
    DatePickerDialog datePickerDialog;
    int year, month, day;
    String savemonth="";
    String saveday = "";
    public static RecyclerView mRecyclerView;
    private ToDoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ToDos> mMyData;
    private TextView when_nothing;

    public fragment_3() {
        // Required empty public constructor
    }

    public static fragment_3 newInstance(String param1, String param2) {
        fragment_3 fragment = new fragment_3();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recyclerview);
        add_todo_button = view.findViewById(R.id.add_todo);
        when_nothing = view.findViewById(R.id.notodo);
        getTODOS();
        addTODO();
        return view;
    }

    private void addTODO() {
        add_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(getContext());
                todoTaskBuilder.setTitle("What do you need to do?");
                final EditText todoET = new EditText(getContext());
                todoTaskBuilder.setView(todoET)
                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toDo = todoET.getText().toString();
                        Log.d("d", "log"+toDo);
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int tyear, int tmonth, int tday) {
                                saveday=Integer.toString(tday);
                                savemonth=Integer.toString(tmonth+1);
                                Log.d("d", "log "+savemonth+"/"+saveday);
                                addTODOToServer();
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                })
                .setNegativeButton("Cancel", null);
                todoTaskBuilder.create().show();
            }
        });
    }


    public void addTODOToServer(){
        class AddTODO extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                User user = SharedPrefManager.getInstance(getContext()).getUser();
                params.put("id", user.getEmail());
                params.put("password", user.getPassword());
                params.put("dowhat", toDo);
                params.put("month", savemonth);
                params.put("day", saveday);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ADD_TODO, params);
            }

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Adding Todo");
                progressDialog.show();
            }
            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                int a = s.indexOf("result");
                final String result = s.substring(a+8, a+9);
                // On complete call either onLoginSuccess or onLoginFailed
                if (result.equals("1")) {
                    Toast.makeText(getContext(), "Todo added successfully", Toast.LENGTH_SHORT).show();
                    getTODOS();
                }else { Toast.makeText(getContext(), "Failed to add Todo", Toast.LENGTH_SHORT).show(); }
                progressDialog.dismiss();

            }
        }
        //executing the async task
        AddTODO at = new AddTODO();
        at.execute();
    }

    public void getTODOS(){
        class GetAllTodo extends AsyncTask<Void, Void, String> {

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
                return requestHandler.sendPostRequest(URLs.URL_GET_TODO, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                mMyData = new ArrayList<ToDos>();
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
                        String dowhat = jo.getString("dowhat");
                        String month= jo.getString("month");
                        String day= jo.getString("day");
                        String id = jo.getString("Oid");

                        mMyData.add(new ToDos(dowhat, parseInt(month), parseInt(day), id));

                        Collections.sort(mMyData, new Comparator<ToDos>() {
                            @Override
                            public int compare(ToDos a, ToDos b) {
                                if (a.getMonth()<b.getMonth()){
                                    return -1;
                                } else if (a.getMonth()==b.getMonth()) {
                                    if (a.getDay()<b.getDay()){
                                        return -1;
                                    } else if (a.getDay()==b.getDay()) {return 0;}
                                }
                                return 1;
                            }
                        });
                    }

                    if (jsonArray.length()!=0) {
                        when_nothing.setVisibility(View.GONE);
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setHasFixedSize(true);
                        mAdapter = new ToDoAdapter(mMyData);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAllTodo gat = new GetAllTodo();
        gat.execute();
    }

}