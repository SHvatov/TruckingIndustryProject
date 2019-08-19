<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cargo: [${cargoId}]</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/js/jquery-confirm/css/jquery-confirm.css"/>"/>
    <script src="<c:url value="/resources/js/jquery.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery-confirm/js/jquery-confirm.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/cargo.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/employee/common.js"/>" type="text/javascript"></script>
</head>
<body>
    <script>
        $(document).ready(function () {
            load_cargo('${pageContext.request.contextPath}', '${cargoId}');
        });
    </script>
    <table class="myTableStyle">
        <tr>
            <th colspan='4'>Cargo [${cargoId}] information</th>
        </tr>
        <tr>
            <td>Unique Identificator</td>
            <td id="cargoIdTd" colspan="4"></td>
        </tr>
        <tr>
            <td>Name</td>
            <td id="cargoNameTd"></td>
            <td hidden="hidden" id="editCargoNameTd">
            <label><input type="text" id="editCargoNameInput"/></label>
            <button class="tableEditButton"
                    onclick="update_cargo_name('${pageContext.request.contextPath}', '${cargoId}')">
                Update
            </button>
            <button class="tableEditButton"
                    onclick='hide_update_interface("editCargoNameTd", "editCargoNameButton");'>
                Cancel
            </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="editCargoNameButton"
                        onclick='show_update_interface("editCargoNameTd", "editCargoNameButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Mass</td>
            <td id="cargoMassTd"></td>
            <td hidden="hidden" id="editCargoMassTd">
                <label><input type="text" id="editCargoMassInput"/></label>
                <button class="tableEditButton"
                        onclick="update_cargo_mass('${pageContext.request.contextPath}', '${cargoId}')">
                    Update
                </button>
                <button class="tableEditButton"
                        onclick='hide_update_interface("editCargoMassTd", "editCargoMassButton");'>
                    Cancel
                </button>
            </td>
            <td>
                <button class="tableEditButton"
                        id="editCargoMassButton"
                        onclick='show_update_interface("editCargoMassTd", "editCargoMassButton");'>
                    Edit
                </button>
            </td>
        </tr>
        <tr>
            <td>Status</td>
            <td id="cargoStatusTd" colspan="3"></td>
        </tr>
    </table>
    <div class="topButtonDiv">
        <button class="myRegularButton"
                onclick="window.location.href = '${pageContext.request.contextPath}/employee/homepage'">
            Go back
        </button>
    </div>
</body>
</html>
