
package acme.features.authenticated.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class AuthenticatedProjectListService extends AbstractService<Manager, Project> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Project> objects;
		int userAccountId;
		int managerId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		managerId = this.repository.findOneManagerByUserAccountId(userAccountId).getId();
		objects = this.repository.findCreatedProjectsByManagerId(managerId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "cost");

		super.getResponse().addData(dataset);
	}

}
