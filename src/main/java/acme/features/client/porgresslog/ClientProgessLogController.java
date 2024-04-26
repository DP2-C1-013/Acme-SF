
package acme.features.client.porgresslog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.progresslog.ProgressLog;
import acme.roles.client.Client;

@Controller
public class ClientProgessLogController extends AbstractController<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogListService	listService;
	@Autowired
	ClientProgressLogShowService			showService;
	@Autowired
	ClientProgressLogCreateService			createService;
	@Autowired
	ClientProgressLogUpdateService			updateService;
	@Autowired
	ClientProgressLogPublishService			publishService;
	@Autowired
	ClientProgressLogDeleteService			deleteService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
