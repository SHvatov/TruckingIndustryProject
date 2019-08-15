/**
 * Removes .chosenHeaderButton style from the previous button,
 * sets it ot the elem.
 *
 * @param elem - button element.
 */
function reset_button(elem) {
    // reset styles
    $(".chosenHeaderButton").addClass("myHeaderButton").removeClass("chosenHeaderButton");
    $("#" + elem).removeClass("myHeaderButton").addClass("chosenHeaderButton");
}

/**
 * Sends employee/city/list GET request and
 * loads list of the available cities from the server.
 *
 * @param pageContext - page context.
 * @param selectorId - id of the selector, where all the cities will be put.
 */
function load_city_list(pageContext, selectorId) {
    // send request
    $.ajax({
        url: pageContext + '/employee/city/list',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: (cityList) => {
            let selectorHtml = "";
            for (let i = 0; i < cityList.length; i++) {
                let temp = cityList[i];
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
 * Checks if object is empty.
 *
 * @param obj - object to check.
 * @returns {boolean} tru, if empty, false otherwise
 */
function is_empty(obj) {
    for(let key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}

/**
 * Hides the update interface.
 *
 * @param tdId - id of the td tag.
 * @param editButtonId - id of the button tag.
 */
function hide_update_interface(tdId, editButtonId) {
    $('#' + editButtonId).show();
    $('#' + tdId).hide();
}

/**
 * Shows the update interface
 *
 * @param tdId - id of the td tag.
 * @param editButtonId - id of the button tag.
 */
function show_update_interface(tdId, editButtonId) {
    $('#' + editButtonId).hide();
    $('#' + tdId).show();
}

/**
 * Removes the parent of the parent of the button with this id.
 * @param buttonId - id of the button.
 */
function remove_option(buttonId) {
    let parent = $(buttonId).parent().parent();
    $(parent).remove();
}