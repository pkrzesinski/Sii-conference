package com.project.siiproject.vaadin;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@PushStateNavigation
@SpringUI
public class MainUI extends UI {

    private LectureService lectureService;
    private final SpringViewProvider viewProvider;
    private UserService userService;

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
        grid.setSizeFull();
        grid.setItems(lectureService.getAllLectures());
        grid.addColumn(Lecture::getLectureDate).setCaption("Data wykładu");
        grid.addColumn(Lecture::getPath).setCaption("Ścieżka");
        grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu");



        CssLayout viewContainer = new CssLayout();

        mainLayout.addComponents(grid, viewContainer);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addView("", new LoginUser(userService));
        navigator.addView(SecurePage.VIEW_NAME, new SecurePage(userService, grid));
//       navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
    }


//    private class ErrorView extends VerticalLayout implements View {
//
//        private Label message;
//
//        ErrorView() {
//            setMargin(true);
//            message = new Label("Please click one of the buttons at the top of the screen.");
//            addComponent(message);
//            message.addStyleName(ValoTheme.LABEL_COLORED);
//        }
//    }
}
