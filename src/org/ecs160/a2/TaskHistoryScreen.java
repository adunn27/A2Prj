package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.spinner.Picker;

import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;


class HistoryTaskObject1 extends Container {
    public HistoryTaskObject1(String startTime, String stopTime,
                              String startDate, String stopDate, Duration totalDuration){
        setLayout(new BorderLayout());

        String durationHours = String.format("%02d",totalDuration.toHours());
        String durationMinutes = String.format("%02d",totalDuration.toMinutes()%60);
        String durationSeconds = String.format("%02d",totalDuration.toSeconds()%60);

        Container LeftContainer = new Container();
        LeftContainer.setLayout(BoxLayout.y());

        Border simpleBorder = Border.createLineBorder(1,UITheme.BLACK);
        getAllStyles().setBorder(simpleBorder);

        Label startDateLabel = new Label("Start: " + startDate + " " + startTime);
        Label stopDateLabel = new Label("Stop: " + stopDate + " " + stopTime);
        Label durationLabel = new Label("Duration: " + durationHours + ":"
                + durationMinutes + ":"
                + durationSeconds);

        LeftContainer.add(startDateLabel);
        LeftContainer.add(stopDateLabel);
        LeftContainer.add(durationLabel);

        add(WEST, LeftContainer);
    }
}

public class TaskHistoryScreen extends Form {
    private Container Header;
    private Container TaskList;

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Task taskData;
    private final UINavigator ui;

    public TaskHistoryScreen(Task task, UINavigator ui){
        taskData = task;
        this.ui = ui;
        createTaskHistoryScreen();
    }

    @Override
    public void show() {
        createTaskHistoryScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createTaskHistoryScreen();
        super.showBack();
    }

    private void createTaskHistoryScreen() {
        setTitle("Task History");
        setLayout(new BorderLayout());

        createHeader();
        createTaskList();

        add(NORTH, Header);
        add(CENTER, TaskList);
    }

    public String timeFormatter(int totalMinutes){
        String formattedTime;
        int hour;
        int minute = totalMinutes % 60;

        if (totalMinutes < 720){
            if (totalMinutes < 60){
                hour = 12;
                String formattedMinute = String.format("%02d", minute);
                String formattedHour = String.format("%02d", hour);
                formattedTime = formattedHour + ":" + formattedMinute + ":00" + " PM";
            } else {
                hour = (totalMinutes / 60);
                String formattedMinute = String.format("%02d", minute);
                String formattedHour = String.format("%02d", hour);
                formattedTime = formattedHour + ":" + formattedMinute + ":00" + " AM";
            }

        } else {
            if (totalMinutes < 780){
                hour = (totalMinutes / 60);
                String formattedMinute = String.format("%02d", minute);
                String formattedHour = String.format("%02d", hour);
                formattedTime = formattedHour + ":" + formattedMinute + ":00" + " AM";
            } else {
                hour = (totalMinutes / 60) - 12;
                String formattedMinute = String.format("%02d", minute);
                String formattedHour = String.format("%02d", hour);
                formattedTime = formattedHour + ":" + formattedMinute + ":00" + " PM";
            }
        }
        return formattedTime;
    }

    private void createTaskList(){
        TaskList = new Container();
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        Container EastContainer = new Container();
        EastContainer.setLayout(BoxLayout.x());
        EastContainer.add("Label");
        EastContainer.add("DELETE");
        EastContainer.add("EDIT");

        //TODO if something is broken remove this line
        taskData= ui.backend.getTaskByName(taskData.getName());
      
        for (int i = 0; i < taskData.getAllTimeSpans().size(); i++){


            TimeSpan thisTimeSpan = taskData.getAllTimeSpans().get(i);

            LocalDateTime startTime = thisTimeSpan.getStartTimeAsDate();
            String startTimeString = startTime.format(timeFormatter);
            LocalDateTime endTime = thisTimeSpan.getEndTimeAsDate();
            String endTimeString = endTime.format(timeFormatter);

            HistoryTaskObject1 newHTO = new HistoryTaskObject1(startTimeString, endTimeString,
                    startTime.format(dateFormatter), endTime.format(dateFormatter),
                    thisTimeSpan.getTimeSpanDuration());

            UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
            editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
            editButton.setMyColor(UITheme.LIGHT_GREY);
            editButton.addActionListener(e -> EditTask(thisTimeSpan, newHTO));

            UIComponents.ButtonObject calendarButton = new UIComponents.ButtonObject();
            calendarButton.setMyIcon(FontImage.MATERIAL_PERM_CONTACT_CALENDAR);
            calendarButton.setMyColor(UITheme.LIGHT_GREY);

            calendarButton.addActionListener(e -> EditTask(thisTimeSpan, newHTO));

            UIComponents.ButtonObject deleteButton = new UIComponents.ButtonObject();
            deleteButton.setMyIcon(FontImage.MATERIAL_DELETE);
            deleteButton.setMyColor(UITheme.RED);
            deleteButton.addActionListener(e -> DeleteTimeSpan(thisTimeSpan, newHTO));

            Container RightContainer = new Container(BoxLayout.y());
            RightContainer.add(editButton);
            RightContainer.add(deleteButton);
            newHTO.add(EAST, RightContainer);
            TaskList.add(newHTO);
        }
    }

    private void DeleteTimeSpan(TimeSpan deletedTimeSpan, Component deletedComponent) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Are you sure you want to delete?");

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setMyText("Yes");
        cancelButton.setMyColor(UITheme.RED);
        cancelButton.addActionListener(e -> {
            //TODO add delete code right here
            taskData.removeTimeSpanComponent(deletedTimeSpan);
            TaskList.removeComponent(deletedComponent);
            d.dispose();
        });

        UIComponents.ButtonObject submitButton = new UIComponents.ButtonObject();
        submitButton.setMyText("No");
        submitButton.setMyColor(UITheme.LIGHT_GREY);
        submitButton.addActionListener(e -> d.dispose());

        d.add(cancelButton);
        d.add(submitButton);

        d.show();
    }

    private void EditTask(TimeSpan editedTimeSpan, Component editedComponent) {
        Dialog PopupDialog = new Dialog();
        PopupDialog.setLayout(BoxLayout.y());

        int h = Display.getInstance().getDisplayHeight();
        int w = Display.getInstance().getDisplayWidth();

        Container d = BoxLayout.encloseYCenter();
        PopupDialog.setTitle("Edit Task History");

        LocalDateTime initStartDate = editedTimeSpan.getStartTimeAsDate();
        LocalDateTime initEndDate = editedTimeSpan.getEndTimeAsDate();
        Date StartDate = Date.from(initStartDate.atZone(ZoneId.systemDefault()).toInstant());
        Date EndDate = Date.from(initEndDate.atZone(ZoneId.systemDefault()).toInstant());

        Container startContainer = BoxLayout.encloseXCenter();
        Container endContainer = BoxLayout.encloseXCenter();

        Container startLabel = BoxLayout.encloseXCenter();
        startLabel.add("Select Start Time");
        d.add(startLabel);

        Picker startDatePicker = new Picker();
        startDatePicker.setType(Display.PICKER_TYPE_CALENDAR);
        startDatePicker.setDate(StartDate);
        startContainer.add(startDatePicker);

        Picker startTimePicker = new Picker();
        startTimePicker.setMinuteStep(1);
        startTimePicker.setType(Display.PICKER_TYPE_TIME);
        startTimePicker.setTime(StartDate.getHours(), StartDate.getMinutes());
        startTimePicker.setShowMeridiem(true);
        startTimePicker.setMinuteStep(1);
        startContainer.add(startTimePicker);
        d.add(startContainer);

        Container endLabel = BoxLayout.encloseXCenter();
        endLabel.add("Select End Time");
        d.add(endLabel);
        Picker endDatePicker = new Picker();
        endDatePicker.setType(Display.PICKER_TYPE_CALENDAR);
        endDatePicker.setDate(EndDate);
        endContainer.add(endDatePicker);

        Picker endTimePicker = new Picker();
        endTimePicker.setMinuteStep(1);
        endTimePicker.setType(Display.PICKER_TYPE_TIME);
        endTimePicker.setTime(EndDate.getHours(), EndDate.getMinutes());
        endTimePicker.setShowMeridiem(true);
        endTimePicker.setMinuteStep(1);
        endContainer.add(endTimePicker);
        d.add(endContainer);

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setMyText("Cancel");
        cancelButton.setMyColor(UITheme.LIGHT_GREY);
        cancelButton.addActionListener(e -> {
            PopupDialog.dispose();
        });

        UIComponents.ButtonObject submitButton = new UIComponents.ButtonObject();
        submitButton.setMyText("Submit");
        submitButton.setMyColor(UITheme.LIGHT_YELLOW);
        submitButton.addActionListener(e -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            String endDate = formatter.format(endDatePicker.getDate());
            String startDate = formatter.format(startDatePicker.getDate());
            String startTime = timeFormatter(startTimePicker.getTime());
            String endTime = timeFormatter(endTimePicker.getTime());
            String start = startDate + " " + startTime;
            String end = endDate + " " + endTime;

            Date start_1 = new Date();
            Date end_1 = new Date();
            try {
                start_1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").parse(start);
                end_1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").parse(end);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }

            LocalDateTime startDateTime = start_1.toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endDateTime = end_1.toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (startDateTime.isAfter(endDateTime)){
                System.out.println("You can't have a negative duration!");
                Dialog newDialog = new Dialog();

                Container warningDialog = new Container();

                warningDialog.setLayout(BoxLayout.y());
                warningDialog.add("You can't have a negative duration!");
                UIComponents.ButtonObject button = new UIComponents.ButtonObject();
                button.setMyText("OK");
                button.setMyColor(UITheme.LIGHT_YELLOW);
                button.addActionListener(event -> {
                    newDialog.dispose();
                });
                warningDialog.add(button);
                newDialog.add(warningDialog);
                newDialog.show();
            } else {
                editedTimeSpan.setStartTime(startDateTime);
                editedTimeSpan.setEndTime(endDateTime);


                PopupDialog.dispose();
                ui.refreshScreen();
                ui.refreshScreen();
            }
        });

        d.add(submitButton);
        d.add(cancelButton);

        PopupDialog.add(d);

        PopupDialog.show(h/8 * 2, h/8 * 3, w / 8, w / 8);
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyPadding(UITheme.PAD_3MM);

        backButton.addActionListener(e-> ui.goBack());
        Header.add(BorderLayout.WEST, backButton);
    }

}
