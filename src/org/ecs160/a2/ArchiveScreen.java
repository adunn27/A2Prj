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
//        add("           ");
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
    Form prevPage;
    Form currentPage;

    Container TaskList = new Container();
    Container Header = new Container();
    Container Footer = new Container();

//    private String nameTemp = "[Task Name]";
//    private String sizeTemp = "S";
//    private String[] tagsTemp = {"tag1", "tag2","tag3"};

    private TaskContainer taskObjects;

    public ArchiveScreen(TaskContainer tasksObj){
        taskObjects = tasksObj;

        prevPage = Display.getInstance().getCurrent();
        currentPage = new Form("Archive");

        currentPage.setLayout(new BorderLayout());

        Container newSearchBar = new SearchBar();

        createHeader();
        createTaskList();

        currentPage.add(NORTH, Header);
        currentPage.add(CENTER, TaskList);
        currentPage.show();
    }

    private void createTaskList(){
        setLayout(BoxLayout.y());
        setScrollableY(true);

        for (int i = 0 ; i < 20; i++){
            UIComponents.StandardTaskObject taskObject = new UIComponents.StandardTaskObject(nameTemp, sizeTemp, tagsTemp);
            TaskList.add(taskObject);
        }
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyPadding(UITheme.PAD_3MM);

        backButton.addActionListener(e-> UINavigator.goBack(prevPage));

        Header.add(BorderLayout.WEST, backButton);
    }
    private void createFooter(){
        setLayout(new GridLayout(1,2));
        setScrollableY(false);

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
        add(historyButton);
        add(archiveButton);
    }
}
