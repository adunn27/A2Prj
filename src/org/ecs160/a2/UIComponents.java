package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;

public class UIComponents {
    // tagObject: taskDetails, homeScreen, archivePage
    static class TagObject extends Container {
        public TagObject (String tagName) {
            setLayout(new BorderLayout());
            Button tagLabel = new Button(tagName);
            tagLabel.getAllStyles().setFgColor(UITheme.BLACK);

            Font smallFont = Font.createSystemFont(FACE_MONOSPACE, STYLE_PLAIN, SIZE_SMALL);
            tagLabel.getAllStyles().setFont(smallFont);

            tagLabel.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            tagLabel.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);
            tagLabel.getAllStyles().setMargin(Component.BOTTOM, UITheme.PAD_1MM);
            tagLabel.getAllStyles().setBorder(
                    RoundBorder.create().rectangle(true).color(UITheme.YELLOW)
            );

            add(BorderLayout.CENTER, tagLabel);
        }
    }
}
