package com.example.myproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;


public class allNotes extends Fragment {
    TextInputLayout txtDate;
    TextInputLayout txtTime;
    TextInputEditText txtEditDate;
    TextInputEditText txtEditTime;
    Button addTask;
    int taskId;
    boolean isEdit;

    int mYear, mMonth, mDay;
    int mHour, mMinute;

    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    MainActivity activity;
    public static int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getWidgets();


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

    }

    public void getWidgets(){
        txtDate=getView().findViewById(R.id.txtDate);
        txtTime=getView().findViewById(R.id.txtTime);
        txtEditDate=getView().findViewById(R.id.txtEditDate);
        txtEditTime=getView().findViewById(R.id.txtEditTime);

    }
}