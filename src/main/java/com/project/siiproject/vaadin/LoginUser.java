package com.project.siiproject.vaadin;

import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

@SpringUI(path = "/login")
public class LoginUser extends UI {

    private VerticalLayout layout;

    private UserService userService;

    @Autowired
    public LoginUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupLayout();
        addHeader();
        addForm();
        addButtonAction();
    }

    private void setupLayout() {
        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void addHeader() {
        Label header = new Label("Strona Logowania");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }

    private void addForm() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);

        TextField login = new TextField("Login");
        TextField email = new TextField("Email");
        Button addButton = new Button("Zaloguj");
        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        formLayout.addComponents( login, email, addButton);
        layout.addComponent(formLayout);

        addButton.addClickListener(clickEvent -> {
            try {
                userService.getUserByLoginAndEmail(login.getValue(), email.getValue());
                VaadinSession.getCurrent().getSession();
                Notification notification = Notification.show("Użytkownik zalogowany");
            } catch (IllegalStateException e) {
                Notification notification = Notification.show("Błąd logowania, sprawdź legin i/lub email.");
            }

            login
                    .clear();
            email.clear();
            login.focus();
        });

    }

    private void addButtonAction() {

    }

}
