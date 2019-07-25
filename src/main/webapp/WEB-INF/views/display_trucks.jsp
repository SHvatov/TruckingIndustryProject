<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>University Enrollments</title>

	<style>
		tr:first-child{
			font-weight: bold;
			background-color: #C6C9C4;
		}
	</style>

</head>


<body>
	<h2>List of Employees</h2>	
	<table>
		<tr>
			Truck data
		</tr>
		<c:forEach items="${trucks}" var="truck">
			<tr>
			<td>${truck.truckCapacity}</td>
			<td>${truck.truckCurrentCity}</td>
			<td>${truck.driverShiftSize}</td>
			<td>${truck.truckRegistrationNumber}</td>
			<td>${truck.truckCondition}</td>
			</tr>
		</c:forEach>
	</table>
	<br/>
	<a href="<c:url value='/new' />">Add New Employee</a>
</body>
</html>