<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Employee homepage</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/truck.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/driver.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/cargo.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/order.js"/>" type="text/javascript"></script>
</head>
<body>
    <script>
        $(document).ready(function () {
            load_truck_table('${pageContext.request.contextPath}', 'displayTrucks', 'trucksTable');
        })
    </script>
    <header class="myHeader">
        <div>
            <button id="displayTrucks"
                    class="myHeaderButton"
                    onclick="load_truck_table('${pageContext.request.contextPath}', 'displayTrucks')">
                Trucks
            </button>
            <button id="displayDrivers"
                    class="myHeaderButton"
                    onclick="load_driver_table('${pageContext.request.contextPath}', 'displayDrivers')">
                Drivers
            </button>
            <button id="displayOrders"
                    class="myHeaderButton"
                    onclick="load_order_list('${pageContext.request.contextPath}', 'displayOrders')">
                Orders
            </button>
            <button id="displayCargo"
                    class="myHeaderButton"
                    onclick="load_cargo_table('${pageContext.request.contextPath}', 'displayCargo')">
                Cargo
            </button>
            <button id="logout"
                    class="myHeaderButton"
                    style="float: right"
                    onclick="window.location.href = '${pageContext.request.contextPath}/login/perform_logout'">
                Logout
            </button>
        </div>
    </header>

    <div class="topButtonDiv">
        <button class="myRegularButton" id="addButton">
        </button>
        <button class="myRegularButton" id="refreshButton">
        </button>
    </div>

    <table class="myTableStyle"></table>
</body>
</html>
