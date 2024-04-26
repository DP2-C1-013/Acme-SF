
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import acme.datatypes.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotNull
	private Integer				totalNumProgressLogLessThan25;
	@NotNull
	private Integer				totalNumProgressLogLessBetween25And50;
	@NotNull
	private Integer				totalNumProgressLogLessBetween50And75;
	@NotNull
	private Integer				totalNumProgressLogAbove75;
	@NotNull
	private Statistics			contractTimeStatistics;
}
