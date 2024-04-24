
package acme.features.manager.projectuserstory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.roles.Manager;

@Controller
public class ManagerProjectUserStoryController extends AbstractController<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerProjectUserStoryListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
