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

<acme:form readonly="false">
	<acme:input-textbox code="auditor.codeaudit.form.label.code" path="code" placeholder="XXX-000"/>
	<acme:input-moment code="auditor.codeaudit.form.label.executionDate" path="executionDate"/>	
	<acme:input-select code="auditor.codeaudit.form.label.type" path="type" choices="${types}"/>
	<acme:input-textbox code="auditor.codeaudit.form.label.correctiveActions" path="correctiveActions"/>
	<acme:input-textbox code="auditor.codeaudit.form.label.mark" path="mark"/>
	<acme:input-url code="auditor.codeaudit.form.label.link" path="link"/>
	<acme:input-select code="auditor.codeaudit.form.label.project" path="project" choices="${projects}"/>
	<acme:input-checkbox code="auditor.codeaudit.form.label.draftMode" path="draftMode" readonly="true"/>
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="auditor.codeaudit.form.button.delete" action="/auditor/code-audit/delete"/>
			<acme:submit code="auditor.codeaudit.form.button.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeaudit.form.button.publish" action="/auditor/code-audit/publish"/>
			<acme:button code="auditor.codeaudit.form.button.invoices" action="/auditor/invoice/list?code-auditId=${id}"/>			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.codeaudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>