package com.myproject.zk;



import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Spinner;

public class SearchStudent extends GenericForwardComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Spinner rollNoSpinner;
	@Wire
	private Button searchButton;


	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		searchButton.addEventListener("onClick", search);
	}

	EventListener<Event> search = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {

			int rollNo = rollNoSpinner.getValue();
			
			String url = "SearchStudentD.zul?rollNo=" + rollNo;
			Executions.sendRedirect(url);
			
			
		}
	};
}
