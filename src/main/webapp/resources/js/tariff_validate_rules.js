/**
 * Created by alex on 04.09.16.
 */

$(function() {
    $('form#add_tariff_form').validate({
        rules: {
            name: "required",
            cost: {
                required: true,
                money: true
            }
        },
        messages: {
            name: 'Please enter name',
            cost: {
                required: 'Please enter cost',
                money: "Wrong money format"
            }
        }
    })
});
