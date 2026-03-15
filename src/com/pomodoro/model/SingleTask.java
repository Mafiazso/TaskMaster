package com.pomodoro.model;

public class SingleTask extends Task {

    public SingleTask(String title, String priority, Category category) {
        super(title, priority, category);
    }

    @Override
    public String getSummary() {
        return "Single Task: " + getTitle();
    }

    @Override
    public String getTaskType() {
        return "Single";
    }
}