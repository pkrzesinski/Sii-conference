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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

@PrototypeScope
@SpringView(name = SecurePage.VIEW_NAME)
public class SecurePage extends VerticalLayout implements View {
    public static final String VIEW_NAME = "secure";

    private User user;
    private VerticalLayout layout = new VerticalLayout();
    private Grid<Lecture> grid = new Grid<>();
    @Autowired
    private UserService userService;

    public SecurePage(UserService userService, Grid<Lecture> mainGrid) {
        setupLayout();
        addHeader();

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);
        formLayout.setSizeFull();

        grid.setSizeFull();

        Button addLectureToUser = new Button("Dodaj wykład");
        formLayout.addComponent(addLectureToUser);

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
                        Notification.show("Nie można zapisać danego wykładu", Notification.Type.ERROR_MESSAGE);
                    }

                });
            }
        });

        Button logout = new Button("Wyloguj");
        logout.addStyleName(ValoTheme.BUTTON_DANGER);

        formLayout.addComponents(logout);
        layout.addComponents(formLayout, grid);

        logout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().removeView(SecurePage.VIEW_NAME);
                VaadinSession.getCurrent().setAttribute("user", null);
                getUI().getNavigator().navigateTo("");
            }
        });
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(layout);
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
            grid.addColumn(Lecture::getLectureDate).setCaption("Data wykładu");
            grid.addColumn(Lecture::getPath).setCaption("Ścieżka");
            grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu");
        }
    }
}
