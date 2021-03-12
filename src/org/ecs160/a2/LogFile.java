package org.ecs160.a2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;
import com.codename1.db.Database;
import com.codename1.io.Storage;
import com.codename1.io.Util;



public class LogFile {
    private static final String TAG_DELIMITER = ",";
    private static final String ESCAPE_CHAR = "\\";
    public static final String LOG_DELIMITER = "|";
    public TaskContainer retrieveTask;
    public int TaskId;
    private File myObj;
    private Database db = null;
    private OutputStream os;
    private InputStream is;
    private InputStream readstorage;
    private String[] alllogs;
    private String log;


    public LogFile() {


        // BELOW IS A CLEAR BUTTON !!! THIS IS EQUIVALENT TO DELETE LOG FILE
        //Storage.getInstance().deleteStorageFile("log");

        if(	Storage.getInstance().exists("log")==false) {
            System.out.println("Storage has created");
            try {
                os = Storage.getInstance().createOutputStream("log");
            } catch (IOException err) {
                System.out.println(err);
            }

        }else{

            String readback = " ";
            try {
                readstorage = Storage.getInstance().createInputStream("log");
                readback = Util.readToString(readstorage, "UTF-8");

            }catch (IOException err) {
                System.out.println(err);
            }

            try {
                os = Storage.getInstance().createOutputStream("log");
                os.write(readback.getBytes("UTF-8"));

            } catch (IOException err) {
                System.out.println(err);
            }


        }

        int length =0 ;
        try {

            is = Storage.getInstance().createInputStream("log");
            String allLogString = Util.readToString(is, "UTF-8");

            if(!allLogString.isEmpty()){
                alllogs = allLogString.split("\\\n");
                length = alllogs.length;
            }
        //    alllogs = allLogString.split("\\|");
         //   System.out.println("ans"+allLogString.split("\\|")[0]);

        } catch(IOException err) {
            System.out.println(err);
        }




        TaskId = 0 ;
        try {
            // initialize writing to the file
            myObj = new File("log");
            if (myObj.createNewFile()) {
                System.out.println("log file created");
            } else {
                System.out.println("File has created");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        retrieveTask = new TaskContainer();

        try {
            File myObj = new File("log");
            Scanner myReader = new Scanner(myObj);
            Task task;
            TimeSpan time;
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String taskName;
           // System.out.println("12333"+alllogs.lengt);
            if(length>0) {
              //  System.out.println("hithere");
                for (String data : alllogs) {
                  //  System.out.println(data);
                    //String data = myReader.nextLine();
                    String[] split = data.split("\\|");
                    //System.out.println(split[1]);
                    switch (split[1]) {
                        case "add":

                            task = new Task(split[2]);
                            task.setDescription(split[3]);
                            task.setTaskSize(split[4]);
                           // System.out.println(split[6]);
                            TaskId = Integer.parseInt(split[6]);
                            task.setId(TaskId);
                            List<String> tags_a = new ArrayList<>();
                            for (String tag : split[5].split(TAG_DELIMITER)) {
                                tags_a.add(tag);
                            }
                            task.addAllTags(tags_a);

                            retrieveTask.addTask(task);

                            break;

                        case "edit":
                            task = retrieveTask.getTaskById(Integer.parseInt(split[6]));

                            task.setName(split[2]);
                            task.setDescription(split[3]); // third column is description
                            task.setTaskSize(split[4]);


                            List<String> tags_e = new ArrayList<>();
                            for (String tag : split[5].split(TAG_DELIMITER)) {
                                tags_e.add(tag);
                            }
                            task.addAllTags(tags_e);

                            break;
                        case "start":
                            taskName = split[2];
                            String stringTime_s = split[3];
                            LocalDateTime taskTime_s = LocalDateTime.parse(stringTime_s, formatter);
                            task = retrieveTask.getTaskByName(taskName);
                            task.getAllTimeSpans().add(new TimeSpan(taskTime_s));
                            task.setActive();

                            break;
                        case "stop":

                            taskName = split[2];
                            String stringTime_e = split[3];
                            LocalDateTime taskTime_e = LocalDateTime.parse(stringTime_e, formatter);
                            task = retrieveTask.getTaskByName(taskName);
                            task.getAllTimeSpans().get(task.getAllTimeSpans().size() - 1).setEndTime(taskTime_e);
                            task.setInActive();

                            break;

                        case "archive":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            task.archive();
                            break;

                        case "unarchive":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            task.unarchive();
                            break;
                        case "delete_task":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            retrieveTask.removeTask(task);
                            break;
                        case "delete_tag":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            task.removeTag(split[3]);
                            break;
                        case "delete_time":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            String timeString = split[3];
                            LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
                            time = task.getTimeSpanByTime(dateTime);
                            task.removeTimeSpanComponent(time);
                            break;
                        case "edit_time":
                            taskName = split[2];
                            task = retrieveTask.getTaskByName(taskName);
                            String oldStartTimeString = split[3];
                            String newStartTimeString = split[4];
                            String oldEndTimeString = split[5];
                            String newEndTimeString = split[6];

                            LocalDateTime oldStartDateTime = LocalDateTime.parse(oldStartTimeString, formatter);
                            LocalDateTime newStartDateTime = LocalDateTime.parse(newStartTimeString, formatter);
                            LocalDateTime oldEndDateTime = LocalDateTime.parse(oldEndTimeString, formatter);
                            LocalDateTime newEndDateTime = LocalDateTime.parse(newEndTimeString, formatter);


                            time = task.getTimeSpanByTime(oldStartDateTime);
                            time.setStartTime(newStartDateTime);
                            time.setEndTime(newEndDateTime);
                            break;
                        default:
                            System.out.println("Invalid command: " + split[1]);
                    }

                }
            }
           // myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void addTask (Task task){
        addOrEditATask(task, "add");
    }
    public void editTask (Task task){
        addOrEditATask(task, "edit");
    }

    private void addOrEditATask(Task task, String commandName) {
        try {
            // convert epoch to date and time format
            System.out.println("log " + commandName);
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));
            String tags = "";
            for(String tag : task.getTags()){
                tags= tags + tag + TAG_DELIMITER;
            }

            log= sdf.format(new Date(time_in_long)) +
                    LOG_DELIMITER + commandName +
                    LOG_DELIMITER + task.getName() +
                    LOG_DELIMITER + task.getDescription() +
                    LOG_DELIMITER + task.getTaskSize() +
                    LOG_DELIMITER + tags +
                    LOG_DELIMITER + task.getId() + "\n";
            os.write(log.getBytes("UTF-8"));
            writer.write(log);
            writer.close();
            System.out.println(log.getBytes("UTF-8"));

           // is = Storage.getInstance().createInputStream("log");
            //String allLogString = Util.readToString(is, "UTF-8");
            //System.out.println("Stringis"+allLogString);
            //alllogs = allLogString.split("\\\n");


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }


    public void startTask(Task task,LocalDateTime time){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatTime = time.format(formatter);

        System.out.println("log start");
        try {
            // convert epoch to date and time format
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            //  String placeholder = "" + task.
            log = sdf.format(new Date(time_in_long)) + "|start|"+
                    task.getName()+ LOG_DELIMITER +  formatTime +"\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    public void stopTask(Task task, LocalDateTime time){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatTime = time.format(formatter);
        System.out.println("log stop");

        try {
            // convert epoch to date and time format
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log= sdf.format(new Date(time_in_long)) + "|stop|"+
                    task.getName()+ LOG_DELIMITER +  formatTime +"\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public void unarchiveTask (Task task){

        try {
            // convert epoch to date and time format
            System.out.println("log unarchive");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));
            log = sdf.format(new Date(time_in_long)) + "|unarchive|"+
                    task.getName() + "\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void archiveTask (Task task){

        try {
            // convert epoch to date and time format
            System.out.println("log archive");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log = sdf.format(new Date(time_in_long)) + "|archive|"+
                    task.getName() + "\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));




        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void delete_task (Task task){

        try {
            // convert epoch to date and time format
            System.out.println("log delete_task");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log = sdf.format(new Date(time_in_long)) + "|delete_task|"+
                    task.getName() + "\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void delete_tag (Task task,String tag){

        try {
            // convert epoch to date and time format
            System.out.println("log delete_tag");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log = sdf.format(new Date(time_in_long)) + "|delete_tag|"+
                    task.getName() + LOG_DELIMITER + tag +"\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void delete_time (Task task, LocalDateTime time){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatTime = time.format(formatter);

        try {
            // convert epoch to date and time format
            System.out.println("log delete_time");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log = sdf.format(new Date(time_in_long)) + "|delete_time|"+
                    task.getName() + LOG_DELIMITER + formatTime +"\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void edit_time (Task task, LocalDateTime startTime,LocalDateTime newStartTime,
                           LocalDateTime endTime,LocalDateTime newEndTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatStartTime = startTime.format(formatter);
        String formatEndTime = endTime.format(formatter);
        String formatNewStartTime = newStartTime.format(formatter);
        String formatNewEndTime = newEndTime.format(formatter);


        try {
            // convert epoch to date and time format
            System.out.println("log edit_time");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));

            log = sdf.format(new Date(time_in_long)) + "|edit_time|"+
                    task.getName() + LOG_DELIMITER + formatStartTime+ LOG_DELIMITER + formatNewStartTime+
                    LOG_DELIMITER + formatEndTime+ LOG_DELIMITER + formatNewEndTime + "\n";
            writer.write(log);
            writer.close();
            os.write(log.getBytes("UTF-8"));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}