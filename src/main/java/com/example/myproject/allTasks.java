package com.example.myproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myproject.TaskDAO.Task;
import com.example.myproject.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import Adapter.TaskAdapter;


public class allTasks extends Fragment  {

    List<Task> taskList=new ArrayList<>();
    RecyclerView taskRecycler;
    TaskAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWidgets();

        DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        taskList=databaseHelper.getAllTasks();
        for(Task task:taskList){
            Log.i("dbproject","ID "+task.getTaskId()+" Task Title "+task.getTaskTitle());
        }

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        taskRecycler.setLayoutManager(manager);
        adapter=new TaskAdapter(getActivity(),taskList);
        taskRecycler.setAdapter(adapter);
       adapter.notifyDataSetChanged();

        EditText inputSearch=getView().findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.cancelTimer();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (taskList.size() != 0){
                    adapter.searchNotes(s.toString());
                }

            }
        });


    }

    public void getWidgets(){


        taskRecycler=getView().findViewById(R.id.taskRecycler);


    }



}