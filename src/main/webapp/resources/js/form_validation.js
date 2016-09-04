/**
 * Created by alex on 20.08.16.
 */


$.validator.addMethod("no_spaces", function (val){
    return val.indexOf(' ') === -1;
});

$.validator.addMethod("dateAb18", function (val) {
    var date = val.split("-");
    var orig_date = new Date(date[0], date[1]-1, date[2]);
    // Ну почему, по какой причине в этом js месяцы начинаются с 0?! Неужели было так сложно сделать январь первым, а не нулевым?!
    console.log(orig_date);
    var today = new Date();
    return orig_date.setFullYear(orig_date.getFullYear()+18) <= today;
});

$.validator.addMethod("phone", function (val) {
    var regex = new RegExp("\\+\\d+ \\(\\d{3}\\) \\d{3}-\\d{4}");
    return regex.test(val);
});

$.validator.addMethod("money", function (val) {
    var ptrn = new RegExp("[0-9]+([,.][0-9]{1,2})?");
    return ptrn.test(val);
});

$.validator.setDefaults({
    errorClass: "has-error",
    onkeyup: false,
    onclick: false,
    errorPlacement: function(error, element) {
        $.notify(error.text(), {position: "top right", className: "error"});
    },
    highlight: function(element, errorClass) {
        $(element).parent().parent().addClass(errorClass);
        $(element).fadeOut(function() {
            $(element).fadeIn();
        });
    },
    unhighlight: function(element, errorClass) {
        $(element).parent().parent().removeClass(errorClass);
    },
    submitHandler: submitting
});


function response_validate($form) {
    return function (response) {
        if (response.success) {
            $form.find('input[type=submit]').notify("Success!", {position: "right", className: "success"});
            $form[0].reset();
        } else {
            $form.find('input[type=submit]').notify("Errors! See above", {position: "right", className: "error"});
            $.each(response.errors, function (prop, val) {
                $.notify("Error: in "+prop+"\n" + val, {position: "top right", className: "error"});
            });
        }
        $form.find(":input").prop("disabled", false);
    }
}

function submitting(form, e) {
    e.preventDefault();
    $('input[type=submit]').notify("Sending data..", {position:"right", className:"success"});

    $.post($(form).attr("action"), $(form).serialize(), response_validate($(form)));
    $(form).find(":input").prop("disabled", true);
    return false;
}
