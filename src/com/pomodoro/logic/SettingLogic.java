package com.pomodoro.logic;

public class SettingLogic {
    private static SettingLogic instance;

    // ตั้งค่าเริ่มต้น: เวลา 25 นาที (1500 วินาที), พัก 5 นาที (300 วินาที)
    private int workTimeSeconds = 25 * 60;
    private int breakTimeSeconds = 5 * 60;
    
    // ตั้งค่า Checkbox เริ่มต้นเป็น true
    private boolean notifyPomodoroEnd = true;
    private boolean notifyDeadline = true;
    
    // break
    private boolean notifyBreakEnd = true; // ตั้งค่าเริ่มต้นให้แจ้งเตือน
    public boolean isNotifyBreakEnd() { return notifyBreakEnd; }
    public void setNotifyBreakEnd(boolean notifyBreakEnd) { this.notifyBreakEnd = notifyBreakEnd; }

    // ป้องกันการสร้าง Object ใหม่จากที่อื่น
    private SettingLogic() {}

    // Method ดึง Instance (Singleton)
    public static SettingLogic getInstance() {
        if (instance == null) {
            instance = new SettingLogic();
        }
        return instance;
    }

    // --- Getters & Setters ---
    public int getWorkTimeSeconds() { return workTimeSeconds; }
    public int getBreakTimeSeconds() { return breakTimeSeconds; }
    
    public boolean isNotifyPomodoroEnd() { return notifyPomodoroEnd; }
    public void setNotifyPomodoroEnd(boolean notifyPomodoroEnd) { this.notifyPomodoroEnd = notifyPomodoroEnd; }

    public boolean isNotifyDeadline() { return notifyDeadline; }
    public void setNotifyDeadline(boolean notifyDeadline) { this.notifyDeadline = notifyDeadline; }

    // ฟังก์ชันอัปเดตเวลา Pomodoro (รับค่าเป็นนาที แปลงเป็นวินาที)
    public void setPomodoroTime(int workMinutes, int breakMinutes) {
        this.workTimeSeconds = workMinutes * 60;
        this.breakTimeSeconds = breakMinutes * 60;
    }
}