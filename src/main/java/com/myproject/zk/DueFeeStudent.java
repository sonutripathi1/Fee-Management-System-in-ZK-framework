package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

public class DueFeeStudent extends GenericForwardComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3227544425304399113L;
	Rows rows;


	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AddstudentF WHERE duef > 0");
		ResultSet rs = stmt.executeQuery();


		while (rs.next()) {
			Row row = new Row();

			// Create cells for each column
			for (int i = 1; i <= 10; i++) {
				Label label = new Label(rs.getString(i));
				row.appendChild(label);
			}

			// Add edit button to the row
			Button editButton = new Button("Edit");
			editButton.addEventListener("onClick", createEditListener(row));
			row.appendChild(editButton);

			// Add delete button to the row
			Button deleteButton = new Button("Delete");
			deleteButton.addEventListener("onClick", createDeleteListener(row));
			row.appendChild(deleteButton);



			rows.appendChild(row);
		}
		stmt.close();
		conn.close();
	}

	private EventListener<Event> createDeleteListener(Row row) {
		return evt -> {
			// Delete logic for the specific row
			rows.removeChild(row);

			// Retrieve the data from the row to identify the record to delete
			String column1Value = ((Label) row.getChildren().get(0)).getValue();
			System.out.println(column1Value);
			// Perform the deletion operation using the retrieved data
			String s= "DELETE FROM AddstudentF WHERE id ="+column1Value;

			try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject", "sonutripathi", "12345");
					PreparedStatement stmt =
							conn.prepareStatement(s)) {
				System.out.println("-----------------------------------------------------");
				// stmt.setString(1, column1Value);
				int rowsAffected = stmt.executeUpdate();

				if (rowsAffected > 0) {
					// Deletion successful
					System.out.println("Record deleted successfully.");
				} else {
					// Deletion failed or no matching record found
					System.out.println("No record found for deletion.");
				}
			} catch (Exception e) {
				// Handle exceptions appropriately
				e.printStackTrace();
			}
		};
	}

	private EventListener<Event> createEditListener( Row row) {
		return evt -> {

			String column1Value = ((Label) row.getChildren().get(0)).getValue();
			System.out.println(column1Value);
			// Update logic for the specific row

			//Executions.sendRedirect("EditAccountant.zul?column1Value"=column1Value);

			String url = "EditStudentF.zul?column1Value=" + column1Value;
			Executions.sendRedirect(url);
		};
	}
}
