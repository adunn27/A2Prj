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
        TimeSpan time;
        Task task = retrieveTask.getTaskById(
                Integer.parseInt(split[TASK_ID_INDEX]));
        switch (split[COMMAND_INDEX]) {
        case "add":
            TaskId = Integer.parseInt(split[TASK_ID_INDEX]);
            task = new Task(split[NAME_INDEX]);
            task.setId(TaskId);
            retrieveTask.addTask(task);

            break;
        case "edit":
            task.setName(split[NAME_INDEX]);
            task.setDescription(split[4]); // third column is description
            task.setTaskSize(split[5]);

            List<String> tags_e= new ArrayList<>();
            for(int i = 6; i < split.length; i++){
                tags_e.add(split[i]);
            }
            task.addAllTags(tags_e);

            break;
        case "start":
            String stringTime_s = split[TIME_INDEX];
            LocalDateTime taskTime_s= LocalDateTime.parse(stringTime_s,formatter);
            task.start(taskTime_s);

            break;
        case "stop":
            String stringTime_e = split[TIME_INDEX];
            LocalDateTime taskTime_e= LocalDateTime.parse(stringTime_e,formatter);
            task.stop(taskTime_e);

            break;

        case "archive":
            task.archive();
            break;

        case "unarchive":
            task.unarchive();
            break;

        case "delete_task":
            retrieveTask.removeTask(task);
            break;

        case "delete_tag":
            task.removeTag(split[4]);
            break;

        case "delete_time":
            String timeString = split[NAME_INDEX];
            LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
            time = task.getTimeSpanByTime(dateTime);
            task.removeTimeSpanComponent(time);
            break;

        case "edit_time":
            String oldStartTimeString = split[NAME_INDEX];
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
            throw new IllegalArgumentException(
                    "Invalid command: " + split[COMMAND_INDEX] );
        }
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

    private String getTagsAsString(Task task) {
        String tags = "";
        boolean isFirstTag = true;
        for(String tag : task.getTags()){
            if (isFirstTag) isFirstTag = false;
            else tags += LOG_DELIMITER;
            tags += encode(tag);
        }
        return tags;
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

    public void delete_time (Task task, LocalDateTime time){

        String formatTime = time.format(formatter);

        System.out.println("log delete_time");
        writeToLog(createLogEntry(task,"delete_time", formatTime));
    }

    public void edit_time (Task task, LocalDateTime startTime,LocalDateTime newStartTime,
                           LocalDateTime endTime,LocalDateTime newEndTime){

        String formatStartTime = startTime.format(formatter);
        String formatEndTime = endTime.format(formatter);
        String formatNewStartTime = newStartTime.format(formatter);
        String formatNewEndTime = newEndTime.format(formatter);

        System.out.println("log edit_time");
        writeToLog( createLogEntry(task,"edit_time",
                formatStartTime,
                formatNewStartTime,
                formatEndTime,
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