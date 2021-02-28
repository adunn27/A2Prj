package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

import java.util.ArrayList;

import static com.codename1.ui.CN.log;

public class HomeScreen extends Form{
    private Container Header = new Container();
    private Container Footer = new Container();
    private Container TaskMenu = new Container();

    private Task activeTask;
    private TaskContainer unarchivedTasks;
    private final UINavigator ui;

    public HomeScreen(UINavigator ui) {
        this.ui = ui;
    }

    @Override
    public void show() {
        createHomeScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createHomeScreen();
        super.showBack();
    }

    private void createHomeScreen() {
        removeAll();
        setTitle("Home");
        setLayout(new BorderLayout());

        this.activeTask = ui.getActiveTask();
        this.unarchivedTasks = ui.getHomeTasks();

        createHeader();
        createFooter();
        createTaskMenu();

//        currentPage.add(BorderLayout.NORTH, Header); TODO: figure out header
        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, TaskMenu);
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());

        TextField SearchBar = new TextField("", "Search", 14, TextArea.ANY);
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyPadding(UITheme.PAD_3MM);

        filterButton.addActionListener(e->filter(filterButton));

        Header.add(BorderLayout.CENTER, SearchBar);
        Header.add(BorderLayout.EAST, filterButton);
    }
    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new BorderLayout());

        UIComponents.ButtonObject summary = new UIComponents.ButtonObject();
        summary.setMyText("Summary");
        summary.setMyIcon(FontImage.MATERIAL_LEADERBOARD);
        summary.setMyColor(UITheme.YELLOW);
        summary.setMyPadding(UITheme.PAD_3MM);
        summary.addActionListener(e-> ui.goSummary());

        UIComponents.ButtonObject archived = new UIComponents.ButtonObject();
        archived.setMyIcon(FontImage.MATERIAL_INBOX);
        archived.setMyColor(UITheme.YELLOW);
        archived.setMyPadding(UITheme.PAD_3MM);
        archived.addActionListener(e-> ui.goArchive());

        UIComponents.ButtonObject addTask = new UIComponents.ButtonObject();
        addTask.setMyIcon(FontImage.MATERIAL_ADD);
        addTask.setMyColor(UITheme.YELLOW);
        addTask.setMyPadding(UITheme.PAD_3MM);
        addTask.addActionListener(e-> ui.goNew());

        Footer.add(BorderLayout.WEST, archived);
        Footer.add(BorderLayout.EAST, addTask);
        Footer.add(BorderLayout.CENTER, summary);
    }

    private void createTaskMenu() {
        TaskMenu = new Container();
        TaskMenu.setLayout(BoxLayout.y());
        TaskMenu.setScrollableY(true);

        if (activeTask != null) {
            UIComponents.TitleObject activeHeader = new UIComponents.TitleObject("Now Playing");
            activeHeader.setSize(Font.SIZE_MEDIUM);
            TaskMenu.add(activeHeader);
//            UIComponents.TaskObject t = new UIComponents.TaskObject(activeTask, ui);
            UIComponents.TaskObject t = new UIComponents.TaskObject(activeTask, ui);
            TaskMenu.add(t);
        }

        if (!unarchivedTasks.isEmpty()) {
            UIComponents.TitleObject inactiveHeader =new UIComponents.TitleObject("My Tasks");
            inactiveHeader.setSize(Font.SIZE_MEDIUM);
            TaskMenu.add(inactiveHeader);
        }

        for (Task taskObj : unarchivedTasks) {
//            UIComponents.TaskObject task = new UIComponents.TaskObject(taskObj, ui);
            UIComponents.TaskObject task = new UIComponents.TaskObject(taskObj, ui);
            TaskMenu.add(task);
        }
    }
    private void filter(UIComponents.ButtonObject b) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());

        Container sizeButtons = new Container(BoxLayout.x());
        UIComponents.SizeButtonObject sizeS = new UIComponents.SizeButtonObject("S");
        UIComponents.SizeButtonObject sizeM = new UIComponents.SizeButtonObject("M");
        UIComponents.SizeButtonObject sizeL = new UIComponents.SizeButtonObject("L");
        UIComponents.SizeButtonObject sizeXL = new UIComponents.SizeButtonObject("XL");
        sizeButtons.addAll(sizeS,sizeM,sizeL,sizeXL);

        Container tagButtons = new Container();
        java.util.List<String> allTags = ui.getAllTags();
        for (String tagName : allTags) {
            UIComponents.ButtonObject tagB = new UIComponents.ButtonObject();
            tagB.setMyText(tagName);
            tagB.setMyColor(UITheme.LIGHT_GREY);
            tagButtons.add(tagB);
        }

        d.addAll(new Label("Sizes"), sizeButtons);
        d.addAll(new Label("Tags"), tagButtons);
        d.showPopupDialog(b);
        d.dispose();
    }

    private void refresh() {
        log("pulled refresh");
    }
}
