
package acme.features.sponsor;

import java.time.temporal.ChronoUnit;
import java.util.Date;

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

		super.bind(object, "code", "moment", "duration", "amount", "type", "email", "link");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("duration")) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getMoment(), 30, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getDuration(), minimunDuration), "duration", "sponsor.sponsorship.form.error.invalid-duration");
		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			Double amount = object.getAmount().getAmount();
			SponsorshipType type = object.getType();
			super.state(amount > 0. && type.equals(SponsorshipType.Financial) || amount.equals(0.) && type.equals(SponsorshipType.In_kind), "amount", "sponsor.sponsorship.form.error.invalid-amount");
		}

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Project existingProject = this.repository.findOneProjectByCode(object.getProject().getCode());
			super.state(existingProject != null && existingProject.isDraftMode() && object.getProject().isDraftMode(), "project", "sponsor.sponsorship.form.error.invalid-project");
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
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeTrue(), "code", object.getProject());

		dataset = super.unbind(object, "code", "moment", "duration", "amount", "type", "email", "link", "draftMode");
		dataset.put("types", types);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
