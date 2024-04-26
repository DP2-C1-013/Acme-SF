
package acme.features.auditor.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.AuditorDashboard;
import acme.roles.Auditor;

@Service
public class AuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditorDashboard dashboard = new AuditorDashboard();
		int auditorId;
		Integer numLinkedCodeAudit;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();

		Integer numStaticCodeAudits = this.repository.numStaticCodeAudits(auditorId);
		Integer numDynamicCodeAudits = this.repository.numDynamicCodeAudits(auditorId);

		Double averageNumRecordsPerAudit = this.repository.averageAuditingRecords(auditorId);
		//Double deviationNumRecordsPerAudit = this.repository.deviationAuditingRecords(auditorId);
		Integer minNumRecords = this.repository.minAuditingRecords(auditorId);
		Integer maxNumRecords = this.repository.maxAuditingRecords(auditorId);

		Double averageRecordPeriod = this.repository.averageRecordPeriod(auditorId);
		Double deviationRecordPeriod = this.repository.deviationRecordPeriod(auditorId);
		Double minRecordPeriod = this.repository.minRecordPeriod(auditorId);
		Double maxRecordPeriod = this.repository.maxRecordPeriod(auditorId);

		dashboard.setNumStaticCodeAudits(numStaticCodeAudits);
		dashboard.setNumDynamicCodeAudits(numDynamicCodeAudits);

		dashboard.setAverageNumRecordsPerAudit(averageNumRecordsPerAudit);
		//dashboard.setDeviationNumRecordsPerAudit(deviationNumRecordsPerAudit);
		dashboard.setMinNumRecords(minNumRecords);
		dashboard.setMaxNumRecords(maxNumRecords);

		dashboard.setAverageRecordPeriod(averageRecordPeriod);
		dashboard.setDeviationRecordPeriod(deviationRecordPeriod);
		dashboard.setMinRecordPeriod(minRecordPeriod);
		dashboard.setMaxRecordPeriod(maxRecordPeriod);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numStaticCodeAudits", "numDynamicCodeAudits", // 
			"averageNumRecordsPerAudit", "minNumRecords", "maxNumRecords", //
			"averageRecordPeriod", "deviationRecordPeriod", "minRecordPeriod", "maxRecordPeriod");

		super.getResponse().addData(dataset);
	}
}
