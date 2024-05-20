
package acme.features.developer.trainingsession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionCreateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;
		int developerId;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		developerId = super.getRequest().getPrincipal().getActiveRoleId();

		Collection<TrainingSession> trainingSessions = this.repository.findTrainingSessionsByTMId(trainingModuleId);

		boolean validTrainingModule = trainingSessions.stream().allMatch(tm -> tm.getTrainingModule().getDeveloper().getId() == developerId);

		status = trainingModule != null && super.getRequest().getPrincipal().hasRole(Developer.class) && developerId == trainingModule.getDeveloper().getId() && validTrainingModule;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		object = new TrainingSession();
		object.setTrainingModule(trainingModule);
		object.setDraftMode(true);

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

		int trainingModuleId;
		TrainingModule existingTM;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		existingTM = this.repository.findTrainingModuleById(trainingModuleId);

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession ts;
			ts = this.repository.findTrainingSessionByCode(object.getCode());
			super.state(ts == null, "code", "developer.training-session.form.error.duplicated-code");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDate")) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(existingTM.getCreationMoment(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartDate(), minimunDuration), "startDate", "developer.training-session.form.error.invalid-start-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("endDate")) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getStartDate(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getEndDate(), minimunDuration), "endDate", "developer.training-session.form.error.invalid-end-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("trainingModule"))
			super.state(existingTM != null && existingTM.isDraftMode() && !existingTM.getProject().isDraftMode(), "trainingModule", "developer.training-module.form.error.training-module-was-published");

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModuleId", super.getRequest().getData("trainingModuleId", int.class));
		dataset.put("trainingModuleNotPublished", object.getTrainingModule().isDraftMode());
		dataset.put("projectPublished", !object.getTrainingModule().getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
