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

        t1.getAllStyles().setBorder(Border.createLineBorder(1,UITheme.DARK_GREY));
        t1.getAllStyles().setPadding(5,5,0,0);

        Container SizeContainer = new Container(BoxLayout.x());

        taskSize = "S";
        Label sizeLabel = new Label(taskSize);
        Style editSizeLabel = sizeLabel.getAllStyles();
        editSizeLabel.setFgColor(UITheme.BLACK);
        editSizeLabel.setPadding(UITheme.PAD_3MM,
                UITheme.PAD_3MM,
                UITheme.PAD_3MM,
                UITheme.PAD_3MM);
        editSizeLabel.setMargin(20,20,20,20);
        editSizeLabel.setBorder(RoundBorder.create().color(UITheme.YELLOW));

        Font largeFont = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_LARGE);

        editSizeLabel.setFont(largeFont);
        SizeContainer.add(sizeLabel);

        t1.add(SizeContainer);

        Container taskInformation = new Container(BoxLayout.y());

        Font mediumFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE);
        Font smallFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, 1);


        taskInformation.add("[Task Name Here]");

        Container taskTags = new Container(BoxLayout.x());
        taskTags.setScrollableX(true);
        Container TagObject = new UIComponents.TagObject("ECS 160");
        Container TagObject1 = new UIComponents.TagObject("ECS 193a");
        Container TagObject2 = new UIComponents.TagObject("ECS 150");

        taskTags.add(TagObject);
        taskTags.add(TagObject1);
        taskTags.add(TagObject2);

        taskInformation.add(taskTags);
        t1.add(taskInformation);
        add(t1);
    }
}
