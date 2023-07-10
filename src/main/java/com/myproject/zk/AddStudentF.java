package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

public class AddStudentF extends GenericForwardComposer<Component> {

    private Textbox nameF, emailF, addressF, feeF, paidF, dueF, contactF;
    private Radiogroup radiogroupF;
    private Combobox comboboxF;
    private Button submitF;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        submitF.addEventListener("onClick", submit);
    }

    private EventListener<Event> submit = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws Exception {
            // Validate email and contact
            if (!isValidEmail(emailF.getValue())) {
                Messagebox.show("Invalid email address");
                return;
            }
            if (!isValidContact(contactF.getValue())) {
                Messagebox.show("Invalid contact number");
                return;
            }

            // Check if email is already registered
            if (isEmailRegistered(emailF.getValue())) {
                Messagebox.show("Email address already exists");
                return;
            }

            // Database insertion code
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
                    PreparedStatement psmt = con.prepareStatement("INSERT INTO AddstudentF (nameF, emailF, radiogroupF, comboboxF, feeF, paidF, dueF, contactF, addressF) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                psmt.setString(1, nameF.getValue());
                psmt.setString(2, emailF.getValue());
                psmt.setString(3, radiogroupF.getSelectedItem().getLabel());
                psmt.setString(4, comboboxF.getValue());
                psmt.setInt(5, Integer.parseInt(feeF.getValue()));
                psmt.setInt(6, Integer.parseInt(paidF.getValue()));
                psmt.setInt(7, Integer.parseInt(dueF.getValue()));
                psmt.setString(8, contactF.getValue());
                psmt.setString(9, addressF.getValue());
                psmt.executeUpdate();

                Messagebox.show("Data inserted into AddStudentF successfully");
            } catch (Exception e) {
                e.printStackTrace();
                Messagebox.show("Failed to insert data into AddStudentF");
            }
        }
    };

    // Email validation method
    private boolean isValidEmail(String email) {
        // A simple email validation using regular expression
        String emailRegex = "(\\S.*\\S)@(\\S.*\\S)\\.\\S[a-z]{2,3}";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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
                PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM AddstudentF WHERE emailF = ?")) {

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
