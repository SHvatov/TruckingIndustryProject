<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Truck Registration Form</title>

<style>

	.error {
		color: #ff0000;
	}
</style>

</head>

<body>

	<h2>Registration Form</h2>
 
	<form:form method="POST" modelAttribute="truck">
		<table>
			<tr>
				<td><label for="truckRegistrationNumber">Reg Number: </label> </td>
				<td><form:input path="truckRegistrationNumber" id="truckRegistrationNumber"/></td>
				<td><form:errors path="truckRegistrationNumber" cssClass="error"/></td>
		    </tr>

			<tr>
				<td><label for="driverShiftSize">Shift Size: </label> </td>
				<td><form:input path="driverShiftSize" id="driverShiftSize"/></td>
				<td><form:errors path="driverShiftSize" cssClass="error"/></td>
			</tr>

			<tr>
				<td><label for="truckCapacity">Capacity: </label> </td>
				<td><form:input path="truckCapacity" id="truckCapacity"/></td>
				<td><form:errors path="truckCapacity" cssClass="error"/></td>
			</tr>

			<tr>
				<td><label for="truckCurrentCity">Current City: </label> </td>
				<td><form:input path="truckCurrentCity" id="truckCurrentCity"/></td>
				<td><form:errors path="truckCurrentCity" cssClass="error"/></td>
			</tr>

			<tr>
				<td><label for="truckCondition">Status: </label> </td>
				<td><form:input path="truckCondition" id="truckCondition"/></td>
				<td><form:errors path="truckCondition" cssClass="error"/></td>
			</tr>
	
			<tr>
				<td colspan="3">
					<c:choose>
						<c:when test="${edit}">
							<input type="submit" value="Update"/>
						</c:when>
						<c:otherwise>
							<input type="submit" value="Register"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
	</form:form>
	<br/>
	<br/>
	Go back to <a href="<c:url value='/list' />">List of All Trucks</a>
</body>
</html>