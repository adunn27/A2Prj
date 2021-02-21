package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import javafx.concurrent.Task;

import static com.codename1.ui.CN.CENTER_BEHAVIOR_CENTER;
import static com.codename1.ui.CN.log;

public class homeScreen extends Form{
    Form currentPage;

    private Container Header = new Container();
    private Container Footer = new Container();
    private Container TaskMenu = new Container();

    private String[] tasksTemp = {"task1","task2","task3","task4","task5","task6"};
    private String[] sizesTemp = {"S","M","L","XL","L","M"};
    private String[] tagsTemp = {"tag1","tag2","tag3","tag4","tag5","tag6","tag7","tag8","tag9","tag10","tag11","tag12"};

    public homeScreen() {
        currentPage = new Form("Home");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        createFooter();
        createTaskMenu();

//        currentPage.add(BorderLayout.NORTH, Header); TODO: figure out header
        currentPage.add(BorderLayout.SOUTH, Footer);
        currentPage.add(BorderLayout.CENTER, TaskMenu);
        currentPage.show();
    }

    private void createHeader() {
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
        Footer.setLayout(new BorderLayout());

        UIComponents.ButtonObject summary = new UIComponents.ButtonObject();
        summary.setMyText("Summary");
        summary.setMyIcon(FontImage.MATERIAL_LEADERBOARD);
        summary.setMyColor(UITheme.YELLOW);
        summary.setMyPadding(UITheme.PAD_3MM);
        summary.addActionListener(e->UIManager.goSummary());

        UIComponents.ButtonObject archived = new UIComponents.ButtonObject();
        archived.setMyIcon(FontImage.MATERIAL_INBOX);
        archived.setMyColor(UITheme.YELLOW);
        archived.setMyPadding(UITheme.PAD_3MM);
        archived.addActionListener(e->UIManager.goArchive());

        UIComponents.ButtonObject addTask = new UIComponents.ButtonObject();
        addTask.setMyIcon(FontImage.MATERIAL_ADD);
        addTask.setMyColor(UITheme.YELLOW);
        addTask.setMyPadding(UITheme.PAD_3MM);
        addTask.addActionListener(e->UIManager.goNew());

        Footer.add(BorderLayout.WEST, archived);
        Footer.add(BorderLayout.EAST, addTask);
        Footer.add(BorderLayout.CENTER, summary);
    }

    private void createTaskMenu() {
        TaskMenu.setLayout(BoxLayout.y());
        TaskMenu.setScrollableY(true);

        UIComponents.ActiveTaskObject t = new UIComponents.ActiveTaskObject("task0", "S", tagsTemp);

        TaskMenu.add(t);

        for (int i = 0; i < tasksTemp.length; i++) {
            UIComponents.StandardTaskObject task = new UIComponents.StandardTaskObject(tasksTemp[i], sizesTemp[i], tagsTemp);
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
        for (int i = 0; i < tagsTemp.length; i++) {
            tagButtons.add(new UIComponents.TagObject(tagsTemp[i]));
        }

        d.addAll(new Label("Sizes"), sizeButtons);
        d.addAll(new Label("Tags"), tagButtons);
        d.showPopupDialog(b);
    }
}
