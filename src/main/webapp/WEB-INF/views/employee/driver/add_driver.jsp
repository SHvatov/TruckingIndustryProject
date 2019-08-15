<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add New Truck</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/driver.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
</head>
<body>
<script>
    $(document).ready(function () {
        load_city_list('${pageContext.request.contextPath}', 'driverCitySelect');
    });
</script>
<table class="myTableStyle">
    <tr>
        <th colspan="3">
            Add new driver
        </th>
    </tr>
    <tr>
        <td>Unique Identificator</td>
        <td><label><input type="text" id="driverUniqueIdentificatorInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="driverUniqueIdentificatorError"></td>
    </tr>
    <tr>
        <td>Name</td>
        <td><label><input type="text" id="driverNameInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="driverNameError"></td>
    </tr>
    <tr>
        <td>Surname</td>
        <td><label><input type="text" id="driverSurnameInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="driverSurnameError"></td>
    </tr>
    <tr>
        <td>Password</td>
        <td><label><input type="text" id="driverPasswordInput"/></label></td>
        <td class="errorMessage" hidden="hidden" id="driverPasswordError"></td>
    </tr>
    <tr>
        <td>City</td>
        <td>
            <label>
                <select id="driverCitySelect">
                </select>
            </label>
        </td>
        <td class="errorMessage" hidden="hidden" id="driverCityError"></td>
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
                    onclick="add_driver('${pageContext.request.contextPath}')">
                Send
            </button>
        </td>
    </tr>
</table>
</body>
</html>
