<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add New Truck</title>
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
        load_city_list('${pageContext.request.contextPath}', 'citySelect');
    })
</script>
<table class="myTableStyle">
        <tr>
            <th colspan="3">
                Add New Truck
            </th>
        </tr>
        <tr>
            <td>Registration number</td>
            <td><label><input type="text" id="uniqueIdentificatorInput"/></label></td>
            <td class="errorMessage" hidden="hidden" id="uniqueIdentificatorError"></td>
        </tr>
        <tr>
            <td>Shift Size</td>
            <td><label><input type="text" id="shiftInput"/></label></td>
            <td><div class="errorMessage" hidden="hidden" id="shiftError"></div></td>
        </tr>
        <tr>
            <td>Capacity (.kg)</td>
            <td><label><input type="text" id="capacityInput"/></label></td>
            <td class="errorMessage" hidden="hidden" id="capacityError"></td>
        </tr>
        <tr>
            <td>Status</td>
            <td>
                <label>
                    <select id="statusSelect">
                        <option value="IN_ORDER">Working</option>
                        <option value="NOT_IN_ORDER">Not Working</option>
                    </select>
                </label>
            </td>
            <td class="errorMessage" hidden="hidden" id="statusError"></td>
        </tr>
        <tr>
            <td>City</td>
            <td>
                <label>
                    <select id="citySelect">
                    </select>
                </label>
            </td>
            <td class="errorMessage" hidden="hidden" id="cityError"></td>
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
                        onclick="add_truck('${pageContext.request.contextPath}')">
                    Send
                </button>
            </td>
        </tr>
    </table>
</body>
</html>
