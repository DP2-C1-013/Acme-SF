
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceCreateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = this.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		object = new Invoice();
		object.setDraftMode(true);
		object.setSponsorship(sponsorship);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existing;
			existing = this.repository.findOneInvoiceByCode(object.getCode());
			super.state(existing == null, "code", "sponsor.invoice.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getRegistrationTime(), 30, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getDueDate(), minimunDuration), "dueDate", "sponsor.invoice.form.error.invalid-due-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			Double amount = object.getQuantity().getAmount();
			SponsorshipType type = object.getSponsorship().getType();
			super.state(amount > 0. && type.equals(SponsorshipType.Financial) || amount.equals(0.) && type.equals(SponsorshipType.In_kind), "quantity", "sponsor.invoice.form.error.invalid-quantity");

			int sponsorshipId = this.getRequest().getData("sponsorshipId", int.class);
			double totalCost = this.repository.findSumTotalAmountBySponsorshipId(sponsorshipId);
			double AmountToAdd = object.totalAmount().getAmount();
			super.state(totalCost + AmountToAdd <= object.getSponsorship().getAmount().getAmount(), "quantity", "sponsor.invoice.form.error.quantity-too-high");
		}

		if (!super.getBuffer().getErrors().hasErrors("sponsorship")) {
			Sponsorship existingSponsorship = this.repository.findOneSponsorshipById(this.getRequest().getData("sponsorshipId", int.class));
			super.state(existingSponsorship != null && existingSponsorship.isDraftMode() && existingSponsorship.getProject().isDraftMode(), "sponsorship", "sponsor.sponsorship.form.error.sponsorship-draft-mode-is-set-to-false");
		}
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
