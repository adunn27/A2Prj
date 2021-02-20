package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;

import static com.codename1.ui.CN.*;

// Contains Buttons and Components frequently used in UI
public class UIComponents {
    //Button object defaults to edit icon
    static class ButtonObject extends Button {
        public ButtonObject () {
            getAllStyles().setFgColor(UITheme.BLACK);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(UITheme.PAD_1MM,
                                     UITheme.PAD_1MM,
                                     UITheme.PAD_1MM,
                                     UITheme.PAD_1MM);
        }

        // pass in UITheme.[color]
        public void setMyColor(int color) {
            this.getAllStyles().setBorder(
                    RoundBorder.create()
                            .rectangle(true)
                            .color(color)
            );
        }

        // pass in FontImage.[icon]
        public void setMyIcon(char icon) {
            this.setIcon(
                FontImage.createMaterial(
                        icon,
                        getUnselectedStyle()
                )
            );
        }

        // pass in String
        public void setMyText(String text) {
            this.setText(text);
        }

        public void setMyPadding(int pad) {
            this.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
            this.getAllStyles().setPadding(pad,pad,pad,pad);
        }
    }

    // args: size
    // used in: StandardTaskObject, SummaryTaskObject
    static class SizeLabelObject extends Label {
        public SizeLabelObject(String size) {
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
    }

    // args: tagName
    // used in: taskDetails, homeScreen, archivePage
    static class TagObject extends Container {
        Button tagLabel;
        public TagObject (String tagName) {
            setLayout(new BorderLayout());
            tagLabel = new Button(tagName);
            tagLabel.getAllStyles().setFgColor(UITheme.BLACK);

            Font smallFont = Font.createSystemFont(FACE_MONOSPACE, STYLE_PLAIN, SIZE_SMALL);
            tagLabel.getAllStyles().setFont(smallFont);

            tagLabel.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            tagLabel.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);
            tagLabel.getAllStyles().setMargin(Component.BOTTOM, UITheme.PAD_1MM);
            tagLabel.getAllStyles().setBorder(
                    RoundBorder.create().rectangle(true).color(UITheme.YELLOW)
            );

//            ButtonListener myButtonListener = new ButtonListener();
//            tagLabel.addActionListener(myButtonListener);

            add(BorderLayout.CENTER, tagLabel);
        }

        public void resetColor(int col) {
            this.getAllStyles().setBorder(
                    RoundBorder.create().rectangle(true).color(col)
            );
        }

        public void addListener() {
            TagListener myTagListener = new TagListener();
            tagLabel.addActionListener(myTagListener);
        }
    }

    // args: name, size, list of tag names
    // used in: homeScreen, archivePage
    static class StandardTaskObject extends Container {
        public StandardTaskObject(String name, String size, String[] tags){
            Container t1 = TableLayout.encloseIn(2, true);

            t1.getAllStyles().setBorder(Border.createLineBorder(1,UITheme.DARK_GREY));
//            t1.getAllStyles().setPadding(5,5,0,0);
            t1.getAllStyles().setPadding(UITheme.PAD_3MM, UITheme.PAD_3MM,0,0);

            Container SizeContainer = new Container(BoxLayout.x());

            Label sizeLabel = new Label(size);
            Style editSizeLabel = sizeLabel.getAllStyles();
            editSizeLabel.setFgColor(UITheme.BLACK);
            editSizeLabel.setPadding(UITheme.PAD_3MM,
                    UITheme.PAD_3MM,
                    UITheme.PAD_3MM,
                    UITheme.PAD_3MM);
            editSizeLabel.setMarginUnit(Style.UNIT_TYPE_DIPS);
            editSizeLabel.setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
            editSizeLabel.setBorder(RoundBorder.create().color(UITheme.YELLOW));

            Font largeFont = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_LARGE);

            editSizeLabel.setFont(largeFont);
            SizeContainer.add(sizeLabel);

            t1.add(SizeContainer);

            Container taskInformation = new Container(BoxLayout.y());

            taskInformation.add(name);

            Container taskTags = new Container(BoxLayout.x());
            taskTags.setScrollableX(true);
//            Container TagObject = new UIComponents.TagObject("ECS 160");
//            Container TagObject1 = new UIComponents.TagObject("ECS 193a");
//            Container TagObject2 = new UIComponents.TagObject("ECS 150");

            for (int i = 0; i < tags.length; i++) {
                Container TagObject = new UIComponents.TagObject(tags[i]);
                taskTags.add(TagObject);
            }

            taskInformation.add(taskTags);
            t1.add(taskInformation);
            add(t1);
        }
    }

    // args: N/A
    // used in: homeScreen, archivePage
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

    // args: name, size, duration
    // used in: summaryScreen
    static class SummaryTaskObject extends Container {
        public SummaryTaskObject(String name, String size, String duration) {
            setLayout(new BorderLayout());
            // left side (size, name)
            Label sizeLabel = new SizeLabelObject(size);
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
}
