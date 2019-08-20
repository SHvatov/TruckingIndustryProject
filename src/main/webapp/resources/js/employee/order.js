/**
 * Sends /employee/order/list GET request to the server
 * and adds the result to the table with tableID.
 *
 * @param pageContext - page context.
 * @param elemId - button.
 */
function load_order_list(pageContext, elemId) {
    // reset button
    reset_button(elemId);

    // send request
    $.ajax({
        url: pageContext + '/employee/order/list',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (orderDtoList) => {
            // modify add button
            $("#addButton")
                .text("Create Order")
                .off()
                .click(function () {
                    redirect_to_add_order(pageContext);
                }).show();

            // modify refresh button
            $("#refreshButton")
                .text("Refresh order list")
                .off()
                .click(function () {
                    load_order_list(pageContext, elemId);
                }).show();

            let tableInnerBlock = "<tr>" +
                "<th>№</th>" +
                "<th>UID</th>" +
                "<th>Status</th>" +
                "<th>Last Updated</th>" +
                "<th>Truck</th>" +
                "<th>Drivers</th>" +
                "<th colspan='2'>Actions</th>" +
                "</tr>";

            for (let i = 0; i < orderDtoList.length; i++) {
                let temp = orderDtoList[i];

                console.log(temp);

                // add data
                tableInnerBlock += `<tr><td>${i}</td>`
                    + `<td>${temp["uniqueIdentificator"]}</td>`;

                switch (temp["status"]) {
                    case 'CANCELED':
                    default:
                        tableInnerBlock += "<td style='color: #880000'>Canceled</td>";
                        break;
                    case 'READY':
                        tableInnerBlock += "<td style='color: #2b542c'>Ready</td>";
                        break;
                    case 'IN_PROGRESS':
                        tableInnerBlock += "<td style='color: #2b542c'>In progress</td>";
                        break;
                    case 'COMPLETED':
                        tableInnerBlock += "<td style='color: #2b542c'>Completed</td>";
                        break;
                }

                tableInnerBlock += `<td>${new Date(temp["lastUpdated"])}</td>`;

                if (temp["truckId"] == null) {
                    tableInnerBlock += "<td>Not assigned</td>"
                } else {
                    tableInnerBlock += `<td>${temp["truckId"]}</td>`
                }

                if (is_empty(temp["assignedDrivers"])) {
                    tableInnerBlock += "<td>Not assigned</td>"
                } else {
                    tableInnerBlock += "<td><ul>";
                    for (let i = 0; i < temp["assignedDrivers"].length; i++) {
                        let driver = temp["assignedDrivers"][i];
                        console.log(driver);
                        tableInnerBlock += "<li>" + driver + "</li>";
                    }
                    tableInnerBlock += "</ul></td>";
                }

                // add edit button
                tableInnerBlock += `<td>` +
                    `<button class='tableEditButton' ` +
                    `onclick='redirect_to_edit_order("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
                    `Show` +
                    `</button>` +
                    `</td>`;

                // add delete button
                tableInnerBlock += `<td>` +
                    `<button class='tableDeleteButton' ` +
                    `onclick='cancel_order("${pageContext}", "${temp["uniqueIdentificator"]}");'>` +
                    `Cancel` +
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
 * Send /employee/cargo/add GET request to the server.
 * If everything is ok, then redirects user to the add_cargo.jsp.
 *
 * @param pageContext - page context.
 */
function redirect_to_add_order(pageContext) {
    // redirect
    window.location.href = pageContext + '/employee/order/add';
}

/**
 * Sends /employee/cargo/{uid}/show GET request to the server.
 * If everything is ok, then server redirects user to the show_cargo.jsp
 * page, otherwise to the error page.
 *
 * @param pageContext - page context.
 * @param orderUID - UID of the order.
 */
function redirect_to_edit_order(pageContext, orderUID) {
    // redirect
    window.location.href = pageContext + '/employee/order/' + orderUID + "/edit";
}

/**
 * Shows a dialog to the user. In case of OK button is pressed, then
 * sends /employee/order/{uid}/cancel POST request to the server.
 *
 * @param pageContext - page context.
 * @param orderUID - UID of the order.
 */
function cancel_order(pageContext, orderUID) {
    $.confirm({
        title: 'Cancel order',
        content: 'Are you sure that you want to cancel\n order № [<b>' + orderUID + '</b>]?',
        boxWidth: '350px',
        useBootstrap: false,
        buttons: {
            confirm: function () {
                $.ajax({
                    url: pageContext + '/employee/order/' + orderUID + "/cancel",
                    type: 'POST',
                    success: function (response) {
                        if (response["status"] === "success") {
                            $.alert("Successfully canceled");
                        } else {
                            $.alert("Error while canceling: " + response["messages"]["error"] + ".");
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
 * Fetches the list of the available for this order trucks
 * from the server. Validates the UID and list of waypoints.
 *
 * @param pageContext - page context.
 */
function fetch_truck_list(pageContext) {
    let waypointArray = [];

    $('.addWayPointTr').each(function () {
        let data = {
            'action': $(this).children('.wayPointAction').text(),
            'status': 'NOT_COMPLETED',
            'orderId': $('#orderUniqueIdentificatorInput').val(),
            'cargoId': $(this).children('.wayPointCargo').text(),
            'cityId': $(this).children('.wayPointCity').text()
        };
        waypointArray.push(data);
    });

    // send request
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/order/trucks',
        data: JSON.stringify({
            "uniqueIdentificator": $('#orderUniqueIdentificatorInput').val(),
            "waypoints": waypointArray
        }),
        success: function (response) {
            // redirect
            if (response["status"] === "success") {
                let truckDtoList = response["object"];

                $('#waypointTable').hide();
                $('#orderTrucksTable').show();

                let selectorHtml = "";
                for (let i = 0; i < truckDtoList.length; i++) {
                    let temp = truckDtoList[i];
                    selectorHtml += `<option value="${temp["uniqueIdentificator"]}">`
                        + "<b>UID: </b>" + temp["uniqueIdentificator"]
                        + " <b>Capacity: </b>: " + temp["capacity"]
                        + " <b>City: </b> " + temp["cityId"]
                        + " <b>Shift Size: </b>" + temp["shiftSize"]
                        + "</option>";
                }
                $("#orderTruckSelect").html(selectorHtml);

            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"];
                } else if (response["messages"].hasOwnProperty("waypoints")) {
                    message = response["messages"]["waypoints"];
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
        },
        error: function () {
        }
    });
}

/**
 * Fetches the list of the available for this order drivers
 * from the server. Validates the UID and list of waypoints.
 *
 * @param pageContext - page context.
 */
function fetch_driver_list(pageContext) {
    let waypointArray = [];

    $('.addWayPointTr').each(function () {
        let data = {
            'action': $(this).children('.wayPointAction').text(),
            'status': 'NOT_COMPLETED',
            'orderId': $('#orderUniqueIdentificatorInput').val(),
            'cargoId': $(this).children('.wayPointCargo').text(),
            'cityId': $(this).children('.wayPointCity').text()
        };
        waypointArray.push(data);
    });

    // send request
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/order/drivers',
        data: JSON.stringify({
            "uniqueIdentificator": $('#orderUniqueIdentificatorInput').val(),
            "truckId": $('#orderTruckSelect').val(),
            "waypoints": waypointArray
        }),
        success: function (response) {
            // redirect
            if (response["status"] === "success") {
                let driverDtoList = response["object"];

                $('#orderTrucksTable').hide();
                $('#orderDriversTable').show();

                let selectorHtml = "";
                for (let i = 0; i < driverDtoList.length; i++) {
                    let temp = driverDtoList[i];
                    selectorHtml += `<option value="${temp["uniqueIdentificator"]}">`
                        + "<b>UID: </b>" + temp["uniqueIdentificator"]
                        + " <b>Name: </b>: " + temp["name"]
                        + " <b>Surname: </b> " + temp["surname"]
                        + "</option>";
                }
                console.log(selectorHtml)
                $("#orderDriversSelect").html(selectorHtml);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"];
                } else if (response["messages"].hasOwnProperty("waypoints")) {
                    message = response["messages"]["waypoints"];
                } else if (response["messages"].hasOwnProperty("truckId")) {
                    message = response["messages"]["truckId"];
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
        },
        error: function () {
        }
    });
}

/**
 * Adds order to the list of orders in the DB.
 *
 * @param pageContext - page context.
 */
function add_order(pageContext) {
    let waypointArray = [];

    $('.addWayPointTr').each(function () {
        let data = {
            'action': $(this).children('.wayPointAction').text(),
            'status': 'NOT_COMPLETED',
            'orderId': $('#orderUniqueIdentificatorInput').val(),
            'cargoId': $(this).children('.wayPointCargo').text(),
            'cityId': $(this).children('.wayPointCity').text()
        };
        waypointArray.push(data);
    });

    let driverArray = [];
    $('.addDriverTr').each(function () {
        driverArray.push($(this).children('.orderDriver').text());
    });

    // send request
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/order/create',
        data: JSON.stringify({
            "uniqueIdentificator": $('#orderUniqueIdentificatorInput').val(),
            "truckId": $('#orderTruckSelect').val(),
            "assignedDrivers": driverArray,
            "waypoints": waypointArray
        }),
        success: function (response) {
            // redirect
            if (response["status"] === "success") {
                window.location.href = pageContext + '/employee/homepage';
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("uniqueIdentificator")) {
                    message = response["messages"]["uniqueIdentificator"];
                } else if (response["messages"].hasOwnProperty("waypoints")) {
                    message = response["messages"]["waypoints"];
                } else if (response["messages"].hasOwnProperty("truckId")) {
                    message = response["messages"]["truckId"];
                } else if (response["messages"].hasOwnProperty("assignedDrivers")) {
                    message = response["messages"]["assignedDrivers"];
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
        },
        error: function () {
        }
    });
}
