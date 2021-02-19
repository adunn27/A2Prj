package org.ecs160.a2;

import static com.codename1.ui.CN.*;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.layouts.BoxLayout;

class HeaderTaskHistory extends Container {
    public HeaderTaskHistory(){
        //should have a back button
        setLayout(new BorderLayout());

        Button backButton = new Button("back");

        Label ArchiveTitle = new Label("Task History");

        add(WEST, backButton);
        add(CENTER, ArchiveTitle);
    }
}

public class taskHistory extends Form {
    public taskHistory(){
        setTitle("Archive UI");
        setLayout(new BorderLayout());


        Label HeaderLabel = new Label("HEADER");
        HeaderLabel.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.DARK_GREY));
        Label FooterLabel = new Label("Footer");
        Label ListLabel = new Label("List");

        add(NORTH, HeaderLabel);
        add(SOUTH, FooterLabel);
        add(CENTER, ListLabel);
    }
}
