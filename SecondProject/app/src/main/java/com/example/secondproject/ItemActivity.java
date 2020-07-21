package com.example.secondproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {
    ImageButton call_btn;
    String number, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_expand);

        TextView tx1 = (TextView)findViewById(R.id.textView1);
        TextView tx2 = (TextView)findViewById(R.id.textView2);
//        call_btn = findViewById(R.id.call_btn);

        Intent intent = getIntent(); /*데이터 수신*/

        name = intent.getExtras().getString("name"); /*String형*/
        tx1.setText(name);

        number = intent.getExtras().getString("number"); /*int형*/
        tx2.setText(String.valueOf(number));

//        call_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
//                startActivity(tt);
//            }
//        });
    }

}
