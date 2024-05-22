
package acme.features.developer.trainingsession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionPublishService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int developerId;
		int trainingSessionId;

		trainingSessionId = request.getData("id", int.class);

		developerId = request.getPrincipal().getActiveRoleId();

		TrainingSession object = this.repository.findTrainingSessionById(trainingSessionId);

		status = object != null && request.getPrincipal().hasRole(Developer.class) && object.getTrainingModule().getDeveloper().getId() == developerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startDate", "endDate", "location", "instructor", "contactEmail", "optionalLink");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		TrainingModule existingTM;

		existingTM = this.repository.findTrainingModuleById(object.getTrainingModule().getId());

		if (!(super.getBuffer().getErrors().hasErrors("endDate") || super.getBuffer().getErrors().hasErrors("startDate"))) {
			Date minimunDuration;
			Date minimunDuration2;
			minimunDuration = MomentHelper.deltaFromMoment(existingTM.getCreationMoment(), 7, ChronoUnit.DAYS);
			minimunDuration2 = MomentHelper.deltaFromMoment(object.getStartDate(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartDate(), minimunDuration), "startDate", "developer.training-session.form.error.invalid-start-date");
			super.state(MomentHelper.isAfterOrEqual(object.getEndDate(), minimunDuration2), "endDate", "developer.training-session.form.error.invalid-end-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("trainingModule"))
			super.state(existingTM != null && existingTM.isDraftMode() && !existingTM.getProject().isDraftMode(), "trainingModule", "developer.training-module.form.error.training-module-was-published");

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModuleCode", object.getTrainingModule().getCode());
		dataset.put("trainingModuleId", super.getRequest().getData("trainingModuleId", int.class));
		dataset.put("trainingModuleNotPublished", object.getTrainingModule().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
