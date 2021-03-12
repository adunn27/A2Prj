package org.ecs160.a2;

import com.codename1.io.Storage;
import com.codename1.io.Util;

import java.io.*;
import java.time.LocalDateTime;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;
import java.util.regex.Pattern;


public class LogFile {
    private static final String LOG_FILE_NAME = "task.log";
    private static final String LOG_DELIMITER = "|";
    // Backup since we could not get escape characters to work
    private static final String LOG_DELIMITER_REPLACE = ":";

    private static final int COMMAND_INDEX = 1;
    private static final int NAME_INDEX = 3;
    private static final int TASK_ID_INDEX = 2;
    private static final int TIME_INDEX = 0;
    private static final int DELETE_TASK_INDEX = 4;
    private static final int DESCRIPTION_INDEX = 4;
    private static final int TASK_SIZE_INDEX = 5;
    private static final int TIMESPAN_INDEX_INDEX = 4;

    private final TaskContainer retrievedTasks;
    private int lastTaskId;

    private OutputStream os;
    //private InputStream readStorage;
    //private String[] alllogs;

    public LogFile() throws IOException {


        // BELOW IS A CLEAR BUTTON !!! THIS IS EQUIVALENT TO DELETE LOG FILE
        //Storage.getInstance().deleteStorageFile(LOG_FILE_NAME);

        if(!Storage.getInstance().exists(LOG_FILE_NAME)) {
            System.out.println("Storage has created");
            os = Storage.getInstance().createOutputStream(LOG_FILE_NAME);
        }
        os = Storage.getInstance().createOutputStream(LOG_FILE_NAME);
        /*else{

            String readback = " ";
            try {
                readstorage = Storage.getInstance().createInputStream(LOG_FILE_NAME);
                readback = Util.readToString(readstorage, "UTF-8");

            }catch (IOException err) {
                System.out.println(err);
            }

            try {
                os = Storage.getInstance().createOutputStream(LOG_FILE_NAME);
                os.write(readback.getBytes("UTF-8"));

            } catch (IOException err) {
                System.out.println(err);
            }


        }*/
        InputStream readStorage = Storage.getInstance().createInputStream(LOG_FILE_NAME);;
       /*try {
            readStorage = Storage.getInstance().createInputStream(LOG_FILE_NAME);
           /* String allLogString = Util.readToString(readStorage, "UTF-8");
            if(!allLogString.isEmpty()){
                alllogs = allLogString.split("\\\n");
            }
        //} catch(IOException err) {
        //    System.out.println(err);

        //}*/

        lastTaskId = 0;
        retrievedTasks = new TaskContainer();

        BufferedReader myReader = new BufferedReader(new InputStreamReader(readStorage));
        int line = 0;
        try {
            while (myReader.ready()) {
                line++;
                String data = myReader.readLine();
                System.out.println(data);
                String[] split = data.split(Pattern.quote(LOG_DELIMITER));
                executeLogLine(split);
            }
        } catch (IOException e) {
            System.out.println("An error occurred in log on line " + line);
            throw e;
        }

        /*
        File myObj;
        try {
            // initialize writing to the file
            myObj = new File(LOG_FILE_NAME);
            if (myObj.createNewFile()) {
                System.out.println("log file created");
            } else {
                System.out.println("log found");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            myObj = new File(LOG_FILE_NAME);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] split = data.split(Pattern.quote(LOG_DELIMITER));
                executeLogLine(split);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }*/
    }

    public int getLastTaskId() {
        return lastTaskId;
    }

    public TaskContainer getRetrievedTasks() {
        return retrievedTasks;
    }

    private void executeLogLine(String[] split) {
        Task task = retrievedTasks.getTaskById(
                Integer.parseInt(split[TASK_ID_INDEX]));

        if (split.length < 2) return; //TODO
        switch (split[COMMAND_INDEX]) {
        case "add":     executeAddFromLog(split); break;
        case "edit":    executeEditFromLog(split, task); break;
        case "start":   executeStartFromLog(split[TIME_INDEX], task); break;
        case "stop":    executeStopFromLog(split[TIME_INDEX], task); break;
        case "archive":     task.archive(); break;
        case "unarchive":   task.unarchive(); break;
        case "delete_task": retrievedTasks.removeTask(task); break;
        case "delete_tag":  task.removeTag(split[DELETE_TASK_INDEX]); break;
        case "delete_time": executeDeleteTimeFromLog(split, task); break;
        case "edit_time":   executeEditTimeFromLog(split, task); break;
        default: throw new IllegalArgumentException("Invalid command: "
                                                    + split[COMMAND_INDEX]);
        }
    }

    private void executeStartFromLog(String time, Task task) {
        LocalDateTime taskTime_s =
                LocalDateTime.parse(time, Utility.timeFormatter24hr);
        task.start(taskTime_s);
    }

    private void executeStopFromLog(String time, Task task) {
        LocalDateTime taskTime_e=
                LocalDateTime.parse(time, Utility.timeFormatter24hr);
        task.stop(taskTime_e);
    }

    private void executeAddFromLog(String[] split) {
        System.out.println(split);
        Task task;
        lastTaskId = Integer.parseInt(split[TASK_ID_INDEX]);
        task = new Task();
        task.setId(lastTaskId);
        retrievedTasks.addTask(task);
    }

    private void executeEditFromLog(String[] split, Task task) {
        task.setName(split[NAME_INDEX]);
        task.setDescription(split[DESCRIPTION_INDEX]);
        task.setTaskSizeWithString(split[TASK_SIZE_INDEX]);

        List<String> tags_e = new ArrayList<>(
                Arrays.asList(split).subList(6, split.length)
        );

        task.addAllTags(tags_e);
    }

    private void executeEditTimeFromLog(String[] split, Task task) {
        TimeSpan time = task.getTimeSpanByIndex(
                Integer.parseInt(split[TIMESPAN_INDEX_INDEX]));

        LocalDateTime newStartDateTime =
                LocalDateTime.parse(split[5], Utility.timeFormatter24hr);
        LocalDateTime newEndDateTime =
                LocalDateTime.parse(split[6], Utility.timeFormatter24hr);

        time.setStartTime(newStartDateTime);
        time.setEndTime(newEndDateTime);
    }

    private void executeDeleteTimeFromLog(String[] split, Task task) {
        TimeSpan time;
        time = task.getTimeSpanByIndex(
                Integer.parseInt(split[TIMESPAN_INDEX_INDEX]));
        task.removeTimeSpan(time);
    }

    public void addTask (Task task){
        System.out.println("log edit");
        writeToLog(createLogEntry(task, "add"));
    }
    public void editTask (Task task){
        System.out.println("log edit");
        List<String> args = new ArrayList<>();
        args.add(task.getDescription());
        args.add(task.getTaskSizeString());
        args.addAll(task.getTags());

        writeToLog(createLogEntry(task, "edit",
                args.toArray(new String[0])
        ));
    }

    public void startTask(Task task, LocalDateTime time){
        System.out.println("log start");
        writeToLog(createLogEntry(time, task, "start"));
    }

    public void stopTask(Task task, LocalDateTime time){
        System.out.println("log stop");
        writeToLog(createLogEntry(time, task, "stop"));
    }

    public void unarchiveTask (Task task){
        System.out.println("log unarchive");
        writeToLog(createLogEntry(task, "unarchive"));
    }

    public void archiveTask (Task task){
        System.out.println("log archive");
        writeToLog(createLogEntry(task, "archive"));
    }

    public void delete_task (Task task){
        System.out.println("log delete_task");
        writeToLog(createLogEntry(task,"delete_task"));
    }

    public void delete_tag (Task task, String tag){
        System.out.println("log delete_tag");
        writeToLog(createLogEntry(task, "delete_tag", tag));
    }

    public void delete_time (Task task, int timeSpanIndex){

        System.out.println("log delete_time");
        writeToLog(createLogEntry(task,"delete_time",
                Integer.toString(timeSpanIndex)));
    }

    public void edit_time (Task task, int timeSpanIndex,
                           LocalDateTime newStartTime,LocalDateTime newEndTime){
        String formatNewStartTime =
                newStartTime.format(Utility.timeFormatter24hr);
        String formatNewEndTime =
                newEndTime.format(Utility.timeFormatter24hr);

        System.out.println("log edit_time");
        writeToLog( createLogEntry(task,"edit_time",
                Integer.toString(timeSpanIndex),
                formatNewStartTime,
                formatNewEndTime));
    }

    private String createLogEntry(Task task, String command,
                                  String ... arguments) {
        return createLogEntry(LocalDateTime.now(), task, command, arguments);
    }

    private String createLogEntry(LocalDateTime when, Task task,
                                  String command, String ... arguments) {
        StringBuilder line = new StringBuilder();

        line.append(when.format(Utility.timeFormatter24hr))
            .append(LOG_DELIMITER).append(command)
            .append(LOG_DELIMITER).append(task.getId())
            .append(LOG_DELIMITER).append(task.getName());

        for (String anArg: arguments) {
            line.append(LOG_DELIMITER)
                .append(encode(anArg));
        }
        line.append("\n");

        return line.toString();
    }

    private String encode(String str) {
        return str.replace(LOG_DELIMITER, LOG_DELIMITER_REPLACE);
    }

    private void writeToLog(String output) {
        try {
            /*BufferedWriter writer = new BufferedWriter(
                new FileWriter(LOG_FILE_NAME, true));

            writer.write(output);
            writer.close();*/
            os.write(output.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}