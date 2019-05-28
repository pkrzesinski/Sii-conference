package com.project.siiproject.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.annotation.PrototypeScope;

@PrototypeScope
@SpringView(name = SecurePage.VIEW_NAME)
public class SecurePage extends VerticalLayout implements View {
    public static final String VIEW_NAME = "secure";

    private VerticalLayout layout = new VerticalLayout();
    private Label secure;
    private Label currentUser;
    private Button logout;

    public SecurePage() {

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);

        setupLayout();
        addHeader();

        currentUser = new Label("Obecny użytkownik");
        logout = new Button("Logout");
        logout.addStyleName(ValoTheme.BUTTON_DANGER);

        formLayout.addComponents(currentUser, logout);
        layout.addComponent(formLayout);

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
        currentUser.setCaption("Obecny użytkownik : " + VaadinSession.getCurrent().getAttribute("user").toString());
    }
}
