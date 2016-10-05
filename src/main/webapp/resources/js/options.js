/**
 * Created by alex on 28.08.16.
 */

function create_boxes(selobj, checkboxs_name) {
    return function (data) {
        $(selobj).empty();
        // var checkboxs_name = selobj.attr('id');
        $.each(data, function (i, obj) {
            var $box = $("<input />", {
                type: "checkbox",
                id: checkboxs_name + i,
                value: obj.id,
                name: checkboxs_name,
                "data-cost": obj.connectCost
            });
            $box.data('boxData', obj);
            $(selobj).append($box);
            var name = obj.name;
            if (obj.connectCost !== undefined)
                name += ' <p class="text-muted" style="display:inline">(' + obj.connectCost + '<i class="fa fa-rub"></i>)</p>';
            $(selobj).append($("<label/>", {"for": checkboxs_name + i, html: name}));
            $(selobj).append($("<br/>"));
        });
    }
}

function check_item(type) {
    return function () {
        var item = $(this);
        var checked_val = parseInt($(this).val(), 10);
        var disabledBy = checked_val + ':' + type;
        if ($(this).is(':checked')) {
            $.getJSON("/rest/options/" + checked_val, {}, function (data) {
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
                var maybeEnable = $('#' + finder[1]).find("input[value=" + finder[0] + "]");
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
            $.getJSON("/rest/options/" + checked_val, {}, function (response) {
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
    var data = $item.data('boxData');
    if ($item.is(':checked')) {
        var enableIt = $();

        $(data.requiredFrom).each(function (i, obj) {
            enableIt = $.merge(enableIt, $('#possibleOptions').find("input[value=" + obj.id + "]"));
        });

        enableIt.prop('checked', true).attr("disabled", true); //.attr('onclick', 'return false');

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
        $(uncheck).prop('checked', false).prop('disabled', false).change();//.removeAttr('onclick').change();
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

function prepare_tariff_list(tariffList, options, selected_val) {
    $(tariffList).change(function (e) {
        e.preventDefault();
        $.getJSON("/rest/tariffs/" + $(this).val() + '/options', {}, create_boxes($(options), "contracts[usedOptions][][id]"));
    });
    loadlist($(tariffList), "/rest/tariffs", "name", "id", selected_val);

    $(options).on('change', 'input[type=checkbox]', optionChecked(options));
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

    var id = '<input type="hidden" name="contract_id" value="' + data.id + '"/>';

    return [id, col_lg1, col_lg2];
}

function create_accordion_node(res) {
    var data = res.data;

    var $node = $('<div class="panel panel-success">' +
        '<div class="panel-heading">' +
        '<h4 class="panel-title">' +
        '<a data-toggle="collapse" data-parent="#accordion" href="#collapse' + data.id + '" >' + data.number + '</a> ' +
        '<small id="balance" data-balance="' + data.balance + '">' + data.balance + ' <i class="fa fa-rub"></i></small>' +
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
        '<li><a href="/editTariff"><p ' + (disable_edit ? 'class="text-muted"' : '') + '>Edit</p></a>' +
        '</li>' +
        '<li><a href=\"/blockContract\"><p>Block</p></a>' +
        '</li>' +
        '<li class="divider"></li>' +
        '<li><a href="/delete_contract"><p class="text-danger">Delete</p></a>' +
        '</li>' +
        '</ul>' +
        '</div>');
}

function loadpage(page) {
    var $sidemenu = $('#side-menu');
    var currentPage = Cookies.get("currentPage");
    $sidemenu.find('#' + currentPage + "_menu").removeClass("active");

    var element = $sidemenu.find('#' + page + "_menu");
    if (element !== undefined) {
        $(element).closest('ul').addClass('in').attr('aria-expanded', true);
        $(element).addClass("active");
    }
    Cookies.set("currentPage", page, {expires: 1});
    var $page_wrapper = $('#page-wrapper');
    $page_wrapper.empty();

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
};

var prepare = {
    "customers": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_table');
        $page_wrapper.append(content.cloneNode(true));
        $page_wrapper.find('h1').text('Customers');
        $page_wrapper.find('div.panel-heading').text('Show all customers');

        var table = $('#content_table').DataTable({
            ajax: {
                url: "/rest/customers",
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
            Cookies.set("lastSeenUserId", data.id, {expires: 1});
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
                url: "/rest/options",
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
                url: "/rest/tariffs",
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
                url: "/rest/contracts",
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
                {title: "Balance", data: "balance", render: $.fn.dataTable.render.number(',', '.', 2, '', ' ₽')}
            ]
        });
        addTableAdditional(table, 'usedOptions', 'name', 'Used options', 'No used options');
    },
    "new_contract": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_contract');
        $page_wrapper.append(content.cloneNode(true));

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
        prepare_tariff_list($('#tariff'), $('#options'));
    },
    "new_option": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_option');
        $page_wrapper.append(content.cloneNode(true));

        var requiredFrom = $("#requiredFrom");
        var forbiddenWith = $("#forbiddenWith");
        var forTariffs = $('#forTariffs');

        forTariffs.empty();
        $.getJSON("/rest/tariffs", {}, create_boxes(forTariffs, "possibleTariffsOfOption[][id]"));

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
            }
        })
    },
    "new_tariff": function ($page_wrapper) {
        var link = document.querySelector('link[href$="pieces.html"]');
        var content = link.import.querySelector('#piece_new_tariff');
        $page_wrapper.append(content.cloneNode(true));

        $('#possibleOptions').on('change', 'input[type=checkbox]', optionCheckedNewTariff);
        $.getJSON("/rest/options", {}, create_boxes($('#possibleOptions'), "possibleOptions[][id]"));
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
        var customerId = Cookies.get('lastSeenUserId');
        $.get('/rest/customers/' + customerId, {}, function (data) {
            $page_wrapper.append(content.cloneNode(true));
            $page_wrapper.find('#customerName').html(data.surname + ' ' + data.name);
            $page_wrapper.find('#customerEmail').html(data.email);
            $page_wrapper.find('#customerPassportNumber').html(data.passportNumber);
            $page_wrapper.find('#customerPassportData').append(data.passportData.replace(/\n/g, '<br/>'));
            $page_wrapper.find('#customerAddress').html(data.address.replace(/\n/g, '<br/>'));

            $(data.contracts).each(function (j, contract) {
                var nodes = link.import.querySelector('#piece_accordion_node').cloneNode(true);
                $(nodes).find('#contractNumber')
                    .html(contract.number)
                    .attr('href', '#collapse' + contract.id);
                $(nodes).find('input[name=contractId]').val(contract.id);

                // if (contract.isBlocked !== 2) {
                // if (contract.isBlocked === 1) {
                // $(menu).find('p:contains("Edit")').addClass('text-muted');
                // $(menu).find('a:contains("Edit")').attr('href', '');
                // }
                if (window.userRole !==1 || contract.isBlocked !== 2) {
                    var menu = link.import.querySelector('#piece_node_menu').cloneNode(true);
                    $(menu).find('#menuBlock')
                        .append('<a href=' + (contract.isBlocked === 0 ? '"/block"' : '"/unblock"')
                            + '><p>' + (contract.isBlocked === 0 ? 'Block' : 'Unblock') + '</p></a>');

                    if (contract.isBlocked !== 0) {
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
                // var opts = '';
                var $usedOptions = $(nodes).find('#usedOptions');
                for (var i = 0; i < contract.usedOptions.length; i++) {
                    var $opts = $('<li>');
                    $opts.html(contract.usedOptions[i].name).data('id', contract.usedOptions[i].id);
                    $usedOptions.append($opts);
                }

                $page_wrapper.find('#accordion').append($(nodes).contents());
            });
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
                        url:"/rest/contracts/"+id,
                        type:"DELETE",
                        success:function (data) {
                            $panel.remove();
                        }
                    })
                } else if (href === "/block") {
                    $.post("/rest/contracts/"+id+"/block", {}, function (data) {
                        $panel.removeClass("panel-default").addClass("panel-red");
                        $obj.attr("href", "/unblock").text("Unblock");
                        $panel.find('#menuEdit a').attr('href', '').find('p').addClass('text-muted');
                    })
                } else if (href === "/unblock") {
                    $.post("/rest/contracts/"+id+"/unblock", {}, function (data) {
                        $panel.removeClass("panel-red").addClass("panel-default");
                        $obj.attr("href", "/block").text("Block");
                        $panel.find('#menuEdit a').attr('href', '/edit').find('p').removeClass('text-muted');
                    })
                } else if (href === "/edit") {
                    edit_tariff($panel);
                }
            })
        });
    },
    "me": function($page_wrapper) {
        if (Cookies.get('lastSeenUserId') === undefined) {
            Cookies.set("lastSeenUserId", window.userId, {expires: 1});
        }
        prepare["customer"]($page_wrapper);
    }
};


function edit_tariff(panel) {
    var panel_backup = $(panel).clone(true).children();
    var id = $(panel).find('input[name=contractId]').val();
    var usedOptions = $(panel).find('#usedOptions > li').map(function () {
        return $(this).data('id')
    });
    var usedTariff = $(panel).find('#tariffName').data('tariffId');

    var tariff = $('<div class="control-group">' +
        '<label class="control-label" for="tariffEdit' + id + '" >Tariff</label>' +
        '<div class="controls">' +
        '<select id="tariffEdit' + id + '" name="tariff" class="form-control"></select>' +
        "</div>" +
        '</div>');

    var options = $('<div class="control-group">' +
        '<label class="control-label" for="optionsEdit' + id + '">Options</label>' +
        '<div class="controls">' +
        '<div id="optionsEdit' + id + '" class="boxes"></div>' +
        '</div>' +
        '</div>');

    var leftPanel = $(panel).find('.col-lg-6:first');
    var rightPanel = $(panel).find('.col-lg-6:last .well');
    leftPanel.html(tariff);
    rightPanel.html(options);
    prepare_tariff_list($('#tariffEdit' + id), $('#optionsEdit' + id), usedTariff);

    $(panel).find('.panel-heading .pull-right').html('<button type="button" class="btn btn-outline btn-danger btn-xs">Exit editing</button>');
    $(panel).find('button:contains("Exit editing")').click(function (e) {
        e.preventDefault();
        $(panel).html(panel_backup);
    });
    $(panel).find('.panel-body').append('<div class="col-lg-12"><div class="controls"><input type="submit" class="btn btn-success"/></div></div>');
    $(panel).find('.panel-body').wrapInner('<form class="form-horizontal" action="edit_contract" method="POST"></form>');

    $(panel).find('form').submit({panel: $(panel), usedOptions: usedOptions}, edit_handler);
}

function edit_handler(e) {
    e.preventDefault();
    var panel = e.data.panel;
    var usedOptions = e.data.usedOptions;
    var form = panel.find("form");
    $(form).find("input[type=checkbox]").prop("disabled", false);
    var cost = 0;
    $(form).find("input[type=checkbox]:checked").each(function (i, item) {
        if ($.inArray(parseInt($(item).val()), usedOptions) === -1) {
            cost += $(item).data("cost");
        }
    });
    $.post($(form).attr("action"), $(form).serialize(), function (e) {
        if (e.success) {
            $.notify("Success!", {position: "top right", className: "success"});
            var filling = fill_accordion_node(e.data);
            $(panel).find(".panel-body").empty();
            $(panel).find(".panel-body").append(filling[0]);
            $(panel).find(".panel-body").append(filling[1]);
            $(panel).find(".panel-body").append(filling[2]);
            $(panel).find(".panel-title .pull-right").empty();
            $(panel).find(".panel-title .pull-right").append(create_panel_menu(e.data));
            var balance = $(panel).find("#balance");
            var res_balance = $(balance).data("balance") - cost;
            $(balance).html(res_balance.toFixed(2) + ' <i class="fa fa-rub"></i>').data("balance", res_balance);
        } else {
            $.each(e.errors, function (prop, val) {
                $.notify("Error: in " + prop + "\n" + val, {position: "top right", className: "error"});
            });
        }
    });
    $(form).find(":input").prop("disabled", true);
}
/*    "/customer": function () {
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

            if (href === "/delete_contract") {
                $.post("/customer" + href, {id: id}, function () {
                    $panel.remove();
                })
            } else if (href === "/blockContract") {
                $.post("/customer" + href, {id: id}, function () {
                    $panel.removeClass("panel-default").addClass("panel-red");
                    $obj.attr("href", "/unblockContract").text("Unblock");
                    $panel.find(":contains('Edit')").addClass("text-muted");
                });
            } else if (href === "/unblockContract") {
                $.post("/customer" + href, {id: id}, function () {
                    $panel.removeClass("panel-red").addClass("panel-default");
                    $obj.attr("href", "/blockContract").text("Block");
                    $panel.find(":contains('Edit')").removeClass("text-muted");
                });
            } else if (href === "/editTariff") {
                edit_tariff($panel)
            }
        })
    },
    "/admin/customer": function () {
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

            if (href === "/delete_contract") {
                $.post("/admin" + href, {id: id}, function () {
                    $panel.remove();
                })
            } else if (href === "/blockContract") {
                $.post("/admin" + href, {id: id}, function () {
                    $panel.removeClass("panel-default").addClass("panel-red");
                    $obj.attr("href", "/admin/unblockContract").text("Unblock");
                    $panel.find(":contains('Edit')").addClass("text-muted");
                });
            } else if (href === "/unblockContract") {
                $.post("/admin" + href, {id: id}, function () {
                    $panel.removeClass("panel-red").addClass("panel-default");
                    $obj.attr("href", "/admin/blockContract").text("Block");
                    $panel.find(":contains('Edit')").removeClass("text-muted");
                });
            } else if (href === "/editTariff") {
                edit_tariff($panel)
            }
        })
    },*/
