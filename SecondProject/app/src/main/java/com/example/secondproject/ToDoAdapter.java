package com.example.secondproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        int month = (calendar.get(Calendar.MONTH)+1);
        Log.d("month", "log month"+month);
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
        ImageView edit_button;
        LinearLayout background;
        String saveday;
        String savemonth;
        String doWhat;
        int year, month, day;

        ViewHolder(View itemView) {
            super(itemView);

            background = itemView.findViewById(R.id.background);
            text_todo = itemView.findViewById(R.id.todo_text);
            text_dueDate = itemView.findViewById(R.id.date_text);
            done_button = itemView.findViewById(R.id.todo_done);
            edit_button = itemView.findViewById(R.id.todo_edit);
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            done_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    final int pos = getAdapterPosition();
                    builder.setMessage("Did you complete this task?").setTitle("Task complete")
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

            text_dueDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    final int pos = getAdapterPosition();
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int tyear, int tmonth, int tday) {
                            if (tday!=myDataList.get(pos).getDay() || (tmonth+1)!=myDataList.get(pos).getMonth()){
                                saveday=Integer.toString(tday);
                                savemonth=Integer.toString(tmonth+1);
                                Log.d("d", "log "+savemonth+"/"+saveday);
                                editDate(view, pos, savemonth, saveday);
                            }
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });

            edit_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    final int pos = getAdapterPosition();
                    AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(view.getContext());
                    todoTaskBuilder.setTitle("Edit Task?");
                    final EditText todoET = new EditText(view.getContext());
                    todoTaskBuilder.setView(todoET)
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    doWhat = todoET.getText().toString();
                                    editTodo(view, pos, doWhat);
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    todoTaskBuilder.create().show();
                }
            });
        }


        private void deleteTodo(final View view, final int pos) {
            class DeleteTODO extends AsyncTask<Void, Void, String> {

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
            DeleteTODO dt = new DeleteTODO();
            dt.execute();
        }
    }

    private void editDate(final View view, final int pos, final String savemonth, final String saveday) {
        class editDate extends AsyncTask<Void, Void, String> {

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
                params.put("month", savemonth);
                params.put("day", saveday);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_EDI_TODO_DATE, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                int a = s.indexOf("result");
                final String result = s.substring(a + 8, a + 9);
                if (result.equals("1")) {
                    Toast.makeText(view.getContext(), "Edited", Toast.LENGTH_LONG).show();
                    filteredList.get(pos).setMonth(Integer.parseInt(savemonth));
                    filteredList.get(pos).setDay(Integer.parseInt(saveday));
                    Collections.sort(filteredList, new Comparator<ToDos>() {
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
                    notifyDataSetChanged();
//                    notifyItemChanged(pos);
                } else {
                    Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        editDate ed = new editDate();
        ed.execute();
    }

    private void editTodo(final View view, final int pos, final String doWhat) {
        class editTodo extends AsyncTask<Void, Void, String> {

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
                params.put("dowhat", doWhat);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_EDI_TODO_DO, params);
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                int a = s.indexOf("result");
                final String result = s.substring(a + 8, a + 9);
                if (result.equals("1")) {
                    Toast.makeText(view.getContext(), "Edited", Toast.LENGTH_LONG).show();
                    filteredList.get(pos).setTodo(doWhat);
                    notifyItemChanged(pos);
                } else {
                    Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        editTodo et = new editTodo();
        et.execute();
    }
}

