package com.example.myproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myproject.TaskDAO.Task;
import com.example.myproject.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.myproject.database.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import Adapter.TaskAdapter;

import static android.content.Context.ALARM_SERVICE;

public class bottomsheetFragment extends BottomSheetDialogFragment {
    TextInputLayout txtTitle;
    TextInputLayout txtDescription;
    TextInputLayout txtDate;
    TextInputLayout txtTime;
    TextInputEditText txtEditDate;
    TextInputEditText txtEditTime;
    TextInputEditText txtEditTitle;
    TextInputEditText txtEditDescription;
    Button addTask;
    int taskId;
    boolean isEdit;
    ImageView topimage;
    TextView topTitle;
    TextView topDesc;



    int mYear, mMonth, mDay;
    int mHour, mMinute;

    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    FragmentActivity activity;
    List<Task> taskList=new ArrayList<>();
    MainActivity mainActivity;

    public static int count = 0;
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm", Locale.US);

    public static bottomsheetFragment newInstance() {
        return new bottomsheetFragment();
    }

    public void setTaskId(int taskId, boolean isEdit,  FragmentActivity activity) {
        this.taskId = taskId;
        this.isEdit = isEdit;
        this.activity = activity;

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_task, container,
                false);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getWidgets();


        //for update
        if(isEdit){
            DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
            Task task=databaseHelper.getTask(taskId);
            txtEditTitle.setText(task.getTaskTitle());
            txtEditDescription.setText(task.getTaskDescription());
            txtEditDate.setText(task.getDate());
            txtEditTime.setText(task.getLastAlarm());
            addTask.setText("Update");
            topTitle.setText("Update Task");
            topDesc.setText("Update the task details below");
            topimage.setImageResource(R.drawable.ic_update);




        }

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        txtEditDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            (view1, year, monthOfYear, dayOfMonth) -> {
                                txtEditDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                datePickerDialog.dismiss();
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }
                return true;
            }
        });

        txtEditTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            (view12, hourOfDay, minute) -> {
                                txtEditTime.setText(hourOfDay + ":" + minute);
                                timePickerDialog.dismiss();
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
                return true;
            }
        });

        //ADD TASK

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String title = txtEditTitle.getText().toString().trim();
                    String description = txtEditDescription.getText().toString().trim();
                    String date = txtEditDate.getText().toString().trim();

                    String time = txtEditTime.getText().toString();

                    Task task = new Task();
                    task.setTaskTitle(title);
                    task.setTaskDescription(description);
                    task.setDate(date);
                    task.setLastAlarm(time);

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

                    if (isEdit) {

                        long rows = databaseHelper.updateData(task, taskId);
                        isEdit = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            createAnAlarm();
                        }
                        Intent refresh = new Intent(getActivity(), MainActivity.class);
                        startActivity(refresh);//Start the same Activity
                        dismiss();

                        Toast.makeText(getActivity(), "Task Updated Successfully!!", Toast.LENGTH_LONG).show();


                    } else {

                        long rows = databaseHelper.insertData(task);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            createAnAlarm();
                        }
                        Intent refresh = new Intent(getActivity(), MainActivity.class);
                        startActivity(refresh);//Start the same Activity
                        dismiss();

                        Toast.makeText(getActivity(), "Task Added Successfully!!", Toast.LENGTH_LONG).show();

                    }


                }
            }
        });

    }



    public void createAnAlarm() {
        try {
            Log.i("entered_create_alarm", "entered in the create alarm ");
            String[] items1 = txtEditDate.getText().toString().split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            String[] itemTime = txtEditTime.getText().toString().split(":");
            String hour = itemTime[0];
            String min = itemTime[1];

            String alarmTime=dd+"-"+month+"-"+year+" "+hour+":"+min;
            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());

            Calendar cal = new GregorianCalendar();
            cal.setTime(inputDateFormat.parse(alarmTime));
           /* cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
            cal.set(Calendar.MINUTE,Integer.parseInt(min) );
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DATE, Integer.parseInt(dd));
            //cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dd));
            cal.set(Calendar.MONTH, Integer.parseInt(month));
            cal.set(Calendar.YEAR,Integer.parseInt(year));*/


            Intent alarmIntent = new Intent(getActivity(), AlarmBroadcastReceiver.class);
            alarmIntent.putExtra("TITLE", txtEditTitle.getText().toString());
            alarmIntent.putExtra("DESC", txtEditDescription.getText().toString());
            alarmIntent.putExtra("DATE", txtEditDate.getText().toString());
            alarmIntent.putExtra("TIME", txtEditTime.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.i("entered_if", "entered in if "+txtEditTitle.getText().toString()+cur_cal.getTimeInMillis()+"  "+cal.getTimeInMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                count ++;

                PendingIntent intent = PendingIntent.getBroadcast(getActivity(), count, alarmIntent, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    }
                }
                count ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getWidgets(){
        txtTitle=getView().findViewById(R.id.txtTitle);
        txtDescription=getView().findViewById(R.id.txtDescription);
        txtDate=getView().findViewById(R.id.txtDate);
        txtTime=getView().findViewById(R.id.txtTime);
        txtEditDate=getView().findViewById(R.id.txtEditDate);
        txtEditTime=getView().findViewById(R.id.txtEditTime);
        txtEditTitle=getView().findViewById(R.id.txtEditTitle);
        txtEditDescription=getView().findViewById(R.id.txtEditDescription);
        addTask=getView().findViewById(R.id.btnTask);
        topimage=getView().findViewById(R.id.topimage);
        topTitle=getView().findViewById(R.id.topTitle);
        topDesc=getView().findViewById(R.id.topDesc);



    }

    public boolean validateFields(){


        String title=txtEditTitle.getText().toString().trim();
        String description=txtEditDescription.getText().toString().trim();
        String date=txtEditDate.getText().toString().trim();

        String time=txtEditTime.getText().toString();

        if(TextUtils.isEmpty(title)){
            txtEditTitle.setError("Please Enter Title");
            txtEditTitle.requestFocus();
            return false;
        }


        else if(TextUtils.isEmpty(description)){
            txtEditDescription.setError("Please Enter Description");
            txtEditDescription.requestFocus();
            return false;
        }

       else if(TextUtils.isEmpty(date)){
            txtEditDate.setError("Please Enter Date");
            txtEditDate.requestFocus();
            return false;
        }

        else if(TextUtils.isEmpty(time)){
            txtEditTime.setError("Please Enter Time");
            txtEditTime.requestFocus();
            return false;
        }

        else {
            return true;
        }



    }








}
