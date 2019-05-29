package com.project.siiproject.vaadin;

import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.validation.ConstraintViolationException;

@PrototypeScope
@SpringView
public class LoginUser extends VerticalLayout implements View {

    //    @Autowired
//    private UserService userService;
    private VerticalLayout layout;
    private UserService userService;

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

        Button loginButton = new Button("Zaloguj");
        loginButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.addStyleNames(ValoTheme.BUTTON_PRIMARY);

        split.addComponents(loginButton, addUserButton);

        formLayout.addComponents(login, email, split);
        layout.addComponent(formLayout);

        loginButton.addClickListener(clickEvent -> {
            try {
                userService.getUserByLoginAndEmail(login.getValue(), email.getValue());
                VaadinSession.getCurrent().setAttribute("user", userService.getUserByLogin(login.getValue()));
                getUI().getNavigator().navigateTo(SecurePage.VIEW_NAME);
                Notification notification = Notification.show("Użytkownik zalogowany");
            } catch (IllegalStateException e) {
                Notification notification = Notification.show("Błąd logowania, sprawdź login i/lub email.",
                        Notification.Type.ERROR_MESSAGE);
            } catch (ConstraintViolationException e) {
                Notification.show("Dane wpisane niepoprawnie !", Notification.Type.ERROR_MESSAGE);
            }
            login.clear();
            email.clear();
            login.focus();
        });

        addUserButton.addClickListener(clickEvent -> {
            try {
                userService.save(new User(login.getValue(), email.getValue()));
                Notification notification = Notification.show("Użytkownik o loginie " + login.getValue() + " i email + "
                        + email.getValue() + " został pomyślnie zapisany");
                Page.getCurrent().reload();
            } catch (IllegalStateException e) {
                Notification.show("Użytkownik o podanym loginie i/lub email jest już zarejestrowany.", Notification.Type.ERROR_MESSAGE);
            } catch (ConstraintViolationException e) {
                Notification.show("Dane wpisane niepoprawnie !", Notification.Type.ERROR_MESSAGE);
            }
            login.focus();
        });
    }
}
