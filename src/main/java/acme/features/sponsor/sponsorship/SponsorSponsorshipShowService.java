
package acme.features.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService<Authenticated, Claim> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int sponsorId;
		int sponsorshipId;

		sponsorshipId = request.getData("id", int.class);

		sponsorId = request.getPrincipal().getActiveRoleId();

		Sponsorship object = this.repository.findOneSponsorshipById(sponsorshipId);

		status = object != null && request.getPrincipal().hasRole(Sponsor.class) && object.getSponsor().getId() == sponsorId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projects;
		Dataset dataset;

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		projects = SelectChoices.from(this.repository.findAllProjects(), "code", object.getProject());

		dataset = super.unbind(object, "code", "moment", "duration", "amount", "type", "email", "link", "draftMode");
		dataset.put("types", choices);
		dataset.put("projects", projects);

		super.getResponse().addData(dataset);
	}
}
