package com.myproject.zk;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


@SuppressWarnings("deprecation")
public class JasperReportControllerA extends GenericAutowireComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6461423808925600039L;
	private Button generateReportBtn,generateReportBtn1;
	private Combobox outputFormatsCombobox;
	private Label reportLabel;
	private Iframe reportIframe;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		generateReportBtn.addEventListener("onClick", generateReportListener);
		generateReportBtn1.addEventListener("onClick", generateReportListener1);

	}

	private EventListener<Event> generateReportListener = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Connection connection = null;
			try {
				//	1. Load the report design.
				String reportTemplatePath = "/home/administrator/irepport zk/StudentRecord.jrxml";
				System.out.println("Loading report design: " + reportTemplatePath);
				JasperDesign design = JRXmlLoader.load(reportTemplatePath);

				// 2. Set the report query.
				String reportQuery = "SELECT * FROM AddStudentF";
				System.out.println("Report query: " + reportQuery);

				// 3. Establish a database connection and execute the query.
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject",
						"sonutripathi", "12345");
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(reportQuery);
				System.out.println("Database connection established and query executed.");

				// 4. Create a data source using the result set.
				JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
				System.out.println("Data source created.");

				// 5. Compile the report design.
				System.out.println("Compiling report design...");
				JasperReport jasperReport = JasperCompileManager.compileReport(design);
				System.out.println("Report design compiled successfully.");

				// 6. Fill the report with data.
				System.out.println("Filling the report with data...");
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
				System.out.println("Report filled with data.");

				// 7. Generate the report based on the selected format.
				String selectedFormat = (String) outputFormatsCombobox.getSelectedItem().getValue();
				String outputPath = generateReport(selectedFormat, jasperPrint);
				if (outputPath != null) {
					//8.View the report using JasperViewer.
					File pdfFile = new File(outputPath);
					AMedia amedia = new AMedia(pdfFile, null, null);
					reportIframe.setContent(amedia);
					reportLabel.setValue("Report generated successfully!");
				} else {
					reportLabel.setValue("Failed to generate report.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Failed to generate report: " + e.getMessage());
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
	};


	private EventListener<Event> generateReportListener1 = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Connection connection = null;
			try {
				//	1. Load the report design.
				String reportTemplatePath = "/home/administrator/irepport zk/StudentRecord.jrxml";
				System.out.println("Loading report design: " + reportTemplatePath);
				JasperDesign design = JRXmlLoader.load(reportTemplatePath);

				// 2. Set the report query.
				String reportQuery = "SELECT * FROM AddstudentF WHERE duef > 0";
				System.out.println("Report query: " + reportQuery);

				// 3. Establish a database connection and execute the query.
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject",
						"sonutripathi", "12345");
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(reportQuery);
				System.out.println("Database connection established and query executed.");

				// 4. Create a data source using the result set.
				JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
				System.out.println("Data source created.");

				// 5. Compile the report design.
				System.out.println("Compiling report design...");
				JasperReport jasperReport = JasperCompileManager.compileReport(design);
				System.out.println("Report design compiled successfully.");

				// 6. Fill the report with data.
				System.out.println("Filling the report with data...");
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
				System.out.println("Report filled with data.");

				// 7. Generate the report based on the selected format.
				String selectedFormat = (String) outputFormatsCombobox.getSelectedItem().getValue();
				String outputPath = generateReport(selectedFormat, jasperPrint);
				if (outputPath != null) {
					//8.View the report using JasperViewer.
					File pdfFile = new File(outputPath);
					AMedia amedia = new AMedia(pdfFile, null, null);
					reportIframe.setContent(amedia);
					reportLabel.setValue("Report generated successfully!");
				} else {
					reportLabel.setValue("Failed to generate report.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Failed to generate report: " + e.getMessage());
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
	};


	private String generateReport(String format, JasperPrint jasperPrint) throws Exception {
		// Generate the report based on the selected format.
		if (format.equalsIgnoreCase("pdf")) {
			String outputPath = "/home/administrator/Downloads/StudentRecord.pdf";
			System.out.println("Exporting report to PDF: " + outputPath);
			JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
			System.out.println("Report exported to PDF successfully.");
			return outputPath;
		} else if (format.equalsIgnoreCase("html")) {
			String outputPath = "/home/administrator/Downloads/StudentRecord.html";
			System.out.println("Exporting report to HTML: " + outputPath);
			JasperExportManager.exportReportToHtmlFile(jasperPrint, outputPath);
			System.out.println("Report exported to HTML successfully.");
			return outputPath;
		} else if (format.equalsIgnoreCase("excel")) {
			String outputPath = "/home/administrator/Downloads/StudentRecord.xlsx";
			System.out.println("Exporting report to XLSX: " + outputPath);
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRCsvExporterParameter.OUTPUT_FILE_NAME, outputPath);
			exporter.exportReport();
			System.out.println("Report exported to XLSX successfully.");
			return outputPath;
		} else if (format.equalsIgnoreCase("csv")) {
			String outputPath = "/home/administrator/Downloads/StudentRecord.csv";
			System.out.println("Exporting report to CSV: " + outputPath);
			// Create a CSV exporter
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRCsvExporterParameter.OUTPUT_FILE_NAME, outputPath);
			exporter.exportReport();
			System.out.println("Report exported to CSV successfully.");
			return outputPath;
		}
		return null;
	}





}