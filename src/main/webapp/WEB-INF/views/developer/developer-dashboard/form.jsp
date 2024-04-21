<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="developer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.number-tm-with-update-moment"/>
		</th>
		<td>
			<acme:print value="${numTrainingModulesUpdateMoment}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.number-ts-with-link"/>
		</th>
		<td>
			<acme:print value="${numTrainingSessionsLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.average-tm-estimated-time"/>
		</th>
		<td>
			<acme:print value="${averageTimeTrainingModules}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.deviation-tm-estimated-time"/>
		</th>
		<td>
			<acme:print value="${deviationTimeTrainingModules}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.minimum-estimated-time"/>
		</th>
		<td>
			<acme:print value="${minimumTimeTrainingModules}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.maximum-estimated-time"/>
		</th>
		<td>
			<acme:print value="${maximumTimeTrainingModules}"/>
		</td>
	</tr>			
</table>

<acme:return/>
