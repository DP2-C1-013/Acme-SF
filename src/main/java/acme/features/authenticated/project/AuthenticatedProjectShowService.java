
package acme.features.authenticated.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;

@Service
public class AuthenticatedProjectShowService extends AbstractService<Authenticated, Project> {

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
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstractText", "hasFatalErrors", "cost", "link", "manager", "draftMode");

		super.getResponse().addData(dataset);
	}
}
