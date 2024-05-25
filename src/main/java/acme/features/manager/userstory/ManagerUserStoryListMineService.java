
package acme.features.manager.userstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListMineService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerUserStoryRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		final int projectId = super.getRequest().getData("projectId", int.class);
		Project project = this.repository.findOneProjectById(projectId);

		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();

		final boolean authorise = project != null && principal.hasRole(Manager.class) && project.getManager().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(authorise);
	}

	@Override
	public void load() {
		final int managerId = super.getRequest().getPrincipal().getAccountId();
		Collection<UserStory> userStories = this.repository.findManyUserStoriesByManagerId(managerId);

		super.getBuffer().addData(userStories);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		final Dataset dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");

		super.getResponse().addData(dataset);
	}
}
