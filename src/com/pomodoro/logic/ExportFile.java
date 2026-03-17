package com.pomodoro.logic;

import com.pomodoro.model.Task;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportFile {
	
    public static String exportData(TaskManager taskManager) {
        // ชื่อไฟล์ที่จะสร้าง
        String filename = "export_tasks.txt"; 
        File file = new File(filename);

        try {
            // เช็คว่ามีไฟล์นี้อยู่แล้วหรือยัง? ถ้ายังไม่มีให้สร้างไฟล์ขึ้นมาใหม่
            if (!file.exists()) {
                file.createNewFile();
            }

            // เริ่มเขียนข้อมูลลงไฟล์
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter writer = new PrintWriter(fileWriter);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // เขียน Header
            writer.println("=========================================");
            writer.println("          TASK MASTER EXPORT DATA        ");
            writer.println("=========================================");
            writer.println();

            // --- ส่วนที่ 1: ข้อมูลงานปกติ (Active Tasks) ---
            writer.println(">>> [ Active Tasks / ข้อมูลงานปกติ ] <<<");
            writer.println("-----------------------------------------");
            List<Task> activeTasks = taskManager.getActiveTasks();
            writeTasksToFile(writer, activeTasks, sdf);

            // เว้นวรรค
            writer.println();
            
            // --- ส่วนที่ 2: ข้อมูลงานในถังขยะ (Trash Tasks) ---
            writer.println(">>> [ Trash Tasks / ข้อมูลงานที่ถูกลบ ] <<<");
            writer.println("-----------------------------------------");
            List<Task> trashTasks = taskManager.getTrashTasks();
            writeTasksToFile(writer, trashTasks, sdf);

            // ปิดการเขียนไฟล์
            writer.close();

            // ส่งคืนที่อยู่แบบเต็มของไฟล์กลับไปให้ผู้ใช้รู้ว่าเซฟไว้ที่ไหน
            return file.getAbsolutePath();

        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการสร้างหรือเขียนไฟล์: " + e.getMessage());
            return null; // ถ้าพังให้ส่งค่า null กลับไป
        }
    }

    private static void writeTasksToFile(PrintWriter writer, List<Task> tasks, SimpleDateFormat sdf) {
        
    	// เช็คว่าไฟล์ว่างมั้ย
    	if (tasks.isEmpty()) {
            writer.println(" - ไม่มีข้อมูลในส่วนนี้ - ");
            return;
        }

        for (Task task : tasks) {
        	// ข้อมูลของ Task
            writer.println("ชื่องาน: " + task.getTitle());
            writer.println("รายละเอียด: " + (task.getDescription() != null && !task.getDescription().isEmpty() ? task.getDescription() : "-"));
            writer.println("ความสำคัญ: " + task.getPriority());
            writer.println("หมวดหมู่: " + (task.getCategory() != null ? task.getCategory().getName() : "-"));
            writer.println("กำหนดส่ง: " + (task.getDueDate() != null ? sdf.format(task.getDueDate()) : "-"));
            writer.println("สถานะ: " + task.getStatus());
            
            // ข้อมูลของ Pomodoro
            int focusSeconds = task.getTotalFocusTime();
            int focusMins = focusSeconds / 60;
            int focusSecs = focusSeconds % 60;
            writer.println("จำนวนครั้งที่ทำเสร็จ (Pomodoro): " + task.getCompletedPomodoro() + " ครั้ง");
            writer.println("เวลาที่โฟกัสงานนี้: " + focusMins + " นาที " + focusSecs + " วินาที");
            
            writer.println("-----------------------------------------");
        }
    }
}