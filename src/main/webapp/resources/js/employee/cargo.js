/**
 * Sends employee/city/list GET request and
 * loads list of the available cities from the server.
 *
 * @param pageContext - page context.
 * @param selectorId - id of the selector, where all the cities will be put.
 */
function load_ready_cargo_list(pageContext, selectorId) {
    // send request
    $.ajax({
        url: pageContext + '/employee/cargo/list_id',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (cargoList) => {
            let selectorHtml = "";
            for (let i = 0; i < cargoList.length; i++) {
                let temp = cargoList[i];
                selectorHtml += "<option>" + temp + "</option>";
            }
            $("#" + selectorId).html(selectorHtml).show();
        },
        error: () => {
            console.log("failed");
        }
    });
}

/**
 * Sends /employee/truck/list GET request to the server
 * and adds the result to the table with tableID.
 *
 * @param pageContext - page context.
 * @param elemId - button.
 */
function load_cargo_table(pageContext, elemId) {
    // reset button
    reset_button(elemId);

    // send request
    $.ajax({
        url: pageContext + '/employee/cargo/list',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (cargoDtoList) => {
            // modify add button
            $("#addButton")
                .text("Add cargo")
                .click(function () {
                    redirect_to_add_cargo(pageContext);
                }).show();

            // modify refresh button
            $("#refreshButton")
                .text("Refresh cargo list")
                .click(function () {
                    load_cargo_table(pageContext, elemId);
                }).show();

            let tableInnerBlock = "<tr>" +
                "<th>№</th>" +
                "<th>UID</th>" +
                "<th>Name</th>" +
                "<th>Mass</th>" +
                "<th>Status</th>" +
                "<th colspan='2'>Actions</th>" +
                "</tr>";

            for (let i = 0; i < cargoDtoList.length; i++) {
                let temp = cargoDtoList[i];

                // add data
                tableInnerBlock += `<tr><td>${i}</td>`
                    + `<td>${temp["id"]}</td>`
                    + `<td>${temp["cargoName"]}</td>`
                    + `<td>${temp["cargoMass"]}</td>`;
                switch (temp["cargoStatus"]) {
                    case 'READY':
                    default:
                        tableInnerBlock += "<td>Ready for delivery</td>";
                        break;
                    case 'SHIPPING':
                        tableInnerBlock += "<td>Being delivered</td>";
                        break;
                    case 'DELIVERED':
                        tableInnerBlock += "<td>Delivered</td>";
                        break;
                }

                // add edit button
                tableInnerBlock += `<td>` +
                    `<button class='tableEditButton' ` +
                    `onclick='redirect_to_show_cargo("${pageContext}", "${temp["id"]}");'>` +
                    `Show` +
                    `</button>` +
                    `</td>`;

                // add delete button
                tableInnerBlock += `<td>` +
                    `<button class='tableDeleteButton' ` +
                    `onclick='delete_cargo("${pageContext}", "${temp["id"]}");'>` +
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
 * Send /employee/cargo/add GET request to the server.
 * If everything is ok, then redirects user to the add_cargo.jsp.
 *
 * @param pageContext - page context.
 */
function redirect_to_add_cargo(pageContext) {
    // redirect
    window.location.href = pageContext + '/employee/cargo/add';
}

/**
 * Sends /employee/cargo/{uid}/show GET request to the server.
 * If everything is ok, then server redirects user to the show_cargo.jsp
 * page, otherwise to the error page.
 *
 * @param pageContext - page context.
 * @param cargoUID - UID of the CARGO.
 */
function redirect_to_show_cargo(pageContext, cargoUID) {
    // redirect
    window.location.href = pageContext + '/employee/cargo/' + cargoUID + "/show";
}

/**
 * Shows a dialog to the user. In case of OK button is pressed, then
 * sends /employee/cargo/{uid}/delete POST request to the server.
 *
 * @param pageContext - page context.
 * @param cargoUID - UID of the cargo.
 */
function delete_cargo(pageContext, cargoUID) {
    $.confirm({
        title: 'Delete Cargo',
        content: 'Are you sure that you want to delete\n cargo № [<b>' + cargoUID + '</b>]?',
        boxWidth: '350px',
        useBootstrap: false,
        buttons: {
            confirm: function () {
                $.ajax({
                    url: pageContext + '/employee/cargo/' + cargoUID + "/delete",
                    type: 'POST',
                    success: function (response) {
                        if (response["status"] === "success") {
                            $.alert("Successfully deleted");
                        } else {
                            $.alert("Error while deleting: " + response["messages"]["error"] + ".");
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
 * sends it in the employee/cargo/create POST request.
 * If successful, then adds new truck to the DB and
 * redirects to the homepage.jsp, otherwise prints the errors.
 *
 * @param pageContext - page context.
 */
function add_cargo(pageContext) {
    // check if capacity is a number
    if (isNaN($("#cargoMassInput").val())) {
        $("#cargoMassError").text("Must be a number!").show();
        return;
    }

    // send request
    $.ajax({
        type: 'POST',
        contentType : 'application/json',
        dataType: 'json',
        url: pageContext + '/employee/cargo/create',
        data: JSON.stringify({
            "cargoName": $("#cargoNameInput").val(),
            "cargoMass": $("#cargoMassInput").val(),
            "cargoStatus": "READY"
        }),
        success: function (data) {
            // redirect
            if (data["status"] === "success") {
                window.location.href = pageContext + '/employee/homepage';
            } else {
                if (data["messages"].hasOwnProperty("cargoName")) {
                    $("#cargoNameError").text(data["messages"]["cargoName"]).show();
                }

                if (data["messages"].hasOwnProperty("cargoMass")) {
                    $("#cargoMassError").text(data["messages"]["cargoMass"]).show();
                }
            }
        },
        error: function () {
        }
    });
}

/**
 * Sends employee/cargo/{uid}/load GET request to the server
 * and loads the data about truck or shows an alert.
 *
 * @param pageContext - page context.
 * @param cargoUID - uid of the truck
 */
function load_cargo(pageContext, cargoUID) {
    $.ajax({
        url: pageContext + '/employee/cargo/' + cargoUID + '/load',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (response) {
            if (response["status"] === 'success') {
                let cargoDto = response["object"];

                $("#cargoIdTd").text(cargoDto["id"]).show();
                $("#cargoNameTd").text(cargoDto["cargoName"]).show();
                $("#cargoMassTd").text(cargoDto["cargoMass"]).show();

                let statusTd = $("#cargoStatusTd");
                switch (cargoDto["cargoStatus"]) {
                    case 'READY':
                    default:
                        statusTd.text("Ready for delivery").show();
                        break;
                    case 'SHIPPING':
                        statusTd.text("Being delivered").show();
                        break;
                    case 'DELIVERED':
                        statusTd.text("Delivered").show();
                        break;
                }
            } else {
                $.confirm({
                    title: 'Error occurred!',
                    content: 'Error occurred while loading information about cargo: '
                        + response["messages"]["id"],
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
 * Sends employee/cargo/{uid}/update_name POST request.
 *
 * @param pageContext - page context.
 * @param cargoUID - UID of the cargo.
 */
function update_cargo_name(pageContext, cargoUID) {
    hide_update_interface("editCargoNameTd", "editCargoNameButton");

    $.ajax({
        url: pageContext + '/employee/cargo/' + cargoUID + '/update_name',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "id": cargoUID,
            "cargoName": $("#editCargoNameInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_cargo(pageContext, cargoUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("id")) {
                    message = response["messages"]["id"]
                } else if (response["messages"].hasOwnProperty("cargoName")) {
                    message = response["messages"]["cargoName"]
                } else if (response["messages"].hasOwnProperty("error")) {
                    message = response["messages"]["error"]
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
 * Sends employee/cargo/{uid}/update_mass POST request.
 *
 * @param pageContext - page context.
 * @param cargoUID - UID of the cargo.
 */
function update_cargo_mass(pageContext, cargoUID) {
    hide_update_interface("editCargoMassTd", "editCargoMassButton");

    // check if capacity is a number
    if (isNaN($("#editCargoMassInput").val())) {
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
        url: pageContext + '/employee/cargo/' + cargoUID + '/update_mass',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "id": cargoUID,
            "cargoMass": $("#editCargoMassInput").val()
        }),
        success: function (response) {
            if (response["status"] === "success") {
                load_truck(pageContext, cargoUID);
            } else {
                let message = "";
                if (response["messages"].hasOwnProperty("id")) {
                    message = response["messages"]["id"]
                } else if (response["messages"].hasOwnProperty("cargoMass")) {
                    message = response["messages"]["cargoMass"]
                } else if (response["messages"].hasOwnProperty("error")) {
                    message = response["messages"]["error"]
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
