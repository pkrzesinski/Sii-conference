package com.project.siiproject.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import org.vaadin.spring.annotation.PrototypeScope;

@PrototypeScope
@SpringView(name = "default")
public class DefaultView extends Composite implements View {

    public DefaultView() {
        setCompositionRoot(new Label("This is default view"));
    }
}
