/**
 * Created by alex on 28.08.16.
 */

function create_boxes(selobj, checkboxs_name, checked) {
    return function (data) {
        $(selobj).empty();
        $.each(data, function (i, obj) {
            var $box = $("<input />", {
                type: "checkbox",
                id: checkboxs_name + i,
                value: obj.id,
                name: checkboxs_name
            });
            $box.data('boxData', obj);
            var name = obj.name;
            if (obj.connectCost !== undefined)
                name += ' <p class="text-muted" style="display:inline">(' + obj.connectCost + '<i class="fa fa-rub"></i>)</p>';
            var div = $('<div class="checkbox checkbox-primary"> </div>');
            $(div).append($box);
            $(div).append($("<label/>", {"for": checkboxs_name + i, html: name}));
            $(div).append($("<br/>"));
            $(selobj).append($(div));
        });
        if (checked !== undefined) {
            $(checked).each(function () {
                var opt = $(selobj).find("input[value="+this+"]");
                if (!$(opt).is(':disabled')) {
                    $(opt).prop('checked', true).change();
                }
            })
        }
    }
}

function check_item(type) {
    return function () {
        var item = $(this);
        var checked_val = parseInt($(this).val(), 10);
        var disabledBy = checked_val + ':' + type;
        if ($(this).is(':checked')) {
            $.getJSON(window.contextPath + "/rest/options/" + checked_val, {}, function (data) {
                var disableItIds = [];
                var disableIt = $();
                var enableItIds = [];
                var enableIt = $();

                if (type === "requiredFrom") {
                    $(data.requiredFrom).each(function (i, obj) {
                        disableIt = $.merge(disableIt, $('#forbiddenWith').find("input[value=" + obj.id + "]"));
                        disableItIds.push(obj.id + ":" + "forbiddenWith");

                        enableIt = $.merge(enableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                        enableItIds.push(obj.id);
                    });
                    $(data.forbiddenWith).each(function (i, obj) {
                        disableIt = $.merge(disableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                        disableItIds.push(obj.id + ":" + "requiredFrom");
                    });
                    disableIt = $.merge(disableIt, $('#forbiddenWith').find("input[value=" + checked_val + "]"));
                    disableItIds.push(checked_val + ":" + "forbiddenWith");
                } else {
                    $(data.requiredMe).each(function (i, obj) {
                        disableIt = $.merge(disableIt, $('#requiredFrom').find("input[value=" + obj.id + "]"));
                        disableItIds.push(obj.id + ":" + "requiredFrom");
                    });
                    disableIt = $.merge(disableIt, $('#requiredFrom').find("input[value=" + checked_val + "]"));
                    disableItIds.push(checked_val + ":" + "requiredFrom");
                }

                enableIt.prop('checked', true).attr("disabled", true).closest('.checkbox').addClass("checkbox-success").removeClass("checkbox-prime");
                $(disableIt).attr("disabled", true).closest('.checkbox').addClass("no checkbox-danger").removeClass("checkbox-prime");

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
                var maybeEnable = $('#' + finder[1]).find("input[value=" + finder[0] + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("disabledBy").splice($.inArray(obj, $(maybeEnable).data("disabledBy")), 1);
                if ($(maybeEnable).data("disabledBy").length === 0)
                    enable = $.merge(enable, maybeEnable);
            });

            $(enable).prop('disabled', false).closest('.checkbox').addClass("checkbox-prime").removeClass("checkbox-danger no");

            var uncheck = $();
            $($(this).data("enableIt")).each(function (i, obj) {
                var maybeEnable = $('#requiredFrom').find("input[value=" + obj + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("enabledBy").splice($.inArray(obj, $(maybeEnable).data("enabledBy")), 1);
                if ($(maybeEnable).data("enabledBy").length === 0)
                    uncheck = $.merge(uncheck, maybeEnable);
            });
            $(uncheck)
                .prop('checked', false)
                .prop('disabled', false).change()
                .closest('.checkbox')
                .addClass("checkbox-prime")
                .removeClass("checkbox-success");
        }
    }
}


function optionChecked(options) {
    return function (e) {
        e.preventDefault();
        var $item = $(this);
        var checked_val = parseInt($(this).val(), 10);
        if ($item.is(':checked')) {
            $.getJSON(window.contextPath + "/rest/options/" + checked_val, {}, function (response) {
                var disableItIds = [];
                var disableIt = $();
                var enableItIds = [];
                var enableIt = $();

                $(response.requiredFrom).each(function (i, obj) {
                    enableIt = $.merge(enableIt, $(options).find("input[value=" + obj.id + "]"));
                    enableItIds.push(obj.id);
                });
                $(response.forbiddenWith).each(function (i, obj) {
                    disableIt = $.merge(disableIt, $(options).find("input[value=" + obj.id + "]"));
                    disableItIds.push(obj.id);
                });

                enableIt.prop('checked', true).attr("disabled", true).closest('.checkbox').addClass("checkbox-success").removeClass("checkbox-prime");
                disableIt.attr("disabled", true).closest('.checkbox').addClass("no checkbox-danger").removeClass("checkbox-prime");

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
            $(enable).prop('disabled', false).closest('.checkbox').addClass("checkbox-prime").removeClass("checkbox-danger no");

            var uncheck = $();
            $($(this).data("enableIt")).each(function (i, obj) {
                var maybeEnable = $(options).find("input[value=" + obj + "]");
                if (maybeEnable.length === 0)
                    return;
                $(maybeEnable).data("enabledBy").splice($.inArray(obj, $(maybeEnable).data("enabledBy")), 1);
                if ($(maybeEnable).data("enabledBy").length === 0)
                    uncheck = $.merge(uncheck, maybeEnable);
            });
            $(uncheck)
                .prop('checked', false)
                .prop('disabled', false)
                .change()
                .closest('.checkbox')
                .addClass("checkbox-prime")
                .removeClass("checkbox-success");
        }
    }
}

function optionCheckedNewTariff(e) {
    e.preventDefault();
    var $item = $(this);
    var checked_val = parseInt($(this).val(), 10);
    var data = $item.data('boxData');
    if ($item.is(':checked')) {
        var enableIt = $();

        $(data.requiredFrom).each(function (i, obj) {
            enableIt = $.merge(enableIt, $('#possibleOptions').find("input[value=" + obj.id + "]"));
        });

        enableIt.prop('checked', true).attr("disabled", true).closest('.checkbox').addClass("checkbox-success").removeClass("checkbox-prime");

        $(enableIt).each(function (i, obj) {
            if ($(obj).data('enabledBy') === undefined) { // DisabledBy is not set (this checkbox is disabled first time
                $(obj).data('enabledBy', [checked_val]);
            } else {
                $(obj).data('enabledBy').push(checked_val);
            }
        });
    } else {
        var uncheck = $();
        $(data.requiredFrom).each(function (i, obj) {
            var maybeEnable = $('#possibleOptions').find("input[value=" + obj.id + "]");
            if (maybeEnable.length === 0)
                return;
            $(maybeEnable).data("enabledBy").splice($.inArray(obj.id, $(maybeEnable).data("enabledBy")), 1);
            if ($(maybeEnable).data("enabledBy").length === 0)
                uncheck = $.merge(uncheck, maybeEnable);
        });
        $(uncheck).prop('checked', false).prop('disabled', false).change().closest('.checkbox')
                .addClass("checkbox-prime")
                .removeClass("checkbox-success");
    }
}

function loadlist(selobj, url, nameattr, valattr, selected_val) {
    $(selobj).empty();
    $.getJSON(url, {}, function (data) {
        $.each(data, function (i, obj) {
            $(selobj).append($("<option></option>").val(obj[valattr]).html(obj[nameattr]));
        });
        if (selected_val !== undefined)
            $(selobj).val(selected_val);
        $(selobj).change();
    });
}

function prepare_tariff_list(tariffList, options, selected_val, boxes_name, checked) {
    $(options).on('change', 'input[type=checkbox]', optionChecked(options));
    $(tariffList).change(function (e) {
        e.preventDefault();
        $.getJSON(window.contextPath + "/rest/tariffs/" + $(this).val() + '/options', {}, create_boxes($(options), boxes_name, checked));
    });
    loadlist($(tariffList), window.contextPath + "/rest/tariffs", "name", "id", selected_val);
}

function fill_accordion_node(data) {
    var $used_options = $('<ul></ul>');
    for (var i = 0; i < data.usedOptions.length; i++) {
        $used_options.append($('<li>', {text: data.usedOptions[i].name}));
    }

    var col_lg1 = $('<div class="col-lg-6">' +
        '<h3>' + data.tariff.name + '</h3>' +
        '<hr>' +
        data.tariff.description +
        '</div>');
    var col_lg2 = $('<div class="col-lg-6"><div class="well">' +
        '<h4><small>Used options</small></h4>' +
        '<hr>' +
        '</div>' +
        '</div>');
    col_lg2.find(".well").append($used_options);

    var id = '<input type="hidden" name="contractId" value="' + data.id + '"/>';

    return [id, col_lg1, col_lg2];
}

function create_accordion_node(data) {
    var $node = $('<div class="panel panel-success">' +
        '<div class="panel-heading">' +
        '<h4 class="panel-title">' +
        '<a data-toggle="collapse" data-parent="#accordion" href="#collapse' + data.id + '" >' + data.number + '</a> ' +
        '<small id="balance" >' + (data.balance).toFixed(2) + ' <i class="fa fa-rub"></i></small>' +
        '<div class="pull-right"></div>' +
        '</h4>' +
        '</div>' +
        '<div id="collapse' + data.id + '" class="panel-collapse collapse" style="height: 0px;">' +
        '<div class="panel-body"' +
        '</div>' +
        '</div>' +
        '</div>');
    var filling = fill_accordion_node(data);
    $node.find(".panel-body").append(filling[0]);
    $node.find(".panel-body").append(filling[1]);
    $node.find(".panel-body").append(filling[2]);
    $node.find(".panel-title .pull-right").append(create_panel_menu(data));
    $('#accordion').prepend($node);
}

function create_panel_menu(data) {
    var disable_edit = data.isBlocked !== 0 || data.balance < 0;
    return $('<div class="btn-group">' +
        '<button type="button" class="btn btn-default btn-xs dropdown-toggle"' +
        ' data-toggle="dropdown" aria-expanded="false">' +
        'Actions' +
        ' <span class="caret"></span>' +
        '</button>' +
        '<ul class="dropdown-menu pull-right" role="menu">' +
        '<li id="menuEdit"><a href="/edit"><p ' + (disable_edit ? 'class="text-muted"' : '') + '>Edit</p></a>' +
        '</li>' +
        '<li id="menuBlock"><a href=\"/block\"><p>Block</p></a>' +
        '</li>' +
        '<li class="divider"></li>' +
        '<li><a href="/delete"><p class="text-danger">Delete</p></a>' +
        '</li>' +
        '</ul>' +
        '</div>');
}

function loadpage(page) {
    var $sidemenu = $('#side-menu');
    var currentPage = getFromSessionStorage("currentPage");
    $sidemenu.find('#' + currentPage + "_menu").removeClass("active");

    var element = $sidemenu.find('#' + page + "_menu");
    if (element !== undefined) {
        $(element).closest('ul').addClass('in').attr('aria-expanded', true);
        $(element).addClass("active");
    }
    storeToSessionStorage("currentPage", page);
    var $page_wrapper = $('#page-wrapper');
    $page_wrapper.empty();

    if (prepare[page] !== undefined)
        prepare[page]($page_wrapper);
}

function formatAdditionalData(d, field, prop, title, empty) {
    var list = '<h4><small>' + title + '</small></h4><ul>';
    if (d[field].length === 0) {
        list += '</ul>' + empty;
        return list;
    }
    for (var i = 0; i < d[field].length; i++) {
        list += '<li>' + d[field][i][prop] + '</li>';
    }
    list += '</ul>';
    return list;
}

function addTableAdditional(table, field, prop, title, empty) {
    $('#content_table').find('tbody').on('click', 'td i.fa', function () {
        var tr = $(this).closest('tr');
        var row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            $(this).removeClass('fa-minus-square').addClass('fa-plus-square');
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            $(this).removeClass('fa-plus-square').addClass('fa-minus-square');
            row.child(formatAdditionalData(row.data(), field, prop, title, empty)).show();
            tr.addClass('shown');
        }
    });
}

function chooseOptionsForTariff($tariffs) {
    var checkedTariffs = $tariffs.find(':checked');
    if (checkedTariffs.length === 0)
        return [];
    var options = $(checkedTariffs[0]).data('boxData').possibleOptions;
    for (var i = 1; i < checkedTariffs.length; i++) {
        options = options.filter(function (n) {
            return $(checkedTariffs[i]).data('boxData').possibleOptions.map(function (e) {
                    return e.id
                }).indexOf(n.id) != -1;
        })
    }

    return options;
}

var prepare = {
    "customers": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_table');
        $page_wrapper.append(content.cloneNode(true));
        $page_wrapper.find('h1').text('Customers');
        $page_wrapper.find('div.panel-heading').text('Show all customers');

        var table = $('#content_table').DataTable({
            ajax: {
                url: window.contextPath + "/rest/customers",
                dataSrc: ''
            },
            order: [[0, 'asc']],
            stateSave: true,
            pagingType: "full_numbers",
            columns: [
                {
                    title: "Name",
                    data: null,
                    className: 'clickableRow',
                    render: function (data, type, row) {
                        if (type === "display") {
                            return '<i class="fa fa-plus-square" style="padding-right: 1.8em"></i>' + data.surname + ' ' + data.name;
                        }
                        return data.surname + ' ' + data.name;
                    }
                },
                {
                    title: "Date ob birth", data: "dateOfBirth", render: function (data, type, row) {
                    if (type === 'display' || type === 'filter') {
                        var d = new Date(data);
                        return ('0' + d.getDate()).slice(-2) + '-' + ('0' + d.getMonth() + 1).slice(-2) + '-' + d.getFullYear();
                    }

                    // Otherwise the data type requested (`type`) is type detection or
                    // sorting data, for which we want to use the integer, so just return
                    // that, unaltered
                    return data;
                }
                },
                {title: "Passport number", data: "passportNumber"},
                {title: "Email", data: "email"},
                {
                    "className": 'showCustomer',
                    "orderable": false,
                    "data": null,
                    "defaultContent": '<button type="button" class="btn btn-outline btn-default btn-xs">Show</button>'
                }
            ]
        });
        $('#content_table').on('click', '.showCustomer', function () {
            var data = table.row(this).data();
            storeToSessionStorage("lastSeenUserId", data.id);
            loadpage("customer");
        });
        addTableAdditional(table, 'contracts', 'number', 'Contracts', 'No contracts');
    },
    "options": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_table');
        $page_wrapper.append(content.cloneNode(true));
        $page_wrapper.find('h1').text('Options');
        $page_wrapper.find('div.panel-heading').text('Show all options');

        var table = $('#content_table').DataTable({
            ajax: {
                url: window.contextPath + "/rest/options",
                dataSrc: ''
            },
            order: [[0, 'asc']],
            stateSave: true,
            pagingType: "full_numbers",
            columns: [
                {
                    title: "Name", data: "name", render: function (data, type, row) {
                    if (type === "display") {
                        return '<i class="fa fa-plus-square" style="padding-right: 1.8em"></i>' + data;
                    }
                    return data;
                }
                },
                {title: "Cost", data: "cost", render: $.fn.dataTable.render.number(',', '.', 2, '', ' ₽')},
                {
                    title: "Connection cost",
                    data: "connectCost",
                    render: $.fn.dataTable.render.number(',', '.', 2, '', ' ₽')
                },
                {title: "Description", data: "description"}
            ]
        });
        addTableAdditional(table, 'possibleTariffsOfOption', 'name', 'Available for this tariffs', 'No tariffs')
    },
    "tariffs": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_table');
        $page_wrapper.append(content.cloneNode(true));
        $page_wrapper.find('h1').text('Tariffs');
        $page_wrapper.find('div.panel-heading').text('Show all tariffs');

        var table = $('#content_table').DataTable({
            ajax: {
                url: window.contextPath + "/rest/tariffs",
                dataSrc: ''
            },
            order: [[0, 'asc']],
            stateSave: true,
            pagingType: "full_numbers",
            columns: [
                {
                    title: "Name", data: "name", render: function (data, type, row) {
                    if (type === "display") {
                        return '<i class="fa fa-plus-square" style="padding-right: 1.8em"></i>' + data;
                    }
                    return data;
                }
                },
                {title: "Cost", data: "cost", render: $.fn.dataTable.render.number(',', '.', 2, '', ' ₽')},
                {title: "Description", data: "description"}
            ]
        });
        addTableAdditional(table, 'possibleOptions', 'name', 'Available options', 'No options')
    },
    "contracts": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_table');
        $page_wrapper.append(content.cloneNode(true));
        $page_wrapper.find('h1').text('Contracts');
        $page_wrapper.find('div.panel-heading').text('Show all contracts');

        var table = $('#content_table').DataTable({
            ajax: {
                url: window.contextPath + "/rest/contracts",
                dataSrc: ''
            },
            order: [[1, 'asc']],
            stateSave: true,
            pagingType: "full_numbers",
            columns: [
                {
                    title: "Number", data: "number", render: function (data, type, row) {
                    if (type === "filter") {
                        return data.replace(/[^\/\d]/g, '');
                    }
                    if (type === "display") {
                        return '<i class="fa fa-plus-square" style="padding-right: 1.8em"></i>' + data;
                    }
                    return data;
                }
                },
                {
                    title: "Customer", data: "customer", render: function (data, type, row) {
                    return data.surname + ' ' + data.name
                }
                },
                {title: "Tariff", data: "tariff", render: "name"},
                {title: "Balance", data: "balance", render: $.fn.dataTable.render.number(',', '.', 2, '', ' ₽')},
                {
                    "className": 'showCustomer',
                    "orderable": false,
                    "data": null,
                    "defaultContent": '<button type="button" class="btn btn-outline btn-default btn-xs">Show</button>'
                }
            ]
        });
        $('#content_table').on('click', '.showCustomer', function () {
            var data = table.row(this).data();
            storeToSessionStorage("lastSeenUserId", data.customer.id);
            loadpage("customer");
        });
        addTableAdditional(table, 'usedOptions', 'name', 'Used options', 'No used options');
    },
    "new_customer": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_customer');
        $page_wrapper.append(content.cloneNode(true));

        $('form input[type="text"].bfh-phone, form input[type="tel"].bfh-phone, span.bfh-phone').each(function () {
            var $phone;

            $phone = $(this);

            $phone.bfhphone($phone.data());
        });

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
        });
        prepare_tariff_list($('#tariff'), $('#options'), 1, "contracts[usedOptions][][id]");
    },
    "new_option": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_option');
        $page_wrapper.append(content.cloneNode(true));

        var requiredFrom = $("#requiredFrom");
        var forbiddenWith = $("#forbiddenWith");
        var forTariffs = $('#forTariffs');

        forTariffs.empty();
        $.getJSON(window.contextPath + "/rest/tariffs", {}, create_boxes(forTariffs, "possibleTariffsOfOption[][id]"));

        $(forTariffs).on('change', 'input[type=checkbox]', function (e) {
            e.preventDefault();
            requiredFrom.empty();
            forbiddenWith.empty();
            var options = chooseOptionsForTariff($(forTariffs));
            create_boxes(requiredFrom, "requiredFrom[][id]")(options);
            create_boxes(forbiddenWith, "forbiddenWith[][id]")(options);
        });

        $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom"));
        $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden"));
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
            },
            submitHandler: submitting(function () {
                requiredFrom.empty();
                forbiddenWith.empty();
                forTariffs.empty();
                $.getJSON(window.contextPath + "/rest/tariffs", {}, create_boxes(forTariffs, "possibleTariffsOfOption[][id]"));
            })
        })
    },
    "new_tariff": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_tariff');
        $page_wrapper.append(content.cloneNode(true));

        $('#possibleOptions').on('change', 'input[type=checkbox]', optionCheckedNewTariff);
        $.getJSON(window.contextPath + "/rest/options", {}, create_boxes($('#possibleOptions'), "possibleOptions[][id]"));
        $('form#add_tariff_form').validate({
            rules: {
                name: "required",
                cost: {
                    required: true,
                    money: true
                }
            },
            messages: {
                name: 'Please enter name',
                cost: {
                    required: 'Please enter cost',
                    money: "Wrong money format"
                }
            }
        })
    },
    "customer": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_customer');
        var customerId = getFromSessionStorage('lastSeenUserId');
        if (customerId === null) {
            customerId = window.userId;
            storeToSessionStorage('lastSeenUserId', window.userId)
        }
        $.get(window.contextPath + '/rest/customers/' + customerId, {}, function (data) {
            $page_wrapper.append(content.cloneNode(true));
            $page_wrapper.find('#customerName').html(data.surname + ' ' + data.name);
            $page_wrapper.find('#customerEmail').html(data.email);
            $page_wrapper.find('#customerPassportNumber').html(data.passportNumber);
            $page_wrapper.find('#customerPassportData').append(data.passportData.replace(/\n/g, '<br/>'));
            $page_wrapper.find('#customerAddress').html(data.address.replace(/\n/g, '<br/>'));

            var fadeOutContract = sessionStorage.getItem("fadeOutContract");

            $(data.contracts).each(function (j, contract) {
                var nodes = link.import.querySelector('#piece_accordion_node').cloneNode(true);
                $(nodes).find('#contractNumber')
                    .html(contract.number)
                    .attr('href', '#collapse' + contract.id);
                $(nodes).find('input[name=contractId]').val(contract.id);

                if (window.userRole !== 1 || contract.isBlocked !== 2) {
                    var menu = link.import.querySelector('#piece_node_menu').cloneNode(true);
                    $(menu).find('#menuBlock')
                        .append('<a href=' + (contract.isBlocked === 0 ? '"/block"' : '"/unblock"')
                            + '><p>' + (contract.isBlocked === 0 ? 'Block' : 'Unblock') + '</p></a>');

                    if (contract.isBlocked !== 0 || contract.balance < 0) {
                        $(menu).find('#menuEdit a').attr('href', '').find('p').addClass('text-muted');
                    }
                    $(nodes).find('#nodeTitle').append($(menu).contents());
                }
                // }

                $(nodes).find('#contractBalance')
                    .html(contract.balance.toFixed(2) + ' <i class="fa fa-rub"></i>')
                    .data('balance', contract.balance);
                $(nodes).find('#contractNode').addClass(contract.isBlocked === 0 ? 'panel-default' : 'panel-red');
                $(nodes).find('#collapse').attr('id', 'collapse' + contract.id);
                $(nodes).find('#tariffName').html(contract.tariff.name).data('tariffId', contract.tariff.id);
                $(nodes).find('#tariffDescription').html(contract.tariff.description);
                // var opts = '';
                var $usedOptions = $(nodes).find('#usedOptions');
                for (var i = 0; i < contract.usedOptions.length; i++) {
                    var $opts = $('<li>');
                    $opts.html(contract.usedOptions[i].name).data('id', contract.usedOptions[i].id);
                    $usedOptions.append($opts);
                }

                $page_wrapper.find('#accordion').append($(nodes).contents());
                if (getFromBasketByContract(contract.id) !== undefined) {
                    edit_tariff($page_wrapper.find('#accordion .panel:last'));
                }

                if (fadeOutContract == contract.id) {
                    $page_wrapper.find('#accordion .panel:last .panel-heading').stop().animate({opacity: 0.25},600,function(){
                        $(this).animate({opacity: 1},600);
                    });

                }

            });
            sessionStorage.removeItem("fadeOutContract");
            $("#accordion").on("click", "ul[role='menu'] a", function (e) {
                e.preventDefault();
                if ($(this).find('p').hasClass("text-muted")) {
                    return false;
                }
                var $panel = $(this).closest('.panel');
                var id = $panel.find('input[type=hidden]').val();
                var href = $(this).attr("href");
                var $obj = $(this);

                if (href === "/delete") {
                    $.ajax({
                        url: window.contextPath + "/rest/contracts/" + id,
                        type: "DELETE",
                        success: function (data) {
                            $panel.remove();
                        }
                    })
                } else if (href === "/block") {
                    $.post(window.contextPath + "/rest/contracts/" + id + "/block", {}, function (data) {
                        $panel.removeClass("panel-default").addClass("panel-red");
                        $obj.attr("href", "/unblock").text("Unblock");
                        $panel.find('#menuEdit a').attr('href', '').find('p').addClass('text-muted');
                    })
                } else if (href === "/unblock") {
                    $.post(window.contextPath + "/rest/contracts/" + id + "/unblock", {}, function (data) {
                        $panel.removeClass("panel-red").addClass("panel-default");
                        $obj.attr("href", "/block").text("Block");
                        $panel.find('#menuEdit a').attr('href', '/edit').find('p').removeClass('text-muted');
                    })
                } else if (href === "/edit") {
                    edit_tariff($panel);
                }
            });

            if (window.userRole === 2) {
                var contractNode = link.import.querySelector('#piece_node_contract').cloneNode(true);
                $(contractNode).find('form input[type="text"].bfh-phone, form input[type="tel"].bfh-phone, span.bfh-phone').each(function () {
                    var $phone = $(this);
                    $phone.bfhphone($phone.data());
                });
                $(contractNode).find("input[name='customer[id]']").val(customerId);
                $page_wrapper.find('#accordion').append($(contractNode).contents());
                prepare_tariff_list($('#tariff'), $('#options'), 1, "usedOptions[][id]");
                $('form#add_contract_form').validate({
                    rules: {
                        number: {
                            required: true,
                            phone: true
                        }
                    },
                    messages: {
                        number: {
                            required: "Please enter phone number",
                            phone: "Wrong phone number format"
                        }
                    },
                    submitHandler: submitting(create_accordion_node)
                })
            }
        });
    },
    "me": function ($page_wrapper) {
        if (getFromSessionStorage('lastSeenUserId') === undefined || getFromSessionStorage('lastSeenUserId') !== window.userId) {
            storeToSessionStorage("lastSeenUserId", window.userId);
        }
        prepare["customer"]($page_wrapper);
    },
    "change_password": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_change_password');
        $page_wrapper.append(content.cloneNode(true));

        var $form = $('#changePassword');
        $form.validate({
            rules: {
                oldPassword: {
                    required: true,
                },
                newPassword: {
                    required: true,
                    minlength: 8
                },
                newPasswordRepeat: {
                    required: true,
                    equalTo: '#newPassword'
                }
            },
            messages: {
                oldPassword: {
                    required: "Please enter old password"
                },
                newPassword: {
                    required: "Please enter new password",
                    minlength: "New password should contain at least 8 characters!"
                },
                newPasswordRepeat: {
                    required: "Please repeat new password",
                    equalTo: "Passwords are not equal!"
                }
            },
            submitHandler: function (form, e) {
                e.preventDefault();
                $.notify("Sending data..", {position: "top right", className: "success"});
                $.ajax({
                    url: window.contextPath + '/rest/users/' + window.userId,
                    type: 'PUT',
                    data: $(form).serialize(),
                    success: function (data) {
                        $.notify("Success!", {position: "top right", className: "success"});
                        $(form)[0].reset();
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
            }
        });
    }
};


function edit_tariff(panel) {
    var id = $(panel).find('input[name=contractId]').val();
    var basket = getFromBasketByContract(id);
    if (basket === undefined)
        basket = {};

    var customerId = getFromSessionStorage('lastSeenUserId');
    var panel_backup = $(panel).clone(true).children();


    var usedOptions;
    if (basket["contractId"] !== undefined) {
        usedOptions = basket["usedOptions"];
    } else {
        usedOptions = $(panel).find('#usedOptions > li').map(function () {
            return $(this).data('id');
        });
    }
    var usedTariff;
    if (basket["contractId"] !== undefined)
        usedTariff = basket["usedTariff"];
    else
        usedTariff = $(panel).find('#tariffName').data('tariffId');

    var tariff = $('<div class="control-group">' +
        '<label class="control-label" for="tariffEdit'+id+'">Tariff</label>' +
        '<div class="controls">' +
        '<select id="tariffEdit'+id+'" name="tariff" class="form-control"></select>' +
        "</div>" +
        '</div>');

    var options = $('<div class="control-group">' +
        '<label class="control-label" for="optionsEdit'+id+'">Options</label>' +
        '<div class="controls">' +
        '<div id="optionsEdit'+id+'" class="boxes"></div>' +
        '</div>' +
        '</div>');

    var leftPanel = $(panel).find('.col-lg-6:first');
    var rightPanel = $(panel).find('.col-lg-6:last .well');
    leftPanel.html(tariff);
    rightPanel.html(options);
    var optionDiv = $('#optionsEdit'+id);
    var tariffList = $('#tariffEdit'+id);
    prepare_tariff_list($('#tariffEdit'+id), optionDiv, usedTariff, "usedOptions", usedOptions);

    optionDiv.on('change', 'input[type=checkbox]', function () {
        var saved = basket["usedOptions"];
        if ($(this).is(":checked")) {
            saved.push($(this).val());
        } else {
            var index = saved.indexOf($(this).val());
            if (index > -1) {
                saved.splice(index, 1);
            }
        }
        basket["usedOptions"] = saved;
        storeToBasket(basket);
    });
    tariffList.change(function (e) {
        e.preventDefault();
        basket["usedOptions"] = [];
        basket["usedTariff"] = $(this).val();
        storeToBasket(basket);
    });


    panel.find('.panel-heading .pull-right').html('<button type="button" class="btn btn-outline btn-danger btn-xs">Exit editing</button>');
    panel.find('button:contains("Exit editing")').click(function (e) {
        e.preventDefault();
        panel.html(panel_backup);
        deleteFromBasket(id);
        updateBasketIcon();
    });
    panel.find('.panel-body').append('<div class="col-lg-12"><div class="controls"><input type="submit" class="btn btn-success"/></div></div>');
    panel.find('.panel-body').wrapInner('<form class="form-horizontal" action="edit_contract" method="POST"></form>');

    panel.find('form').submit({contractId:id, panel: panel, usedOptions: usedOptions}, edit_handler);
    if (basket["contractId"] === undefined) {
        basket["customerId"] = customerId;
        basket["usedTariff"] = usedTariff;
        basket["contractId"] = id;
        basket["userName"] = $('#customerName').text();
        basket["contractNumber"] = $(panel).find('#contractNumber').text();
        basket["usedOptions"] = usedOptions;
        storeToBasket(basket);
    }
    updateBasketIcon();
}

function storeToBasket(dataToStore) {
    var basket = localStorage.getItem("basket");
    if (basket == null) {
        var store = {};
        store[window.userId] = [dataToStore];
        localStorage.setItem("basket", JSON.stringify(store));
    } else {
        basket = JSON.parse(basket);
        var existingData = basket[window.userId];
        if (existingData === undefined) {
            basket[window.userId] = [dataToStore];
            localStorage.setItem("basket", JSON.stringify(basket));
        } else {
            for (var i = 0; i < basket[window.userId].length; i++) {
                if (basket[window.userId][i]["contractId"] == dataToStore.contractId) {
                    basket[window.userId][i] = dataToStore;
                    localStorage.setItem("basket", JSON.stringify(basket));
                    return;
                }
            }
            basket[window.userId].push(dataToStore);
            localStorage.setItem("basket", JSON.stringify(basket));
        }
    }
}

function getFromBasket() {
    var basket = localStorage.getItem("basket");
    if (basket == null)
        return undefined;
    basket = JSON.parse(basket);
    return basket[window.userId];
}

function getFromBasketByContract(contractId) {
    var basket = localStorage.getItem("basket");
    if (basket == null)
        return undefined;
    basket = JSON.parse(basket);
    var userBasket = basket[window.userId];
    if (userBasket === undefined)
        return undefined;
    for (var i = 0; i < userBasket.length; i++) {
        if (userBasket[i]["contractId"] == contractId)
            return userBasket[i];
    }
    return undefined;
}

function deleteFromBasket(contractId) {
    var basket = localStorage.getItem("basket");
    if (basket == null)
        return;
    basket = JSON.parse(basket);
    var userBasket = basket[window.userId];
    if (userBasket === undefined)
        return;

    basket[window.userId] = basket[window.userId].filter(function (item) {
        return item.contractId != contractId;
    });
    for (var property in basket) {
        if (basket.hasOwnProperty(property) && basket[property].length > 0) {
            // Basket is not empty
            localStorage.setItem("basket", JSON.stringify(basket));
            return;
        }
    }
    // Basket is empty
    localStorage.removeItem("basket");
    hideBasket();
}

function storeToSessionStorage(key, value) {
    var saved = sessionStorage.getItem("store");
    var data = {};
    data[key] = value;
    if (saved == null) {
        saved = {};
        saved[window.userId] = data;
    } else {
        saved = JSON.parse(saved);
        var userData = saved[window.userId];
        if (userData === undefined) {
            saved[window.userId] = data;
        } else {
            saved[window.userId][key] = value;
        }
    }
    sessionStorage.setItem("store", JSON.stringify(saved))
}

function getFromSessionStorage(key) {
    var saved = sessionStorage.getItem("store");
    if (saved == null)
        return undefined;
    saved = JSON.parse(saved);
    if (saved[window.userId] === undefined)
        return undefined;
    return saved[window.userId][key];
}

function edit_handler(e) {
    e.preventDefault();
    deleteFromBasket(e.data.contractId);
    updateBasketIcon();
    var panel = e.data.panel;
    var form = panel.find("form");
    $(form).find("input[type=checkbox]").prop("disabled", false);

    $.ajax({
        url: window.contextPath + '/rest/contracts/' + $(panel).find('input[name=contractId]').val(),
        type: "PUT",
        // contentType:"application/json; charset=utf-8",
        data: $(form).find(':not([type=hidden])').serialize(),
        success: function (data) {
            $.notify("Success!", {position: "top right", className: "success"});
            var filling = fill_accordion_node(data);
            $(panel).find(".panel-body").empty();
            $(panel).find(".panel-body").append(filling[0]);
            $(panel).find(".panel-body").append(filling[1]);
            $(panel).find(".panel-body").append(filling[2]);
            $(panel).find(".panel-title .pull-right").empty();
            $(panel).find(".panel-title .pull-right").append(create_panel_menu(data));
            $(panel).find("#contractBalance").html(data.balance.toFixed(2) + ' <i class="fa fa-rub"></i>');
        }
    });
    $(form).find(":input").prop("disabled", true);
}

function updateBasketIcon() {
    var basket = getFromBasket();
    if (basket === undefined || basket.length === 0) {
        hideBasket();
        return;
    }

    var link = document.querySelector('link[href$="pieces.html"]');
    var content = link.import.querySelector('#piece_basket_item');

    var $basket = $('#basket');

    var list = $basket.find('#basket_list').empty();
    for (var i = 0; i<basket.length; i++) {
        var basket_item = content.cloneNode(true);
        $(basket_item).find('.basketName').html(basket[i]['userName']);
        $(basket_item).find('.basketText').html(basket[i]['contractNumber']);
        $(basket_item).find('.basketContractId').val(basket[i]['contractId']);
        $(basket_item).find('a').click(basketContinue);
        list.append($(basket_item).children());
    }
    list.append('<li><a class="text-center" onclick="basketContinue()"><strong>Edited contracts</strong></a></li>');
    $basket.show();
}

function hideBasket() {
    $('#basket').hide();
}

function basketContinue(e) {
    e.preventDefault();
    var contractId = $(this).find(".basketContractId").val();
    var basket = getFromBasketByContract(contractId);
    if (basket === undefined)
        return;

    if (window.userRole == 1) {
        loadpage("me");
    } else {
        storeToSessionStorage("lastSeenUserId", basket["customerId"]);
        loadpage("customer");
    }

    sessionStorage.setItem("fadeOutContract", contractId);
}
