package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

// TODO: needs list of tags

//class SummaryHeader extends Container {
//    public SummaryHeader() {
//        setLayout(new BorderLayout());
//        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
//        filterButton.setMyColor(UITheme.YELLOW);
//        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
//        filterButton.addActionListener(e->{
//            Dialog fd = new FilterDialogue();
//            fd.show();
//        });
//
//        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
//        filterButton.setMyColor(UITheme.YELLOW);
//        filterButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
//        filterButton.addActionListener(e->goBack());
//
//        add(BorderLayout.EAST, filterButton);
//        add(BorderLayout.WEST, backButton);
//    }
//}

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
    Form prevPage;
    Form currentPage;

    Container Header = new Container();

    private String name = "Task Name";
    private String size = "S";
    private String duration = "HH:mm:ss";

    private TaskContainer allTaskData;

    public SummaryScreen(TaskContainer allTasks) {
        allTaskData = allTasks;

        prevPage = Display.getInstance().getCurrent();
        currentPage = new Form("Summary");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        currentPage.add(BorderLayout.NORTH, Header);

        Container taskList = new Container(BoxLayout.y());
        if (allTaskData.isEmpty()) {
        } else {
            taskList.add(new UIComponents.SummaryTaskObject(name, size, duration)); //TODO: add all tasks
        }

        currentPage.add(BorderLayout.SOUTH, taskList);
        currentPage.show();
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.addActionListener(e->{
            Dialog fd = new FilterDialogue();
            fd.show();
        });

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.addActionListener(e-> UINavigator.goBack(prevPage));

        Header.add(BorderLayout.EAST, filterButton);
        Header.add(BorderLayout.WEST, backButton);
    }
}