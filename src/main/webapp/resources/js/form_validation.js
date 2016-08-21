/**
 * Created by alex on 20.08.16.
 */

$(document).ready(function() {
    $('#add_customer_form').submit(function (event) {
        var $form = $(this);

        var inputs = $(this).parent().find("input[type=text],input[type=date],input[type=email]");
        inputs.removeClass("warning");

        var empty = inputs.filter(function() {
            return this.value === "";
        });
        if(empty.length) {
            empty.addClass("warning");
            event.preventDefault(); // Important! Prevents submitting the form.
            return false;
        }
        $.post($form.attr("action"), $form.serialize(), function(response) {

        });
        return false;
    });
});

function isValidEmailAddress(emailAddress) {
    var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(emailAddress);
}
