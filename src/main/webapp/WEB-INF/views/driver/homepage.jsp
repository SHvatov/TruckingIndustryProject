<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Employee homepage</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/driver/driver_utils.js"/>" type="text/javascript"></script>
</head>
<body>
<script>
    $(document).ready(function () {
        load_driver_info('${pageContext.request.contextPath}', '${driverUID}');
    });
</script>
<table class="myTableStyle">
    <tr>
        <th>
            Driver
        </th>
        <th>
            Unique Identificator
        </th>
        <th>
            Status
        </th>
        <th>
            Other Driver ID
        </th>
        <th>
            Truck
        </th>
        <th>
            Order
        </th>
        <th>
            Order Status
        </th>
    </tr>
    <tr id="driverInfoTable">
    </tr>
</table>

<table class="myTableStyle" id="wayPointInfoTable">
</table>

<table class="myTableStyle">
    <tr>
        <th rowspan='2'>
            Change Status
        </th>
    </tr>
    <tr>
        <td>
            <select id="driverStatusSelect">
                <option value="DRIVING">DRIVING</option>
                <option value="SECOND_DRIVER">SECOND DRIVER</option>
                <option value="LOADING_UNLOADING">LOADING / UNLOADING</option>
            </select>
        </td>
        <td>
            <button class="myRegularButton"
                    onclick="change_status('${pageContext.request.contextPath}', '${driverUID}')">
                Change Status
            </button>
        </td>
    </tr>
    <tr>
        <td colspan='1'>
            <button class="myCancelButton"
                    id="logoutButton"
                    onclick="window.location.href = '${pageContext.request.contextPath}/login/perform_logout'">
                Logout
            </button>
        </td>
        <td colspan='1'>
            <button class="myRegularButton"
                    onclick="change_session('${pageContext.request.contextPath}', '${driverUID}')">
                Start / End session
            </button>
        </td>
        <td colspan='1'>
            <button class="myRegularButton"
                    onclick="load_driver_info('${pageContext.request.contextPath}', '${driverUID}')">
                Refresh
            </button>
        </td>
    </tr>
</table>
</body>
</html>
