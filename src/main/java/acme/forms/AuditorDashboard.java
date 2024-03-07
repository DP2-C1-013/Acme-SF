
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	@NotNull
	public Integer	numCodeAuditsStaticOrDinamyc;

	@NotNull
	public Double	averageNumAuditRecords;

	@NotNull
	public Double	deviationNumAuditRecords;

	@NotNull
	public Double	minNumAuditRecords;

	@NotNull
	public Double	maxNumAuditRecords;

	@NotNull
	public Double	averagePeriodLength;

	@NotNull
	public Double	deviationPeriodLength;

	@NotNull
	public Double	minPeriodLength;

	@NotNull
	public Double	maxPeriodLength;

}
