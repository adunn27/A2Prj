package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;

import javax.swing.border.LineBorder;

class TaskItem extends Container{
    public TaskItem(){

    }
}

class TaskList extends Container {
    public TaskList(){
        setLayout(BoxLayout.y());
        setScrollableY(true);
        for (int iter = 0 ; iter < 20; iter++){
            MultiButton mb = new MultiButton("List entry " + iter);
            mb.setTextLine2("Further details ....");
            add(mb);
        }
    }
}

class Header extends Container {
    public Header(){
        setLayout(new BorderLayout());

        Button doneButton = new Button("done");
        doneButton.setText("back");
        doneButton.getAllStyles().setBgColor(UITheme.BLACK);
        doneButton.getAllStyles().setPadding(0,0,1,1);
        doneButton.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.BLACK));
        doneButton.getAllStyles().setFgColor(UITheme.WHITE);

        TextField searchBar = new TextField("", "search", 20, TextArea.ANY);
        searchBar.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
        searchBar.getAllStyles().setFgColor(UITheme.BLACK);

        add(WEST, doneButton);
        add(EAST, searchBar);
    }
}

class Footer extends Container{
    public Footer(){
        setLayout(new BorderLayout());
        getAllStyles().setPadding(5,5,5,5);
        getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.DARK_GREY));
        add(WEST, "Footer");
    }
}

public class archiveUI extends Form {

    public archiveUI(){
        setTitle("Archive UI");
        setLayout(new BorderLayout());
        Container newHeader = new Header();
        Container newFooter = new Footer();
        add(NORTH, newHeader);
        add(SOUTH, newFooter);

        Container TaskList = new TaskList();
        add(CENTER, TaskList);
    }

}
