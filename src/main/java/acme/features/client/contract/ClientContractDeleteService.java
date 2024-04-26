
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.progresslog.ProgressLog;
import acme.entities.project.Project;
import acme.roles.client.Client;

@Service
public class ClientContractDeleteService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------	


	@Override
	public void authorise() {
		boolean status;
		int clientId;
		int contractId;

		contractId = super.getRequest().getData("id", int.class);

		clientId = super.getRequest().getPrincipal().getActiveRoleId();

		Contract object = this.repository.findOneContractById(contractId);

		status = object != null && super.getRequest().getPrincipal().hasRole(Client.class) && object.getClient().getId() == clientId && object.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		Project project;

		project = super.getRequest().getData("project", Project.class);

		super.bind(object, "code", "instantationMoment", "providerName", "customerName", "goals", "budget");
		object.setProject(project);
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		boolean status;
		status = object.getDraftMode();

		super.state(status, "*", "client.contract.delete.is-draftMode");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		Collection<ProgressLog> progressLogs = this.repository.findManyProgressLogsByContractId(object.getId());

		this.repository.deleteAll(progressLogs);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		SelectChoices projects;
		Dataset dataset;

		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeTrue(), "code", object.getProject());
		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget");
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

}
