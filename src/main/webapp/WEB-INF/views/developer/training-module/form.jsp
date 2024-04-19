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
	<acme:input-textbox code="developer.trainingModule.form.label.code" path="code"/>
	<acme:input-moment code="developer.trainingModule.form.label.creationMoment" path="creationMoment"/>
	<acme:input-textarea code="developer.trainingModule.form.label.details" path="details"/>
	<acme:input-select code="developer.trainingModule.form.label.difficultyLevel" path="difficultyLevel" choices="${difficultyLevels}"/>
	<acme:input-moment code="developer.trainingModule.form.label.updateMoment" path="updateMoment"/>
	<acme:input-url code="developer.trainingModule.form.label.optionalLink" path="optionalLink"/>
	<acme:input-integer code="developer.trainingModule.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-select code="developer.trainingModule.form.label.project" path="project" choices="${projects}"/>
	<acme:input-checkbox code="developer.trainingModule.form.label.draftMode" path="draftMode" readonly="true"/>
</acme:form>