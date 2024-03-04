
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer				numInvoicesLessTax;

	@NotNull
	public Integer				numLinkedSponsorship;

	@NotNull
	public Double				averageAmountSponsorship;

	@NotNull
	public Double				deviationAmountSponsorship;

	@NotNull
	public Double				minAmountSponsorship;

	@NotNull
	public Double				maxAmountSponsorship;

	@NotNull
	public Double				averageQuantityInvoices;

	@NotNull
	public Double				deviationQuantityInvoices;

	@NotNull
	public Double				minQuantityInvoices;

	@NotNull
	public Double				maxQuantityInvoices;

}
