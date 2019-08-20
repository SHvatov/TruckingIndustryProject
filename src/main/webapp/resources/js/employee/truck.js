/**
 * Sends /employee/truck/list GET request to the server
 * and adds the result to the table with tableID.
 *
 * @param pageContext - page context.
 * @param elemId - button.
 */
function load_truck_table(pageContext, elemId) {
    // reset button
    reset_button(elemId);

    // send request
    $.ajax({
        url: pageContext + '/employee/truck/list',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (truckDtoList) => {
            // modify add button
            $("#addButton")
                .text("Add truck")
                .off()
                .click(function () {
                    redirect_to_add_truck(pageContext);
                }).show();

            // modify refresh button
            $("#refreshButton")
                .text("Refresh trucks list")
                .off()
                .click(function () {
                    load_truck_table(pageContext, elemId);
                }).show();

            let tableInnerBlock = "<tr>" +
                "<th>№</th>" +
                "<th>Reg. Number</th>" +
                "<th>Capacity</th>" +
                "<th>Shift size</th>" +
                "<th>Condition</th>" +
                "<th>City</th>" +
                "<th colspan='2'>Actions</th>" +
                "</tr>";

            for (let i = 0; i < truckDtoList.length; i++) {
                let temp = truckDtoList[i];

                // add data
                tableInnerBlock += `<tr><td>${i}</td>`
                    + `<td>${temp["uniqueIdentificator"]}</td>`
                    + `<td>${temp["capacity"]}</td>`
                    + `<td>${temp["shiftSize"]}</td>`;
                if (temp["status"] === "IN_ORDER") {
                    tableInnerBlock += "<td>In order</td>";
                } else {
                    tableInnerBlock += "<td>Broken</td>";
                }
                tableInnerBlock += `<td>${temp["cityId"]}</td>`;

                // add edit button
                tableInnerBlock += `<td>` +
                    `<button class='tableEditButton' ` +
                    `onclick='redirect_to_show_truck("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
                    `Show` +
                    `</button>` +
                    `</td>`;

                // add delete button
                tableInnerBlock += `<td>` +
                    `<button class='tableDeleteButton' ` +
                    `onclick='delete_truck("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
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
 * Send /employee/truck/add GET request to the server.
 * If everything is ok, then redirects user to the add_truck.jsp.
 *
 * @param pageContext - page context.
 */
function redirect_to_add_truck(pageContext) {
    // redirect
    window.location.href = pageContext + '/employee/truck/add';
}

/**
 * Sends /employee/truck/{uid}/show GET request to the server.
 * If everything is ok, then server redirects user to the show_truck.jsp
 * page, otherwise to the error page.
 *
 * @param pageContext - page context.
 * @param truckUID - UID of the truck.
 */
function redirect_to_show_truck(pageContext, truckUID) {
    // redirect
    window.location.href = pageContext + '/employee/truck/' + truckUID + "/show";
}

/**
 * Shows a dialog to the user. In case of OK button is pressed, then
 * sends /employee/truck/{uid}/delete POST request to the server.
 *
 * @param pageContext - page context.
 * @param truckUID - UID of the truck.
 */
function delete_truck(pageContext, truckUID) {
    $.confirm({
        title: 'Delete Truck',
        content: 'Are you sure that you want to delete\n truck with registration number [<b>' + truckUID + '</b>]?',
        boxWidth: '350px',
        useBootstrap: false,
        buttons: {
            confirm: function () {
                $.ajax({
                    url: pageContext + '/employee/truck/' + truckUID + "/delete",
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
 * sends it in the employee/truck/create POST request.
 * If successful, then adds new truck to the DB and
 * redirects to the homepage.jsp, otherwise prints the errors.
 *
 * @param pageContext - page context.
 */
function add_truck(pageContext) {
    let incorrect = false;

    // check if capacity is a number
    if (isNaN($("#capacityInput").val())) {
        $("#capacityError").text("Must be a number!").show();
        incorrect = true;
    }

    // check if shift size is a number
    if (isNaN($("#shiftInput").val())) {
        $("#shiftError").text("Must be a number!").show();
        incorrect = true;
    }

    // if there are mistakes then leave
    if (incorrect) return;

    // send request
    $.ajax({
        type: 'POST',
        contentType : 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/truck/create',
        data: JSON.stringify({
            "uniqueIdentificator": $("#uniqueIdentificatorInput").val(),
            "capacity": $("#capacityInput").val(),
            "shiftSize": $("#shiftInput").val(),
            "status": $("#statusSelect").val(),
            "cityId": $("#citySelect").val(),
        }),
        success: function (data) {
            // redirect
            if (data["status"] === "success") {
                window.location.href = pageContext + '/employee/homepage';
            } else {
                // output error
                if (data["messages"].hasOwnProperty("uniqueIdentificator")) {
                    $("#uniqueIdentificatorError").text(data["messages"]["uniqueIdentificator"]).show();
                }

                if (data["messages"].hasOwnProperty("capacity")) {
                    $("#capacityError").text(data["messages"]["capacity"]).show();
                }

                if (data["messages"].hasOwnProperty("shiftSize")) {
                    $("#shiftError").text(data["messages"]["shiftSize"]).show();
                }

                if (data["messages"].hasOwnProperty("status")) {
                    $("#statusError").text(data["messages"]["status"]).show();
                }

                if (data["messages"].hasOwnProperty("cityId")) {
                    $("#cityError").text(data["messages"]["cityId"]).show();
                }
            }
        },
        error: function () {
        }
    })
}

/**
 * Sends employee/truck/{uid}/load GET request to the server
 * and loads the data about truck or shows an alert.
 *
 * @param pageContext - page context.
 * @param truckUID - uid of the truck
 */
function load_truck(pageContext, truckUID) {
    $.ajax({
        url: pageContext + '/employee/truck/' + truckUID + '/load',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (response) {
            if (response["status"] === 'success') {
                let truckDto = response["object"];

                $("#uniqueIdentificatorTd").text(truckDto["uniqueIdentificator"]).show();
                $("#shiftSizeTd").text(truckDto["shiftSize"]).show();
                $("#capacityTd").text(truckDto["capacity"]).show();

                if (truckDto["status"] === "IN_ORDER") {
                    $("#conditionTd").text("In Order").show();
                } else {
                    $("#conditionTd").text("Not in Order").show();
                }

                if (truckDto["cityId"] === null) {
                    $("#cityTd").text("No city").show();
                } else {
                    $("#cityTd").text(truckDto["cityId"]).show();
                }

                if (truckDto["orderId"] === null) {
                    $("#orderTd").text("No order").show();
                } else {
                    $("#orderTd").text(truckDto["orderId"]).show();
                }

                if (is_empty(truckDto["assignedDrivers"])) {
                    $("#driversTd").text("No assigned drivers").show();
                } else {
                    let driversList = "<ul>";
                    for (let driver in truckDto["assignedDrivers"]) {
                        driversList += "<li>" + driver + "</li>";
                    }
                    driversList += "</ul>";
                    $("#driversTd").html(driversList);
                }
            } else {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Error occurred while loading information about truck: '
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
 * Sends employee/truck/{uid}/update_capacity POST request.
 *
 * @param pageContext - page context.
 * @param truckUID - UID of the truck.
 */
function update_truck_capacity(pageContext, truckUID) {
    hide_update_interface("editCapacityTd", "capacityButton");

    // check if capacity is a number
    if (isNaN($("#editCapacityInput").val())) {
        $.confirm({
            title: 'Error occurred!',
            content: 'Incorrect input: Must be a number.',
            boxWidth: '350px',
            useBootstrap: false,
            buttons: {
                confirm: function () {
                },
            }
        });
        return;
    }

    $.ajax({
        url: pageContext + '/employee/truck/' + truckUID + '/update_capacity',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": truckUID,
            "capacity": $("#editCapacityInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_truck(pageContext, truckUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("capacity")) {
                    message = response["messages"]["capacity"]
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
 * Sends employee/truck/{uid}/update_shift POST request.
 *
 * @param pageContext - page context.
 * @param truckUID - UID of the truck.
 */
function update_truck_shift_size(pageContext, truckUID) {
    hide_update_interface("editShiftSizeTd", "shiftSizeButton");

    // check if capacity is a number
    if (isNaN($("#editShiftSizeInput").val())) {
        $.confirm({
            title: 'Error occurred!',
            content: 'Incorrect input: Must be a number.',
            boxWidth: '350px',
            useBootstrap: false,
            buttons: {
                confirm: function () {
                },
            }
        });
        return;
    }

    $.ajax({
        url: pageContext + '/employee/truck/' + truckUID + '/update_shift',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": truckUID,
            "shiftSize": $("#editShiftSizeInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_truck(pageContext, truckUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("shiftSize")) {
                    message = response["messages"]["shiftSize"]
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
 * Sends employee/truck/{uid}/update_condition POST request.
 *
 * @param pageContext - page context.
 * @param truckUID - UID of the truck.
 */
function update_truck_condition(pageContext, truckUID) {
    hide_update_interface("editConditionTd", "conditionButton");

    // check if capacity is a number
    if (isNaN($("#editShiftSizeInput").val())) {
        $.confirm({
            title: 'Error occurred!',
            content: 'Incorrect input: Must be a number.',
            boxWidth: '350px',
            useBootstrap: false,
            buttons: {
                confirm: function () {
                },
            }
        });
        return;
    }

    $.ajax({
        url: pageContext + '/employee/truck/' + truckUID + '/update_condition',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "uniqueIdentificator": truckUID,
            "status": $("#editConditionSelect").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_truck(pageContext, truckUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"]
                } else if (response["messages"].hasOwnProperty("status")) {
                    message = response["messages"]["status"]
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