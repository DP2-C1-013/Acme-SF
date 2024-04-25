
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceDeleteService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int sponsorId;
		int invoiceId;

		invoiceId = request.getData("id", int.class);

		sponsorId = request.getPrincipal().getActiveRoleId();

		Invoice object = this.repository.findOneInvoiceById(invoiceId);

		status = object != null && request.getPrincipal().hasRole(Sponsor.class) && object.getSponsorship().getSponsor().getId() == sponsorId && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");

	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("sponsorship", object.getSponsorship().getCode());
		dataset.put("sponsorshipDraftMode", object.getSponsorship().isDraftMode());
		dataset.put("sponsorshipId", object.getSponsorship().getId());

		super.getResponse().addData(dataset);
	}
}
