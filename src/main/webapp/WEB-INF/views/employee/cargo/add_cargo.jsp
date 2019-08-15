<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add New Truck</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/cargo.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
</head>
<body>
<table class="myTableStyle">
    <tr>
        <th colspan="3">
            Add new driver
        </th>
    </tr>
    <tr>
        <td>Name</td>
        <td><label><input type="text" id="cargoNameInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="cargoNameError"></td>
    </tr>
    <tr>
        <td>Mass</td>
        <td><label><input type="text" id="cargoMassInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="cargoMassError"></td>
    </tr>
    <tr>
        <td>
            <button class="myCancelButton"
                    onclick="window.location.href = '${pageContext.request.contextPath}/employee/homepage'">
                Cancel
            </button>
        </td>
        <td>
            <button class="myRegularButton"
                    onclick="add_cargo('${pageContext.request.contextPath}')">
                Send
            </button>
        </td>
    </tr>
</table>
</body>
</html>
