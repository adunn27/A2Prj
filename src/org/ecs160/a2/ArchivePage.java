package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;


public class ArchivePage {

    private Form current;
    private Resources theme;
    private Object Center;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }

    public void start() {
        if(current != null){
            current.show();
            return;
        }
        ShowArchivePage();
    }

    public void ShowArchivePage(){
        //https://www.youtube.com/watch?v=0m7Bay4g93k&t=114s
        System.out.println("Showing the Archive Page");
        Form archive_page = new Form("Archive Page", BoxLayout.y());

        //I should have 3 components

        Button doneButton = new Button("done");
        Button fillerButton = new Button("                                     ");
        Button filterButton = new Button("filter");
        Container Header = new Container(new FlowLayout());
        Header.addComponent(doneButton);
        Header.addComponent(fillerButton);
        Header.addComponent(filterButton);
        Header.setHeight(200);
        Header.setWidth(200);

        archive_page.add(Header);

        TextField SearchBar = new TextField("", "Search", 20, TextArea.ANY);
        String[] abc = new String[]{"A", "B", "C"};
        archive_page.add(SearchBar);
        Container List = new Container(BoxLayout.y());
        List.setScrollableY(true);
        for (int i = 0; i < 10 ; i++){
            MultiButton mb = new MultiButton("List Entry " + i);
            mb.setTextLine2("Further details...");
            List.add(mb);
        }
        archive_page.add(Center, List);
        archive_page.show();
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }

    public void destroy() {
    }

}
