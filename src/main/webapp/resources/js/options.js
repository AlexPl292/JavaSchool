/**
 * Created by alex on 28.08.16.
 */

function create_boxes(selobjs) {
    return function (data) {
        $.each(selobjs, function (j, selobj) {
            $(selobj).empty();
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
            $.getJSON("/admin/load_option", {"loadtype": "toDisable", type: type, data: checked_val}, function (e) {
                var disableItIds = [];
                var disableIt = $();
                var enableItIds = [];
                var enableIt = $();

                $(e.not_forbidden).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $('#forbiddenWith').find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id+":"+"forbiddenWith");
                });
                $(e.not_required_from).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id+":"+"requiredFrom");
                });
                $(e.not_forbidden).each(function (i, obj) {
                    if (obj.id === checked_val)
                        return;
                    enableIt = $.merge(enableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                    enableItIds.push(obj.id);
                });

                enableIt.prop('checked', true).attr("disabled", true);
                $(disableIt).attr("disabled", true);

                $(disableIt).each(function (i, obj) {
                    if ($(obj).data('disabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                        $(obj).data('disabledBy', [disabledBy]);
                    } else {
                        $(obj).data('disabledBy').push(disabledBy);
                    }
                });
                $(enableIt).each(function (i, obj) {
                    if ($(obj).data('enabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                        $(obj).data('enabledBy', [checked_val]);
                    } else {
                        $(obj).data('enabledBy').push(checked_val);
                    }
                });
                $(item).data("disableIt", disableItIds);
                $(item).data("enableIt", enableItIds);
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

            var uncheck = $();
            $($(this).data("enableIt")).each(function (i, obj) {
                var maybeEnable = $('#requiredFrom').find("input[value=" + obj + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("enabledBy").splice($.inArray(obj, $(maybeEnable).data("enabledBy")), 1);
                if ($(maybeEnable).data("enabledBy").length === 0)
                    uncheck = $.merge(uncheck, maybeEnable);
            });
            $(uncheck).prop('checked', false).prop('disabled', false).change();
        }
    }
}


function optionChecked(options) {
    return function (e) {
        e.preventDefault();
        var $item = $(this);
        var checked_val = parseInt($(this).val(), 10);
        if ($item.is(':checked')) {
            $.getJSON("/admin/load_option", {"loadtype": "getDependencies", data: checked_val}, function (response) {
                var disableItIds = [];
                var disableIt = $();
                var enableItIds = [];
                var enableIt = $();

                $(response.required).each(function (i, obj) {
                    enableIt = $.merge(enableIt, $(options).find("input[value=" + obj.id + "]"));
                    enableItIds.push(obj.id);
                });
                $(response.forbidden).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $(options).find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id);
                });

                enableIt.prop('checked', true).attr("disabled", true);
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
                var maybeEnable = $(options).find("input[value=" + obj + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("disabledBy").splice($.inArray(obj, $(maybeEnable).data("disabledBy")), 1);
                if ($(maybeEnable).data("disabledBy").length === 0)
                    enable = $.merge(enable, maybeEnable);
            });
            $(enable).prop('disabled', false);

            var uncheck = $();
            $($(this).data("enableIt")).each(function (i, obj) {
                var maybeEnable = $(options).find("input[value=" + obj + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("enabledBy").splice($.inArray(obj, $(maybeEnable).data("enabledBy")), 1);
                if ($(maybeEnable).data("enabledBy").length === 0)
                    uncheck = $.merge(uncheck, maybeEnable);
            });
            $(uncheck).prop('checked', false).prop('disabled', false).change();
        }
    }
}

function optionCheckedNewTariff(e) {
    e.preventDefault();
    var $item = $(this);
    var checked_val = parseInt($(this).val(), 10);
    if ($item.is(':checked')) {
        $.getJSON("/admin/load_option", {"loadtype": "getDependencies", data: checked_val}, function (response) {
            var disableItIds = [];
            var disableIt = $();
            var enableItIds = [];
            var enableIt = $();

            $(response.required).each(function (i, obj) {
                enableIt = $.merge(enableIt, $('#options').find("input[value=" + obj.id + "]"));
                enableItIds.push(obj.id);
            });

            enableIt.prop('checked', true).attr("disabled", true); //.attr('onclick', 'return false');

            $(enableIt).each(function (i, obj) {
                if ($(obj).data('enabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                    $(obj).data('enabledBy', [checked_val]);
                } else {
                    $(obj).data('enabledBy').push(checked_val);
                }
            });

            $item.data("enableIt", enableItIds);
        })
    } else {
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

function loadlist(selobj, url, nameattr, valattr) {
    $(selobj).empty();
    $.getJSON(url, {page: -1, updateCount: false, search: ""}, function (data) {
        $.each(data.data, function (i, obj) {
            $(selobj).append($("<option></option>").val(obj[valattr]).html(obj[nameattr]));
        });
        $(selobj).change();
    });
}

function prepare_tariff_list(tariffList, options) {
    $(tariffList).change(function (e) {
        e.preventDefault();
        $.getJSON("/admin/load_option", {
            loadtype: "possibleOfTariff",
            tariff_id: $(this).val()
        }, create_boxes([$(options)]));
    });
    loadlist($(tariffList), "/admin/load_tariffs", "name", "id");

    $(options).on('change', 'input[type=checkbox]', optionChecked(options));
}


function edit_tariff(panel) {
    var panel_backup = $(panel).clone().children();
    var id = $(panel).find('input[name=contract_id]').val();
    var tariff = $('<div class="control-group">'+
        '<label class="control-label" for="tariffEdit'+id+'" >Tariff</label>'+
        '<div class="controls">'+
        '<select id="tariffEdit'+id+ '" name="tariff" class="form-control"></select>'+
        "</div>"+
        '</div>');

    var options = $('<div class="control-group">'+
        '<label class="control-label" for="optionsEdit'+id+'">Options</label>'+
        '<div class="controls">'+
        '<div id="optionsEdit'+id+'" class="boxes"></div>'+
        '</div>'+
        '</div>');

    var leftPanel = $(panel).find('.col-lg-6:first');
    var rightPanel = $(panel).find('.col-lg-6:last .well');
    leftPanel.html(tariff);
    rightPanel.html(options);
    prepare_tariff_list($('#tariffEdit'+id), $('#optionsEdit'+id));
    $(panel).find('.panel-heading .pull-right').html('<button type="button" class="btn btn-outline btn-danger btn-xs">Exit editing</button>');
    $(panel).find('button:contains("Exit editing")').click(function (e) {
        e.preventDefault();
        $(panel).html(panel_backup);
    });
    $(panel).find('.panel-body').append('<div class="col-lg-12"><div class="controls"><input type="submit" class="btn btn-success"/></div></div>');
    $(panel).find('.panel-body').wrapInner('<form class="form-horizontal" action="edit_contract" method="POST"></form>');
    $(panel).find('form').submit({panel:$(panel)}, edit_handler);
}

function edit_handler(e) {
    e.preventDefault();
    var panel = e.data.panel;
    var form = panel.find("form");
    $(form).find("input[type=checkbox]").prop("disabled", false);
    $.post($(form).attr("action"), $(form).serialize(), function (e) {
        if (e.success) {
            $.notify("Success!", {position: "top right", className: "success"});
            var filling = fill_accordion_node(e.data);
            $(panel).find(".panel-body").empty();
            $(panel).find(".panel-body").append(filling[0]);
            $(panel).find(".panel-body").append(filling[1]);
            $(panel).find(".panel-body").append(filling[2]);
            $(panel).find(".panel-title .pull-right").empty();
            $(panel).find(".panel-title .pull-right").append(create_panel_menu());
        } else {
            $.each(e.errors, function (prop, val) {
                $.notify("Error: in " + prop + "\n" + val, {position: "top right", className: "error"});
            });
        }
    });
    $(form).find(":input").prop("disabled", true);
}

function fill_accordion_node(data) {
    var $used_options = $('<ul></ul>');
    for (var i = 0; i < data.usedOptions.length; i++) {
        $used_options.append($('<li>', {text:data.usedOptions[i].name}));
    }

    var col_lg1 = $('<div class="col-lg-6">' +
        '<h3>'+data.tariff.name+'</h3>' +
        '<hr>' +
        data.tariff.description +
        '</div>');
    var col_lg2 = $('<div class="col-lg-6"><div class="well">' +
        '<h4><small>Used options</small></h4>' +
        '<hr>'+
        '</div>' +
        '</div>');
    col_lg2.find(".well").append($used_options);

    var id = '<input type="hidden" name="contract_id" value="'+data.id+'"/>';

    return [id, col_lg1, col_lg2];
}

function create_accordion_node(res) {
    var data = res.data;

    var $node = $('<div class="panel panel-success">' +
        '<div class="panel-heading">' +
        '<h4 class="panel-title">' +
        '<a data-toggle="collapse" data-parent="#accordion" href="#collapse'+data.id+'" >'+data.number+'</a>' +
            '<div class="pull-right"></div>'+
        '</h4>' +
        '</div>' +
        '<div id="collapse'+data.id+'" class="panel-collapse collapse" style="height: 0px;">' +
        '<div class="panel-body"' +
        '</div>' +
        '</div>' +
        '</div>');
    var filling = fill_accordion_node(data);
    $node.find(".panel-body").append(filling[0]);
    $node.find(".panel-body").append(filling[1]);
    $node.find(".panel-body").append(filling[2]);
    $node.find(".panel-title .pull-right").append(create_panel_menu());
    $('#accordion').prepend($node);
}

function create_panel_menu() {
    return $('<div class="btn-group">'+
        '<button type="button" class="btn btn-default btn-xs dropdown-toggle"'+
            ' data-toggle="dropdown" aria-expanded="false">'+
        'Actions'+
        '<span class="caret"></span>'+
        '</button>'+
        '<ul class="dropdown-menu pull-right" role="menu">'+
        '<li><a href="/editTariff"><p >Edit</p></a>'+
        '</li>'+
        '<li><a href=\"/admin/blockContract\"><p>Block</p></a>'+
        '</li>'+
        '<li class="divider"></li>'+
        '<li><a href="/admin/delete_contract"><p class="text-danger">Delete</p></a>'+
        '</li>'+
        '</ul>'+
        '</div>');
}
var prepare = {
    "/admin/customer" : function() {
        prepare_tariff_list($('#tariff'), $('#options'));


        $("#accordion").on("click", "ul[role='menu'] a", function (e) {
            e.preventDefault();
            if ($(this).find('p').hasClass("text-muted")) {
                return false;
            }
            var $panel = $(this).closest('.panel');
            var id = $panel.find('input[type=hidden]').val();
            var href = $(this).attr("href");
            var $obj = $(this);

            if (href === "/admin/delete_contract") {
                $.post(href, {id: id}, function (e) {
                    $panel.remove();
                })
            } else if (href === "/admin/blockContract") {
                $.post(href, {id: id}, function (e) {
                    $panel.removeClass("panel-default").addClass("panel-red");
                    $obj.attr("href", "/admin/unblockContract").text("Unblock");
                    $panel.find(":contains('Edit')").addClass("text-muted");
                });
            } else if (href === "/admin/unblockContract") {
                $.post(href, {id: id}, function (e) {
                    $panel.removeClass("panel-red").addClass("panel-default");
                    $obj.attr("href", "/admin/blockContract").text("Block");
                    $panel.find(":contains('Edit')").removeClass("text-muted");
                });
            } else if (href === "/editTariff") {
                edit_tariff($panel)
            }
        })
    },
    "/admin/add_customer" : function() {
        prepare_tariff_list($('#tariff'), $('#options'));
    },
    "/admin/add_option" : function () {
        var requiredFrom = $("#requiredFrom");
        var forbiddenWith = $("#forbiddenWith");
        var forTariffs = $('#forTariffs');

        forTariffs.empty();
        $.getJSON("/admin/load_tariffs", {page:-1, updateCount:false, search:""}, create_boxes([forTariffs]));

        // TODO add disablind or notifications
        $(forTariffs).on('change', 'input[type=checkbox]',  function (e) {
            e.preventDefault();
            requiredFrom.empty();
            forbiddenWith.empty();
            var data = $("#forTariffs").find("input").serializeArray();
            data.push({"loadtype": "newOptionDependency"});
            $.getJSON("/admin/load_option", $.param(data), create_boxes([requiredFrom, forbiddenWith]));
        });

        $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom"));
        $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden"));
    },
    "/admin/add_tariff" : function () {
        $('#options').on('change', 'input[type=checkbox]', optionCheckedNewTariff);
        $.getJSON("/admin/load_options_table", {}, create_boxes([$('#options')]));
    }
};
$(function () {
    var path = window.location.pathname;
    if (path in prepare)
        prepare[path]();
});