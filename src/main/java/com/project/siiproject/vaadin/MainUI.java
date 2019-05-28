package com.project.siiproject.vaadin;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@SpringUI
public class MainUI extends UI {

    private UserService userService;
    private LectureService lectureService;
    private final SpringViewProvider viewProvider;

    private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
    private LoginUser loginUser;

    private Grid<Lecture> grid = new Grid<>(Lecture.class);

   ;

    @Autowired
    public MainUI(SpringViewProvider viewProvider, UserService userService, LectureService lectureService) {
        this.viewProvider = viewProvider;
        this.userService = userService;
        this.lectureService = lectureService;

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        LoginUser loginUser = new LoginUser();

        grid.setSizeFull();
        grid.setItems(lectureService.getAllLectures());
        grid.setColumns("path", "title");
        grid.setDescription("Konferencja 01-02.06.2019");

        loginUser.setSizeFull();

        splitPanel.setSizeFull();
        splitPanel.setFirstComponent(grid);
        splitPanel.setSecondComponent(loginUser);

        setContent(splitPanel);

        Navigator navigator = new Navigator(this, splitPanel);
        navigator.addView(LoginUser.VIEW_NAME, new LoginUser());
//        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getNavigator().navigateTo(viewName);
            }
        });
        return button;
    }

    private class ErrorView extends VerticalLayout implements View {

        private Label message;

        ErrorView() {
            setMargin(true);
            message = new Label("Please click one of the buttons at the top of the screen.");
            addComponent(message);
            message.addStyleName(ValoTheme.LABEL_COLORED);
        }
    }

    //    @Override
//    protected void init(VaadinRequest vaadinRequest) {
//        Navigator navigator = new Navigator(this, this);
//
//        navigator.addView(LoginUser.NAME, new LoginUser(navigator));
//
//        setupLayout();
//        addHeader();
//        addForm();
//        addButtonAction();
//    }
//
//    private void setupLayout() {
//        layout = new VerticalLayout();
//        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
//        setContent(layout);
//    }
//
//    private void addHeader() {
//        Label header = new Label("Plan konferencji");
//        header.addStyleName(ValoTheme.LABEL_H1);
//        header.setSizeUndefined();
//        layout.addComponent(header);
//    }
//
//    private void addForm() {
//        VerticalLayout formLayout = new VerticalLayout();
//        Label label = new Label("Dodawanie Użytkownika");
//        formLayout.setSpacing(true);
//
//        TextField login = new TextField("Login");
//        TextField email = new TextField("Email");
//        Button addButton = new Button("Dodaj użytkownika");
//        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
//
//        formLayout.addComponents(label, login, email, addButton);
//        layout.addComponent(formLayout);
//
//        addButton.addClickListener(clickEvent -> {
//            User user = new User(login.getValue(), email.getValue());
//            try {
//                userService.save(user);
//                Notification notification = Notification.show("Użytkownik dodany");
//            } catch (ConstraintViolationException e) {
//                Notification notification = Notification.show("Wystąpił błąd przy dodawaniu");
//            }
//            login
//                    .clear();
//            email.clear();
//            login.focus();
//        });
//
//    }
//
//    private void addButtonAction() {
//
//    }
}