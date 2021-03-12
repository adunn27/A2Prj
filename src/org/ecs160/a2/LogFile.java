package org.ecs160.a2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;
import java.util.regex.Pattern;


public class LogFile {
    public static final int COMMAND_INDEX = 1;
    public static final String LOG_DELIMITER = "|";
    public static final String LOG_DELIMITER_REPLACE = ":";
    // Could not figure out escape characters
    //private static final Character ESCAPE_CHAR = '\\';
    //public static final String DELIMITER_REGEX = String.format("%c(?!%c)",
    //                                            LOG_DELIMITER, ESCAPE_CHAR);
    public static final String LOG_FILE_NAME = "log";
    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final int NAME_INDEX = 3;
    public static final int TASK_ID_INDEX = 2;
    public static final int TIME_INDEX = 0;
    public TaskContainer retrieveTask;
    public int TaskId;
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
        TaskId = 0;
        File myObj;
        retrieveTask = new TaskContainer();

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
        }
    }

    private void executeLogLine(String[] split) {
        Task task = retrieveTask.getTaskById(
                Integer.parseInt(split[TASK_ID_INDEX]));

        switch (split[COMMAND_INDEX]) {
        case "add": executeAdd(split); break;
        case "edit": executeEdit(split, task); break;
        case "start": executeStart(split[TIME_INDEX], task); break;
        case "stop": executeStop(split[TIME_INDEX], task); break;
        case "archive": task.archive(); break;
        case "unarchive": task.unarchive(); break;
        case "delete_task": retrieveTask.removeTask(task); break;
        case "delete_tag": task.removeTag(split[4]); break;
        case "delete_time": executeDeleteTime(split, task); break;
        case "edit_time": executeEditTime(split, task); break;
        default: throw new IllegalArgumentException("Invalid command: "
                                                    + split[COMMAND_INDEX]);
        }
    }

    private void executeEditTime(String[] split, Task task) {
        TimeSpan time = task.getTimeSpanByIndex(Integer.parseInt(split[4]));

        LocalDateTime newStartDateTime =
                LocalDateTime.parse(split[5], formatter);
        LocalDateTime newEndDateTime =
                LocalDateTime.parse(split[6], formatter);

        time.setStartTime(newStartDateTime);
        time.setEndTime(newEndDateTime);
    }

    private void executeDeleteTime(String[] split, Task task) {
        TimeSpan time;
        time = task.getTimeSpanByIndex(Integer.parseInt(split[4]));
        task.removeTimeSpanComponent(time);
    }

    private void executeStop(String time, Task task) {
        LocalDateTime taskTime_e= LocalDateTime.parse(time, formatter);
        task.stop(taskTime_e);
    }

    private void executeStart(String time, Task task) {
        LocalDateTime taskTime_s = LocalDateTime.parse(time, formatter);
        task.start(taskTime_s);
    }

    private void executeEdit(String[] split, Task task) {
        task.setName(split[NAME_INDEX]);
        task.setDescription(split[4]);
        task.setTaskSize(split[5]);

        List<String> tags_e = new ArrayList<>(
                Arrays.asList(split).subList(6, split.length)
        );

        task.addAllTags(tags_e);
    }

    private void executeAdd(String[] split) {
        Task task;
        TaskId = Integer.parseInt(split[TASK_ID_INDEX]);
        task = new Task(split[NAME_INDEX]);
        task.setId(TaskId);
        retrieveTask.addTask(task);
    }

    public void addTask (Task task){
        System.out.println("log edit");
        writeToLog(createLogEntry(task, "add"));
    }
    public void editTask (Task task){
        System.out.println("log edit");
        List<String> args = new ArrayList<>();
        args.add(task.getDescription());
        args.add(task.getTaskSize().toString());
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
        String formatNewStartTime = newStartTime.format(formatter);
        String formatNewEndTime = newEndTime.format(formatter);

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

        line.append(when.format(formatter))
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
            BufferedWriter writer = new BufferedWriter(
                new FileWriter(LOG_FILE_NAME, true));

            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}