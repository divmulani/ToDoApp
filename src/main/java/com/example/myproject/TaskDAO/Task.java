package com.example.myproject.TaskDAO;

public class Task {

    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private String date;
    private String firstAlarm;
    private String lastAlarm;
    boolean isComplete;

    public Task() {

    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstAlarm() {
        return firstAlarm;
    }

    public void setFirstAlarm(String firstAlarm) {
        this.firstAlarm = firstAlarm;
    }

    public String getLastAlarm() {
        return lastAlarm;
    }

    public void setLastAlarm(String lastAlarm) {
        this.lastAlarm = lastAlarm;
    }
}
