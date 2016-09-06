/**
 * Created by alex on 04.09.16.
 */

$(function() {
    $('form#add_option_form').validate({
        rules: {
            name: "required",
            cost: {
                required: true,
                money: true
            },
            connect_cost: {
                required: true,
                money: true
            },
            forTariffs: "required"
        },
        messages: {
            name: 'Please enter name',
            cost: {
                required: 'Please enter cost',
                money: "Wrong money format"
            },
            connect_cost: {
                required: 'Please enter connection cost',
                money: "Wrong money format"
            },
            forTariffs: "You need to choose at least one tariff"
        }
    })
});
