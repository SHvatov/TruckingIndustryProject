<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create New Order</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/order.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/cargo.js"/>" type="text/javascript"></script>
</head>
<body>
<script>
    $(document).ready(function () {
        load_city_list('${pageContext.request.contextPath}', 'wayPointCitySelect');
        load_ready_cargo_list('${pageContext.request.contextPath}', 'wayPointCargoSelect');

        $("#addWayPointButton").click(function () {
            let text = "<tr class='addWayPointTr'><td>Waypoint</td>" +
                "<td class='wayPointCity'>" + $("#wayPointCitySelect").val() + "</td>" +
                "<td class='wayPointCargo'>" + $("#wayPointCargoSelect").val() + "</td>" +
                "<td class='wayPointAction'>" + $("#wayPointActionSelect").val() + "</td>" +
                "<td><button class='tableDeleteButton' onclick='remove_option(this)'>Remove</button></td>";
            $("#wayPointInputTr").before(text);
        });

        $("#addDriverButton").click(function () {
            let text = "<tr class='addDriverTr'><td>Driver</td>" +
                "<td class='orderDriver'>" + $("#orderDriversSelect").val() + "</td>" +
                "<td><button class='tableDeleteButton' onclick='remove_option(this)'>Remove</button></td>";
            $("#orderDriverInputTr").before(text);
        });

        $('#backToWaypoints').click(function () {
            $('#orderTrucksTable').hide();
            $('#waypointTable').show();
        });

        $('#backToTrucks').click(function () {
            $('#orderDriversTable').hide();
            $('#orderTrucksTable').show();
        });
    })
</script>

<table class="myTableStyle" id="waypointTable">
    <tr>
        <th colspan='5'>
            Create New Order
        </th>
    </tr>
    <tr>
        <td>UID</td>
        <td colspan='4'>
            <input type="text" id="orderUniqueIdentificatorInput"/>
        </td>
    </tr>
    <tr id="wayPointInputTr">
        <td>
            Way Point
        </td>
        <td>
            <select id="wayPointCitySelect">
            </select>
        </td>
        <td>
            <select id="wayPointCargoSelect">
            </select>
        </td>
        <td>
            <select id="wayPointActionSelect">
                <option value="LOADING">Loading</option>
                <option value="UNLOADING">Unloading</option>
            </select>
        </td>
        <td>
            <button id="addWayPointButton" class="myRegularButton">
                Add Waypoint
            </button>
        </td>
    </tr>
    <tr>
        <td colspan='2'>
            <button class="myCancelButton"
                    onclick="window.location.href = '${pageContext.request.contextPath}/employee/homepage'">
                Cancel
            </button>
        </td>
        <td colspan='3'>
            <button class="myRegularButton"
                    onclick="fetch_truck_list('${pageContext.request.contextPath}')">
                Find Trucks
            </button>
        </td>
    </tr>
</table>

<table class="myTableStyle" id="orderTrucksTable" hidden>
    <tr>
        <th colspan="3">Assign Truck</th>
    </tr>
    <tr>
        <td>Select Truck</td>
        <td>
            <select id="orderTruckSelect" size="3">
            </select>
        </td>
    </tr>
    <tr>
        <td>
            <button class="myCancelButton"
                    id="backToWaypoints">
                Back
            </button>
        </td>
        <td>
            <button class="myRegularButton"
                    onclick="fetch_driver_list('${pageContext.request.contextPath}')">
                Find Drivers
            </button>
        </td>
    </tr>
</table>

<table class="myTableStyle" id="orderDriversTable" hidden>
    <tr>
        <th colspan='3'>
            Assign Drivers
        </th>
    </tr>
    <tr id="orderDriverInputTr">
        <td>Select Drivers</td>
        <td>
            <select multiple id="orderDriversSelect" size="3">
            </select>
        </td>
        <td>
            <button id="addDriverButton" class="myRegularButton">
                Add Driver
            </button>
        </td>
    <tr>
        <td colspan='1'>
            <button class="myCancelButton"
                    id="backToTrucks">
                Back
            </button>
        </td>
        <td colspan='2'>
            <button class="myRegularButton"
                    onclick="add_order('${pageContext.request.contextPath}')">
                Save Order
            </button>
        </td>
    </tr>
</table>

</body>
</html>
