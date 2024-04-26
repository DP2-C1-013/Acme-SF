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
	<acme:message code="auditor.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.num-static-code-audits"/>
		</th>
		<td>
			<acme:print value="${numStaticCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.num-dynamic-code-audits"/>
		</th>
		<td>
			<acme:print value="${numDynamicCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.average-number-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${averageNumRecordsPerAudit}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.standar-deviation-of-number-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${deviationNumRecordsPerAudit}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.minimum-number-of-audit-records"/>
		</th>
		<td>
			<acme:print value="${minNumRecords}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.maximum-number-of-audit-records"/>
		</th>
		<td>
			<acme:print value="${maxNumRecords}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.average-period-of-audit-records"/>
		</th>
		<td>
			<acme:print value="${averageRecordPeriod}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.standar-deviation-of-period-of-audit-records"/>
		</th>
		<td>
			<acme:print value="${deviationRecordPeriod}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.minimum-period"/>
		</th>
		<td>
			<acme:print value="${minRecordPeriod}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.maximum-period"/>
		</th>
		<td>
			<acme:print value="${maxRecordPeriod}"/>
		</td>
	</tr>
</table>

<acme:return/>
