package com.project.siiproject.vaadin;

import com.project.siiproject.feature.user.service.UserService;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;

@PrototypeScope
@SpringView(name = LoginUser.VIEW_NAME)
public class LoginUser extends VerticalLayout implements View {
    public static final String VIEW_NAME = "login";

    private VerticalLayout layout;
    private UserService userService;

    public LoginUser() {
    }

    @Autowired
    public LoginUser(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void init() {
        setupLayout();
        addHeader();
        addForm();
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
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);

        TextField login = new TextField("Login");
        TextField email = new TextField("Email");
        Button addButton = new Button("Zaloguj");
        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        formLayout.addComponents(login, email, addButton);
        layout.addComponent(formLayout);

        addButton.addClickListener(clickEvent -> {
            try {
                userService.getUserByLoginAndEmail(login.getValue(), email.getValue());
                VaadinSession.getCurrent().setAttribute("user", login.getValue());
                getUI().getNavigator().navigateTo(SecurePage.VIEW_NAME);
//                Page.getCurrent().setUriFragment(SecurePage.VIEW_NAME);
                Notification notification = Notification.show("Użytkownik zalogowany");
            } catch (IllegalStateException e) {
                Notification notification = Notification.show("Błąd logowania, sprawdź legin i/lub email.",
                        Notification.Type.ERROR_MESSAGE);
            }

            login
                    .clear();
            email.clear();
            login.focus();
        });
    }
}
