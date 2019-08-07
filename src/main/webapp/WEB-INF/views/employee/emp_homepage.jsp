<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Employee Homepage</title>
    </head>
    <body>
        <div>
            <div class="header-left">
                <h3>Main menu</h3>
            </div>
            <div>
                User: ${user}
                <input type="submit" value="Logout"
                       onclick="window.location='${pageContext.request.contextPath}/login/perform_logout/';"/>
            </div>
        </div>
        <h1>Available operations:</h1>
        <table>
            <tr>
                <td><input type="submit" value="LIST ALL TRUCKS"
                   onclick="window.location='${pageContext.request.contextPath}/employee/list_trucks';" /></td>
                <td><input type="submit" value="ADD TRUCK" formmethod="get"
                   onclick="window.location='${pageContext.request.contextPath}/employee/add_truck';" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="LIST ALL DRIVERS"
                               onclick="window.location='';" /></td>
                <td><input type="submit" value="ADD DRIVER"
                           onclick="window.location='';" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="LIST ALL ORDERS"
                               onclick="window.location='';" /></td>
                <td><input type="submit" value="ADD ORDER"
                           onclick="window.location='';" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="LIST ALL CARGO"
                               onclick="window.location='';" /></td>
                <td><input type="submit" value="ADD CARGO"
                           onclick="window.location='';" /></td>
            </tr>
        </table>
    </body>
</html>
