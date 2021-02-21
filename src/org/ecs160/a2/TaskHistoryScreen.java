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

    public TaskHistoryScreen(){
        prevPage = Display.getInstance().getCurrent();
        currentPage = new Form("Archive");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        currentPage.add(NORTH, Header);
        currentPage.add(CENTER, new Label("History"));
        currentPage.show();
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
