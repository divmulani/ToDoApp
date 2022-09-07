package com.example.myproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myproject.TaskDAO.Task;
import com.example.myproject.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import Adapter.TaskAdapter;


public class tomorrow extends Fragment {

    List<Task> taskList=new ArrayList<>();
    RecyclerView tomorrowRecycler;
    TaskAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomorrow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getWidgets();

        DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        taskList=databaseHelper.getTomorrowTasks();
        for(Task task:taskList){
            Log.i("dbtomorrow","ID "+task.getTaskId()+" Task Title "+task.getTaskTitle());
        }

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        tomorrowRecycler.setLayoutManager(manager);
        adapter=new TaskAdapter(getActivity(),taskList);
        tomorrowRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getWidgets(){


        tomorrowRecycler=getView().findViewById(R.id.tomorrowRecycler);


    }
}