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
	<acme:message code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.number-must-user-stories"/>
		</th>
		<td>
			<acme:print value="${numOfMustUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.number-should-user-stories"/>
		</th>
		<td>
			<acme:print value="${numOfShouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.number-could-user-stories"/>
		</th>
		<td>
			<acme:print value="${numOfCouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.number-wont-user-stories"/>
		</th>
		<td>
			<acme:print value="${numOfWontUserStories}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.average-estimated-cost"/>
		</th>
		<td>
			<acme:print value="${averageEstimatedCost}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.deviation-estimated-cost"/>
		</th>
		<td>
			<acme:print value="${deviationEstimatedCost}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.min-estimated-cost"/>
		</th>
		<td>
			<acme:print value="${minEstimatedCost}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.max-estimated-cost"/>
		</th>
		<td>
			<acme:print value="${maxEstimatedCost}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.average-cost"/>
		</th>
		<td>
			<acme:print value="${averageCost}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.deviation-cost"/>
		</th>
		<td>
			<acme:print value="${deviationCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.min-cost"/>
		</th>
		<td>
			<acme:print value="${minCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.max-cost"/>
		</th>
		<td>
			<acme:print value="${maxCost}"/>
		</td>
	</tr>		
</table>

<acme:return/>

