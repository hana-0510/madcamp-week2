package com.example.secondproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements Serializable

        {

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

    public GalleryData getItem(int position){
        return filteredList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);
    }


    private OnItemClickListener galleryListener = null;
    private OnItemLongClickListener galleryLongListener = null;

    public void setOnItemClickListner(OnItemClickListener listner) {
        this.galleryListener = listner;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listner) {
        this.galleryLongListener = listner;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView GalleryItemView;
/*
        ViewHolder(View itemView) {
            super(itemView);

            GalleryItemView = itemView.findViewById(R.id.Gallery_item);

            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        }
*/
        ViewHolder(View itemView)
        {
            super(itemView);

            GalleryItemView = itemView.findViewById(R.id.Gallery_item);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        // 리스너 객체의 메서드 호출
                        notifyItemChanged(pos) ;
                        if (galleryListener != null){
                            galleryListener.onItemClick(v,pos);
                        }

                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        notifyItemChanged(pos);
                        if (galleryLongListener != null){
                            galleryLongListener.onItemLongClick(v,pos);
                        }
                    }
                    return true;
                }
            });


        }
    }
}
