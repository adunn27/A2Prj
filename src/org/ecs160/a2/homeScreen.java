package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

public class homeScreen extends Form{
    public homeScreen() {
        setLayout(new BorderLayout());
        setTitle("Home");

        TextField SearchBar = new TextField("", "Search", 20, TextArea.ANY);
        add(BorderLayout.NORTH, SearchBar);

        Container footerBar = new Container(new BorderLayout());
        footerBar.add(BorderLayout.WEST, new Button("Summary"));
        footerBar.add(BorderLayout.CENTER, new Button("View Archives"));
        footerBar.add(BorderLayout.EAST, new Button("+"));
        add(BorderLayout.SOUTH, footerBar);

        Container taskMenu = new Container(new BorderLayout());
        Container taskList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container currentTask = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        taskMenu.add(BorderLayout.CENTER, taskList);
        taskMenu.add(BorderLayout.NORTH, currentTask);
        add(BorderLayout.CENTER, taskMenu);
    }
}
