package org.ecs160.a2;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

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

        public void setMyMargin(int margin) {
            this.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            this.getAllStyles().setMargin(margin,margin,margin,margin);
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

            getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);

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

    static class SizeButtonObject extends Container {
        ButtonObject b = new ButtonObject();
        public SizeButtonObject(String size) {
            setLayout(new BorderLayout());
            b.setText(size);
//            b.setMyPadding(UITheme.PAD_3MM);

//            b.setWidth(100);
//            b.setHeight(100);
            Dimension d = new Dimension(UITheme.PAD_3MM,UITheme.PAD_3MM);
            b.setSize(d);

            b.setMyColor(setColor(size));
            b.getAllStyles().setFgColor(UITheme.WHITE);
            add(BorderLayout.CENTER, b);
        }

        public void addMyListener() {
            SizeListen listener = new SizeListen();
            b.addActionListener(listener);
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

        class SizeListen implements ActionListener {
            public void actionPerformed(ActionEvent ev) {
                // filter by size
                if (!b.isToggle()) {
                    b.setMyColor(UITheme.YELLOW);
                    b.setToggle(true);

                } else {
                    b.setMyColor(setColor(b.getText()));
                    b.setToggle(false);
                }
            }
        }

    }

    static class TitleObject extends Label {
        public TitleObject(String title)  {
            setText(title);
            getAllStyles().setFgColor(UITheme.GREY);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);
        }

        public void setSize(int size) {
            getAllStyles().setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, size)));
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

            add(BorderLayout.CENTER, tagLabel);
        }

        public void resetColor(int col) {
            this.getAllStyles().setBorder(
                    RoundBorder.create().rectangle(true).color(col)
            );
        }
    }

    // args: name, size, list of tag names
    // used in: homeScreen, archivePage

    static class ActiveTaskObject extends Container {
        public ActiveTaskObject(String name, String size, String[] tags) {
            setLayout(new BorderLayout());
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMarginBottom(UITheme.PAD_3MM);

            add(BorderLayout.NORTH, new Label("Now Playing"));

            MultiButton test = new MultiButton(name);
            test.getAllStyles().setBgColor(UITheme.LIGHT_GREEN);
            test.addActionListener(e->stop(name));

            String tagsTemp = tags[0];
            for (int i = 1; i < 2; i++) { //TODO: wraparound!
                tagsTemp += '\t' + tags[i];
            }

            test.setTextLine2(tagsTemp);
            add(BorderLayout.CENTER, test);
        }

        private void stop(String name) {
            // TODO: stop task
            log("stop " + name);
        }

    }


    static class StandardTaskObject extends Container {
        public StandardTaskObject(String name, String size, String[] tags){
            setLayout(new BorderLayout());
            getAllStyles().setBorder(Border.createLineBorder(UITheme.PAD_1MM,UITheme.DARK_GREY));
            getAllStyles().setPadding(UITheme.PAD_3MM, UITheme.PAD_3MM,0,0);

            MultiButton taskButton = new MultiButton(name);


            String tagsTemp = tags[0];
            for (int i = 1; i < 5; i++) { //TODO: wraparound!
                tagsTemp += '\t' + tags[i];
            }

            taskButton.setTextLine2(tagsTemp);
            taskButton.setIcon(FontImage.createMaterial(FontImage.MATERIAL_ALARM, getUnselectedStyle()));
            taskButton.setIconPosition(BorderLayout.WEST);
            add(BorderLayout.CENTER, taskButton);
            add(BorderLayout.EAST, new SizeLabelObject(size));

            // LISTENERS
            taskButton.addActionListener(e-> UINavigator.goStart(name));
            taskButton.addLongPressListener(e-> UINavigator.goDetails());
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

            Button myButton = new Button();
            myButton.addActionListener(e->goDescription(name));
            setLeadComponent(myButton);
        }

        private void goDescription(String name) {
            log("go description: " + name);
        }
    }

    static class HistoryTaskObject extends Container {
        public HistoryTaskObject(String startTime, String stopTime){
            setLayout(new BorderLayout());
            Border simpleBorder = Border.createLineBorder(1,UITheme.BLACK);
            getAllStyles().setBorder(simpleBorder);

            Label startLabel = new Label("Start: " + startTime);
            Label stopLabel = new Label("Stop: " + stopTime);


            UIComponents.ButtonObject Delete = new UIComponents.ButtonObject();
            Delete.setMyIcon(FontImage.MATERIAL_DELETE);
            Delete.setMyColor(UITheme.WHITE);
            Delete.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.RED));

            UIComponents.ButtonObject Edit = new UIComponents.ButtonObject();
            Edit.setMyIcon(FontImage.MATERIAL_EDIT);
            Edit.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));

            Container EastSide = new Container(BoxLayout.x());

            EastSide.add(stopLabel);
            EastSide.add(Edit);
            EastSide.add(Delete);

            add(WEST, startLabel);
            add(EAST, EastSide);
        }
    }
}
