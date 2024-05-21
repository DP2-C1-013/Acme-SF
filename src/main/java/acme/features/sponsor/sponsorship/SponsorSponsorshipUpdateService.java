
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

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
		if (project != null)
			project = this.repository.findOneProjectByCode(project.getCode());

		super.bind(object, "code", "moment", "duration", "amount", "type", "email", "link");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;
			existing = this.repository.findOneSponsorshipByCode(object.getCode());
			super.state(existing == null || existing.getId() == object.getId(), "code", "sponsor.sponsorship.form.error.duplicated-code");
		}

		if (!(super.getBuffer().getErrors().hasErrors("duration") || super.getBuffer().getErrors().hasErrors("moment"))) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getMoment(), 30, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getDuration(), minimunDuration), "duration", "sponsor.sponsorship.form.error.invalid-duration");

			Date minDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");
			Date maxDate = MomentHelper.parse("2200/12/31 23:59", "yyyy/MM/dd HH:mm");
			super.state(MomentHelper.isAfterOrEqual(object.getMoment(), minDate) && MomentHelper.isBeforeOrEqual(object.getMoment(), maxDate), "moment", "sponsor.sponsorship.form.error.moment-out-of-range");
			super.state(MomentHelper.isAfterOrEqual(object.getDuration(), minDate) && MomentHelper.isBeforeOrEqual(object.getDuration(), maxDate), "duration", "sponsor.sponsorship.form.error.duration-out-of-range");
		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			Double amount = object.getAmount().getAmount();
			SponsorshipType type = object.getType();
			super.state((amount > 0. && type.equals(SponsorshipType.Financial) || amount.equals(0.) && type.equals(SponsorshipType.In_kind)) && amount <= 1000000.00, "amount", "sponsor.sponsorship.form.error.invalid-amount");

			List<String> currencies = Arrays.asList(this.repository.findSystemCurrencies().get(0).getAcceptedCurrencies().split(","));
			super.state(currencies.stream().anyMatch(c -> c.equals(object.getAmount().getCurrency())), "amount", "sponsor.sponsorship.form.error.invalid-currency");

			List<String> invoiceCurrencies = (List<String>) this.repository.findManyCurrenciesInInvoiceBySponsorshipId(object.getId());
			super.state(invoiceCurrencies.stream().allMatch(invoiceCurrency -> invoiceCurrency.equals(object.getAmount().getCurrency())), "amount", "sponsor.sponsorship.form.error.currency-does-not-match-with-invoices");
			Double sumAmountInvoices = this.repository.findSumAmountInvoicesBySponsorshipId(this.getRequest().getData("id", int.class));
			super.state(sumAmountInvoices <= amount, "amount", "sponsor.sponsorship.form.error.new-amount-small-than-sum-of-invoice-quantity");
		}

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Project existingProject = this.repository.findOneProjectByCode(object.getProject().getCode());
			super.state(existingProject != null && !existingProject.isDraftMode(), "project", "sponsor.sponsorship.form.error.invalid-project");
		}

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
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
		dataset.put("project", projects.getSelected());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("PUT"))
			PrincipalHelper.handleUpdate();
	}
}
