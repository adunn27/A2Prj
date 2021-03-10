package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.*;

import com.codename1.ui.layouts.BoxLayout;

class SearchBar extends Container{
    public SearchBar(){
        setLayout(BoxLayout.xRight());
        getAllStyles().setMarginLeft(100);
        TextField searchBar = new TextField("", "search", 12, TextArea.ANY);
        searchBar.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
        searchBar.getAllStyles().setFgColor(UITheme.BLACK);
        add(searchBar);
        Button filterButton = new Button("Filter");
        filterButton.getAllStyles().setFgColor(UITheme.BLACK);
        filterButton.setIcon(FontImage.createMaterial(
                FontImage.MATERIAL_FILTER_LIST,
                filterButton.getUnselectedStyle()
        ));
        add(filterButton);
    }
}

public class ArchiveScreen extends Form {
    Container TaskList;
    Container Footer;

    private TaskContainer tasks;
    private final UINavigator ui;

    public ArchiveScreen(UINavigator ui) {
        createToolbar();
        this.ui = ui;
    }

    @Override
    public void show() {
        createArchiveScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createArchiveScreen();
        super.showBack();
    }

    public void createArchiveScreen() {
        tasks = ui.backend.getArchivedTasks();

        setTitle("Archive");

        setLayout(new BorderLayout());

        createTaskList();

//        add(NORTH, Header);
        add(CENTER, TaskList);
    }

    private void createTaskList(){
        TaskList = new Container();
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        for (Task taskObj : tasks) {
            UIComponents.TaskObject taskObject = new UIComponents.TaskObject(taskObj, ui);
            TaskList.add(taskObject);
        }
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("",
                FontImage.MATERIAL_ARROW_BACK, UITheme.PAD_6MM,
                e->ui.goBack());
    }

    private void createFooter(){
        Footer = new Container();
        Footer.setLayout(new GridLayout(1,2));
        Footer.setScrollableY(false);

        // history
        Button historyButton = new Button("History");
        historyButton.getAllStyles().setFgColor(UITheme.WHITE);
        historyButton.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        historyButton.getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
        historyButton.getAllStyles().setBorder(RoundBorder.create().
                rectangle(true).
                color(UITheme.DARK_GREEN));

        historyButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_HISTORY,
                        historyButton.getUnselectedStyle()
                )
        );

        // archive
        Button archiveButton = new Button("Archive");
        archiveButton.getAllStyles().setFgColor(UITheme.WHITE);
        archiveButton.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        archiveButton.getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
        archiveButton.getAllStyles().setBorder(RoundBorder.create().
                rectangle(true).
                color(UITheme.DARK_GREEN));

        archiveButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_SAVE,
                        archiveButton.getUnselectedStyle()
                )
        );

        // add to container
        Footer.add(historyButton);
        Footer.add(archiveButton);
    }
}
