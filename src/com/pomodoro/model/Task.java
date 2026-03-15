package com.pomodoro.model;

import java.util.Date;
import java.util.UUID;

public abstract class Task{

	private String id;
    private String title;
    private String description;
    private Date dueDate;
    private String status;
    private String priority;
    private Date reminderTime;
    private Category category;
    
    // เก็บเวลาที่เหลือของ task ไว้ resume ต่อได้ (-1 = ยังไม่เคย start)

    // - เปลี่ยนจาก minute เป็นทั้งนาทีและวินาที
    private int totalFocusTime;
    // บันทึกเวลาที่เหลือไว้เผื่อกลับมาทำต่อ ถ้าเป็นลบหนึ่งแปลว่ายังไม่ได้เริ่มทำ

    private int remainingSeconds = -1;
    // - เพิ่มโค้ดสำหรับเก็บค่า pomodoro
    private int completedPomodoro = 0;
	private long lastTimeWork = 0;
    

    public Task(String title, String priority, Category category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.status = TaskStatus.TODO;
        this.totalFocusTime = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return status == TaskStatus.DONE;
    }

    public void setCompleted(boolean completed) {
        if (completed) {
            this.status = TaskStatus.DONE;
        } else {
            this.status = TaskStatus.TODO;
        }
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getTotalFocusTime() {
        return totalFocusTime;
    }

    public void addFocusTime(int time) {
        this.totalFocusTime += time;
    }

    // Getter/Setter สำหรับเวลาที่เหลือ
    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }
    
    // - เพิ่ม getter / setter สำหรับ pomodoro
    public int getCompletedPomodoro() {
		return completedPomodoro;
	}

	public void setCompletedPomodoro(int completedPomodoro) {
		this.completedPomodoro = completedPomodoro;
	}

	public long getLastTimeWork() {
		return lastTimeWork;
	}

	public void setLastTimeWork(long lastTimeWork) {
		this.lastTimeWork = lastTimeWork;
	}
    

    // Abstract methods for subclasses
    public abstract String getSummary();

    public abstract String getTaskType();
}