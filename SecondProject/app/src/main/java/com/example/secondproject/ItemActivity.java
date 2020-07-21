package com.example.secondproject;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ItemActivity extends AppCompatActivity {

    String number, name, Oid;
    ImageView edit_name, edit_num, fixContact;
    TextView name_text, num_text;
    EditText name_edited, num_edited;
    String newName, newNum;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_clicked);

        name_text = (TextView) findViewById(R.id.textView1);
        num_text = (TextView) findViewById(R.id.textView2);
        edit_name = (ImageView) findViewById(R.id.edit_name);
        edit_num = (ImageView) findViewById(R.id.edit_num);
        name_edited = (EditText) findViewById(R.id.name_edited);
        num_edited = (EditText) findViewById(R.id.num_edited);
        fixContact = (ImageView) findViewById(R.id.fix_contact);
//        check_num = (ImageView) findViewById(R.id.num_check);


        Intent intent = getIntent(); /*데이터 수신*/

        name = intent.getExtras().getString("name"); /*String형*/
        name_text.setText(name);

        number = intent.getExtras().getString("number"); /*int형*/
        num_text.setText(String.valueOf(number));

        Oid = intent.getExtras().getString("Oid");
        pos = intent.getExtras().getInt("pos");
        Log.d("d", "Log"+pos);

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_edited.setText(name);
                name_edited.setVisibility(View.VISIBLE);
                name_text.setVisibility(View.GONE);
                fixContact.setVisibility(View.VISIBLE);
                fixContact.setClickable(true);
                edit_name.setVisibility(View.GONE);
                edit_num.setEnabled(false);
                fixContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newName = name_edited.getText().toString();
                        name_text.setText(newName);
                        edit_num.setEnabled(true);
                        ContactAdapter.filteredList.get(pos).setName(newName);
                        editContact(newName, number);
                        name_edited.setVisibility(View.GONE);
                        name_text.setVisibility(View.VISIBLE);
                        fixContact.setVisibility(View.INVISIBLE);
                        fixContact.setClickable(false);
                        edit_name.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        edit_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num_edited.setText(number);
                num_edited.setVisibility(View.VISIBLE);
                num_text.setVisibility(View.GONE);
                fixContact.setVisibility(View.VISIBLE);
                fixContact.setClickable(true);
                edit_num.setVisibility(View.GONE);
                edit_name.setEnabled(false);
                fixContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newNum = num_edited.getText().toString();
                        num_text.setText(newNum);
                        edit_name.setEnabled(true);
                        editContact(name, newNum);
                        ContactAdapter.filteredList.get(pos).setNumber(newNum);
                        num_edited.setVisibility(View.GONE);
                        num_text.setVisibility(View.VISIBLE);
                        fixContact.setVisibility(View.INVISIBLE);
                        fixContact.setClickable(false);
                        edit_num.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void editContact(final String newName, final String newNum) {
        class EditContact extends AsyncTask<Void, Void, String> {

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
                Log.d("d", "log "+Oid);

                params.put("Oid", Oid);
                Log.d("d", "log "+newName);
                params.put("name", newName);
                params.put("phone_no", newNum);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_EDI_CONTACT, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                int a = s.indexOf("result");
                final String result = s.substring(a + 8, a + 9);
                if (result.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Contact edited successfully", Toast.LENGTH_LONG).show();
                    fragment_1.mRecyclerView.getAdapter().notifyDataSetChanged();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to edit", Toast.LENGTH_LONG).show();
                }
            }
        }
        EditContact ec = new EditContact();
        ec.execute();
    }
}

