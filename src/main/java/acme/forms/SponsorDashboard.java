
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import acme.client.data.datatypes.Money;
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
	public Money				averageAmountSponsorship;

	@NotNull
	public Money				deviationAmountSponsorship;

	@NotNull
	public Money				minAmountSponsorship;

	@NotNull
	public Money				maxAmountSponsorship;

	@NotNull
	public Money				averageQuantityInvoices;

	@NotNull
	public Money				deviationQuantityInvoices;

	@NotNull
	public Money				minQuantityInvoices;

	@NotNull
	public Money				maxQuantityInvoices;

}
