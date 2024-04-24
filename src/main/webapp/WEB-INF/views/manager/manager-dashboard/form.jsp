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
	<acme:message code="manager.dashboard.form.title.general-indicators" />
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message
				code="manager.dashboard.form.label.number-must-user-stories" /></th>
		<td><acme:print value="${numOfMustUserStories}" /></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="manager.dashboard.form.label.number-should-user-stories" /></th>
		<td><acme:print value="${numOfShouldUserStories}" /></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="manager.dashboard.form.label.number-could-user-stories" /></th>
		<td><acme:print value="${numOfCouldUserStories}" /></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="manager.dashboard.form.label.number-wont-user-stories" /></th>
		<td><acme:print value="${numOfWontUserStories}" /></td>
	</tr>
</table>

<h2>
	<acme:message code="manager.dashboard.form.title.statistics" />
</h2>

<h3>
	<acme:message
		code="manager.dashboard.form.label.statistics-object-user-story" />
</h3>

<table class="table table-sm">
	<tr>
		<th><acme:message
				code="manager.dashboard.form.label.statistics-currency" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.average-estimated-cost" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.deviation-estimated-cost" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.max-estimated-cost" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.min-estimated-cost" /></th>
	</tr>
	<jstl:forEach var="entry" items="${userStoryEstimatedCostStatistics}">
		<tr>
			<td><acme:print value="${entry.key}" /></td>
			<td><acme:print value="${entry.value.getAverage()}" /></td>
			<td><acme:print value="${entry.value.getDeviation()}" /></td>
			<td><acme:print value="${entry.value.getMaximum()}" /></td>
			<td><acme:print value="${entry.value.getMinimum()}" /></td>
		</tr>
	</jstl:forEach>
</table>

<h3>
	<acme:message
		code="manager.dashboard.form.label.statistics-object-project" />
</h3>

<table class="table table-sm">
	<tr>
		<th><acme:message
				code="manager.dashboard.form.label.statistics-currency" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.average-cost" /></th>
		<th><acme:message
				code="manager.dashboard.form.label.deviation-cost" /></th>
		<th><acme:message code="manager.dashboard.form.label.max-cost" />
		</th>
		<th><acme:message code="manager.dashboard.form.label.min-cost" />
		</th>
	</tr>

	<jstl:forEach var="entry" items="${projectCostStatistics}">
		<tr>
			<td><acme:print value="${entry.key}" /></td>
			<td><acme:print value="${entry.value.getAverage()}" /></td>
			<td><acme:print value="${entry.value.getDeviation()}" /></td>
			<td><acme:print value="${entry.value.getMaximum()}" /></td>
			<td><acme:print value="${entry.value.getMinimum()}" /></td>
		</tr>
	</jstl:forEach>
</table>

<acme:return />

