/**
 * Created by alex on 28.08.16.
 */

function create_boxes(selobjs) {
    return function (data) {
        $.each(selobjs, function (j, selobj) {
            var checkboxs_name = selobj.attr('id');
            $.each(data.data, function (i, obj) {
                $(selobj).append($("<input />", {type:"checkbox", id:checkboxs_name+i, value:obj.id, name:checkboxs_name}));
                $(selobj).append($("<label/>", {"for": checkboxs_name+i, text:obj.name}));
                $(selobj).append($("<br/>"));
            })
        })
    }
}


function check_item(type) {
    return function () {
        var item = $(this);
        var checked_val = parseInt($(this).val(), 10);
        var disabledBy = checked_val + ':'+type;
        if ($(this).is(':checked')) {
            $.getJSON("/add_option", {type: type, data: checked_val}, function (e) {
                var disableItIds = [];
                var disableIt = $();
                $(e.not_forbidden).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $('#forbiddenWith').find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id+":"+"forbiddenWith");
                });
                $(e.not_required_from).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id+":"+"requiredFrom");
                });

                $(disableIt).attr("disabled", true);

                $(disableIt).each(function (i, obj) {
                    if ($(obj).data('disabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                        $(obj).data('disabledBy', [disabledBy]);
                    } else {
                        $(obj).data('disabledBy').push(disabledBy);
                    }
                });
                $(item).data("disableIt", disableItIds);
            });
        } else {
            var enable = $();
            $($(this).data("disableIt")).each(function (i, obj) {
                var finder = obj.split(":");
                var maybeEnable = $('#'+finder[1]).find("input[value="+finder[0]+"]");
                $(maybeEnable).data("disabledBy").splice($.inArray(obj, $(maybeEnable).data("disabledBy")), 1);
                if ($(maybeEnable).data("disabledBy").length === 0)
                    enable = $.merge(enable, maybeEnable);
            });

            $(enable).prop('disabled', false);
        }
    }
}

function prepare() {
    var requiredFrom = $("#requiredFrom");
    var forbiddenWith = $("#forbiddenWith");
    var forTariffs = $('#forTariffs');

    forTariffs.empty();
    $.getJSON("/load_tariffs", {page:-1, updateCount:false, search:""}, create_boxes([forTariffs]));

    $(forTariffs).on('change', 'input[type=checkbox]',  function (e) {
        e.preventDefault();
        requiredFrom.empty();
        forbiddenWith.empty();
        $.getJSON("/load_options_by_tariffs", $("#forTariffs").find("input").serialize(), create_boxes([requiredFrom, forbiddenWith]));
    });

    $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom"));
    $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden"));
}