package com.myproject.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class Adminlogin extends GenericForwardComposer<Component> {

	Textbox AdEmail, AdPassword;
	Button AdLoginbtn;

	public void doAfterCompose(Component com) throws Exception
	{
		super.doAfterCompose(com);
		AdLoginbtn.addEventListener("onClick", Login);
	}

	EventListener<Event> Login = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			// TODO Auto-generated method stub
			if(AdEmail.getValue().equals(null) || AdPassword.getValue().equals(null) ) {
				Messagebox.show("Please fill the value");
			}else {
				try {
					
					if(AdEmail.getValue().equals("Sonu@123")||AdPassword.getValue().equals("12345")) {
						Executions.sendRedirect("/AdminP.zul");
					}
					else
					{
						Messagebox.show("Invalid login or password. Please try again.","error",Messagebox.OK,Messagebox.ERROR);		
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	};
}


