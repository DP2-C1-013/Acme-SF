
package acme.features.manager.projectuserstory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryShowService extends AbstractService<Manager, ProjectUserStory> {

	@Autowired
	private ManagerProjectUserStoryRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		boolean status;
		Manager manager;
		ProjectUserStory assignment;

		assignment = this.repository.findOneProjectUserStoryById(id);
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = assignment != null && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ProjectUserStory assignment = this.repository.findOneProjectUserStoryById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Dataset dataset = super.unbind(object, "userStory");

		super.getResponse().addData(dataset);
	}
}
