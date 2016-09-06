/**
 * Created by alex on 06.09.16.
 */

$(function() {
    $('form#add_contract_form').validate({
        rules: {
            number: {
                required: true,
                phone: true
            },
            options: "required"
        },
        messages: {
            number: {
                required: "Please enter phone number",
                phone: "Wrong phone number format"
            },
            options: "Choose options"
        },
        submitHandler: handler
    })
});
