package com.example.app.todo.views;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.example.app.todo.AppUtils;
import com.example.ms.users.def.model.User;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout implements AfterNavigationObserver {
	private static final long serialVersionUID = 2863699250925129379L;

	private static final Map<String, Map<String, Boolean>> TODOS = new HashMap<>();

	private User activeUser;

	private VerticalLayout todosLayout;

	public MainView() {
		// To-do text field
		TextField txtTodo = new TextField("New to-do");
		txtTodo.addClassName("bordered");

		// Add button
		Button cmdAdd = new Button("Add", e -> {
			Map<String, Boolean> userTodos = TODOS.get(activeUser.getUsername());

			if (userTodos == null) {
				userTodos = new LinkedHashMap<>();
				TODOS.put(activeUser.getUsername(), userTodos);
			}

			userTodos.put(txtTodo.getValue(), false);
			txtTodo.setValue("");

			repaintTodos();
		});
		cmdAdd.addClickShortcut(Key.ENTER);

		// List of to-dos
		todosLayout = new VerticalLayout();

		Button cmdLogout = new Button("Log out");
		cmdLogout.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cmdLogout.addClickListener(event -> {
			getUI().get().getSession().close();
			getUI().get().navigate(LoginView.class);
		});

		// Add everything to the component
		add(txtTodo, cmdAdd, todosLayout, cmdLogout);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// If no user is logged in, we ask for logging in.
		if (!AppUtils.isLoggedIn()) {
			getUI().get().navigate(LoginView.class);
			return;
		}

		// Otherwise, we show his to-dos.
		activeUser = AppUtils.getActiveUser();
		repaintTodos();
	}

	private void repaintTodos() {
		Map<String, Boolean> userTodos = TODOS.get(activeUser.getUsername());

		if (userTodos == null) {
			userTodos = new LinkedHashMap<>();
			TODOS.put(activeUser.getUsername(), userTodos);
		}

		Set<Entry<String, Boolean>> entrySet = userTodos.entrySet();

		todosLayout.removeAll();

		for (Entry<String, Boolean> entry : entrySet) {
			Checkbox chkTodo = new Checkbox(entry.getKey());
			chkTodo.setValue(entry.getValue());
			chkTodo.addValueChangeListener(event -> {
				TODOS.get(activeUser.getUsername()).put(entry.getKey(), event.getValue());
			});

			Button cmdDeleteTodo = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
			cmdDeleteTodo.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON);

			Paragraph p = new Paragraph(chkTodo, cmdDeleteTodo);

			cmdDeleteTodo.addClickListener(event -> {
				TODOS.get(activeUser.getUsername()).remove(entry.getKey());
				todosLayout.remove(p);
			});

			todosLayout.add(p);
		}
	}
}