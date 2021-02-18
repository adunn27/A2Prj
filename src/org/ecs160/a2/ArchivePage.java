package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;


public class ArchivePage {

    public void ShowArchivePage(){
        //https://www.youtube.com/watch?v=0m7Bay4g93k&t=114s
        System.out.println("Showing the Archive Page");
        Form newForm = new Form("Archive Page", BoxLayout.y());
        newForm.getToolbar().addCommandToLeftBar("Done", null, (e)->Log.p("Clicked"));
        newForm.getToolbar().addCommandToRightBar("Filter", null, (e)->Log.p("Filter Clicked"));
        TextField SearchBar = new TextField("", "Search", 20, TextArea.ANY);
        newForm.add(SearchBar);
        Container List = new Container(BoxLayout.y());
        List.setScrollableY(true);
        for (int i = 0; i < 1000 ; i++){
            MultiButton mb = new MultiButton("List Entry " + i);
            mb.setTextLine2("Further details...");
            List.add(mb);
        }
        newForm.add(CENTER, List);
        newForm.show();
    }

}
