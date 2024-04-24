
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.trainingmodule.DifficultyLevel;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleDeleteService extends AbstractService<Developer, TrainingModule> {

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
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		Project project;
		project = this.getRequest().getData("project", Project.class);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime");
		object.setProject(project);

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		boolean status;
		status = object.isDraftMode();

		super.state(status, "*", "developer.training-module.delete.is-draftMode");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		Collection<TrainingSession> trainingSessions = this.repository.findManyTrainingSessionsByTMId(object.getId());

		this.repository.deleteAll(trainingSessions);
		this.repository.delete(object);
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
}
