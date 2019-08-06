<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Edit Users</title>
    </head>
    <body>
        <h1>Current User Data:</h1>
        <table>
            <tr>
                <td>UID</td>
                <td>Password</td>
                <td>Authority</td>
            </tr>
            <tr>
                <c:set var="user" value="${user}"/>
                <td><c:out value="${user.uniqueIdentificator}" /></td>
                <td><c:out value="${user.password}" /></td>
                <td><c:out value="${user.authority.toString()}" /></td>
            </tr>
        </table>

        <form:form method="post" action="/admin/save_user" modelAttribute="userDto">
            <form:label path ="uniqueIdentificator">UID</form:label>
            <form:input path="uniqueIdentificator"/>

            <form:label path="password">Password</form:label>
            <form:input path="password"/>

            <form:radiobutton path="authority" value="ROLE_ADMIN"/>Admin
            <form:radiobutton path="authority" value="ROLE_USER"/>Employee
            <form:radiobutton path="authority" value="ROLE_DRIVER"/>Driver

            <input type="submit" value="Update"/>
        </form:form>
    </body>
</html>
