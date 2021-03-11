package org.ecs160.a2;

import com.codename1.ui.Dialog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.concurrent.TimeUnit; // Program to measure elapsed time in
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import com.codename1.db.Database;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import com.codename1.io.Storage;



public class LogFile {


    public TaskContainer retrieveTask;
    public int TaskId;
    private File myObj;
    private Database db = null;
    private OutputStream os;

    public LogFile() {

        /*
        //db = Display.getInstance().openOrCreate("MyDB.db");

        try{
           // os.write(body.getText().getBytes("UTF-8"));
            os = Storage.getInstance().createOutputStream("text");

        } catch(IOException err) {
            Log.e(err);
        }*/
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


            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                switch (data.split("\\|")[1]) {
                    case "add":
                        //System.out.println("the exp is "+data.split("\\|")[1]);
                        task = new Task(data.split("\\|")[2]);

                        task.setDescription(data.split("\\|")[3]);
                        //  task.setName(data.split("|")[3]);
                        task.setTaskSize(data.split("\\|")[4]);
                        TaskId = Integer.parseInt(data.split("\\|")[6]);
                        task.setId(TaskId);
                        //TaskId;
                        List<String> tags_a= new ArrayList<>();
                        // String stringTags= data.split("\\,")[0];
                        for(String tag : data.split("\\|")[5].split(" ")){
                            tags_a.add(tag);
                        }
                        task.addAllTags(tags_a);

                        retrieveTask.addTask(task);

                        break;

                    case "edit":
                        task = retrieveTask.getTaskById(Integer.parseInt(data.split("\\|")[6]));

                        task.setName(data.split("\\|")[2]);
                        task.setDescription(data.split("\\|")[3]); // third column is description
                        task.setTaskSize(data.split("\\|")[4]);


                        List<String> tags_e= new ArrayList<>();
                        for(String tag : data.split("\\|")[5].split(" ")){
                            tags_e.add(tag);
                        }
                        /*
                        boolean check_bit = data.split("\\|").length >5;
                        if (check_bit){
                            for(String tag : data.split("\\|")[5].split(" ")){
                                tags_e.add(tag);
                            }
                        }*/
                        task.addAllTags(tags_e);

                        break;
                    case "start":
                        String taskName_s = data.split("\\|")[2];
                        String stringTime_s = data.split("\\|")[3];
                        DateTimeFormatter formatter_s= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime taskTime_s= LocalDateTime.parse(stringTime_s,formatter_s);

                        task = retrieveTask.getTaskByName(taskName_s);
                        task.start(taskTime_s);

                        break;
                    case "stop":

                        String taskName_e = data.split("\\|")[2];
                        String stringTime_e = data.split("\\|")[3];
                        DateTimeFormatter formatter_e= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime taskTime_e= LocalDateTime.parse(stringTime_e,formatter_e);
                        task = retrieveTask.getTaskByName(taskName_e);
                        task.stop(taskTime_e);

                        break;

                    case "archive":
                        String taskName_ar = data.split("\\|")[2];
                        task = retrieveTask.getTaskByName(taskName_ar);
                        task.archive();
                        break;

                    case "unarchive":
                        String taskName_uar = data.split("\\|")[2];
                        task = retrieveTask.getTaskByName(taskName_uar);
                        task.unarchive();
                        break;

                    default:
                        System.out.println("No Match Command, Try Again!");

                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void addTask (Task task){

        try {
            // convert epoch to date and time format
            System.out.println("log add");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));
            String tags= new String("");
            for(String tag : task.getTags()){
                tags= tags+tag+" ";

            }

            writer.write(sdf.format(new Date(time_in_long)) + "|add|"+
                    task.getName() + "|" + task.getDescription() +
                    "|" + task.getTaskSize() + "|" + tags+ "|"+ task.getId() +"\n");
            TaskId++;
            writer.close();



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void editTask (Task task){

        try {
            // convert epoch to date and time format
            System.out.println("log edit");
            long time_in_long = System.currentTimeMillis();

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss ");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("log", true));
            String tags= new String("");
            for(String tag : task.getTags()){
                tags= tags+tag+" ";

            }

            writer.write(sdf.format(new Date(time_in_long)) + "|edit|"+
                    task.getName() + "|" + task.getDescription() +
                    "|" + task.getTaskSize() + "|" + tags+ "|"+task.getId()+"\n");
            writer.close();



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    public void startTask(Task task){

        LocalDateTime time= LocalDateTime.now();
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
            System.out.println(LocalDateTime.now());

            writer.write(sdf.format(new Date(time_in_long)) + "|start|"+
                    task.getName()+  "|" +  formatTime +"\n");
            writer.close();



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    public void stopTask(Task task){
        LocalDateTime time= LocalDateTime.now();
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


            writer.write(sdf.format(new Date(time_in_long)) + "|stop|"+
                    task.getName()+  "|" +  formatTime +"\n");
            writer.close();

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
            String tags= new String("");
            for(String tag : task.getTags()){
                tags= tags+tag+" ";

            }

            writer.write(sdf.format(new Date(time_in_long)) + "|unarchive|"+
                    task.getName() + "\n");
            writer.close();



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
            String tags= new String("");
            for(String tag : task.getTags()){
                tags= tags+tag+" ";

            }

            writer.write(sdf.format(new Date(time_in_long)) + "|archive|"+
                    task.getName() + "\n");
            writer.close();



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }







}