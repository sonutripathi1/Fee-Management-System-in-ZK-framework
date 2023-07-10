package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EditAccountantController extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5027508589732722198L;
	@Wire
	private Textbox name, id;
	@Wire
	private Textbox pass;
	@Wire
	private Textbox email;
	@Wire
	private Textbox addr;
	@Wire
	private Textbox cont;
	@Wire
	private Button update;

	String column1Value = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		update.addEventListener("onClick", updateAccountant);
		column1Value = Executions.getCurrent().getParameter("column1Value");
	}

	EventListener<Event> updateAccountant = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			// Validate input fields
			if (!isValidName(name.getValue())) {
				Messagebox.show("Invalid name");
				return;
			}

			if (!isValidPassword(pass.getValue())) {
				Messagebox.show("Invalid password. It must contain at least 8 characters, including uppercase, lowercase, and numeric characters.");
				return;
			}

			if (!isValidEmail(email.getValue())) {
				Messagebox.show("Invalid email address");
				return;
			}

			if (!isValidContact(cont.getValue())) {
				Messagebox.show("Invalid contact number. It must be a 10-digit number.");
				return;
			}
			
			 // Check if email is already registered
            if (isEmailRegistered(email.getValue())) {
                Messagebox.show("Email address already exists");
                return;
            }

			// Database code
			String sql = "UPDATE AddAccountant SET name = ?,email = ?,  cnt = ?, address = ?, password = ? WHERE id = " + column1Value;
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
			PreparedStatement psmt = con.prepareStatement(sql);
			psmt.setString(1, name.getValue());
			psmt.setString(2, email.getValue());
			psmt.setString(3,cont.getValue());
			psmt.setString(4, addr.getValue());
			psmt.setString(5, pass.getValue());
			psmt.executeUpdate();
			Messagebox.show("Update data in AddAccountant successfully");
		}
	};

	// Name validation method
	private boolean isValidName(String name) {
		return name != null && !name.isEmpty();
	}

	// Password validation method
	private boolean isValidPassword(String password) {
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[a-zA-Z\\d@$!%*#?&]{8,}$"
				+ ""
				+ "";
		Pattern pattern = Pattern.compile(passwordRegex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	// Email validation method
	private boolean isValidEmail(String email) {
		String emailRegex = "(\\S.*\\S)(@)(\\S.*\\S)(.\\S[a-z]{2,3})";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// Contact validation method
	private boolean isValidContact(String contact) {
		String contactRegex = "\\d{10}";
		Pattern pattern = Pattern.compile(contactRegex);
		Matcher matcher = pattern.matcher(contact);
		return matcher.matches();
	}


	// Check if email is already registered
	private boolean isEmailRegistered(String email) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
			PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM AddAccountant WHERE email = ?");
			psmt.setString(1, email);
			ResultSet rs = psmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
