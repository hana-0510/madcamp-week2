package com.example.secondproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<ContactData> myDataList;
    public static ArrayList<ContactData> filteredList;

    ContactAdapter(ArrayList<ContactData> dataList) {
        this.myDataList = dataList;
        this.filteredList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
//        viewHolder.imageView.setImageResource(filteredList.get(position).getImage());
        viewHolder.name.setText(filteredList.get(position).getName());
        viewHolder.number.setText(filteredList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView number;
        ImageView delete_contact;

        ViewHolder(View itemView) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            delete_contact = itemView.findViewById(R.id.contact_delete);
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

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

//            itemView.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION)
//                    {
//                        // 리스너 객체의 메서드 호출
//                        notifyItemChanged(pos) ;
//                        if (contactListener != null){
//                            contactListener.onItemClick(v,pos);
//                        }
//
//                    }
//                }
//            });
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(view.getContext(), ItemActivity.class);
                        intent.putExtra("name", myDataList.get(pos).getName());
                        intent.putExtra("number", myDataList.get(pos).getNumber());
                        intent.putExtra("Oid", myDataList.get(pos).getOid());
                        intent.putExtra("pos", pos);
                        view.getContext().startActivity(intent);
                    }
                    notifyItemChanged(pos);
                }
            });
        }

        private void deleteContact(final View view, final int pos) {
            class DeleteContact extends AsyncTask<Void, Void, String> {
                ContactAdapter myAdapter;

                @Override
                protected void onPreExecute() {
                    myAdapter = (ContactAdapter) fragment_1.mRecyclerView.getAdapter();
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    User user = SharedPrefManager.getInstance(view.getContext()).getUser();
                    params.put("id", user.getEmail());
                    params.put("password", user.getPassword());
                    params.put("Oid", myDataList.get(pos).getOid());
                    //returing the response
                    return requestHandler.sendPostRequest(URLs.URL_DEL_CONTACT, params);
                }

                @Override
                protected void onPostExecute(final String s) {
                    super.onPostExecute(s);
                    int a = s.indexOf("result");
                    final String result = s.substring(a + 8, a + 9);
                    if (result.equals("1")) {
                        Toast.makeText(view.getContext(), "Contact deleted successfully", Toast.LENGTH_LONG).show();
                        filteredList.remove(pos);
                        notifyItemRemoved(pos);
                    } else {
                        Toast.makeText(view.getContext(), "Contact not deleted", Toast.LENGTH_LONG).show();
                    }
                }
            }
            DeleteContact dc = new DeleteContact();
            dc.execute();
        }
    }

}
