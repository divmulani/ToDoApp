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


public class upcoming extends Fragment {


    List<Task> taskList=new ArrayList<>();
    RecyclerView upcomingRecycler;
    TaskAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getWidgets();

        DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        taskList=databaseHelper.getUpcomingTasks();
        for(Task task:taskList){
            Log.i("dbupcoming","ID "+task.getTaskId()+" Task Title "+task.getTaskTitle());
        }

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        upcomingRecycler.setLayoutManager(manager);
        adapter=new TaskAdapter(getActivity(),taskList);
        upcomingRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getWidgets(){


        upcomingRecycler=getView().findViewById(R.id.upcomingRecycler);


    }
}