
package acme.features.authenticated.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;

@Service
public class AuthenticatedProjectListService extends AbstractService<Authenticated, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedProjectRepository repository;

	// AbstractService<Authenticated, Claim> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Project> objects;
		objects = this.repository.findAllPublishedProjects();
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "cost", "manager", "draftMode");

		super.getResponse().addData(dataset);
	}
}
