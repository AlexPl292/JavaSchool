/**
 * Created by alex on 04.09.16.
 */

$(function() {
    $('form#add_customer_form').validate({
        rules: {
            name: {
                required: true,
                no_spaces: true
            },
            surname: {
                required: true,
                no_spaces: true
            },
            passport_number: {
                required: true,
                no_spaces: true
            },
            birthday: {
                required: true,
                date: true,
                dateAb18: true
            },
            email: {
                required: true,
                email: true
            },
            number: {
                required: true,
                phone: true
            },
            options: "required"
        },
        messages: {
            name: {
                required: 'Please enter name',
                no_spaces: 'Name should not contain spaces'
            },
            surname: {
                required: 'Please enter surname',
                no_spaces: 'Surname should not contain spaces'
            },
            passport_number: {
                required: 'Please enter passport number',
                no_spaces: "Passport number shouldn't contain spaces"
            },
            birthday: {
                required: "Please enter date of birth",
                dateAb18: "New customer should be 18 years of age or older"
            },
            email: {
                required: "Please enter email",
                email: "Email is invalid"
            },
            number: {
                required: "Please enter phone number",
                phone: "Wrong phone number format"
            },
            options: "Choose options"
        }
    })
});
