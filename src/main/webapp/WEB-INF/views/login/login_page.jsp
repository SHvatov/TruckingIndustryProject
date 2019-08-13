<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
</head>
<body>
    <form action='<spring:url value="/login/perform_login"/>'
          method="post">
        <table class="myTableStyle" style="width:40%; margin-left:30%; margin-right:30%;">
            <tr><th colspan="2">Login</th></tr>
            <tr><td>Username</td></tr>
            <tr><td>
                <label><input type="text" name="username"></label>
            </td></tr>
            <tr><td>Password</td></tr>
            <tr><td>
                <label><input type="password" name="password"></label>
            </td></tr>
            <tr><td><button type="submit" class="myRegularButton" style="height: auto">Enter</button></td></tr>
        </table>
    </form>
</body>
</html>