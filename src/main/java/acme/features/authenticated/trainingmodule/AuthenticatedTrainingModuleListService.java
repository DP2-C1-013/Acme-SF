
package acme.features.authenticated.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;

@Service
public class AuthenticatedTrainingModuleListService extends AbstractService<Authenticated, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedTrainingModuleRepository repository;

	// AbstractService<Authenticated, TrainingModule> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrainingModule> objects;
		objects = this.repository.findAllPublishedTrainingModule();
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "updateMoment", "difficultyLevel");

		super.getResponse().addData(dataset);
	}
}
