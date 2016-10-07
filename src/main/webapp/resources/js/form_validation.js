/**
 * Created by alex on 20.08.16.
 */


/*$.fn.serializeObject = function()
 {
 var o = {};
 var a = this.serializeArray();
 $.each(a, function() {
 if (o[this.name] !== undefined) {
 if (!o[this.name].push) {
 o[this.name] = [o[this.name]];
 }
 o[this.name].push(this.value || '');
 } else {
 o[this.name] = this.value || '';
 }
 });
 return o;
 };*/

$.validator.addMethod("no_spaces", function (val) {
    return val.indexOf(' ') === -1;
});

$.validator.addMethod("dateAb18", function (val) {
    var date = val.split("-");
    var orig_date = new Date(date[0], date[1] - 1, date[2]);
    // Ну почему, по какой причине в этом js месяцы начинаются с 0?! Неужели было так сложно сделать январь первым, а не нулевым?!
    var today = new Date();
    return orig_date.setFullYear(orig_date.getFullYear() + 18) <= today;
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
    errorPlacement: function (error) {
        $.notify(error.text(), {position: "top right", className: "error"});
    },
    highlight: function (element, errorClass) {
        $(element).parent().parent().addClass(errorClass);
        $(element).fadeOut(function () {
            $(element).fadeIn();
        });
    },
    unhighlight: function (element, errorClass) {
        $(element).parent().parent().removeClass(errorClass);
    },
    submitHandler: submitting()
});


function submitting(full_success) {
    return function (form, e) {
        e.preventDefault();
        $.notify("Sending data..", {position: "top right", className: "success"});

        $(form).find("input[type=checkbox]").prop("disabled", false);
        $.ajax({
            url: window.contextPath + $(form).attr("action"),
            data: JSON.stringify($(form).serializeObject()),
            method: "POST",
            contentType: "application/json",
            success: function (response) {
                $.notify("Success!", {position: "top right", className: "success"});
                $(form)[0].reset();
                if (full_success !== undefined) {
                    full_success(response);
                }
                $(form).find(":input").prop("disabled", false);
            },
            error: function (jqXHR) {
                $.each(jqXHR.responseJSON.errors, function (prop, val) {
                    $.notify("Error: in " + prop + "\n" + val, {position: "top right", className: "error"});
                });
                $(form).find(":input").prop("disabled", false);
            }
        });
        $(form).find(":input").prop("disabled", true);
        return false;
    }
}
