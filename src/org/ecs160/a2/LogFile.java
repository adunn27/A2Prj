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


public class LogFile {


    public TaskContainer retrieveTask;
    private File myObj;
    private Database db = null;
    private OutputStream os;
    public LogFile() {
        //db = Display.getInstance().openOrCreate("MyDB.db");
/*
        try( os = Storage.getInstance().createOutputStream("text")) {
            os.write(body.getText().getBytes("UTF-8"));

        } catch(IOException err) {
            Log.e(err);
        }*/

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
/*
                for (String token : data.split("\\|"))
                {
                    System.out.println(token);
                }

 */
                switch (data.split("\\|")[1]) {
                    case "add":
                        //System.out.println("the exp is "+data.split("\\|")[1]);
                        task = new Task(data.split("\\|")[2]);

                        task.setDescription(data.split("\\|")[3]);
                        //  task.setName(data.split("|")[3]);
                        task.setTaskSize(data.split("\\|")[4]);
                        List<String> tags_a= new ArrayList<>();
                        // String stringTags= data.split("\\,")[0];
                        for(String tag : data.split("\\|")[5].split(" ")){
                            tags_a.add(tag);
                        }
                        task.addAllTags(tags_a);
                        retrieveTask.addTask(task);

                        break;

                    case "edit":
                        task = new Task(data.split("\\|")[2]); // second column is name
                        task.setDescription(data.split("\\|")[3]); // third column is description
                        task.setTaskSize(data.split("\\|")[4]);

                        List<String> tags_e= new ArrayList<>();
                        boolean check_bit = data.split("\\|").length >5;
                        if (check_bit){
                            for(String tag : data.split("\\|")[5].split(" ")){
                                tags_e.add(tag);
                            }
                        }


                        task.addAllTags(tags_e);
                        retrieveTask.addTask(task);
                        System.out.println("fix here");
                        //retrieveTask.removeTask(retrieveTask.getTaskByName(data.split("\\|")[2]));

                        break;
                    case "start":
                        String taskName_s = data.split("\\|")[2];
                        String stringTime_s = data.split("\\|")[3];
                        DateTimeFormatter formatter_s= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime taskTime_s= LocalDateTime.parse(stringTime_s,formatter_s);

                        task = retrieveTask.getTaskByName(taskName_s);
                        //System.out.println(taskName_s);
                        task.getAllTimes().add(new TimeSpan(taskTime_s));
                        // System.out.println("!!!"+task.getAllTimes().size());

                        break;
                    case "stop":

                        String taskName_e = data.split("\\|")[2];
                        String stringTime_e = data.split("\\|")[3];
                        DateTimeFormatter formatter_e= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime taskTime_e= LocalDateTime.parse(stringTime_e,formatter_e);
                        task = retrieveTask.getTaskByName(taskName_e);
                        task.getAllTimes().get(task.getAllTimes().size() - 1).setEndTime(taskTime_e);

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

            writer.write(sdf.format(new Date(time_in_long)) + "|add|"+
                    task.getName() + "|" + task.getDescription() +
                    "|" + task.getTaskSize() + "|" + tags+ "\n");
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
                    "|" + task.getTaskSize() + "|" + tags+ "\n");
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





    /*
    public helper(){
        // TODO implement the helper function
        // a helper function reads the log file and write back to the task_list (Vector)
        try(InputStream is = Storage.getInstance().createInputStream("logfile")) {

            String result = Util.readToString(is, "UTF-8");

        } catch(IOException err) {
            Log.e(err);
        }
    }


    public writeTask (Task task){

        os.write("placeholder".getBytes("UTF-8"));

    }


    public readTask (Task task){
        task_list = helper();
        // TODO implement readTask
        // everytime when task is requested, always make sure that the task_list has updated
    }
*/


}