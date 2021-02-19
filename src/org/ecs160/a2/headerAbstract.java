package org.ecs160.a2;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;

public class headerAbstract extends Container{
    public headerAbstract(){
        //Reusable class for headerAbstract
        setLayout(new BorderLayout());
        Button editButton = new Button();
        Style editStyle = editButton.getAllStyles();
        editStyle.setFgColor(UITheme.BLACK);

        editButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_MODE_EDIT,
                        editButton.getUnselectedStyle()
                )
        );

        editStyle.setBorder(
                RoundBorder.create()
                        .rectangle(true)
                        .color(UITheme.LIGHT_YELLOW)
        );

        Button backButton = new Button();
        Style backStyle = backButton.getAllStyles();
        backStyle.setFgColor(UITheme.BLACK);

        // set edit icon
        backButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_ARROW_BACK,
                        backButton.getUnselectedStyle()
                )
        );

        // set edit button background and shape
        backStyle.setBorder(
                RoundBorder.create().
                        rectangle(true).
                        color(UITheme.LIGHT_YELLOW)
        );

        add(BorderLayout.WEST, backButton);
        add(BorderLayout.EAST, editButton);
    }
}
