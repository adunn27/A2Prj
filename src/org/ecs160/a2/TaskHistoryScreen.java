package org.ecs160.a2;

import static com.codename1.ui.CN.*;
import static org.ecs160.a2.UITheme.*;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.spinner.Picker;

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
    private Container TaskList;

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final Task taskData;
    private final UINavigator ui;

    public TaskHistoryScreen(Task task, UINavigator ui){
        taskData = task;
        this.ui = ui;
        createToolbar();
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
        removeAll();
        setTitle("Task History");
        setLayout(new BorderLayout());

        createTaskList();

        add(CENTER, TaskList);
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

        for (TimeSpan thisTimeSpan: taskData.getAllTimeSpans()){
            if (thisTimeSpan.isActive()) {
                TaskList.add(new Label("New run started: " +
                        thisTimeSpan.getStartTimeAsString()));
                continue;
            }

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
          
            deleteButton.setMyColor(RED);
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
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Are you sure you want to delete?");

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setMyText("Yes");
        cancelButton.setMyColor(RED);
        cancelButton.addActionListener(e -> {
            //TODO add delete code right here
            ui.backend.removeTimeSpan(taskData, deletedTimeSpan);
            TaskList.removeComponent(deletedComponent);
            d.dispose();

        });

        UIComponents.ButtonObject submitButton = new UIComponents.ButtonObject();
        submitButton.setMyText("No");
        submitButton.setMyColor(COL_UNSELECTED);
        submitButton.addActionListener(e -> {
            d.dispose();
        });

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
            LocalDateTime startDateTime = getTimeFromPickers(startDatePicker,
                                                             startTimePicker);

            LocalDateTime endDateTime = getTimeFromPickers(endDatePicker,
                                                           endTimePicker);

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
                ui.backend.editTimeSpan(taskData, editedTimeSpan,
                        startDateTime, endDateTime);

                PopupDialog.dispose();
                ui.refreshScreen();
            }
        });

        d.add(submitButton);
        d.add(cancelButton);

        PopupDialog.add(d);

        PopupDialog.show(h/8 * 2, h/8 * 3, w / 8, w / 8);
    }

    private LocalDateTime getTimeFromPickers(Picker datePicker,
                                             Picker timePicker) {
        return datePicker.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().atStartOfDay()
                .plusMinutes(timePicker.getTime());
    }
    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("",
                UITheme.ICON_BACK, UITheme.PAD_6MM,
                e->ui.goBack());
    }
}
