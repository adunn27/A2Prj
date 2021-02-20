package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

// TODO: get name, size, duration
public class summaryScreen extends Form {
    String name = "Task Name";
    String size = "S";
    String duration = "HH:mm:ss";
    public summaryScreen() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        add(new UIComponents.SummaryTaskObject(name, size, duration));
    }
}

class summaryTaskObject extends Container {
    public summaryTaskObject(String name, String size, String duration) {
        setLayout(new BorderLayout());
        // left side (size, name)
        Label sizeLabel = new UIComponents.SizeLabelObject(size);
        Label nameLabel = new Label(name);
        nameLabel.getAllStyles().setFgColor(UITheme.BLACK);

        Container leftContainer = new Container(new BorderLayout());
        leftContainer.add(BorderLayout.WEST, sizeLabel);
        leftContainer.add(BorderLayout.CENTER, nameLabel);

        // right side (time)
        Label durationLabel = new Label(duration);

        add(BorderLayout.WEST, leftContainer);
        add(BorderLayout.EAST, durationLabel);
    }
}

class sizeLabelObject extends Label {
    private int setColor(String size) {
        if (size == "XL") {
            return UITheme.COL_SIZE_XL;
        } else if (size == "L") {
            return UITheme.COL_SIZE_L;
        } else if (size == "M") {
            return UITheme.COL_SIZE_M;
        } else {
            return UITheme.COL_SIZE_S;
        }
    }

    public sizeLabelObject(String size) {
        setText(size);
        getAllStyles().setFgColor(UITheme.WHITE);
        getAllStyles().setPadding(UITheme.PAD_3MM,
                UITheme.PAD_3MM,
                UITheme.PAD_3MM,
                UITheme.PAD_3MM);

        getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);

        getAllStyles().setBorder(RoundBorder.create().color(setColor(size)));

    }
}
