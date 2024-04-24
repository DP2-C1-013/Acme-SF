
package acme.features.administrator.systemconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.systemconfiguration.SystemConfiguration;

@Service
public class AdministratorSystemConfigurationUpdateService extends AbstractService<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		final boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		final SystemConfiguration systemConfiguration = this.repository.findSystemConfigurationsById(id);

		super.getBuffer().addData(systemConfiguration);
	}

	@Override
	public void bind(final SystemConfiguration object) {
		assert object != null;

		super.bind(object, "currency", "acceptedCurrencies");
	}

	@Override
	public void validate(final SystemConfiguration object) {
		assert object != null;
	}

	@Override
	public void perform(final SystemConfiguration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final SystemConfiguration object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "currency", "acceptedCurrencies");

		super.getResponse().addData(dataset);
	}
}
