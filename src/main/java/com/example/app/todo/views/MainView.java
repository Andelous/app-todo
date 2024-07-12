package com.example.app.todo.views;

import com.example.app.todo.AppUtils;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout implements AfterNavigationObserver {
	private static final long serialVersionUID = 2863699250925129379L;

	public MainView() {
		// Use TextField for standard text input
		TextField textField = new TextField("Your name");
		textField.addClassName("bordered");

		// Button click listeners can be defined as lambda expressions
		Button button = new Button("Say hello", e -> {
			add(new Paragraph("Hi " + textField.getValue()));
		});

		// You can specify keyboard shortcuts for buttons.
		// Example: Pressing enter in this view clicks the Button.
		button.addClickShortcut(Key.ENTER);

		add(textField, button);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// If no user is logged in, we ask for logging in.
		if (!AppUtils.isLoggedIn()) {
			getUI().get().navigate(LoginView.class);
			return;
		}
	}
}