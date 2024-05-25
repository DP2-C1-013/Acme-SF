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

<acme:form readonly="true">
	<acme:input-textbox code="authenticated.training-module.form.label.code" path="code"/>
	<acme:input-moment code="authenticated.training-module.form.label.creationMoment" path="creationMoment"/>
	<acme:input-textarea code="authenticated.training-module.form.label.details" path="details"/>
	<acme:input-textarea code="authenticated.training-module.form.label.difficultyLevel" path="difficultyLevel"/>
	<acme:input-moment code="authenticated.training-module.form.label.updateMoment" path="updateMoment"/>
	<acme:input-url code="authenticated.training-module.form.label.optionalLink" path="optionalLink"/>
	<acme:input-textbox code="authenticated.training-module.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-textbox code="authenticated.training-module.form.label.project" path="project"/>
	<acme:input-textbox code="authenticated.training-module.form.label.developer" path="developer"/>
</acme:form>
