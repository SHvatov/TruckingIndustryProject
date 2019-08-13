<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Truck [${truckUID}]</title>
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
            load_truck('${pageContext.request.contextPath}', '${truckUID}');
        });
    </script>
    <table class="myTableStyle">
        <tr>
            <th colspan='5'>Truck ${truckUID} information</th>
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
                        id="saveButton"
                        onclick="update_truck_shift_size('${pageContext.request.contextPath}', '${truckUID}')">
                    Update
                </button>
                <button class="tableEditButton"
                        id="cancelButton"
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
                        id="updateCapacityButton"
                        onclick="update_truck_capacity('${pageContext.request.contextPath}', '${truckUID}')">
                    Update
                </button>
                <button class="tableEditButton"
                        id="cancelCapacityUpdateButton"
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
            <td id="conditionTd" colspan="4"></td>
        </tr>
        <tr>
            <td>City</td>
            <td id="cityTd" colspan="4"></td>
        </tr>
        <tr>
            <td>Order</td>
            <td id="orderTd" colspan="4"></td>
        </tr>
        <tr>
            <td>Drivers</td>
            <td id="driversTd" colspan="4"></td>
        </tr>
    </table>
</body>
</html>
