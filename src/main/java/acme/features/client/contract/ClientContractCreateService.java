
package acme.features.client.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.project.Project;
import acme.roles.client.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;


	// AbstractService interface ----------------------------------------------	
	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		Client client;

		client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Contract();
		object.setDraftMode(true);
		object.setClient(client);

		super.getBuffer().addData(object);
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findOneContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() > 0, "budget", "client.form.error.negative-amount");
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		Project project;

		project = super.getRequest().getData("project", Project.class);

		if (project != null)
			project = this.repository.findOneProjectByCode(project.getCode());

		super.bind(object, "code", "instantiationMoment", "providerName", "customerName", "goal", "budget");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		SelectChoices projects;
		Dataset dataset;

		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeTrue(), "code", object.getProject());
		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goal", "budget");
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
