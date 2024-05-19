
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoice.Invoice;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int sponsorId;
		int sponsorshipId;

		sponsorshipId = request.getData("id", int.class);

		sponsorId = request.getPrincipal().getActiveRoleId();

		Sponsorship object = this.repository.findOneSponsorshipById(sponsorshipId);

		status = object != null && request.getPrincipal().hasRole(Sponsor.class) && object.getSponsor().getId() == sponsorId && object.isDraftMode();

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
	public void bind(final Sponsorship object) {
		assert object != null;

		Project project;
		project = this.getRequest().getData("project", Project.class);

		super.bind(object, "code", "moment", "duration", "amount", "type", "email", "link");
		object.setProject(project);

	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		int id = super.getRequest().getData("id", int.class);
		int numPublishedInvoice = this.repository.findNumberPublishedInvoicesBySponsorshipId(id);
		super.state(numPublishedInvoice == 0, "*", "sponsor.sponsorship.form.error.published-invoices-cannot-delete");
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		Collection<Invoice> invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());

		this.repository.deleteAll(invoices);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		SelectChoices types;
		SelectChoices projects;
		Dataset dataset;

		types = SelectChoices.from(SponsorshipType.class, object.getType());
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeFalse(), "code", object.getProject());

		dataset = super.unbind(object, "code", "moment", "duration", "amount", "type", "email", "link", "draftMode");
		dataset.put("types", types);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
