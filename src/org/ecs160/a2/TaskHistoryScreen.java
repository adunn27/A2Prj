package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.spinner.Picker;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


class HistoryTaskObject1 extends Container {
    private Label startDateLabel;
    private Label stopDateLabel;
    public HistoryTaskObject1(String startTime, String stopTime,
                              String startDate, String stopDate){
        setLayout(new BorderLayout());

        Container LeftContainer = new Container();
        LeftContainer.setLayout(BoxLayout.y());

        Border simpleBorder = Border.createLineBorder(1,UITheme.BLACK);
        getAllStyles().setBorder(simpleBorder);

        startDateLabel = new Label ("Start: " + startDate + " " + startTime);
        stopDateLabel = new Label("Stop: " + stopDate + " " + stopTime);

        LeftContainer.add(startDateLabel);
        LeftContainer.add(stopDateLabel);

        add(WEST, LeftContainer);
    }
}

public class TaskHistoryScreen extends Form {
    private Container Header;
    private Container Footer;
    private Container TaskList;

    private String name;
    private String size;
    private java.util.List<String> tags = new ArrayList<>();
    private java.util.List<String> times = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
    private Task taskData;
    private UINavigator ui;

    private void initData(Task taskData) throws Error{
        if (taskData == null) {
            name = "[Task Name]";
            size = "S";
            throw new Error("Task is not null");
        } else {
            name = taskData.getName();
            size = taskData.getTaskSizeString();
//            times = task.getTotalTime(); // TODO: needs total time
        }
    }

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

    private void createTaskList(){
        TaskList = new Container();
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        //Because Border Layout Acts wierd I had to
        Container EastContainer = new Container();
        EastContainer.setLayout(BoxLayout.x());
        EastContainer.add("Label");
        EastContainer.add("DELETE");
        EastContainer.add("EDIT");

        System.out.println(taskData.getAllTimeSpans().size());

        for (int i = 0; i < taskData.getAllTimeSpans().size(); i++){

            TimeSpan thisTimeSpan = taskData.getAllTimeSpans().get(i);

            LocalDateTime startTime = thisTimeSpan.getStartTimeAsDate();
            String startTimeString = startTime.format(timeFormatter);
            LocalDateTime endTime = thisTimeSpan.getEndTimeAsDate();
            String endTimeString = endTime.format(timeFormatter);

            HistoryTaskObject1 newHTO = new HistoryTaskObject1(startTimeString, endTimeString,
                    startTime.format(dateFormatter), endTime.format(dateFormatter));

            UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
            editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
            editButton.setMyColor(UITheme.LIGHT_GREY);
            editButton.addActionListener(e -> {
                EditTask(thisTimeSpan, newHTO);
            });

            UIComponents.ButtonObject calendarButton = new UIComponents.ButtonObject();
            calendarButton.setMyIcon(FontImage.MATERIAL_PERM_CONTACT_CALENDAR);
            calendarButton.setMyColor(UITheme.LIGHT_GREY);

            calendarButton.addActionListener(e -> {
                EditTask(thisTimeSpan, newHTO);
            });

            UIComponents.ButtonObject deleteButton = new UIComponents.ButtonObject();
            deleteButton.setMyIcon(FontImage.MATERIAL_DELETE);
            deleteButton.setMyColor(UITheme.RED);
            deleteButton.addActionListener(e -> {
                DeleteTimeSpan(thisTimeSpan, newHTO);
            });

            Container RightContainer = new Container(BoxLayout.y());
            RightContainer.add(editButton);
            RightContainer.add(deleteButton);
            newHTO.add(EAST, RightContainer);
            TaskList.add(newHTO);
        }
    }

    private void DeleteTimeSpan(TimeSpan deletedTimeSpan, Component deletedComponent) {
        System.out.println("DELETING UI Component");
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
        submitButton.addActionListener(e -> {
            d.dispose();
        });

        d.add(cancelButton);
        d.add(submitButton);

        d.show();
    }

    private void EditTask(TimeSpan editedTimeSpan, Component editedComponent) {
        System.out.println("editUI Component");
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Edit Task History Dialog");

        LocalDateTime initStartDate = editedTimeSpan.getStartTimeAsDate();
        LocalDateTime initEndDate = editedTimeSpan.getEndTimeAsDate();

        d.add("Select Start Time");
        Picker startTimePicker = new Picker();
        startTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        startTimePicker.setFormatter(dateTimeFormat);
        Date StartDate = Date.from(initStartDate.atZone(ZoneId.systemDefault()).toInstant());
        startTimePicker.setDate(StartDate);
        d.add(startTimePicker);

        d.add("Select End Time");
        Picker endTimePicker = new Picker();
        endTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        endTimePicker.setFormatter(dateTimeFormat);
        Date EndDate = Date.from(initEndDate.atZone(ZoneId.systemDefault()).toInstant());
        endTimePicker.setDate(EndDate);
        d.add(endTimePicker);

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setMyText("Cancel");
        cancelButton.setMyColor(UITheme.RED);
        cancelButton.addActionListener(e -> {
            d.dispose();
        });

        UIComponents.ButtonObject submitButton = new UIComponents.ButtonObject();
        submitButton.setMyText("Submit");
        submitButton.setMyColor(UITheme.LIGHT_GREY);
        submitButton.addActionListener(e -> {
            //TODO add changes here
            Date endDate = endTimePicker.getDate();
            Date startDate = startTimePicker.getDate();

            LocalDateTime startDateTime = startDate.toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endDateTime = endDate.toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime();

            editedTimeSpan.setStartTime(startDateTime);
            editedTimeSpan.setEndTime(endDateTime);
            editedTimeSpan.setStartTime(startDateTime);

            d.dispose();
            ui.refreshScreen();
            ui.refreshScreen();
        });


        d.add(submitButton);
        d.add(cancelButton);

        d.show();
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
