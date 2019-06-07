package com.project.siiproject.vaadin;

import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.validation.ConstraintViolationException;

@PrototypeScope
@SpringView
public class LoginUser extends VerticalLayout implements View {

    private static final Logger LOG = LogManager.getLogger(LoginUser.class);
    private VerticalLayout layout;
    private UserService userService;

    @Autowired
    public LoginUser(UserService userService) {
        this.userService = userService;
        setupLayout();
        addHeader();
        addForm();
    }

    public LoginUser() {
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(layout);
    }

    private void addHeader() {
        Label header = new Label("Strona Logowania");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }

    private void addForm() {

        FormLayout formLayout = new FormLayout();
        formLayout.setSpacing(true);
        formLayout.setSizeFull();

        TextField login = new TextField("Login");
        TextField email = new TextField("Email");

        HorizontalLayout split = new HorizontalLayout();

        Button buttonLogin = new Button("Zaloguj");
        buttonLogin.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button buttonAddUser = new Button("Dodaj użytkownika");
        buttonAddUser.addStyleNames(ValoTheme.BUTTON_PRIMARY);

        split.addComponents(buttonLogin, buttonAddUser);

        formLayout.addComponents(login, email, split);
        layout.addComponent(formLayout);

        buttonLogin.addClickListener(clickEvent -> {
            try {
                User user = userService.getUserByLoginAndEmail(login.getValue(), email.getValue());
                navigateSecurePage(user);
                Notification notification = Notification.show("Użytkownik zalogowany");
            } catch (IllegalStateException e) {
                Notification notification = Notification.show("Błąd logowania, sprawdź login i/lub email.",
                        Notification.Type.ERROR_MESSAGE);
                LOG.warn("User: {} failed to log in", login.getValue() + e);
            } catch (ConstraintViolationException e) {
                Notification.show("Dane wpisane niepoprawnie !", Notification.Type.ERROR_MESSAGE);
                LOG.error("New user creating account violated database constraints: {}, {} ", login.getValue(),
                        email.getValue() + e);
            }
        });

        buttonAddUser.addClickListener(clickEvent -> {
            try {
                User user = userService.save(new User(login.getValue(), email.getValue()));
                navigateSecurePage(user);
                Notification notification = Notification.show("Użytkownik o loginie " + login.getValue() + " i email + "
                        + email.getValue() + " został pomyślnie zapisany");
            } catch (IllegalStateException e) {
                Notification.show("Użytkownik o podanym loginie i/lub email jest już zarejestrowany.", Notification.Type.ERROR_MESSAGE);
            } catch (ConstraintViolationException e) {
                Notification.show("Dane wpisane niepoprawnie !", Notification.Type.ERROR_MESSAGE);
                LOG.error("New user creating account violated database constraints: {}, {} ", login.getValue(), email.getValue() + e);
            }
        });
    }

    private void navigateSecurePage(User user) {
        VaadinSession.getCurrent().setAttribute("user", user);
        getUI().getNavigator().navigateTo(SecurePage.VIEW_NAME);
    }
}
