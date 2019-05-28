package com.project.siiproject.vaadin;

import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

@SpringUI
public class AddNewUser extends UI {

    private VerticalLayout layout;

    private UserService userService;

    @Autowired
    public AddNewUser(UserService userService) {
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
        Label header = new Label("Plan konferencji");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }

    private void addForm() {
        VerticalLayout formLayout = new VerticalLayout();
        Label label = new Label("Dodawanie Użytkownika");
        formLayout.setSpacing(true);

        TextField login = new TextField("Login");
        TextField email = new TextField("Email");
        Button addButton = new Button("Dodaj użytkownika");
        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        formLayout.addComponents(label, login, email, addButton);
        layout.addComponent(formLayout);

        addButton.addClickListener(clickEvent -> {
            User user = new User(login.getValue(), email.getValue());
            try {
                userService.save(user);
                Notification notification = Notification.show("Użytkownik dodany");
            } catch (ConstraintViolationException e) {
                Notification notification = Notification.show("Wystąpił błąd przy dodawaniu");
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
