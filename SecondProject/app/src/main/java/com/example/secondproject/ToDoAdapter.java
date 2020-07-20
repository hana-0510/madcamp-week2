package com.example.secondproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private ArrayList<ToDos> myDataList;
    private ArrayList<ToDos> filteredList;

    ToDoAdapter(ArrayList<ToDos> dataList) {
        this.myDataList = dataList;
        this.filteredList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
//        viewHolder.imageView.setImageResource(filteredList.get(position).getImage());
        viewHolder.text_todo.setText(filteredList.get(position).getToDo());
        int savedmonth = filteredList.get(position).getMonth();
        int savedday = filteredList.get(position).getDay();
        viewHolder.text_dueDate.setText(savedmonth+" / "+savedday);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (savedmonth<month) {
            viewHolder.background.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red));
        } else if (savedmonth==month && savedday<day){
            viewHolder.background.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_todo;
        TextView text_dueDate;
        ImageView done_button;
        LinearLayout background;

        ViewHolder(View itemView) {
            super(itemView);

            background = itemView.findViewById(R.id.background);
            text_todo = itemView.findViewById(R.id.todo_text);
            text_dueDate = itemView.findViewById(R.id.date_text);
            done_button = itemView.findViewById(R.id.todo_done);
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            done_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    final int pos = getAdapterPosition();
                    builder.setMessage("Are you sure you want to delete this contact?").setTitle("DELETE")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteTodo(view, pos);
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
        }


        private void deleteTodo(final View view, final int pos) {
            class DeleteContact extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() { super.onPreExecute(); }

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
                    return requestHandler.sendPostRequest(URLs.URL_DEL_TODO, params);
                }

                @Override
                protected void onPostExecute(final String s) {
                    super.onPostExecute(s);
                    int a = s.indexOf("result");
                    final String result = s.substring(a + 8, a + 9);
                    if (result.equals("1")) {
                        Toast.makeText(view.getContext(), "Well Done!", Toast.LENGTH_LONG).show();
                        filteredList.remove(pos);
                        notifyItemRemoved(pos);
                    } else {
                        Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
            DeleteContact dc = new DeleteContact();
            dc.execute();
        }
    }
}
