package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

public class EditStudentF extends GenericForwardComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Wire the necessary components from the ZUL page
	@Wire
	private Textbox name, id;
	@Wire
	private Textbox email;
	@Wire
	private Radiogroup radiogroupF;
	@Wire
	private Textbox comboboxF;
	@Wire
	private Textbox fee;
	@Wire
	private Textbox paid;
	@Wire
	private Textbox due;
	@Wire
	private Textbox address;
	@Wire
	private Textbox contact;
	@Wire
	private Button update;

	String column1Value = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		update.addEventListener("onClick", updateStudentF);
		column1Value = Executions.getCurrent().getParameter("column1Value");

		// Perform any initialization or setup here
	}

	EventListener<Event> updateStudentF = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			// Validate email
			if (!isValidEmail(email.getValue())) {
				Messagebox.show("Invalid email address");
				return;
			}

			// Validate contact
			if (!isValidContact(contact.getValue())) {
				Messagebox.show("Invalid contact number");
				return;
			}

			// Check if email is already registered
			if (isEmailRegistered(email.getValue())) {
				Messagebox.show("Email address already exists");
				return;
			}

			// Rest of your code for updating the student record
			String str = "UPDATE AddstudentF SET namef = ?, emailf = ?, radiogroupf = ?, comboboxf = ?, feef= ?, paidf = ?, duef = ?, contactf = ?, addressf = ? where id =" + column1Value;
			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
			PreparedStatement psmt = con.prepareStatement(str);

			psmt.setString(1, name.getValue());
			psmt.setString(2, email.getValue());
			psmt.setString(3, radiogroupF.getSelectedItem().getLabel());
			psmt.setString(4, comboboxF.getValue());
			psmt.setInt(5, Integer.parseInt(fee.getValue()));
			psmt.setInt(6, Integer.parseInt(paid.getValue()));
			psmt.setInt(7, Integer.parseInt(due.getValue()));
			psmt.setString(8, contact.getValue());
			psmt.setString(9, address.getValue());
			psmt.executeUpdate();
			Messagebox.show("Update data in AddStudent successfully");
		}
	};

	// Email validation method
	private boolean isValidEmail(String email) {
		// Implement your email validation logic here
		// For example, you can use a regular expression pattern
		String emailPattern = "(\\S.*\\S)(@)(\\S.*\\S)(.\\S[a-z]{2,3})";
		return email.matches(emailPattern);
	}

	// Contact validation method
	private boolean isValidContact(String contact) {
		// Validate contact number (e.g., 1234567890 or +911234567890)
		String contactRegex = "^[+]?[0-9]{10,13}$";
		Pattern pattern = Pattern.compile(contactRegex);
		return pattern.matcher(contact).matches();
	}

	// Check if email is already registered
	private boolean isEmailRegistered(String email) {
		try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
				PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM AddstudentF WHERE emailf = ?")) {

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
