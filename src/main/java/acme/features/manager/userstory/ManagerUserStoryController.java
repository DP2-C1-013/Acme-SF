
package acme.features.manager.userstory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Controller
public class ManagerUserStoryController extends AbstractController<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryListService				listService;

	@Autowired
	private ManagerUserStoryShowService				showService;

	@Autowired
	private ManagerUserStoryCreateService			createService;

	@Autowired
	private ManagerUserStoryCreateInProjectService	createInProjectService;

	@Autowired
	private ManagerUserStoryListMineService			listMineService;

	@Autowired
	private ManagerUserStoryUpdateService			updateService;

	@Autowired
	private ManagerUserStoryDeleteService			deleteService;

	@Autowired
	private ManagerUserStoryPublishService			publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("create-in-project", "create", this.createInProjectService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-mine", "list", this.listMineService);

	}
}
