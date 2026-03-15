package com.pomodoro.ui.MenuUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import com.pomodoro.logic.SettingLogic;
import com.pomodoro.ui.pomodoroTimer;

public class settingMenuDesign extends JPanel {
    private JTextField focusField;
    private JTextField breakField;
    private pomodoroTimer pomodoroTimer;

    public static final int DEFAULT_NOTIFY_BEFORE_DEADLINE_MINUTES = 1;

    public settingMenuDesign(pomodoroTimer pomodoroTimer) {
        this.pomodoroTimer = pomodoroTimer;
        SettingLogic logic = SettingLogic.getInstance(); 

        setBounds(113, 5, 601, 551);
        setLayout(null);
        
        JPanel settingPanel = new JPanel();
        settingPanel.setBounds(10, 11, 568, 190);
        add(settingPanel);
        settingPanel.setLayout(null);
        
        JPanel pomoPanel = new JPanel();
        pomoPanel.setBounds(0, 0, 568, 183);
        settingPanel.add(pomoPanel);
        pomoPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel pomorodoLabel = new JLabel("Pomodoro Timer");
        pomorodoLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pomorodoLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        pomorodoLabel.setBounds(6, 4, 164, 14);
        pomoPanel.add(pomorodoLabel, BorderLayout.NORTH);
        
        JPanel pomoRadio = new JPanel();
        pomoRadio.setBounds(31, 27, 139, 69);
        pomoPanel.add(pomoRadio, BorderLayout.CENTER);
        pomoRadio.setLayout(new BoxLayout(pomoRadio, BoxLayout.Y_AXIS));
        
        JRadioButton p_Radio1 = new JRadioButton("25 : 5");
        pomoRadio.add(p_Radio1);
        
        JRadioButton p_Radio2 = new JRadioButton("50 : 10");
        pomoRadio.add(p_Radio2);
        
        JRadioButton p_Radio3 = new JRadioButton("Custom");
        pomoRadio.add(p_Radio3);

        javax.swing.ButtonGroup pomoGroup = new javax.swing.ButtonGroup();
        pomoGroup.add(p_Radio1);
        pomoGroup.add(p_Radio2);
        pomoGroup.add(p_Radio3);
        p_Radio1.setSelected(true); 
        
        JPanel customSetting = new JPanel();
        customSetting.setBounds(0, 103, 151, 56);
        pomoPanel.add(customSetting, BorderLayout.SOUTH);
        customSetting.setLayout(new BorderLayout(0, 0));
        
        JPanel Setting = new JPanel();
        customSetting.add(Setting, BorderLayout.WEST);
        Setting.setLayout(new BoxLayout(Setting, BoxLayout.Y_AXIS));
        
        JPanel focusSetting = new JPanel();
        Setting.add(focusSetting);
        
        JLabel focusLabel = new JLabel("Focus : ");
        focusLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        focusSetting.add(focusLabel);
        
        focusField = new JTextField();
        focusSetting.add(focusField);
        focusField.setColumns(10);
        
        JPanel breakSetting = new JPanel();
        Setting.add(breakSetting);
        
        JLabel breakLabel = new JLabel("Break : ");
        breakLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        breakSetting.add(breakLabel);
        
        breakField = new JTextField();
        breakSetting.add(breakField);
        breakField.setColumns(10);
        
        JButton saveCustom = new JButton("Save");
        Setting.add(saveCustom);

        focusField.setEnabled(false);
        breakField.setEnabled(false);
        saveCustom.setEnabled(false);

        p_Radio1.addActionListener(e -> {
            focusField.setEnabled(false);
            breakField.setEnabled(false);
            saveCustom.setEnabled(false);
            logic.setPomodoroTime(25, 5);
            pomodoroTimer.setSettingsTime(logic.getWorkTimeSeconds(), logic.getBreakTimeSeconds());
        });

        p_Radio2.addActionListener(e -> {
            focusField.setEnabled(false);
            breakField.setEnabled(false);
            saveCustom.setEnabled(false);
            logic.setPomodoroTime(50, 10);
            pomodoroTimer.setSettingsTime(logic.getWorkTimeSeconds(), logic.getBreakTimeSeconds());
        });

        p_Radio3.addActionListener(e -> {
            focusField.setEnabled(true);
            breakField.setEnabled(true);
            saveCustom.setEnabled(true);
        });

        saveCustom.addActionListener(e -> {
            try {
                int focus = Integer.parseInt(focusField.getText());
                int breakTime = Integer.parseInt(breakField.getText());
                logic.setPomodoroTime(focus, breakTime);
                pomodoroTimer.setSettingsTime(logic.getWorkTimeSeconds(), logic.getBreakTimeSeconds());
                JOptionPane.showMessageDialog(this, "บันทึกเวลาสำเร็จ!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกเฉพาะตัวเลขเท่านั้น!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Notification Setting ---
        JPanel notiPanel = new JPanel();
        notiPanel.setBounds(10, 235, 568, 134);
        add(notiPanel);
        notiPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JPanel notiSetting = new JPanel();
        notiPanel.add(notiSetting);
        notiSetting.setLayout(new BoxLayout(notiSetting, BoxLayout.Y_AXIS));
        
        JLabel notiLabel = new JLabel("Notification Setting");
        notiLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        notiSetting.add(notiLabel);
        
        JPanel notiRadio = new JPanel();
        notiSetting.add(notiRadio);
        notiRadio.setLayout(new BoxLayout(notiRadio, BoxLayout.Y_AXIS));
        
        JCheckBox checkbox1 = new JCheckBox("แจ้งเตือนเมื่อหมดเวลาทำงาน (Focus)");
        notiRadio.add(checkbox1);
        
        // เพิ่ม Checkbox สำหรับโหมดพักเบรก
        JCheckBox checkboxBreak = new JCheckBox("แจ้งเตือนเมื่อหมดเวลาพักเบรก (Break)");
        notiRadio.add(checkboxBreak);
        
        JCheckBox checkbox2 = new JCheckBox("แจ้งเตือนก่อนถึง Deadline (1 นาที)");
        notiRadio.add(checkbox2);

        // ตั้งค่า Checkbox ตาม Logic ปัจจุบัน
        checkbox1.setSelected(logic.isNotifyPomodoroEnd());
        checkboxBreak.setSelected(logic.isNotifyBreakEnd());
        checkbox2.setSelected(logic.isNotifyDeadline());

        // --- Logic การกด Checkbox ให้บันทึกลง SettingLogic แบบ Real-time ---
        checkbox1.addActionListener(e -> logic.setNotifyPomodoroEnd(checkbox1.isSelected()));
        checkboxBreak.addActionListener(e -> logic.setNotifyBreakEnd(checkboxBreak.isSelected()));
        checkbox2.addActionListener(e -> logic.setNotifyDeadline(checkbox2.isSelected()));
    }
}