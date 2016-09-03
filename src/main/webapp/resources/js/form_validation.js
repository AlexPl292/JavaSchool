/**
 * Created by alex on 20.08.16.
 */


function valid_inputs(obj) {
    //var inputs = $(obj).find("input[type=text],input[type=date],input[type=email],textarea");
    var inputs = $(obj).find(".required");
    inputs.removeClass("has-error");

    var empty = inputs.filter(function() {
        return this.value === "";
    });
    if(empty.length) {
        empty.addClass("has-error");
        return false;
    }
    return true;
}
