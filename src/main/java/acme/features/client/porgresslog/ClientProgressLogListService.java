
package acme.features.client.porgresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.progresslog.ProgressLog;
import acme.roles.client.Client;

@Service
public class ClientProgressLogListService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		int clientId;

		contractId = this.getRequest().getData("masterId", int.class);
		clientId = this.getRequest().getPrincipal().getActiveRoleId();

		Collection<ProgressLog> progressLogs = this.repository.findManyProgressLogByContractId(contractId);

		System.out.println("Progress Logs1: " + progressLogs);

		boolean validContractId = progressLogs.stream().allMatch(pl -> pl.getContract().getClient().getId() == clientId);

		status = super.getRequest().getPrincipal().hasRole(Client.class) && validContractId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int contractId;

		contractId = this.getRequest().getData("masterId", int.class);

		objects = this.repository.findManyProgressLogByContractId(contractId);
		System.out.println("Progress Logs2: " + objects);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ProgressLog> objects) {
		assert objects != null;

		int contractId;

		contractId = this.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", contractId);
	}

}
