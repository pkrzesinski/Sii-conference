package com.project.siiproject.vaadin;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
public class MainUI extends UI {

    private LectureService lectureService;
    private final SpringViewProvider viewProvider;

    private HorizontalLayout mainLayout = new HorizontalLayout();

    private Grid<Lecture> grid = new Grid<>();
    private LoginUser loginUser;
    private SecurePage securePage;

    @Autowired
    public MainUI(SpringViewProvider viewProvider, LectureService lectureService, LoginUser loginUser, SecurePage securePage) {
        this.viewProvider = viewProvider;
        this.lectureService = lectureService;
        this.loginUser = loginUser;
        this.securePage = securePage;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        grid.setSizeFull();
        grid.setItems(lectureService.getAllLectures());
        grid.addColumn(Lecture::getLectureDate).setCaption("Data wykładu");
        grid.addColumn(Lecture::getPath).setCaption("Ścieżka");
        grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu");

        grid.asSingleSelect().addValueChangeListener(event -> {
            Lecture selectedLecture = event.getValue();
        });

        CssLayout viewContainer = new CssLayout();
        viewContainer.addComponent(loginUser);

        mainLayout.addComponents(grid, viewContainer);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addView("", new LoginUser());
        navigator.addView(SecurePage.VIEW_NAME, new SecurePage());
//       navigator.setErrorView(new ErrorView());
//        navigator.addProvider(viewProvider);
    }

//    private Button createNavigationButton(String caption, final String viewName) {
//        Button button = new Button(caption);
//        button.addStyleName(ValoTheme.BUTTON_SMALL);
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                getUI().getNavigator().navigateTo(viewName);
//            }
//        });
//        return button;
//    }
//
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
