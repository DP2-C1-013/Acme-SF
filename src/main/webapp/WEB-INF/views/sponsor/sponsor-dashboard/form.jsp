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
	<acme:message code="sponsor.dashboard.form.title.general-indicators" />
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message
				code="sponsor.dashboard.form.label.number-invoices-less-than-21-tax" /></th>
		<td><acme:print value="${numInvoicesLessTax}" /></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="sponsor.dashboard.form.label.number-linked-sponsorships" /></th>
		<td><acme:print value="${numLinkedSponsorship}" /></td>
	</tr>
</table>

<h2>
	<acme:message code="sponsor.dashboard.form.title.statistics" />
</h2>

<h3>
	<acme:message
		code="sponsor.dashboard.form.label.statistics-sponsorship" />
</h3>

<table class="table table-sm">
	<tr>
		<th><acme:message
				code="sponsor.dashboard.form.label.statistics-currency" /></th>
		<th><acme:message
				code="sponsor.dashboard.form.label.average-amount" /></th>
		<th><acme:message 
				code="sponsor.dashboard.form.label.deviation-amount" /></th>
		<th><acme:message
				code="sponsor.dashboard.form.label.min-amount" /></th>
		<th><acme:message
				code="sponsor.dashboard.form.label.max-amount" /></th>
	</tr>
	<jstl:forEach var="entry" items="${sponsorshipCostStatistics}">
		<tr>
			<td><acme:print value="${entry.key}" /></td>
			<td><acme:print value="${entry.value.getAverage()}" /></td>
			<td><acme:print value="${entry.value.getDeviation()}" /></td>
			<td><acme:print value="${entry.value.getMinimum()}" /></td>
			<td><acme:print value="${entry.value.getMaximum()}" /></td>
		</tr>
	</jstl:forEach>
</table>

<h3>
	<acme:message
		code="sponsor.dashboard.form.label.statistics-invoice" />
</h3>

<table class="table table-sm">
	<tr>
		<th><acme:message
				code="sponsor.dashboard.form.label.statistics-currency" /></th>
		<th><acme:message
				code="sponsor.dashboard.form.label.average-amount" /></th>
		<th><acme:message
				code="sponsor.dashboard.form.label.deviation-amount" /></th>
		<th><acme:message 
				code="sponsor.dashboard.form.label.min-amount" />
		</th>
		<th><acme:message 
				code="sponsor.dashboard.form.label.max-amount" />
		</th>
	</tr>

	<jstl:forEach var="entry" items="${invoiceCostStatistics}">
		<tr>
			<td><acme:print value="${entry.key}" /></td>
			<td><acme:print value="${entry.value.getAverage()}" /></td>
			<td><acme:print value="${entry.value.getDeviation()}" /></td>
			<td><acme:print value="${entry.value.getMinimum()}" /></td>
			<td><acme:print value="${entry.value.getMaximum()}" /></td>
		</tr>
	</jstl:forEach>
</table>

<acme:return />
