package com.pomodoro.ui;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Task;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.pomodoro.logic.SettingLogic;

public class pomodoroTimer extends JPanel {

    private int settingWorkTime = 25 * 60; // ตั้งต้น 25 นาที (1500 วิ)
    private int settingBreakTime = 5 * 60; // ตั้งต้น 5 นาที (300 วิ)
    
    private int currentTime = settingWorkTime; 
    private int totalTime = 0;
    private boolean isWorkMode = true;
    
    private Timer timer;
    
    private JLabel timerLabel;
    private JLabel breakLabel;
    private JLabel allTimeLabel;
    private JLabel completedLabel;
    
    private Task currentTask;
    private TaskManager taskManager;
    
    // ============================================================
    // UI Pomodoro
    // ============================================================

    public pomodoroTimer(TaskManager taskManager) {
        
        this.taskManager = taskManager;
        
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
        UIManager.put("Label.font", thaiFont);

        setVisible(false);
        
        setBounds(719, 5, 260, 551);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setLayout(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(9, 98, 235, 216);
        add(mainPanel);
        mainPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel timerPanel = new JPanel();
        timerPanel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        mainPanel.add(timerPanel, BorderLayout.CENTER);
        timerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        timerLabel = new JLabel("  25:00  ");
        timerLabel.setBackground(new Color(192, 192, 192));
        timerLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerPanel.add(timerLabel);
        
        breakLabel = new JLabel("โหมด: ทำงาน");
        breakLabel.setHorizontalAlignment(SwingConstants.CENTER);
        breakLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        breakLabel.setBackground(Color.LIGHT_GRAY);
        timerPanel.add(breakLabel);
        
        JPanel btnPanel = new JPanel();
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setLayout(new GridLayout(0, 3, 10, 0));
        
        JButton playBtn = new JButton("Play");
        playBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentTask != null) {
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(pomodoroTimer.this, "กรุณาเลือกงานก่อนเริ่มจับเวลา");
                }
            }
        });
        btnPanel.add(playBtn);
        
        JButton stopBtn = new JButton("Stop");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                saveCurrentTime();
                updateDetailsLabels();
            }
        });
        btnPanel.add(stopBtn);
        
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                saveCurrentTime();
                
                isWorkMode = true;
                currentTime = settingWorkTime;
                breakLabel.setText("โหมด: ทำงาน");
                timerLabel.setForeground(Color.BLACK);
                
                updateTimerLabel();
                updateDetailsLabels();
            }
        });
        btnPanel.add(resetBtn);
        
        JPanel showDetailsPanel = new JPanel();
        showDetailsPanel.setBorder(BorderFactory.createTitledBorder("Pomodoro Details"));
        showDetailsPanel.setBounds(9, 370, 235, 92);
        add(showDetailsPanel);
        showDetailsPanel.setLayout(null);
        
        JLabel allTimeTitle = new JLabel("เวลาทั้งหมด :");
        allTimeTitle.setBounds(10, 21, 88, 25);
        showDetailsPanel.add(allTimeTitle);
        
        allTimeLabel = new JLabel("00:00");
        allTimeLabel.setBounds(101, 21, 117, 25);
        showDetailsPanel.add(allTimeLabel);
        
        JLabel completedTitle = new JLabel("จำนวนครั้งที่ทำเสร็จ : ");
        completedTitle.setBounds(10, 47, 128, 25);
        showDetailsPanel.add(completedTitle);
        
        completedLabel = new JLabel("0");
        completedLabel.setBounds(148, 47, 67, 25);
        showDetailsPanel.add(completedLabel);
    
        timer = new Timer(1000, e -> updateTimer());
    }
    
    // ============================================================
    // LOGIC Pomodoro
    // ============================================================
    
    public void setSettingsTime(int workSeconds, int breakSeconds) {
        this.settingWorkTime = workSeconds;
        this.settingBreakTime = breakSeconds;
        
        // ไม่อนุญาตให้เปลี่ยนเวลา เมื่อเวลาเดิน
        if (!timer.isRunning()) {
        	// Ternary Operator --> condition ? value1 : value2
            currentTime = isWorkMode ? settingWorkTime : settingBreakTime;
            
            if (currentTask != null && isWorkMode) {
                currentTask.setRemainingSeconds(currentTime);
                taskManager.updateTask(currentTask);
            }
            updateTimerLabel();
        }
    }

    public void setTimeTask(Task task) {
        if (timer.isRunning()) {
            timer.stop();
            saveCurrentTime();
        }

        this.currentTask = task;
        this.isWorkMode = true;
        this.timerLabel.setForeground(Color.BLACK);
        this.breakLabel.setText("โหมด: ทำงาน");
        
        if (task.getRemainingSeconds() > 0) {
            this.currentTime = task.getRemainingSeconds();
        } else {
            this.currentTime = settingWorkTime;
        }
        
        updateTimerLabel();
        updateDetailsLabels();
    }
    
    private void updateTimerLabel() {
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;
        timerLabel.setText(String.format("  %02d:%02d  ", minutes , seconds));
    }
    
    private void updateTimer() {
        if (currentTime > 0) {
            currentTime--;
            if (isWorkMode) {
                totalTime++;
            }
        }
        
        updateTimerLabel();
        updateDetailsLabels();
        
        if (currentTime == 0) {
        	
        	SettingLogic logic = SettingLogic.getInstance();
        	
            timer.stop();
            
            if (isWorkMode) {
                if (currentTask != null) {
                    currentTask.setCompletedPomodoro(currentTask.getCompletedPomodoro() + 1);
                    currentTask.addFocusTime(totalTime);
                    currentTask.setRemainingSeconds(-1);
                    currentTask.setLastTimeWork(System.currentTimeMillis()); //--> ms
                    taskManager.updateTask(currentTask);
                    totalTime = 0;
                }
                
                // ตรวจสอบตั้งค่าของ โหมดทำงาน
                if (logic.isNotifyPomodoroEnd()) {
                    JOptionPane.showMessageDialog(this, "หมดเวลาทำงาน! ได้เวลาพักเบรกแล้ว");
                }
                
                // ตั้งค่าไปเป็น โหมดทำงาน
                isWorkMode = false;
                currentTime = settingBreakTime;
                breakLabel.setText("โหมด: พักเบรก");
                timerLabel.setForeground(new Color(0, 153, 0));
                
            } else {
                // ตรวจสอบตั้งค่าของ โหมดพักเบรก (แก้เป็น isNotifyBreakEnd)
                if (logic.isNotifyBreakEnd()) {
                    JOptionPane.showMessageDialog(this, "หมดเวลาพัก! ลุยงานต่อเลย");
                }
                
                // ตั้งค่าไปเป็น โหมดพัก
                isWorkMode = true;
                currentTime = settingWorkTime;
                breakLabel.setText("โหมด: ทำงาน");
                timerLabel.setForeground(Color.BLACK);
            }
            
            updateTimerLabel();
            updateDetailsLabels();
        }
    }
    
    private void saveCurrentTime() {
        if (currentTask != null && isWorkMode) {
            currentTask.setRemainingSeconds(currentTime);
            currentTask.setLastTimeWork(System.currentTimeMillis()); // -> ms
            
            currentTask.addFocusTime(totalTime); 
            taskManager.updateTask(currentTask);
            
            totalTime = 0;
        }
    }
    
    private void updateDetailsLabels() {
        if (currentTask != null) {
            int totalSec = currentTask.getTotalFocusTime() + totalTime;
            int minute = totalSec / 60;
            int second = totalSec % 60;
            
            allTimeLabel.setText(String.format("%02d:%02d", minute, second));
            completedLabel.setText(String.valueOf(currentTask.getCompletedPomodoro()));
        }
    }
}