<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sergey Khvatov
  Date: 01.08.2019
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>
</head>
<body>
<h1>Admin homepage</h1>
<a href="${pageContext.request.contextPath}/employee/homepage">click1</a>
<a href="<c:url value="/driver/homepage"/>">click2</a>

</body>
</html>
