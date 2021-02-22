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


public class TaskHistoryScreen extends Form {
    Form currentPage;
    Form prevPage;

    Container Header = new Container();
    Container Footer = new Container();
    Container TaskList = new Container();

    private String nameTemp = "[Task Name]";
    private String sizeTemp = "S";
    private String[] tagsTemp = {"tag1", "tag2","tag3"};
    private String[] timesTemp = {"1:00", "2:00"};
    public TaskHistoryScreen(){
        prevPage = Display.getInstance().getCurrent();
        currentPage = new Form("Archive");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        createTaskList();

        currentPage.add(NORTH, Header);
        currentPage.add(CENTER, TaskList);

        currentPage.show();
    }

    private void createTaskList(){
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        for (int i = 0 ; i < 20; i++){
            UIComponents.HistoryTaskObject newHTO = new UIComponents.HistoryTaskObject(timesTemp[0],timesTemp[1]);
            TaskList.add(newHTO);
        }
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyPadding(UITheme.PAD_3MM);

        backButton.addActionListener(e->UIManager.goBack(prevPage));
        Header.add(BorderLayout.WEST, backButton);
    }

}
