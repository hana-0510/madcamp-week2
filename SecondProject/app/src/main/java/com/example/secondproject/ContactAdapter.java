package com.example.secondproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<ContactData> myDataList;
    private ArrayList<ContactData> filteredList;

    ContactAdapter(ArrayList<ContactData> dataList)
    {
        this.myDataList = dataList;
        this.filteredList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
//        viewHolder.imageView.setImageResource(filteredList.get(position).getImage());
        viewHolder.name.setText(filteredList.get(position).getName());
        viewHolder.number.setText(filteredList.get(position).getNumber());
    }

    @Override
    public int getItemCount()
    {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return filteredList.size();
    }

    public ContactData getItem(int position){
        return filteredList.get(position);
    }

    public interface OnItemClickListner {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListner contactListener = null;

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.contactListener = listner;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView number;

        ViewHolder(View itemView)
        {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);

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
                        if (contactListener != null){
                            contactListener.onItemClick(v,pos);
                        }

                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemCLick(View v, int position) ;
    }

    //@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = myDataList;
                } else {
                    ArrayList<ContactData> filteringList = new ArrayList<>();
                    for(ContactData name : myDataList) {
                        if(name.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                        else if(name.getNumber().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                        else if(name.getNumber_raw().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ContactData>)results.values;
                notifyDataSetChanged();
            }
        };
    }

}
