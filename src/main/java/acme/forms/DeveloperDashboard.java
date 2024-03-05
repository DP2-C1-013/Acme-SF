
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	// Serialization identifier ----------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -----------------------------------------------------------------

	@NotNull
	public Integer				numTrainingModulesUpdateMoment;

	@NotNull
	public Integer				numTrainingSessionsLink;

	@NotNull
	public Double				averageTimeTrainingModules;

	@NotNull
	public Double				deviationTimeTrainingModules;

	@NotNull
	public Double				minimumTimeTrainingModules;

	@NotNull
	public Double				maximumTimeTrainingModules;
}
