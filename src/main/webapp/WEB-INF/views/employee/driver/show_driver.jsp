<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Driver: [${driverId}]</title>
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
            load_driver('${pageContext.request.contextPath}', '${driverId}');
            load_city_list('${pageContext.request.contextPath}', 'editDriverCurrentCitySelect');
        });
    </script>
    <table class="myTableStyle">
        <tr>
            <th colspan='4'>Driver [${driverId}] information</th>
        </tr>
        <tr>
            <td>Unique Identificator</td>
            <td id="driverUniqueIdentificatorTd" colspan="4"></td>
        </tr>
        <tr>
            <td>Name</td>
            <td id="driverNameTd"></td>
            <td hidden="hidden" id="editDriverNameTd">
            <label><input type="text" id="editDriverNameInput"/></label>
            <button class="tableEditButton"
                    onclick="update_driver_name('${pageContext.request.contextPath}', '${driverId}')">
                Update
            </button>
            <button class="tableEditButton"
                    onclick='hide_update_interface("editDriverNameTd", "driverNameEditButton");'>
                Cancel
            </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="driverNameEditButton"
                        onclick='show_update_interface("editDriverNameTd", "driverNameEditButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Surname</td>
            <td id="driverSurnameTd"></td>
            <td hidden="hidden" id="editDriverSurnameTd">
                <label><input type="text" id="editDriverSurnameInput"/></label>
                <button class="tableEditButton"
                        onclick="update_driver_surname('${pageContext.request.contextPath}', '${driverId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick='hide_update_interface("editDriverSurnameTd", "driverSurnameEditButton");'>
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="driverSurnameEditButton"
                        onclick='show_update_interface("editDriverSurnameTd", "driverSurnameEditButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Worked Hours</td>
            <td id="driverWorkedHoursTd" colspan="3"></td>
        </tr>
        <tr>
            <td>Status</td>
            <td id="driverStatusTd" colspan="3"></td>
        </tr>
        <tr>
            <td>Current city</td>
            <td id="driverCurrentCityTd"></td>
            <td hidden="hidden" id="editDriverCurrentCityTd">
                <label>
                    <select id="editDriverCurrentCitySelect">
                    </select>
                </label>
                <button class="tableEditButton"
                        onclick="update_driver_city('${pageContext.request.contextPath}', '${driverId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick='hide_update_interface("editDriverCurrentCityTd", "driverCurrentCityEditButton");'>
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="driverCurrentCityEditButton"
                        onclick='show_update_interface("editDriverCurrentCityTd", "driverCurrentCityEditButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Current truck</td>
            <td id="driverCurrentTruckTd" colspan="3"></td>
        </tr>
        <tr>
            <td>Last Time Updated</td>
            <td id="driverLastTimeUpdatedTd" colspan="3"></td>
        </tr>
    </table>
    <div class="topButtonDiv">
        <button class="myRegularButton"
                onclick="window.location.href = '${pageContext.request.contextPath}/employee/homepage'">
            Go back
        </button>
        <button class="myRegularButton"
                onclick="load_truck('${pageContext.request.contextPath}', '${driverId}');">
            Refresh
        </button>
    </div>
</body>
</html>
