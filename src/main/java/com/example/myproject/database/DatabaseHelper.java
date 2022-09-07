package com.example.myproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myproject.TaskDAO.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "myProject";
    private static final String TABLE_NAME = "myTask";
    private static final int VERSION = 1;
    private static final String TASKID="TaskId";
    private static final String TITLE="Title";
    private static final String DESCRIPTION="Description";
    private static final String DATE="Date";
    private static final String TIME="Time";


    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_table="CREATE TABLE "+TABLE_NAME + " ("+TASKID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+
                " VARCHAR(50),"+DESCRIPTION+ " VARCHAR(100), "+DATE+" VARCHAR(20),"+TIME+" VARCHAR(20))";
        try {
            sqLiteDatabase.execSQL(create_table);
        }catch (Exception e){
            Log.e("Database Error",e.getLocalizedMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertData(Task task){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE,task.getTaskTitle());
        values.put(DESCRIPTION,task.getTaskDescription());
        values.put(DATE,task.getDate());
        values.put(TIME,task.getLastAlarm());

        long rows=db.insert(TABLE_NAME,null,values);
        return rows;

    }

    public long updateData(Task task,int id){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE,task.getTaskTitle());
        values.put(DESCRIPTION,task.getTaskDescription());
        values.put(DATE,task.getDate());
        values.put(TIME,task.getLastAlarm());

        long rows=db.update(TABLE_NAME,values,TASKID+"=?",new String[]{String.valueOf(id)});
        return rows;
    }

    //delete task
    public void deleteTask(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_NAME,TASKID+" =?",new String[]{String.valueOf(id)});

    }





    // get Data
    public List<Task> getAllTasks(){

        List<Task> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String select="SELECT * FROM "+TABLE_NAME+" ORDER BY "+DATE+","+TIME;
        Cursor cursor=db.rawQuery(select,null);
        int count=cursor.getCount();

        if (cursor.moveToFirst()){

            do {
                Task task=new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskDescription(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setLastAlarm(cursor.getString(4));
                taskList.add(task);

            }while (cursor.moveToNext());
        }
        return  taskList;

    }

    public List<Task> getTodayTasks(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = new Date();
        String newdate=formatter.format(date);
        Log.i("todayDate",newdate);
        System.out.println(formatter.format(date));

        List<Task> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String select="SELECT * FROM "+TABLE_NAME+" WHERE "+DATE+" = '"+newdate+"' ORDER BY "+TIME;
        Cursor cursor=db.rawQuery(select,null);
        int count=cursor.getCount();

        if (cursor.moveToFirst()){

            do {
                Task task=new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskDescription(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setLastAlarm(cursor.getString(4));
                taskList.add(task);

            }while (cursor.moveToNext());
        }
        return  taskList;

    }

    public List<Task> getTomorrowTasks(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);  // number of days to add
        String tomorrow = (String)(formatter.format(c.getTime()));

        Log.i("tomorrowDate",tomorrow);

        List<Task> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String select="SELECT * FROM "+TABLE_NAME+" WHERE "+DATE+" = '"+tomorrow+"' ORDER BY "+TIME;
        Cursor cursor=db.rawQuery(select,null);
        int count=cursor.getCount();

        if (cursor.moveToFirst()){

            do {
                Task task=new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskDescription(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setLastAlarm(cursor.getString(4));
                taskList.add(task);

            }while (cursor.moveToNext());
        }
        return  taskList;

    }

    public List<Task> getUpcomingTasks(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = new Date();
        String today=formatter.format(date);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);  // number of days to add
        String tomorrow = (String)(formatter.format(c.getTime()));

        Log.i("tomorrowDate",tomorrow);

        List<Task> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String select="SELECT * FROM "+TABLE_NAME+" WHERE "+DATE+" NOT IN('"+tomorrow+"','"+today+"') ORDER BY "+DATE+","+TIME;
        Cursor cursor=db.rawQuery(select,null);
        int count=cursor.getCount();

        if (cursor.moveToFirst()){

            do {
                Task task=new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskDescription(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setLastAlarm(cursor.getString(4));
                taskList.add(task);

            }while (cursor.moveToNext());
        }
        return  taskList;

    }

    public Task  getTask(int id){


        SQLiteDatabase db=this.getReadableDatabase();

        String select="SELECT * FROM "+TABLE_NAME+" WHERE "+TASKID+" = "+id;
        Cursor cursor=db.rawQuery(select,null);

        if( cursor != null && cursor.moveToFirst() ) {
            Task task = new Task();
            task.setTaskId(cursor.getInt(0));
            task.setTaskTitle(cursor.getString(1));
            task.setTaskDescription(cursor.getString(2));
            task.setDate(cursor.getString(3));
            task.setLastAlarm(cursor.getString(4));
            return task;

        }
        else{
            return null;
        }


    }




}
