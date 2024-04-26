
package acme.features.developer.trainingsession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

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
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModule", object.getTrainingModule().getCode());
		dataset.put("trainingModuleDraftMode", object.getTrainingModule().isDraftMode());
		dataset.put("projectDraftMode", object.getTrainingModule().getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
