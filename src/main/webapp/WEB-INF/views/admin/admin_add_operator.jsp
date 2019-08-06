<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Create new operator</title>
    </head>
    <body>
        <h1>New operator</h1>
        <form:form action="${pageContext.request.contextPath}/admin/save_operator" modelAttribute="userDto">
            <form:label path ="uniqueIdentificator">UID</form:label>
            <form:input path="uniqueIdentificator" />

            <form:label path="password">Password</form:label>
            <form:input path="password" />

            <input type="submit" value="Save"/>
        </form:form>
    </body>
</html>
