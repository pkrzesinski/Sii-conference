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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.spring.annotation.PrototypeScope;

import java.time.format.DateTimeFormatter;

@PrototypeScope
@SpringView(name = SecurePage.VIEW_NAME)
public class SecurePage extends VerticalLayout implements View {

    private static final Logger LOG = LogManager.getLogger(SecurePage.class);
    public static final String VIEW_NAME = "userPage";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private User user;
    private VerticalLayout layout = new VerticalLayout();
    private Grid<Lecture> grid = new Grid<>();
    private TextField email = new TextField("Email");

    public SecurePage(UserService userService, Grid<Lecture> mainGrid) {
        setupLayout();
        addLogoutButton();
        addHeader();

        grid.setSizeFull();

        FormLayout formLayout = new FormLayout();
        formLayout.setSpacing(true);
        formLayout.setSizeFull();

        Button buttonChangeEmail = new Button("Zmień email");
        buttonChangeEmail.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttonChangeEmail.addClickListener(clickEvent -> {
            try {
                User userUpdatedEmail = userService.emailUpdate(user, email.getValue());
                updateSession(userUpdatedEmail);
                Notification.show("Adres email został zmienniony");
            } catch (IllegalStateException e) {
                Notification.show("Podany adres jest już zajęty !", Notification.Type.ERROR_MESSAGE);
            }
        });

        formLayout.addComponents(email, buttonChangeEmail);

        HorizontalLayout buttonsUnderGrid = new HorizontalLayout();

        Button buttonRemoveLecture = new Button("Usuń");
        buttonRemoveLecture.addStyleName(ValoTheme.BUTTON_DANGER);

        buttonRemoveLecture.addClickListener(clickEvent -> {
            Lecture selectedLectureToBeRemoved = grid.asSingleSelect().getValue();

            if (selectedLectureToBeRemoved != null) {
                try {
                    User userRemovedLecture = userService.lectureToRemove(user, selectedLectureToBeRemoved);
                    updateSession(userRemovedLecture);
                } catch (IllegalStateException e) {
                    Notification.show("Nie można usunąć", Notification.Type.ERROR_MESSAGE);
                    LOG.warn("User: {} unable to remove lecture {} ", user.getLogin(), selectedLectureToBeRemoved.getTitle());
                } finally {
                    grid.deselectAll();
                }
            }
        });

        Button buttonAddLectureToUser = new Button("Dodaj wykład");
        buttonAddLectureToUser.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttonAddLectureToUser.addClickListener(clickEvent -> {
            Lecture selectedLecture = mainGrid.asSingleSelect().getValue();

            if (selectedLecture != null) {
                try {
                    User userNewLecture = userService.addNewLecture(user, selectedLecture);
                    updateSession(userNewLecture);
                } catch (IllegalStateException e) {
                    Notification.show("Nie można zapisać danego wykładu", Notification.Type.ERROR_MESSAGE);
                } finally {
                    mainGrid.deselectAll();
                }
            }
        });

        buttonsUnderGrid.addComponents(buttonAddLectureToUser, buttonRemoveLecture);
        layout.addComponents(formLayout, grid, buttonsUnderGrid);
    }

    private void updateSession(User user) {
        VaadinSession.getCurrent().setAttribute("user", user);
        Page.getCurrent().reload();
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.setSizeFull();
        addComponent(layout);
    }

    private void addLogoutButton() {
        Button buttonLogout = new Button("Wyloguj");
        buttonLogout.addStyleName(ValoTheme.BUTTON_DANGER);

        buttonLogout.addClickListener(clickEvent -> {
            LOG.info("User: {} logged out.", user.getLogin());
            VaadinSession.getCurrent().setAttribute("user", null);
            getUI().getNavigator().navigateTo("");
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(buttonLogout);
        horizontalLayout.setComponentAlignment(buttonLogout, Alignment.TOP_RIGHT);
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
            LOG.info("User {} has opened user's page", user.getLogin());
            email.setValue(user.getEmail());

            grid.setItems(user.getLectures());
            grid.setHeightByRows(user.getLectures().size() <= 0 ? 1 : user.getLectures().size());
            grid.addColumn(lecture -> lecture.getLectureDate().format(formatter)).setCaption("Data wykładu").setWidthUndefined();
            grid.addColumn(Lecture::getPath).setCaption("Ścieżka").setWidthUndefined();
            grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu").setWidthUndefined();
        }
    }
}
