package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.spinner.Picker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;


class HistoryTaskObject1 extends Container {
    private Label startLabel;
    private Label stopLabel;

    public HistoryTaskObject1(String startTime, String stopTime, String startDate, String endDate){
        setLayout(BoxLayout.x());

        Container LeftContainer = new Container();
        LeftContainer.setLayout(BoxLayout.y());

        Label startDateLabel = new Label("Start: " + startDate);
        Label endDateLabel = new Label("End: " + endDate);

        LeftContainer.add(startDateLabel);
        LeftContainer.add(endDateLabel);

        Container TimeContainer = new Container(BoxLayout.x());

        Border simpleBorder = Border.createLineBorder(1,UITheme.BLACK);
        getAllStyles().setBorder(simpleBorder);

        startLabel = new Label("Start: " + startTime);
        stopLabel = new Label("Stop: " + stopTime);

        TimeContainer.add(startLabel);
        TimeContainer.add(stopLabel);

        LeftContainer.add(TimeContainer);

        add(LeftContainer);
    }

    public void setStartLabel(String startDate){

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

    private Task taskData;
    private UINavigator ui;

    private void initData(Task taskData) {
        if (taskData == null) {
            name = "[Task Name]";
            size = "S";
            times.add("1:00");
            times.add("2:00");
            times.add("3:00");
            times.add("4:00");
            times.add("5:00");
            times.add("6:00");
        } else {
            name = taskData.getName();
            size = taskData.getTaskSizeString();
//            times = task.getTotalTime(); // TODO: needs total time
            times.add("1:00");
            times.add("2:00");
            times.add("1:00");
            times.add("2:00");
            times.add("3:00");
            times.add("4:00");
            times.add("5:00");
            times.add("6:00");
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
        initData(taskData);

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


        for (int i = 0; i < times.size(); i = i + 2){

            HistoryTaskObject1 newHTO = new HistoryTaskObject1(times.get(i), times.get(i+1), "1/2/22", "1/2/22");

            UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
            editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
            editButton.setMyColor(UITheme.LIGHT_GREY);
            editButton.addActionListener(e -> {
                EditTask(newHTO);
            });

            UIComponents.ButtonObject calendarButton = new UIComponents.ButtonObject();
            calendarButton.setMyIcon(FontImage.MATERIAL_PERM_CONTACT_CALENDAR);
            calendarButton.setMyColor(UITheme.LIGHT_GREY);
            calendarButton.addActionListener(e -> {
                EditTask(newHTO);
            });

            UIComponents.ButtonObject deleteButton = new UIComponents.ButtonObject();
            deleteButton.setMyIcon(FontImage.MATERIAL_DELETE);
            deleteButton.setMyColor(UITheme.RED);
            deleteButton.addActionListener(e -> {
                DeleteTask(newHTO);
            });

            newHTO.add(editButton);
            newHTO.add(deleteButton);
            TaskList.add(newHTO);
        }
    }

    private void DeleteTask(Component newHTO) {
        System.out.println("DELETING UI Component");
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Are you sure you want to delete?");

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setMyText("Yes");
        cancelButton.setMyColor(UITheme.RED);
        cancelButton.addActionListener(e -> {
            d.dispose();
        });

        UIComponents.ButtonObject submitButton = new UIComponents.ButtonObject();
        submitButton.setMyText("No");
        submitButton.setMyColor(UITheme.LIGHT_GREY);
        submitButton.addActionListener(e -> {
            //TODO add delete code right here
            removeComponent(newHTO);
            d.dispose();
        });

        d.add(cancelButton);
        d.add(submitButton);

        d.show();
    }

    private void EditTask(Component newHTO) {
        System.out.println("editUI Component");
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Edit Task History Dialog");

        SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-yyyy");

        //TODO ADD Editing code within here
        d.add("Select Start Time");
        Picker startTimePicker = new Picker();
        startTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        startTimePicker.setFormatter(DateFormat);
        d.add(startTimePicker);

        d.add("Select End Time");
        Picker endTimePicker = new Picker();
        endTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        endTimePicker.setFormatter(DateFormat);
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
            Date startDate = endTimePicker.getDate();

            System.out.println(endDate.toString().format("dd-MM-YYYY"));


            System.out.println("End Date: " + endTimePicker.getDate().toString());
            System.out.println("Start Date: " + startTimePicker.getDate().toString());
            d.dispose();
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
