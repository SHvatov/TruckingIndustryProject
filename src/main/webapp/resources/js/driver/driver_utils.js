/**
 * Loads information about driver with this uid.
 *
 * @param pageContext - page context.
 * @param driverUID - uid of the driver.
 */
function load_driver_info(pageContext, driverUID) {
    // send request
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/load',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (driverInfo) => {
            if (driverInfo["status"] === "success") {
                // set driver info
                let driverDto = driverInfo["object"]["driverInformation"];
                console.log(driverDto);
                let tableTrHtml = `<td>${driverDto["driverName"]} ` + `${driverDto["driverSurname"]}</td>`
                    + `<td>${driverDto["driverStatus"]}</td>`
                    + `<td>${driverDto["uniqueIdentificator"]}</td>`
                    + `<td>${driverInfo["object"]["secondDriverUID"]}</td>`;

                if (driverDto["driverTruckUID"] == null) {
                    tableTrHtml += "<td>Truck is not assigned</td>"
                } else {
                    tableTrHtml += `<td>${driverDto["driverTruckUID"]}</td>`
                }

                if (driverDto["driverOrderUID"] == null) {
                    tableTrHtml += "<td>Order is not assigned</td>"
                } else {
                    tableTrHtml += `<td>${driverDto["driverOrderUID"]}</td>`
                }

                if (driverInfo["object"]["orderStatus"] == null) {
                    tableTrHtml += `<td>No status</td>`
                } else {
                    tableTrHtml += `<td>${driverInfo["object"]["orderStatus"]}</td>`;
                }
                $('#driverInfoTable').html(tableTrHtml);

                // set order info
                let waypointArray = driverInfo["object"]["wayPointDtoArray"];
                let waypointTr = "<tr>" +
                    "        <th colspan='5'>" +
                    "            Order Details" +
                    "        </th>" +
                    "    </tr>" +
                    "    <tr>" +
                    "        <th>" +
                    "            City" +
                    "        </th>" +
                    "        <th>" +
                    "            Cargo" +
                    "        </th>" +
                    "        <th>" +
                    "            Action" +
                    "        </th>" +
                    "        <th>" +
                    "            Status" +
                    "        </th>" +
                    "        <th>" +
                    "            Complete" +
                    "        </th>" +
                    "    </tr>";
                for (let i = 0; i < waypointArray.length; i++) {
                    let waypoint = waypointArray[i];
                    waypointTr += `<tr><td>${waypoint["waypointCityUID"]}</td>`
                        + `<td>${waypoint["waypointCargoUID"]}</td>`
                        + `<td>${waypoint["cargoAction"]}</td>`
                        + `<td>${waypoint["wayPointStatus"]}</td>`;

                    waypointTr += "<td>"
                        + `<button class="myRegularButton"`
                        + `onclick='complete_waypoint("${pageContext}", "${driverUID}", "${waypoint["id"]}")'>COMPLETE</button></td></tr>`
                }
                waypointTr += `<tr> 
                            <td colspan='3'> 
                                <button class='myRegularButton' 
                                        id='startOrderButton' 
                                        onclick="start_order('${pageContext}', '${driverUID}')"> 
                                    Start Order 
                                </button> 
                            </td> 
                            <td colspan='2'> 
                                <button class='myRegularButton' 
                                        id='completeOrderButton' 
                                        onclick="complete_order('${pageContext}', '${driverUID}')"> 
                                    Order Completed 
                                </button> 
                            </td 
                        </tr>`;

                $('#wayPointInfoTable').html(waypointTr);
            } else {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + driverInfo["messages"]["uniqueIdentificator"] + ".",
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                        },
                    }
                });
            }
        },
        error: () => {
            console.log("failed");
        }
    });
}

/**
 * Completes the waypoint and changes its status.
 *
 * @param pageContext - page context.
 * @param driverUID - UID of the driver.
 * @param waypointUID - UID of the waypoint.
 */
function complete_waypoint(pageContext, driverUID, waypointUID) {
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/waypoint/' + waypointUID,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        success: (response) => {
            if (response["status"] === "failed") {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + response["messages"]["error"] + ".",
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
 * Start the order of the driver.
 *
 * @param pageContext - page context.
 * @param driverUID - driver UID.
 */
function start_order(pageContext, driverUID) {
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/start',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (response) => {
            if (response["status"] === "failed") {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + response["messages"]["error"] + ".",
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
 * Completes the order of the driver.
 *
 * @param pageContext - page context.
 * @param driverUID - driver UID.
 */
function complete_order(pageContext, driverUID) {
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/complete',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (response) => {
            if (response["status"] === "failed") {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + response["messages"]["error"] + ".",
                    boxWidth: '350px',
                    useBootstrap: false,
                    buttons: {
                        confirm: function () {
                        },
                    }
                });
            } else {
                $('#wayPointInfoTable').html("<tr><td>No order assigned.</td></tr>");
            }
        }
    });
}

/**
 * Changes the status of the driver.
 *
 * @param pageContext - page context.
 * @param driverUID - driver UID.
 */
function change_status(pageContext, driverUID) {
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/status/' + $("#driverStatusSelect").val(),
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        success: (response) => {
            if (response["status"] === "failed") {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + response["messages"]["error"] + ".",
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
 * Changes the session of the driver.
 *
 * @param pageContext - page context.
 * @param driverUID - driver UID.
 */
function change_session(pageContext, driverUID) {
    $.ajax({
        url: pageContext + '/driver/' + driverUID + '/session',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (response) => {
            if (response["status"] === "failed") {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Incorrect input: ' + response["messages"]["error"] + ".",
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