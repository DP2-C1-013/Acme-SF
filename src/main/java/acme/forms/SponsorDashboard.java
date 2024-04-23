
package acme.forms;

import java.util.Map;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import acme.datatypes.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialization indentifier --------------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer					numInvoicesLessTax;

	@NotNull
	public Integer					numLinkedSponsorship;

	//	Average, deviation, minimum, and maximum estimated cost of the sponsorships mapped by currency
	private Map<String, Statistics>	sponsorshipCostStatistics;

	//	Average, deviation, minimum, and maximum estimated cost of the invoices mapped by currency
	private Map<String, Statistics>	invoiceCostStatistics;

}
