/**
 * Created by alex on 28.08.16.
 */

function loadlist(selobjs, checkbox_names, url, nameattr, valattr) {
    $(selobjs).empty();
    $.getJSON(url, {page:-1, updateCount:false, search:""}, function (data) {
        $.each(selobjs, function (j, selobj) {
            $.each(data.data, function (i, obj) {
                $(selobj).append($("<input />", {type:"checkbox", id:"rcb"+i, value:obj[valattr], name:checkbox_names[j]}));
                $(selobj).append($("<label/>", {"for": "rcb"+i, text:obj[nameattr]}));
                $(selobj).append($("<br/>"));
            })
        })
    });
}

function check_item(type, state) {
    return function () {
        var checked_val = parseInt($(this).val(), 10);
        if ($(this).is(':checked')) {
            $.getJSON("/add_option", {type: type, data: checked_val}, function (e) {
                if (!state.disablesIt.forbiddenWith.hasOwnProperty(checked_val)) {
                    state.disablesIt.forbiddenWith[checked_val] = [];
                }
                if (!state.disablesIt.requiredFrom.hasOwnProperty(checked_val)) {
                    state.disablesIt.requiredFrom[checked_val] = [];
                }
                if (!state.disablesIt.requiredMe.hasOwnProperty(checked_val)) {
                    state.disablesIt.requiredMe[checked_val] = [];
                }

                $(e.not_forbidden).each(function (i, obj) {
                    $('#forbiddenWith').find("input[value=" + obj.id + "]").attr("disabled", true);

                    state.disablesIt.forbiddenWith[checked_val].push(obj.id);
                    if (!state.disabledBy.forbiddenWith.hasOwnProperty(obj.id)) {
                        state.disabledBy.forbiddenWith[obj.id] = [checked_val];
                    } else {
                        state.disabledBy.forbiddenWith[obj.id].push(checked_val);
                    }
                });
                $(e.not_required_from).each(function (i, obj) {
                    $('#requiredFrom').find("input[value=" + obj.id + "]").attr("disabled", true);

                    state.disablesIt.requiredFrom[checked_val].push(obj.id);
                    if (!state.disabledBy.requiredFrom.hasOwnProperty(obj.id)) {
                        state.disabledBy.requiredFrom[obj.id] = [checked_val];
                    } else {
                        state.disabledBy.requiredFrom[obj.id].push(checked_val);
                    }
                });
                $(e.not_required_me).each(function (i, obj) {
                    $('#requiredMe').find("input[value=" + obj.id + "]").attr("disabled", true);

                    state.disablesIt.requiredMe[checked_val].push(obj.id);
                    if (!state.disabledBy.requiredMe.hasOwnProperty(obj.id)) {
                        state.disabledBy.requiredMe[obj.id] = [checked_val];
                    } else {
                        state.disabledBy.requiredMe[obj.id].push(checked_val);
                    }
                });
            })
        } else {
            state.disablesIt.forbiddenWith[checked_val].forEach(function (id) {
                var tmp = state.disabledBy.forbiddenWith[id];
                tmp.splice($.inArray(checked_val, tmp), 1);
                if (tmp.length === 0)
                    $('#forbiddenWith').find("input[value=" + id + "]").removeAttr("disabled");
            });
            delete state.disablesIt.forbiddenWith[checked_val];

             state.disablesIt.requiredFrom[checked_val].forEach(function (id) {
                var tmp = state.disabledBy.requiredFrom[id];
                tmp.splice($.inArray(checked_val, tmp), 1);
                if (tmp.length === 0)
                    $('#requiredFrom').find("input[value=" + id + "]").removeAttr("disabled");
            });
            delete state.disablesIt.requiredFrom[checked_val];

             state.disablesIt.requiredMe[checked_val].forEach(function (id) {
                var tmp = state.disabledBy.requiredMe[id];
                tmp.splice($.inArray(checked_val, tmp), 1);
                if (tmp.length === 0)
                    $('#requiredMe').find("input[value=" + id + "]").removeAttr("disabled");
            });
            delete state.disablesIt.requiredMe[checked_val];

        }
    }
}

function prepare() {
    var state = {disabledBy:{forbiddenWith:{}, requiredFrom:{}, requiredMe:{}},
        disablesIt:{forbiddenWith:{}, requiredFrom:{}, requiredMe:{}}}; //TODO тонкий момент с этой штукой
    var requiredFrom = $("#requiredFrom");
    var forbiddenWith = $("#forbiddenWith");
    var requiredMe = $("#requiredMe");
    loadlist([requiredFrom, forbiddenWith, requiredMe], ["requiredFrom", "forbiddenWith", "requiredMe"], "/show_options", "name", "id");

    $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom", state));
    $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden", state));
    $(requiredMe).on('change', 'input[type=checkbox]', check_item("requiredMe", state));
}