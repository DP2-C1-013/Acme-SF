
package acme.features.authenticated.claim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.claim.Claim;

@Service
public class AuthenticatedClaimPublishService extends AbstractService<Authenticated, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(claimId);
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneClaimById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link", "draftMode");
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "error.confirmation.required");

	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link", "draftMode");
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
