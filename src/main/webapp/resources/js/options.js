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
            $.getJSON("/load_options", {"loadtype": "toDisable", type: type, data: checked_val}, function (e) {
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
                if (maybeEnable.length === 0)
                    return;
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

    // TODO add disablind or notifications
    $(forTariffs).on('change', 'input[type=checkbox]',  function (e) {
        e.preventDefault();
        requiredFrom.empty();
        forbiddenWith.empty();
        var data = $("#forTariffs").find("input").serializeArray();
        data.push({"loadtype": "newOptionDependency"});
        $.getJSON("/load_options", $.param(data), create_boxes([requiredFrom, forbiddenWith]));
    });

    $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom"));
    $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden"));
}

function optionChecked(e) {
    e.preventDefault();
    var $item = $(this);
    var checked_val = parseInt($(this).val(), 10);
    if ($item.is(':checked')) {
        $.getJSON("/load_options", {"loadtype": "getDependencies", data: checked_val}, function (response) {
            var disableItIds = [];
            var disableIt = $();
            var enableItIds = [];
            var enableIt = $();

            $(response.required).each(function (i, obj) {
                enableIt = $.merge(enableIt, $('#options').find("input[value=" + obj.id + "]"));
                enableItIds.push(obj.id);
            });
            $(response.forbidden).each(function (i, obj) {
                disableIt = $.merge(disableIt, $('#options').find("input[value=" + obj.id + "]"));
                disableItIds.push(obj.id);
            });

            enableIt.prop('checked', true).attr("disabled", true); //.attr('onclick', 'return false');
            disableIt.attr("disabled", true);

            $(disableIt).each(function (i, obj) {
                if ($(obj).data('disabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                    $(obj).data('disabledBy', [checked_val]);
                } else {
                    $(obj).data('disabledBy').push(checked_val);
                }
            });
            $(enableIt).each(function (i, obj) {
                if ($(obj).data('enabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                    $(obj).data('enabledBy', [checked_val]);
                } else {
                    $(obj).data('enabledBy').push(checked_val);
                }
            });

            $item.data("disableIt", disableItIds);
            $item.data("enableIt", enableItIds);
        })
    } else {
        var enable = $();
        $($(this).data("disableIt")).each(function (i, obj) {
            var maybeEnable = $('#options').find("input[value="+obj+"]");
            if (maybeEnable.length === 0)
                return;
            $(maybeEnable).data("disabledBy").splice($.inArray(obj, $(maybeEnable).data("disabledBy")), 1);
            if ($(maybeEnable).data("disabledBy").length === 0)
                enable = $.merge(enable, maybeEnable);
        });
        $(enable).prop('disabled', false);

        var uncheck = $();
        $($(this).data("enableIt")).each(function (i, obj) {
            var maybeEnable = $('#options').find("input[value="+obj+"]");
            if (maybeEnable.length === 0)
                return;
            $(maybeEnable).data("enabledBy").splice($.inArray(obj, $(maybeEnable).data("enabledBy")), 1);
            if ($(maybeEnable).data("enabledBy").length === 0)
                uncheck = $.merge(uncheck, maybeEnable);
        });
        $(uncheck).prop('checked', false).prop('disabled', false).change();//.removeAttr('onclick').change();
    }
}