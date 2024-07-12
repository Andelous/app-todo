package com.example.app.todo.views;

import com.example.app.todo.AppUtils;
import com.example.ms.users.def.ex.MSUsersException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout implements AfterNavigationObserver {
	private static final long serialVersionUID = 7574714034461350987L;

	public LoginView() {
		TextField txtUsername = new TextField();
		txtUsername.setPlaceholder("Username");
		txtUsername.setClearButtonVisible(true);
		txtUsername.setPrefixComponent(VaadinIcon.USER.create());

		PasswordField txtPassword = new PasswordField();
		txtPassword.setPlaceholder("Password");
		txtPassword.setClearButtonVisible(true);
		txtPassword.setPrefixComponent(VaadinIcon.LOCK.create());

		Button cmdLogin = new Button("Log in");
		cmdLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cmdLogin.addClickListener(event -> {
			try {
				AppUtils.login(txtUsername.getValue(), txtPassword.getValue());
				getUI().get().navigate(MainView.class);
			} catch (MSUsersException e) {
				Notification notification = Notification.show(e.getMessage());
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});

		add(txtUsername, txtPassword, cmdLogin);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (AppUtils.isLoggedIn()) {
			getUI().get().navigate(MainView.class);
			return;
		}
	}

}
