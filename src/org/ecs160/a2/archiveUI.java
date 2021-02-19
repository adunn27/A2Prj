package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;

import javax.swing.border.LineBorder;

class TaskList extends Container {
    public TaskList(){
        setLayout(BoxLayout.y());
        setScrollableY(true);
        //Component TagObject = new UIComponents.TagObject("Name");
        //add(TagObject);
        for (int iter = 0 ; iter < 20; iter++){
            Container newTask = new taskItem();
            add(newTask);
        }
    }
}

class SearchBar extends Container{
    public SearchBar(){
        setLayout(BoxLayout.xRight());
        getAllStyles().setMarginLeft(100);
        TextField searchBar = new TextField("", "search", 20, TextArea.ANY);
        add("           ");
        searchBar.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
        searchBar.getAllStyles().setFgColor(UITheme.BLACK);
        add(searchBar);
        Button filterButton = new Button("Filter");
        filterButton.getAllStyles().setBgColor(UITheme.BLACK);
        add(filterButton);
    }
}

public class archiveUI extends Form {

    public archiveUI(){
        setTitle("Archive UI");
        setLayout(new BorderLayout());
        Container newHeader = new headerAbstract();
        newHeader.getComponentAt(1).remove();
        Container newSearchBar = new SearchBar();
        newHeader.add(EAST, newSearchBar);
        Container newFooter = new footerAbstract();
        add(NORTH, newHeader);
        //add(SOUTH, newFooter);

        Container TaskList = new TaskList();
        add(CENTER, TaskList);
    }

}
