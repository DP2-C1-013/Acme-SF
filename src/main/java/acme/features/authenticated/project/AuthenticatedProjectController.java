
package acme.features.authenticated.project;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Authenticated;
import acme.entities.project.Project;

@Controller
public class AuthenticatedProjectController extends AbstractController<Authenticated, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedProjectListService	listService;

	@Autowired
	private AuthenticatedProjectShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
