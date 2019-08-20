<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Truck: [${truckId}]</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/truck.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
</head>
<body>
    <script>
        $(document).ready(function () {
            load_truck('${pageContext.request.contextPath}', '${truckId}');
        });
    </script>
    <table class="myTableStyle">
        <tr>
            <th colspan='4'>Truck [${truckId}] information</th>
        </tr>
        <tr>
            <td>Registration Number</td>
            <td id="uniqueIdentificatorTd" colspan="4"></td>
        </tr>
        <tr>
            <td>Driver shift size</td>
            <td id="shiftSizeTd"></td>
            <td hidden="hidden" id="editShiftSizeTd">
                <label><input type="text" id="editShiftSizeInput"/></label>
                <button class="tableEditButton"
                        onclick="update_truck_shift_size('${pageContext.request.contextPath}', '${truckId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick='hide_update_interface("editShiftSizeTd", "shiftSizeButton");'>
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="shiftSizeButton"
                        onclick='show_update_interface("editShiftSizeTd", "shiftSizeButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Capacity</td>
            <td id="capacityTd"></td>
            <td hidden="hidden" id="editCapacityTd">
                <label><input type="text" id="editCapacityInput"/></label>
                <button class="tableEditButton"
                        onclick="update_truck_capacity('${pageContext.request.contextPath}', '${truckId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick="hide_update_interface('editCapacityTd', 'capacityButton')">
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="capacityButton"
                        onclick="show_update_interface('editCapacityTd', 'capacityButton')">
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Condition</td>
            <td id="conditionTd"></td>
            <td hidden="hidden" id="editConditionTd">
                <label>
                    <select id="editConditionSelect">
                        <option value="IN_ORDER">In Order</option>
                        <option value="NOT_IN_ORDER">Not in Order</option>
                    </select>
                </label>
                <button class="tableEditButton"
                        onclick="update_truck_condition('${pageContext.request.contextPath}', '${truckId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick="hide_update_interface('editConditionTd', 'conditionButton')">
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="conditionButton"
                        onclick="show_update_interface('editConditionTd', 'conditionButton')">
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>City</td>
            <td id="cityTd" colspan="3"></td>
        </tr>
        <tr>
            <td>Order</td>
            <td id="orderTd" colspan="3"></td>
        </tr>
        <tr>
            <td>Drivers</td>
            <td id="driversTd" colspan="3"></td>
        </tr>
    </table>
    <div class="topButtonDiv">
        <button class="myRegularButton"
                onclick="window.location.href = '${pageContext.request.contextPath}/employee/homepage'">
            Go back
        </button>
        <button class="myRegularButton"
                onclick="load_truck('${pageContext.request.contextPath}', '${truckId}');">
            Refresh
        </button>
    </div>
</body>
</html>
