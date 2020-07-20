package com.example.secondproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<GalleryData> myDataList;
    private ArrayList<GalleryData> filteredList;

    GalleryAdapter(ArrayList<GalleryData> dataList) {
        this.myDataList = dataList;
        this.filteredList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder viewHolder, int position) {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        Bitmap bit_temp = filteredList.get(position).getBitmap();
        //Bitmap bitmap_r = BitmapFactory.decodeByteArray(bit_temp, 0, bit_temp.length);
        viewHolder.GalleryItemView.setImageBitmap(bit_temp);
//        viewHolder.imageView.setImageResource(filteredList.get(position).getImage());
        //viewHolder.name.setText(filteredList.get(position).getOid());
        //viewHolder.number.setText(filteredList.get(position).getBitmap());
//        viewHolder.itemView.setOnClickListener(fil);
    }

    @Override
    public int getItemCount() {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return filteredList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView GalleryItemView;

        ViewHolder(View itemView) {
            super(itemView);

            GalleryItemView = itemView.findViewById(R.id.Gallery_item);

            //delete_contact = itemView.findViewById(R.id.contact_delete);
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
/*
            delete_contact.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    final int pos = getAdapterPosition();
                    builder.setMessage("Are you sure you want to delete this contact?").setTitle("DELETE")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteContact(view, pos);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            */
        }
    }
}
