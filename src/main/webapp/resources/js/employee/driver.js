/**
 * Sends /employee/driver/list GET request to the server
 * and adds the result to the table with tableID.
 *
 * @param pageContext - page context.
 * @param elemId - button.
 */
function load_driver_table(pageContext, elemId) {
    // reset button
    reset_button(elemId);

    // send request
    $.ajax({
        url: pageContext + '/employee/driver/list',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (driverDtoList) => {
            // modify add button
            $("#addButton")
                .text("Add driver")
                .off()
                .click(function () {
                    redirect_to_add_driver(pageContext);
                }).show();

            // modify refresh button
            $("#refreshButton")
                .text("Refresh driver list")
                .off()
                .click(function () {
                    load_driver_table(pageContext, elemId);
                }).show();

            let tableInnerBlock = "<tr>" +
                "<th>№</th>" +
                "<th>Name</th>" +
                "<th>Surname</th>" +
                "<th>UID</th>" +
                "<th>Worked Hours</th>" +
                "<th>Status</th>" +
                "<th>City</th>" +
                "<th>Truck Reg. Num.</th>" +
                "<th>Last Updated</th>" +
                "<th colspan='2'>Actions</th>" +
                "</tr>";

            for (let i = 0; i < driverDtoList.length; i++) {
                let temp = driverDtoList[i];

                // add data
                tableInnerBlock += `<tr><td>${i}</td>`
                    + `<td>${temp["name"]}</td>`
                    + `<td>${temp["surname"]}</td>`
                    + `<td>${temp["uniqueIdentificator"]}</td>`;

                // convert seconds into hours
                let date = new Date(null);
                date.setSeconds(temp["workedHours"]);
                tableInnerBlock += "<td>" + date.toISOString().substr(11, 8) + "</td>";

                switch (temp["status"]) {
                    case "IDLE":
                    default:
                        tableInnerBlock += "<td>Idle</td>";
                        break;
                    case "IN_SHIFT":
                        tableInnerBlock += "<td>Second driver</td>";
                        break;
                    case "DRIVING":
                        tableInnerBlock += "<td>Driving</td>";
                        break;
                    case "LOADING_UNLOADING":
                        tableInnerBlock += "<td>Loading / Unloading</td>";
                        break;
                }

                tableInnerBlock += `<td>${temp["cityId"]}</td>`;

                if (temp["truckId"] == null) {
                    tableInnerBlock += `<td>Not assigned to a truck</td>`;
                } else {
                    tableInnerBlock += `<td>${temp["truckId"]}</td>`;
                }

                tableInnerBlock += `<td>${new Date(temp["lastUpdated"])}</td>`;

                // add edit button
                tableInnerBlock += `<td>` +
                    `<button class='tableEditButton' ` +
                    `onclick='redirect_to_show_driver("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
                    `Show` +
                    `</button>` +
                    `</td>`;

                // add delete button
                tableInnerBlock += `<td>` +
                    `<button class='tableDeleteButton' ` +
                    `onclick='delete_driver("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
                    `Delete` +
                    `</button>` +
                    `</td></tr>`;
            }
            $("table").html(tableInnerBlock).show();
        },
        error: () => {
            console.log("failed");
        }
    });
}

/**
 * Send /employee/driver/add GET request to the server.
 * If everything is ok, then redirects user to the add_driver.jsp.
 *
 * @param pageContext - page context.
 */
function redirect_to_add_driver(pageContext) {
    // redirect
    window.location.href = pageContext + '/employee/driver/add';
}

/**
 * Sends /employee/driver/{uid}/show GET request to the server.
 * If everything is ok, then server redirects user to the show_driver.jsp
 * page, otherwise to the error page.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 */
function redirect_to_show_driver(pageContext, driverUID) {
    // redirect
    window.location.href = pageContext + '/employee/driver/' + driverUID + "/show";
}

/**
 * Shows a dialog to the user. In case of OK button is pressed, then
 * sends /employee/driver/{uid}/delete POST request to the server.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 */
function delete_driver(pageContext, driverUID) {
    $.confirm({
        title: 'Delete Cargo',
        content: 'Are you sure that you want to delete\n driver with registration number [<b>' + driverUID + '</b>]?',
        boxWidth: '350px',
        useBootstrap: false,
        buttons: {
            confirm: function () {
                $.ajax({
                    url: pageContext + '/employee/driver/' + driverUID + "/delete",
                    type: 'POST',
                    success: function (response) {
                        if (response["status"] === "success") {
                            $.alert("Successfully deleted");
                        } else {
                            $.alert("Error while deleting: " + response["messages"]["orderId"] + ".");
                        }
                    }
                });
            },
            cancel: function () {
            }
        }
    });
}

/**
 * Gets data from the user input, and then
 * sends it in the employee/driver/create POST request.
 * If successful, then adds new driver to the DB and
 * redirects to the homepage.jsp, otherwise prints the errors.
 *
 * @param pageContext - page context.
 */
function add_driver(pageContext) {
    // send request
    $.ajax({
        type: 'POST',
        contentType : 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/driver/create',
        data: JSON.stringify({
            "uniqueIdentificator": $("#driverUniqueIdentificatorInput").val(),
            "name": $("#driverNameInput").val(),
            "surname": $("#driverSurnameInput").val(),
            "cityId": $("#driverCitySelect").val(),
            "password": $("#driverPasswordInput").val(),
            "workedHours": 0,
            "status": "IDLE",
            "lastUpdated": new Date()
        }),
        success: function (data) {
            // redirect
            if (data["status"] === "success") {
                window.location.href = pageContext + '/employee/homepage';
            } else {
                // output error
                if (data["messages"].hasOwnProperty("uniqueIdentificator")) {
                    $("#driverUniqueIdentificatorError").text(data["messages"]["uniqueIdentificator"]).show();
                }

                if (data["messages"].hasOwnProperty("name")) {
                    $("#driverNameError").text(data["messages"]["name"]).show();
                }

                if (data["messages"].hasOwnProperty("surname")) {
                    $("#driverSurnameError").text(data["messages"]["surname"]).show();
                }

                if (data["messages"].hasOwnProperty("password")) {
                    $("#driverPasswordError").text(data["messages"]["password"]).show();
                }

                if (data["messages"].hasOwnProperty("cityId")) {
                    $("#driverCityError").text(data["messages"]["cityId"]).show();
                }
            }
        },
        error: function () {
        }
    });
}

/**
 * Sends employee/driver/{uid}/load GET request to the server
 * and loads the data about driver or shows an alert.
 *
 * @param pageContext - page context.
 * @param driverUID - uid of the driver.
 */
function load_driver(pageContext, driverUID) {
    $.ajax({
        url: pageContext + '/employee/driver/' + driverUID + '/load',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (response) {
            if (response["status"] === 'success') {
                let driverDto = response["object"];

                $("#driverUniqueIdentificatorTd").text(driverDto["uniqueIdentificator"]).show();
                $("#driverNameTd").text(driverDto["name"]).show();
                $("#driverSurnameTd").text(driverDto["surname"]).show();
                $("#driverCurrentCityTd").text(driverDto["cityId"]).show();
                $("#driverLastTimeUpdatedTd").text(new Date(driverDto["lastUpdated"])).show();

                let workedHoursTd = $("#driverWorkedHoursTd");
                let date = new Date(null);
                date.setSeconds(driverDto["workedHours"]);
                workedHoursTd.text(date.toISOString().substr(11, 8)).show();

                let truckTd = $("#driverCurrentTruckTd");
                if (driverDto["truckId"] == null) {
                    truckTd.text("Not assigned to a truck").show();
                } else {
                    truckTd.text(driverDto["truckId"]).show();
                }

                let statusTd = $("#driverStatusTd");
                switch (driverDto["status"]) {
                    case "IDLE":
                    default:
                        statusTd.text("Idle").show();
                        break;
                    case "IN_SHIFT":
                        statusTd.text("In shift").show();
                        break;
                    case "DRIVING":
                        statusTd.text("Driving").show();
                        break;
                    case "LOADING_UNLOADING":
                        statusTd.text("Loading / Unloading").show();
                        break;
                }
            } else {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Error occurred while loading information about driver: '
                        + response["messages"]["uniqueIdentificator"],
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                            window.location.href = pageContext + '/employee/homepage'
                        },
                    }
                });
            }
        }
    });
}

/**
 * Sends employee/driver/{uid}/update_name POST request.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 */
function update_driver_name(pageContext, driverUID) {
    hide_update_interface("editDriverNameTd", "driverNameEditButton");

    $.ajax({
        url: pageContext + '/employee/driver/' + driverUID + '/update_name',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": driverUID,
            "name": $("#editDriverNameInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_driver(pageContext, driverUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("name")) {
                    message = response["messages"]["name"]
                } else if (response["messages"].hasOwnProperty("orderId")) {
                    message = response["messages"]["orderId"]
                }

                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + message + ".",
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                        },
                    }
                });
            }
        }
    });
}

/**
 * Sends employee/driver/{uid}/update_name POST request.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 */
function update_driver_surname(pageContext, driverUID) {
    hide_update_interface("editDriverSurnameTd", "driverSurnameEditButton");

    $.ajax({
        url: pageContext + '/employee/driver/' + driverUID + '/update_surname',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": driverUID,
            "surname": $("#editDriverSurnameInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_driver(pageContext, driverUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("surname")) {
                    message = response["messages"]["surname"]
                } else if (response["messages"].hasOwnProperty("orderId")) {
                    message = response["messages"]["orderId"]
                }

                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + message + ".",
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                        },
                    }
                });
            }
        }
    });
}

/**
 * Sends employee/driver/{uid}/update_city POST request.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 */
function update_driver_city(pageContext, driverUID) {
    hide_update_interface("editDriverCurrentCityTd", "driverCurrentCityEditButton");

    $.ajax({
        url: pageContext + '/employee/driver/' + driverUID + '/update_city',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": driverUID,
            "cityId": $("#editDriverCurrentCitySelect").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_driver(pageContext, driverUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("cityId")) {
                    message = response["messages"]["cityId"]
                } else if (response["messages"].hasOwnProperty("orderId")) {
                    message = response["messages"]["orderId"]
                }

                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + message + ".",
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                        },
                    }
                });
            }
        }
    });
}

