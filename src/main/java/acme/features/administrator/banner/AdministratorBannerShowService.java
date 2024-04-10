
package acme.features.administrator.banner;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

public class AdministratorBannerShowService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository repository;

	// AdministratorService<Administrator, Banner> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneBannerById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "startDisplay", "endDisplay", "picture", "slogan", "link");

		super.getResponse().addData(dataset);
	}
}
