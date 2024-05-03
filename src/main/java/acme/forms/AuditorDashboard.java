
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer				numStaticCodeAudits;

	@NotNull
	public Integer				numDynamicCodeAudits;

	private Double				averageNumRecordsPerAudit;
	private Double				deviationNumRecordsPerAudit;
	private Integer				minNumRecords;
	private Integer				maxNumRecords;

	private Double				averageRecordPeriod;
	private Double				deviationRecordPeriod;
	private Double				minRecordPeriod;
	private Double				maxRecordPeriod;

}
