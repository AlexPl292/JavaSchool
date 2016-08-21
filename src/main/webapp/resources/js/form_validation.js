/**
 * Created by alex on 20.08.16.
 */


function valid_inputs(obj) {
    //var inputs = $(obj).find("input[type=text],input[type=date],input[type=email],textarea");
    var inputs = $(obj).find(".required");
    inputs.removeClass("warning");

    var empty = inputs.filter(function() {
        return this.value === "";
    });
    if(empty.length) {
        empty.addClass("warning");
        return false;
    }
    return true;
}
