
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {
	// Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer				numOfMustUserStories;

	@NotNull
	public Integer				numOfShouldUserStories;

	@NotNull
	public Integer				numOfCouldUserStories;

	@NotNull
	public Integer				numOfWontUserStories;

	@NotNull
	public Double				averageEstimatedCost;

	@NotNull
	public Double				deviationEstimatedCost;

	@NotNull
	public Double				minEstimatedCost;

	@NotNull
	public Double				maxEstimatedCost;

	@NotNull
	public Double				averageCost;

	@NotNull
	public Double				deviationCost;

	@NotNull
	public Double				minCost;

	@NotNull
	public Double				maxCost;
}
