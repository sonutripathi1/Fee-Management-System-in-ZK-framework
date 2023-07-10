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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class AccountantLogin extends GenericForwardComposer<Component> {

	Textbox AccEmail, AccPassword;
	Button AccLogin;

	public void doAfterCompose(Component com) throws Exception {
		super.doAfterCompose(com);
		AccLogin.addEventListener("onClick", Login);

	}

	EventListener<Event> Login = new EventListener<Event>(){

		@Override
		public void onEvent(Event event) throws Exception {
			// TODO Auto-generated method stub

			if(AccEmail.getValue().equals(" ")|| AccPassword.getValue().equals(" ")) {
				Messagebox.show("Please fill the value.");
			}
			try {

				Class.forName("org.postgresql.Driver");
				Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/zkproject","sonutripathi","12345");
				PreparedStatement psmt = con.prepareStatement("Select * from AddAccountant");
				ResultSet rs = psmt.executeQuery();

				while (rs.next())
				{
					if(AccEmail.getValue().equals(rs.getString(4)) && AccPassword.getValue().equals(rs.getString(3))) 
					{
						Executions.sendRedirect("/AccountantP.zul");
					}
					else
					{
						Messagebox.show("Wrong password","error",Messagebox.OK,Messagebox.ERROR);		
					}
				}

			}catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("user name already registered");
			}
		}

	};




}
