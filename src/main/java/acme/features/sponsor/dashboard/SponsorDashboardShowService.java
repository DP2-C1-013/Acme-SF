
package acme.features.sponsor.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.datatypes.Statistics;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SponsorDashboard dashboard;
		int sponsorId;
		Integer numInvoicesLessTax;
		Integer numLinkedSponsorship;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		numInvoicesLessTax = this.repository.numOfInvoicesLessThan21Tax(sponsorId);
		numLinkedSponsorship = this.repository.numOfLinkedSponsorships(sponsorId);

		final Map<String, Statistics> sponsorshipCurrencyStatistics = new HashMap<>();

		final Map<String, Statistics> invoiceCurrencyStatistics = new HashMap<>();

		for (String currency : this.repository.findSystemCurrencies().get(0).getAcceptedCurrencies().split(",")) {
			Double averageCostSponsorship = this.repository.averageAmountSponsorship(sponsorId, currency);
			Double deviationCostSponsorship = this.repository.deviationAmountSponsorship(sponsorId, currency);
			Double minCostSponsorship = this.repository.minAmountSponsorship(sponsorId, currency);
			Double maxCostSponsorship = this.repository.maxAmountSponsorship(sponsorId, currency);

			Double averageCostInvoice = this.repository.averageQuantityInvoice(sponsorId, currency);
			Double deviationCostInvoice = this.repository.deviationQuantityInvoice(sponsorId, currency);
			Double minCostInvoice = this.repository.minQuantityInvoice(sponsorId, currency);
			Double maxCostInvoice = this.repository.maxQuantityInvoice(sponsorId, currency);

			final Statistics sponsorshipStatistics = new Statistics();
			sponsorshipStatistics.setAverage(averageCostSponsorship);
			sponsorshipStatistics.setDeviation(deviationCostSponsorship);
			sponsorshipStatistics.setMaximum(maxCostSponsorship);
			sponsorshipStatistics.setMinimum(minCostSponsorship);

			final Statistics invoiceStatistics = new Statistics();
			invoiceStatistics.setAverage(averageCostInvoice);
			invoiceStatistics.setDeviation(deviationCostInvoice);
			invoiceStatistics.setMaximum(maxCostInvoice);
			invoiceStatistics.setMinimum(minCostInvoice);

			sponsorshipCurrencyStatistics.put(currency, sponsorshipStatistics);
			invoiceCurrencyStatistics.put(currency, invoiceStatistics);

		}

		dashboard = new SponsorDashboard();
		dashboard.setNumInvoicesLessTax(numInvoicesLessTax);
		dashboard.setNumLinkedSponsorship(numLinkedSponsorship);
		dashboard.setSponsorshipCostStatistics(sponsorshipCurrencyStatistics);
		dashboard.setInvoiceCostStatistics(invoiceCurrencyStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numInvoicesLessTax", "numLinkedSponsorship", // 
			"sponsorshipCostStatistics", "invoiceCostStatistics");

		super.getResponse().addData(dataset);
	}
}
