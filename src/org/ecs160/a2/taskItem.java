package org.ecs160.a2;
import static com.codename1.ui.CN.*;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.*;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;

import javax.swing.border.LineBorder;

public class taskItem extends Container{
    private String taskName;
    private String taskSize;

    public taskItem(){
        Container t1 = TableLayout.encloseIn(2, true);
        taskSize = "S";
        Label sizeLabel = new Label(taskSize);
        Style editSizeLabel = sizeLabel.getAllStyles();
        editSizeLabel.setFgColor(UITheme.WHITE);
        editSizeLabel.setPadding(2,2,2,2);
        editSizeLabel.setMargin(15,15,15,15);
        editSizeLabel.setBorder(RoundBorder.create().rectangle(true).color(UITheme.DARK_GREY));

        Font largeFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE);

        editSizeLabel.setFont(largeFont);
        t1.add(sizeLabel);

        //within the subcomponent, i need a task name and task tag

        Container taskInformation = new Container(BoxLayout.y());


        Font mediumFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE);
        Font smallFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE);


        taskInformation.add("[Task Name Here]");
        taskInformation.add("[Task Tag Here]");
        t1.add(taskInformation);
        add(t1);
    }
}
