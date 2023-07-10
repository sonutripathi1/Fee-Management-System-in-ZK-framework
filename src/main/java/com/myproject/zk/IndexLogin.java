package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

public class IndexLogin extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4797601858009714872L;
	private Radiogroup userTypeGroup;
	private Textbox usernameBox;
	private Textbox passwordBox;
	private Button login;
	private String userType;
	private Connection connection;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		login.addEventListener(Events.ON_CLICK, loginListener);
	}

	EventListener<Event> loginListener = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			String username = usernameBox.getValue();
			String password = passwordBox.getValue();
			userType = getUserType();

			if (userType == null) {
				Messagebox.show("Please select user type.");
				return;
			}

			if (username.isEmpty() || password.isEmpty()) {
				Messagebox.show("Please fill in both username and password fields.");
				return;
			}

			try {
				boolean loginSuccessful = loginUser(username, password, userType);

				if (loginSuccessful) {
					if ("admin".equals(userType)) {
						Executions.sendRedirect("/AdminP.zul");
					} else if ("accountant".equals(userType)) {
						Executions.sendRedirect("/AccountantP.zul");
					} else {
						Messagebox.show("Invalid user type.", "Error", Messagebox.OK, Messagebox.ERROR);
					}
				} else {
					Messagebox.show("Invalid login or password. Please try again.", "Error", Messagebox.OK,
							Messagebox.ERROR);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("An error occurred during login.", "Error", Messagebox.OK, Messagebox.ERROR);
			} finally {
				closeConnection();
			}
		}
	};

	private String getUserType() {
		Radio selectedRadio = userTypeGroup.getSelectedItem();
		if (selectedRadio != null) {
			return selectedRadio.getValue();
		}
		return null;
	}

	private boolean loginUser(String username, String password, String userType) throws Exception {
		if ("admin".equals(userType)) {
			return validateAdminLogin(username, password);
		} else if ("accountant".equals(userType)) {
			return validateUserLogin(username, password);
		} else {
			return false;
		}
	}

	private boolean validateAdminLogin(String username, String password) {
		return username.equals("Admin") && password.equals("12345");
	}

	private boolean validateUserLogin(String username, String password) throws Exception {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi",
				"12345");
		PreparedStatement psmt = connection.prepareStatement("SELECT * FROM AddAccountant");
		ResultSet rs = psmt.executeQuery();

		while (rs.next()) {
			if (username.equals(rs.getString(3)) && password.equals(rs.getString(6))) {
				return true;
			}
		}

		return false;
	}

	private void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
