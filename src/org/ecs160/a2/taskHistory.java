package org.ecs160.a2;

import static com.codename1.ui.CN.*;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.layouts.BoxLayout;


public class taskHistory extends Form {
    public taskHistory(){
        setTitle("Archive UI");
        setLayout(new BorderLayout());

        Container taskHistoryHeader = new headerAbstract();
        taskHistoryHeader.getComponentAt(1).remove();

        add(NORTH, taskHistoryHeader);
    }
}
