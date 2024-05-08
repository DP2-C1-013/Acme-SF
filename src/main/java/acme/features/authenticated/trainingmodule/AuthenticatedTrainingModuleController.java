
package acme.features.authenticated.trainingmodule;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Authenticated;
import acme.entities.trainingmodule.TrainingModule;

@Controller
public class AuthenticatedTrainingModuleController extends AbstractController<Authenticated, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedTrainingModuleListService	listService;

	@Autowired
	private AuthenticatedTrainingModuleShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
