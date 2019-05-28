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
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "")
public class MainUI extends UI {

    private UserService userService;
    private LectureService lectureService;
    private final SpringViewProvider viewProvider;

    private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

    private Grid<Lecture> grid = new Grid<>();
    private LoginUser loginUser;

    @Autowired
    public MainUI(SpringViewProvider viewProvider, UserService userService, LectureService lectureService,
                  LoginUser loginUser) {
        this.viewProvider = viewProvider;
        this.userService = userService;
        this.lectureService = lectureService;
        this.loginUser = loginUser;
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

        loginUser.setSizeFull();

        splitPanel.setSizeFull();
        splitPanel.setFirstComponent(grid);
        splitPanel.setSecondComponent(loginUser);

        setContent(splitPanel);

        Navigator navigator = new Navigator(this, splitPanel);
        navigator.addView(LoginUser.VIEW_NAME, new LoginUser());
        navigator.addView(SecurePage.VIEW_NAME, new SecurePage());
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
