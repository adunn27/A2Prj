package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;

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
    }
}
