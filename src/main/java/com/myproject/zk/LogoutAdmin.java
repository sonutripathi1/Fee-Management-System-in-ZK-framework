package com.myproject.zk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;

@SuppressWarnings("serial")
public class LogoutAdmin extends GenericForwardComposer<Component>{

	Button logout;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		logout.addEventListener("onClick", logoutListner);

	}

	private EventListener<Event> logoutListner = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			// Invalidate the session
			Executions.getCurrent().getSession().invalidate();

			// Redirect the user to the login page
			Executions.sendRedirect("/index1.zul");

		}
	};
}
