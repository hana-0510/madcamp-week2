package com.example.secondproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

//implements DatePickerDialog.OnDateSetListener
public class fragment_3 extends Fragment {

    private FloatingActionButton add_todo_button;
    EditText dueDate;
    String selectedDate;
    public static final int REQUEST_CODE = 11;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
//    private OnFragmentInteractionListener mListener;
    int year, month, day;



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
        add_todo_button = view.findViewById(R.id.add_todo);
        dueDate = view.findViewById(R.id.date_todo);
        addDate();
//        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
//        add_todo_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // create the datePickerFragment
//                AppCompatDialogFragment newFragment = new DatePickerFragment();
//                // set the targetFragment to receive the results, specifying the request code
//                newFragment.setTargetFragment(fragment_3.this, REQUEST_CODE);
//                // show the datePicker
//                newFragment.show(fm, "datePicker");
//            }
//        });
        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // check for the results
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            // get date from string
//            selectedDate = data.getStringExtra("selectedDate");
//            // set the value of the editText
//            dueDate.setText(selectedDate);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }




//    private void addTODO() {
//        add_todo_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(getContext());
//                todoTaskBuilder.setTitle("What do you need to do?");
//                final EditText todoET = new EditText(getContext());
//                todoTaskBuilder.setView(todoET)
//                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNegativeButton("Cancel", null);
//                todoTaskBuilder.create().show();
//            }
//        });
//    }
//
    private void addDate() {
        add_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                 datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                     @Override
                     public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                         dueDate.setText(day+"/"+month+"/"+year);
                     }
                 }, year, month, day);
                 datePickerDialog.show();
            }
        });
    }

}