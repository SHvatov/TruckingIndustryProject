<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Login failed!</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    </head>
    <body>
        <table class="myTableStyle">
            <tr>
                <th>Login Failed!</th>
            </tr>
            <tr>
                <button class="myCancelButton"
                        onclick="window.location.href = '${pageContext.request.contextPath}/login/login'">
                    Return Back
                </button>
            </tr>
        </table>
    </body>
</html>
