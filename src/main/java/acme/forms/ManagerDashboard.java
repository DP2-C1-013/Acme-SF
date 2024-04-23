
package acme.forms;

import java.util.Map;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import acme.datatypes.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {
	// Serialization indentifier --------------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer					numOfMustUserStories;

	@NotNull
	public Integer					numOfShouldUserStories;

	@NotNull
	public Integer					numOfCouldUserStories;

	@NotNull
	public Integer					numOfWontUserStories;

	//	Average, deviation, minimum, and maximum estimated cost of the user stories
	private Map<String, Statistics>	userStoryEstimatedCostStatistics;

	//	Average, deviation, minimum, and maximum estimated cost of the project
	private Map<String, Statistics>	projectCostStatistics;
}
