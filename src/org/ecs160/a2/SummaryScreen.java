package org.ecs160.a2;

import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.charts.*;

class FilterDialogue extends Dialog {
    public FilterDialogue() {
        setLayout(BoxLayout.y());
        Container sizeOptions = new Container(BoxLayout.x());
        UIComponents.SizeButtonObject sizeS = new UIComponents.SizeButtonObject("S");
        UIComponents.SizeButtonObject sizeM = new UIComponents.SizeButtonObject("M");
        UIComponents.SizeButtonObject sizeL = new UIComponents.SizeButtonObject("L");
        UIComponents.SizeButtonObject sizeXL = new UIComponents.SizeButtonObject("XL");

        sizeS.addMyListener();
        sizeM.addMyListener();
        sizeL.addMyListener();
        sizeXL.addMyListener();

        sizeOptions.addAll(sizeS, sizeM, sizeL, sizeXL);

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);

        cancel.addActionListener(c -> {
            this.dispose();
        });

        add(sizeOptions);
        add(cancel);
    }
}

public class SummaryScreen extends Form {
    Container Header = new Container();
    Container TaskList = new Container(BoxLayout.y());
    Dialog FilterDialog = new Dialog(BoxLayout.y());
    ChartComponent pieChart;

    private String name = "Task Name";
    private String size = "S";
    private String duration = "HH:mm:ss"; // TODO: replace with real duration

    private TaskContainer allTaskData;
    private final UINavigator ui;

    public SummaryScreen(UINavigator ui) {
        this.ui = ui;
    }

    @Override
    public void show() {
        createSummaryScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createSummaryScreen();
        super.showBack();
    }

    public void createSummaryScreen() {
        allTaskData = ui.backend.getUnarchivedTasks();

        setTitle("Summary");
        setLayout(new BorderLayout());

        SummaryGraph g = new SummaryGraph();
        pieChart = g.createPieChart();

        createHeader();
        createTaskList();

        add(BorderLayout.NORTH, Header);
        add(BorderLayout.NORTH, pieChart);
        add(BorderLayout.SOUTH, TaskList);
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.addActionListener(e->{
            showFilterDialog();
        });

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.addActionListener(e-> ui.goBack());

        Header.add(BorderLayout.EAST, filterButton);
        Header.add(BorderLayout.WEST, backButton);
    }

    private void createTaskList() {
        TaskList = new Container(BoxLayout.y());
        if (allTaskData.isEmpty()) {
            TaskList.add("No Tasks to Display");
        } else {
            for (Task taskObj : allTaskData) {
                TaskList.add(new UIComponents.SummaryTaskObject(taskObj, ui));
            }
        }
    }

    private void showFilterDialog() {
        FilterDialog = new Dialog(BoxLayout.y());
        Container sizeOptions = new Container(BoxLayout.x());

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);

        cancel.addActionListener(c -> {
            FilterDialog.dispose();
        });

        FilterDialog.add(sizeOptions);
        FilterDialog.add(cancel);

        FilterDialog.show();
    }
}