package com.myproject.zk;
import java.sql.*;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zul.*;

@SuppressWarnings("serial")
public class StudentSearchControllerTest extends SelectorComposer<Component> {

    @Wire
    private Spinner rollNumberSpinner;

    @Wire
    private Button searchButton;

    @Wire
    private Include resultInclude;

    private Connection connection;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
    }

    @Listen("onClick = #searchButton")
    public void searchButtonClick(Event event) throws SQLException {
        int rollNumber = (Integer) rollNumberSpinner.getValue();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM AddstudentF WHERE id = ?");
        statement.setInt(1, rollNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            // Get student record details
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String grade = resultSet.getString("grade");

            // Set the result details in the ZUL page
            Executions.createComponents("result.zul", resultInclude, null);
            Label nameLabel = (Label) resultInclude.getFellow("nameLabel");
            Label ageLabel = (Label) resultInclude.getFellow("ageLabel");
            Label gradeLabel = (Label) resultInclude.getFellow("gradeLabel");
            nameLabel.setValue(name);
            ageLabel.setValue(Integer.toString(age));
            gradeLabel.setValue(grade);
        } else {
            Messagebox.show("No records found!");
        }

        resultSet.close();
        statement.close();
    }
}
