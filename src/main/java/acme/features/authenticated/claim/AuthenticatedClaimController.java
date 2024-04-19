
package acme.features.authenticated.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Authenticated;
import acme.entities.claim.Claim;

@Controller
public class AuthenticatedClaimController extends AbstractController<Authenticated, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedClaimListService		listService;

	@Autowired
	private AuthenticatedClaimShowService		showService;

	@Autowired
	private AuthenticatedClaimPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("publish", "update", this.publishService);

	}

}
