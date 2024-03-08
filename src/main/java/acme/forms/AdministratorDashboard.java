
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {
	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------
	@NotNull
	public Integer				totalManagerPrincipals;

	@NotNull
	public Integer				totalSponsorsPrincipals;

	@NotNull
	public Double				noticeEmailLinkRatio;

	@NotNull
	public Double				criticalObjectiveRatio;

	@NotNull
	public Double				nonCriticalObjectiveRatio;

	@NotNull
	public Double				averageRiskValue;

	@NotNull
	public Double				riskValueDeviaton;

	@NotNull
	public Double				minRiskValue;

	@NotNull
	public Double				maxRiskValue;

	@NotNull
	public Double				averageClaimsLast10Weeks;

	@NotNull
	public Double				claimsLast10WeeksDeviation;

	@NotNull
	public Integer				minClaimsLast10Weeks;

	@NotNull
	public Integer				maxClaimsLast10Weeks;

}
