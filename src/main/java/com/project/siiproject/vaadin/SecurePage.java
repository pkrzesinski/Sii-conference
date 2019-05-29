package com.project.siiproject.vaadin;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.annotation.PrototypeScope;

import java.time.format.DateTimeFormatter;

@PrototypeScope
@SpringView(name = SecurePage.VIEW_NAME)
public class SecurePage extends VerticalLayout implements View {

    public static final String VIEW_NAME = "userPage";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private User user;
    private VerticalLayout layout = new VerticalLayout();
    private Grid<Lecture> grid = new Grid<>();

    public SecurePage(UserService userService, Grid<Lecture> mainGrid) {
        setupLayout();
        addLogoutButton();
        addHeader();

        grid.setSizeFull();

        FormLayout formLayout = new FormLayout();
        formLayout.setSpacing(true);
        formLayout.setSizeFull();

        TextField login = new TextField("Login");
        TextField email = new TextField("Email");
        formLayout.addComponents(login, email);

        HorizontalLayout buttonsUnderGrid = new HorizontalLayout();

        Button removeLecture = new Button("Usuń");
        removeLecture.addStyleName(ValoTheme.BUTTON_DANGER);

        Button addLectureToUser = new Button("Dodaj wykład");
        addLectureToUser.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttonsUnderGrid.addComponents(addLectureToUser, removeLecture);

        mainGrid.asSingleSelect().addValueChangeListener(event -> {
            Lecture selectedLecture = event.getValue();

            if (selectedLecture != null) {

                addLectureToUser.addClickListener(clickEvent -> {

                    User userLectureToSave = userService.getUserByLogin(user.getLogin());
                    try {
                        userService.addNewLecture(userLectureToSave, selectedLecture);
                        VaadinSession.getCurrent().setAttribute("user", userService.getUserByLogin(user.getLogin()));
                        mainGrid.deselectAll();
                        Page.getCurrent().reload();

                    } catch (IllegalStateException e) {
                        mainGrid.deselectAll();
                        Notification.show("Nie można zapisać danego wykładu", Notification.Type.ERROR_MESSAGE);
                    }
                });
            }
        });

        layout.addComponents(formLayout, grid, buttonsUnderGrid);
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.setSizeFull();
        addComponent(layout);
    }

    private void addLogoutButton() {
        Button logout = new Button("Wyloguj");
        logout.addStyleName(ValoTheme.BUTTON_DANGER);

        logout.addClickListener(clickEvent -> {
            VaadinSession.getCurrent().setAttribute("user", null);
            getUI().getNavigator().navigateTo("");
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(logout);
        horizontalLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        layout.addComponent(horizontalLayout);
    }

    private void addHeader() {
        Label header = new Label("Strona użytkownika");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user = (User) VaadinSession.getCurrent().getAttribute("user");

        setCaption("Zalogowany użytkownik : " + user.getLogin().toString());

        if (user != null) {
            grid.setItems(user.getLectures());
            grid.setHeightByRows(user.getLectures().size());
            grid.addColumn(lecture -> lecture.getLectureDate().format(formatter)).setCaption("Data wykładu").setWidthUndefined();
            grid.addColumn(Lecture::getPath).setCaption("Ścieżka").setWidthUndefined();
            grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu").setWidthUndefined();
        }
    }
}
