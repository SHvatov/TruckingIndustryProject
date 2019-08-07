<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add New Truck</title>
    </head>
    <body>
        <h2>Add new truck:</h2>
        <form action='<c:url value="${pageContext.request.contextPath}/employee/add_truck"/>' method="post">
            <table>
                <tr><td>Username</td><td><input type="text" name="username"></td></tr>
                <tr><td>Password</td><td><input type="password" name="password"></td></tr>
                <tr><td><button type="submit">Log in</button></td></tr>
            </table>
        </form>
    </body>
</html>
