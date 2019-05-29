package com.project.siiproject.vaadin;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PushStateNavigation
@SpringUI
public class MainUI extends UI {

    private LectureService lectureService;
    private final SpringViewProvider viewProvider;
    private UserService userService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-mm-yyyy");

    private HorizontalLayout mainLayout = new HorizontalLayout();

    private Grid<Lecture> grid = new Grid<>();

    @Autowired
    public MainUI(SpringViewProvider viewProvider, LectureService lectureService, UserService userService) {
        this.viewProvider = viewProvider;
        this.lectureService = lectureService;
        this.userService = userService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        CssLayout leftView = new CssLayout();
        leftView.setSizeFull();

        Label header = new Label("Konfernecja 01-02.06.2019");
        header.addStyleName(ValoTheme.LABEL_H1);

        grid.setSizeFull();
        grid.setHeightByRows(lectureService.getAllLectures().size());
        grid.setItems(lectureService.getAllLectures());
        grid.addColumn(lecture -> lecture.getLectureDate().format(formatter)).setCaption("Data wykładu").setWidthUndefined();
        grid.addColumn(Lecture::getPath).setCaption("Ścieżka").setWidthUndefined();
        grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu").setWidthUndefined().;

        leftView.addComponents(header, grid);

        CssLayout viewContainer = new CssLayout();

        mainLayout.addComponents(leftView, viewContainer);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addView("", new LoginUser(userService));
        navigator.addView(SecurePage.VIEW_NAME, new SecurePage(userService, grid));
        navigator.addProvider(viewProvider);
    }
}
