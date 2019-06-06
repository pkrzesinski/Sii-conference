package com.project.siiproject.vaadin;

import com.project.siiproject.feature.emailsender.EmailSender;
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

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PrototypeScope
@SpringView(name = SecurePage.VIEW_NAME)
public class SecurePage extends VerticalLayout implements View {

    private static final Logger LOG = LogManager.getLogger(SecurePage.class);
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final String VIEW_NAME = "userPage";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private User user;
    private VerticalLayout layout = new VerticalLayout();
    private Grid<Lecture> grid = new Grid<>();
    private TextField email = new TextField("Email");
    private EmailSender emailSender = new EmailSender();

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
            if (emailValidate(email.getValue())) {
                try {
                    user.setEmail(email.getValue());
                    userService.emailUpdate(user);
                    Notification.show("Adres email został zmienniony");
                    LOG.info("User: {} has changed email.", user.getLogin());
                } catch (IllegalStateException e) {
                    Notification.show("Podany adres jest już zajęty !", Notification.Type.ERROR_MESSAGE);
                    LOG.warn("Illegal State Exception occurred" + e);
                }
            } else {
                Notification.show("Błędny format adresu email!", Notification.Type.ERROR_MESSAGE);
                LOG.warn("User: {} has tried to change email address, but wrong format was inserted.", user.getLogin());
            }
        });

        formLayout.addComponents(email, buttonChangeEmail);

        HorizontalLayout buttonsUnderGrid = new HorizontalLayout();

        Button buttonRemoveLecture = new Button("Usuń");
        buttonRemoveLecture.addStyleName(ValoTheme.BUTTON_DANGER);

        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            Lecture selectedLectureToBeRemoved = valueChangeEvent.getValue();

            if (selectedLectureToBeRemoved != null) {

                buttonRemoveLecture.addClickListener(clickEvent -> {
                    User userLectureToBeRemoved = userService.getUserByLogin(user.getLogin());
                    try {
                        List<Lecture> newList = userLectureToBeRemoved.getLectures();
                        newList.removeIf(lecture -> lecture.getTitle().equals(selectedLectureToBeRemoved.getTitle()));

                        user.setLectures(newList);
                        userService.update(user);

                        LOG.info("User: {} has deleted lecture {}.", user.getLogin(), selectedLectureToBeRemoved.getTitle());

                        VaadinSession.getCurrent().setAttribute("user", userService.getUserByLogin(user.getLogin()));
                        grid.deselectAll();
                        Page.getCurrent().reload();
                    } catch (IllegalStateException e) {
                        grid.deselectAll();
                        Notification.show("Nie można usunąć", Notification.Type.ERROR_MESSAGE);
                    }
                });
            }
        });

        Button buttonAddLectureToUser = new Button("Dodaj wykład");
        buttonAddLectureToUser.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttonsUnderGrid.addComponents(buttonAddLectureToUser, buttonRemoveLecture);

        mainGrid.asSingleSelect().addValueChangeListener(event -> {

            Lecture selectedLecture = event.getValue();
            if (selectedLecture != null) {

                buttonAddLectureToUser.addClickListener(clickEvent -> {

                    User userLectureToSave = userService.getUserByLogin(user.getLogin());
                    try {
                        userService.addNewLecture(userLectureToSave, selectedLecture);
                        VaadinSession.getCurrent().setAttribute("user", userService.getUserByLogin(user.getLogin()));
                        emailSender.sendEmail(user.getEmail(), "Zapisy na konferencję 01-02.06.2019",
                                "Serdecznie zapraszamy na wykład: " + selectedLecture.getTitle() + ", dnia" +
                                        selectedLecture.getLectureDate().format(formatter) + "\nDo zobaczenia!");
                        mainGrid.deselectAll();
                        Page.getCurrent().reload();
                    } catch (IllegalStateException e) {
                        mainGrid.deselectAll();
                        Notification.show("Nie można zapisać danego wykładu", Notification.Type.ERROR_MESSAGE);
                    } catch (IOException e) {
                        Notification.show("Wysłanie maila się niepowiodło");
                        LOG.warn("Confirmation email was not send to {}", user.getLogin());
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
        Button buttonLogout = new Button("Wyloguj");
        buttonLogout.addStyleName(ValoTheme.BUTTON_DANGER);

        buttonLogout.addClickListener(clickEvent -> {
            LOG.info("User " + user.getLogin() + " logged out.");
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
            LOG.info("User " + user.getLogin() + " has opened user's page");
            email.setValue(user.getEmail());

            grid.setItems(user.getLectures());
            grid.setHeightByRows(user.getLectures().size() <= 0 ? 1 : user.getLectures().size());
            grid.addColumn(lecture -> lecture.getLectureDate().format(formatter)).setCaption("Data wykładu").setWidthUndefined();
            grid.addColumn(Lecture::getPath).setCaption("Ścieżka").setWidthUndefined();
            grid.addColumn(Lecture::getTitle).setCaption("Temat wykładu").setWidthUndefined();
        }
    }

    private boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
