
package acme.features.developer.trainingmodule;

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
import acme.entities.trainingmodule.DifficultyLevel;
import acme.entities.trainingmodule.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int developerId;
		int trainingModuleId;

		trainingModuleId = request.getData("id", int.class);

		developerId = request.getPrincipal().getActiveRoleId();

		TrainingModule object = this.repository.findTrainingModuleById(trainingModuleId);

		status = object != null && request.getPrincipal().hasRole(Developer.class) && object.getDeveloper().getId() == developerId && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		Developer developer;

		developer = this.repository.findDeveloperById(this.getRequest().getPrincipal().getActiveRoleId());

		object = new TrainingModule();
		object.setDraftMode(true);
		object.setDeveloper(developer);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		Project project;

		project = this.getRequest().getData("project", Project.class);
		if (project != null)
			project = this.repository.findOneProjectByCode(project.getCode());

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;
			existing = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-module.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("updateMoment") && object.getUpdateMoment() != null) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getCreationMoment(), 1, ChronoUnit.SECONDS);
			super.state(MomentHelper.isAfterOrEqual(object.getUpdateMoment(), minimunDuration), "updateMoment", "developer.training-module.form.error.invalid-update-moment");
		}

		if (!super.getBuffer().getErrors().hasErrors("difficultyLevel")) {
			DifficultyLevel level = object.getDifficultyLevel();
			super.state(level.equals(DifficultyLevel.Basic) || level.equals(DifficultyLevel.Intermediate) || level.equals(DifficultyLevel.Advanced), "difficultyLevel", "developer.training-module.form.error.invalid-difficulty-level");
		}

		if (!super.getBuffer().getErrors().hasErrors("estimatedTotalTime"))
			super.state(object.getEstimatedTotalTime() > 1, "estimatedTotalTime", "developer.training-module.form.error.invalid-estimated-total-time");

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Project existingProject = this.repository.findOneProjectByCode(object.getProject().getCode());
			super.state(existingProject != null && existingProject.isDraftMode() && object.getProject().isDraftMode(), "project", "developer.training-module.form.error.invalid-project");
		}

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices difficultyLevels;
		SelectChoices projects;
		Dataset dataset;

		difficultyLevels = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeTrue(), "code", object.getProject());

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime", "draftMode");
		dataset.put("difficultyLevels", difficultyLevels);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
