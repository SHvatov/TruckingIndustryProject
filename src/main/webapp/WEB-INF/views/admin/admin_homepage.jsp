<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Admin Homepage</title>
    </head>
    <body>
        <header>
            <nav>
                <span>Welcome! </span>
                <span><a href="<c:url value="/admin/add_operator"/>">ADD NEW OPERATOR</a></span>
                <span><a href="<c:url value="/admin/add_driver"/>">ADD NEW DRIVER</a></span>
            </nav>
        </header>
        <table>
            <tr>
                <td>#</td>
                <td>UID</td>
                <td>Authority</td>
            </tr>
            <c:forEach var="user" items="${usersList}" varStatus="cnt">
                <tr>
                    <td>${cnt.count}</td>
                    <td>${user.uniqueIdentificator}</td>
                    <td>${user.authority.toString()}</td>
                    <td>
                        <c:url value="/admin/edit_user" var="edit_url">
                            <c:param name="uid" value="${user.uniqueIdentificator}"/>
                        </c:url>
                        <a href="${edit_url}">EDIT</a>
                    </td>
                    <td>
                        <c:url value="/admin/delete_user" var="delete_url">
                            <c:param name="uid" value="${user.uniqueIdentificator}"/>
                        </c:url>
                        <a href="${delete_url}">DELETE</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
