
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerCreateService extends AbstractService<Administrator, Banner> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBannerRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		final boolean status = super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Banner banner = new Banner();

		final Date instantiation = MomentHelper.getCurrentMoment();

		banner.setInstantiationMoment(instantiation);

		super.getBuffer().addData(banner);
	}
	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "startDisplay", "endDisplay", "picture", "slogan", "link");

	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		Date minDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");
		Date maxDate = MomentHelper.parse("2200/12/31 23:59", "yyyy/MM/dd HH:mm");

		if (!(super.getBuffer().getErrors().hasErrors("startDisplay") || super.getBuffer().getErrors().hasErrors("instantiationMoment"))) {
			super.state(MomentHelper.isAfterOrEqual(object.getInstantiationMoment(), minDate) && MomentHelper.isBeforeOrEqual(object.getInstantiationMoment(), maxDate), "instantiationMoment", "administrator.banner.form.error.moment-out-of-range");
			super.state(MomentHelper.isAfterOrEqual(object.getStartDisplay(), minDate) && MomentHelper.isBeforeOrEqual(object.getStartDisplay(), maxDate), "startDisplay", "administrator.banner.form.error.start-out-of-range");

			super.state(MomentHelper.isAfterOrEqual(object.getStartDisplay(), object.getInstantiationMoment()), "startDisplay", "administrator.banner.form.error.invalid-start");
		}

		if (!(super.getBuffer().getErrors().hasErrors("endDisplay") || super.getBuffer().getErrors().hasErrors("startDisplay"))) {
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getStartDisplay(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getEndDisplay(), minimunDuration), "endDisplay", "administrator.banner.form.error.invalid-end");

			super.state(MomentHelper.isAfterOrEqual(object.getEndDisplay(), minDate) && MomentHelper.isBeforeOrEqual(object.getEndDisplay(), maxDate), "endDisplay", "administrator.banner.form.error.end-out-of-range");
		}
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "startDisplay", "endDisplay", "picture", "slogan", "link");
		dataset.put("instantiation", object.getInstantiationMoment());

		super.getResponse().addData(dataset);
	}

}
