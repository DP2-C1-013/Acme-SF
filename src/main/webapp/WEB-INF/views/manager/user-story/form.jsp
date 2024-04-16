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

<acme:form>
	<acme:input-textbox code="manager.userstory.form.label.title" path="title"/>
	<acme:input-textarea code="manager.userstory.form.label.description" path="description"/>
	<acme:input-money code="manager.userstory.form.label.estimatedcost" path="estimatedCost"/>
	<acme:input-textarea code="manager.userstory.form.label.acceptancecriteria" path="acceptanceCriteria"/>
	<acme:input-select code="manager.userstory.form.label.priority" path="priority" choices="${priorities}"/>
	<acme:input-url code="manager.userstory.form.label.link" path="link"/>
	<acme:input-textbox code="manager.userstory.form.label.draftmode" path="draftMode" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.userstory.form.button.create" action="/manager/user-story/create?projectId=${projectId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="manager.userstory.form.button.publish" action="/manager/user-story/publish"/>
			<acme:submit code="manager.userstory.form.button.update" action="/manager/user-story/update"/>
			<acme:submit code="manager.userstory.form.button.delete" action="/manager/user-story/delete"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>