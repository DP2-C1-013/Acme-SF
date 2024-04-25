
package acme.features.manager.projectuserstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryListService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectUserStoryRepository repository;

	// AbstractService<Manager, ProjectUserStory> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		final int managerId = super.getRequest().getPrincipal().getActiveRoleId();
		Collection<ProjectUserStory> projectUserStories = this.repository.findProjectUserStoriesByManagerId(managerId);

		super.getBuffer().addData(projectUserStories);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Dataset dataset = super.unbind(object, "project", "userStory");

		super.getResponse().addData(dataset);
	}
}
