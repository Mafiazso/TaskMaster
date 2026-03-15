package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PomodoroTimer {

    private int durationMinutes = 25;
    private int secondsLeft;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private Task activeTask;

    private final Timer timer = new Timer(true);
    private TimerTask scheduledTimerTask;

    private Consumer<String> onTick;
    private Runnable onFinish;
    private Runnable onStatusChange;
    private Runnable onFocusMinuteAdded;

    // นับวินาทีสะสมในนาทีปัจจุบัน เพื่อเพิ่ม focus minute ทุก 60 วินาที
    private int secondsInCurrentMinute = 0;

    public PomodoroTimer(Consumer<String> onTick, Runnable onFinish, Runnable onStatusChange) {
        this.onTick = onTick;
        this.onFinish = onFinish;
        this.onStatusChange = onStatusChange;
        this.secondsLeft = durationMinutes * 60;
        updateTimerDisplay();
    }

    // กำหนด callback ที่จะถูกเรียกทุกครั้งที่สะสม focus time ครบ 1 นาที
    public void setOnFocusMinuteAdded(Runnable onFocusMinuteAdded) {
        this.onFocusMinuteAdded = onFocusMinuteAdded;
    }

    // เรียกทุก 1 วินาที — นับถอยหลังและสะสม focus time
    private void tick() {
        if (secondsLeft > 0) {
            secondsLeft--;
            updateTimerDisplay();
            if (activeTask != null) {
                secondsInCurrentMinute++;
                if (secondsInCurrentMinute >= 60) {
                    activeTask.addFocusTime(1);
                    secondsInCurrentMinute = 0;
                    if (onFocusMinuteAdded != null) {
                        onFocusMinuteAdded.run();
                    }
                }
            }
        } else {
            completeSession();
        }
    }

    // เรียกเมื่อนับถอยหลังครบ — mark task เป็น DONE แล้วแจ้ง UI
    private void completeSession() {
        clearRunningState();
        if (activeTask != null) {
            activeTask.setStatus(TaskStatus.DONE);
        }
        if (onFinish != null) {
            onFinish.run();
        }
        notifyStatusChange();
    }

    // ยกเลิก scheduled timer task ที่กำลังนับถอยหลังอยู่
    private void cancelScheduledTask() {
        if (scheduledTimerTask != null) {
            scheduledTimerTask.cancel();
            scheduledTimerTask = null;
        }
    }

    // สร้างและเริ่ม timer task ใหม่ที่เรียก tick() ทุก 1 วินาที
    private void scheduleTimer() {
        cancelScheduledTask();
        scheduledTimerTask = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
        timer.schedule(scheduledTimerTask, 1000, 1000);
    }

    // หยุด timer และเคลียร์ flag running/paused (ไม่ reset เวลา)
    private void clearRunningState() {
        isRunning = false;
        isPaused = false;
        cancelScheduledTask();
    }

    // อัปเดต label เวลาบน UI ผ่าน onTick callback
    private void updateTimerDisplay() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        if (onTick != null) {
            onTick.accept(String.format("%02d:%02d", minutes, seconds));
        }
    }

    public void start(Task task) {
        if (task == null) {
            return;
        }
        // ถ้า pause อยู่และเป็น task เดิม ให้ resume ต่อจากเวลาที่เหลือ
        if (isPaused && this.activeTask == task) {
            resume();
            return;
        }
        clearRunningState();
        secondsInCurrentMinute = 0;
        this.activeTask = task;
        this.activeTask.setStatus(TaskStatus.IN_PROGRESS);
        // ถ้า task มีเวลาที่เหลือค้างไว้ ให้เริ่มจากตรงนั้น ไม่ reset ใหม่
        if (task.getRemainingSeconds() > 0) {
            secondsLeft = task.getRemainingSeconds();
        } else {
            secondsLeft = durationMinutes * 60;
        }
        updateTimerDisplay();
        isRunning = true;
        isPaused = false;
        scheduleTimer();
        notifyStatusChange();
    }

    public void pause() {
        if (isRunning) {
            isRunning = false;
            isPaused = true;
            cancelScheduledTask();
            if (onFocusMinuteAdded != null) {
                onFocusMinuteAdded.run();
            }
            notifyStatusChange();
        }
    }

    public void resume() {
        if (isPaused && activeTask != null) {
            isRunning = true;
            isPaused = false;
            scheduleTimer();
            notifyStatusChange();
        }
    }

    public void stop() {
        if (activeTask != null) {
            activeTask.setRemainingSeconds(secondsLeft);
            activeTask.setStatus(TaskStatus.TODO);
            if (onFocusMinuteAdded != null) {
                onFocusMinuteAdded.run();
            }
        }
        clearRunningState();
        resetTime();
        secondsInCurrentMinute = 0;
        activeTask = null;
        notifyStatusChange();
    }

    private void notifyStatusChange() {
        if (onStatusChange != null) {
            onStatusChange.run();
        }
    }

    public void setDuration(int minutes) {
        this.durationMinutes = minutes;
        if (!isRunning && !isPaused) {
            resetTime();
        }
    }

    private void resetTime() {
        this.secondsLeft = durationMinutes * 60;
        updateTimerDisplay();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Task getActiveTask() {
        return activeTask;
    }

    public void shutdown() {
        cancelScheduledTask();
        timer.cancel();
    }
}