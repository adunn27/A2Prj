package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
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

    //Button object defaults to edit icon
    static class ButtonObject extends Button {
        public ButtonObject () {
            getAllStyles().setFgColor(UITheme.BLACK);
            setIcon(
                    FontImage.createMaterial(
                            FontImage.MATERIAL_MODE_EDIT,
                            getUnselectedStyle()
                    )
            );
            getAllStyles().setBorder(
                    RoundBorder.create()
                        .rectangle(true)
                        .color(UITheme.LIGHT_YELLOW)
            );
        }
    }

    static class SearchBoxObject extends Container {
        public SearchBoxObject(){
            setLayout(BoxLayout.xRight());
            getAllStyles().setMarginLeft(100);
            TextField searchBar = new TextField("", "search", 12, TextArea.ANY);
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

    static class TaskItemObject extends Container {
        public TaskItemObject(){
            
        }
    }
}
