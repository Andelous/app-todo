package com.example.app.todo;

import com.example.ms.users.MSUsersClient;
import com.example.ms.users.def.ex.IncorrectPasswordException;
import com.example.ms.users.def.ex.IncorrectUsernameException;
import com.example.ms.users.def.model.User;
import com.vaadin.flow.server.VaadinSession;

public class AppUtils {
	private static final MSUsersClient MS_USERS_CLIENT = new MSUsersClient();

	private static final String SESSION_AUTH_TOKEN = "auth-token";

	/**
	 * Returns the currently logged in {@linkplain User}.
	 */
	public static User getActiveUser() {
		VaadinSession session = VaadinSession.getCurrent();

		String authToken = (String) session.getAttribute(SESSION_AUTH_TOKEN);

		if (authToken == null)
			return null;

		User activeUser = MS_USERS_CLIENT.getUserByToken(authToken);

		return activeUser;
	}

	/**
	 * Returns {@code true} if there's a {@linkplain User} logged in, {@code false}
	 * otherwise.
	 */
	public static boolean isLoggedIn() {
		return getActiveUser() != null;
	}

	/**
	 * Authenticates the user against the users microservice. If the log in is
	 * successful, then its token is stored under the session property
	 * {@code "auth-token"}.
	 */
	public static void login(String username, String password)
			throws IncorrectPasswordException, IncorrectUsernameException {
		VaadinSession session = VaadinSession.getCurrent();

		session.setAttribute(SESSION_AUTH_TOKEN, MS_USERS_CLIENT.authenticateUser(username, password));
	}
}
