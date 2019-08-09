<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Edit Truck</title>
    </head>
    <body>
        <h2>Edit truck: ${truckUID}</h2>
        <form:form method="post"
                   action="${pageContext.request.contextPath}/employee/edit_truck?uid=${truckUID}"
                   modelAttribute="truckDto">
            <table border="1px solid">
                <tr>
                    <td><form:label path="truckDriverShiftSize">Shift size</form:label></td>
                    <td><form:input path="truckDriverShiftSize"/></td>
                    <td><form:errors path="truckDriverShiftSize" cssClass="error"/></td>
                </tr>
                <tr>
                    <td><form:label path="truckCapacity">Capacity</form:label></td>
                    <td><form:input path="truckCapacity"/></td>
                    <td><form:errors path="truckCapacity" cssClass="error"/></td>
                </tr>
                <tr>
                    <td><form:label path="truckCondition">Condition</form:label></td>
                    <td>
                        <form:select path="truckCondition">
                            <form:option value="IN_ORDER" label="IN ORDER"/>
                            <form:option value="NOT_IN_ORDER" label="NOT IN ORDER"/>
                        </form:select>
                    </td>
                    <td><form:errors path="truckCondition" cssClass="error"/></td>
                </tr>
                <tr>
                    <td><form:label path="truckCityUID">City</form:label></td>
                    <td><form:select path="truckCityUID" items="${cityList}" /></td>
                    <td><form:errors path="truckCityUID" cssClass="error"/></td>
                </tr>
                <tr>
                    <td><form:label path="truckDriverUIDSet">Driver</form:label>
                    <td>
                        <form:select path="truckDriverUIDSet" multiple="true">
                            <form:option value="${null}" label="NONE"/>
                            <form:options items="${driverList}"/>
                        </form:select>
                    </td>
                    <td><form:errors path="truckDriverUIDSet" cssClass="error"/></td>
                </tr>
                <tr><td><input type="submit"/></td></tr>
            </table>
        </form:form>
    </body>
</html>
