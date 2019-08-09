<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/WEB-INF/views/employee/css/homepage.css" />">
    </head>
    <body>
        <header>
            <h1>List of all trucks in the system:</h1>
        </header>
        <table>
            <tr>
                <th>#</th>
                <th>UID</th>
                <th>Condition</th>
                <th>Capacity</th>
                <th>ORDER</th>
                <th colspan="2">DRIVERS</th>
                <th>SHIFT SIZE</th>
                <th>CITY</th>
            </tr>
            <c:forEach var="truck" items="${truckDtoList}" varStatus="cnt">
                <tr>
                    <td>${cnt.count}</td>
                    <td>${truck.uniqueIdentificator}</td>
                    <td>${truck.truckCondition}</td>
                    <td>${truck.truckCapacity}</td>
                    <td>
                        <c:if test="${empty truck.truckOrderUID}">NO ORDER</c:if>
                        <c:if test="${not empty truck.truckOrderUID}">${truck.truckOrderUID}</c:if>
                    </td>
                    <td>
                        <c:if test="${empty truck.truckDriverUIDSet}">NO DRIVERS</c:if>
                        <c:if test="${not empty truck.truckDriverUIDSet}">
                            <table>
                                <c:forEach var="driver" items="${truck.truckDriverUIDSet}" varStatus="cnt1">
                                    <tr><td>${driver}</td></tr>
                                </c:forEach>
                            </table>
                        </c:if>
                    </td>
                    <td>${truck.truckDriverShiftSize}</td>
                    <td>
                        <c:if test="${empty truck.truckCityUID}">UNKNOWN CITY</c:if>
                        <c:if test="${not empty truck.truckCityUID}">${truck.truckCityUID}</c:if>
                    </td>
                    <td>
                        <input type="submit" value="EDIT"
                               onclick="window.location='${pageContext.request.contextPath}/employee/edit_truck?uid=${truck.uniqueIdentificator}';" />
                    </td>
                    <td>
                        <input type="submit" value="DELETE"
                               onclick="window.location='${pageContext.request.contextPath}/employee/delete_truck?uid=${truck.uniqueIdentificator}';" />
                    </td>
                </tr>
            </c:forEach>
        </table>
        <input type="submit" value="BACK"
               onclick="window.location='${pageContext.request.contextPath}/employee/homepage';" />
        <input type="submit" value="ADD NEW TRUCK" formmethod="get"
               onclick="window.location='${pageContext.request.contextPath}/employee/add_truck';" />
    </body>
</html>
