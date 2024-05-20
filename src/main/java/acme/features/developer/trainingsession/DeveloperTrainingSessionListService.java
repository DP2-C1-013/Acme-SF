
package acme.features.developer.trainingsession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionListService extends AbstractService<Developer, TrainingSession> {

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
		Collection<TrainingSession> objects;
		int trainingModuleId;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		objects = this.repository.findTrainingSessionsByTMId(trainingModuleId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "instructor", "draftMode");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrainingSession> objects) {
		assert objects != null;

		int trainingModuleId;
		TrainingModule trainingModule;
		final boolean showCreate;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		showCreate = !trainingModule.getProject().isDraftMode() && trainingModule.isDraftMode() && super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().addGlobal("trainingModuleId", trainingModuleId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
