/**
 * Created by alex on 06.09.16.
 */

function create_accordion_node(res) {
    data = res.data;
    var $used_options = $('<ul></ul>');
    for (var i = 0; i < data.usedOptions.length; i++) {
        $used_options.append($('<li>', {text:data.usedOptions[i].name}));
    }

    var col_lg1 = $('<div class="col-lg-6">' +
            '<h3>'+data.tariff.name+'</h3>' +
            '<hr>' +
            data.tariff.description +
        '</div>');
    var col_lg2 = $('<div class="col-lg-6">' +
            '<div class="well">' +
                '<h4><small>Used options</small></h4>' +
                '<hr>'+
            '</div>' +
        '</div>').find(".well").append($used_options);

    var $node = $('<div class="panel panel-success">' +
            '<div class="panel-heading">' +
                '<h4 class="panel-title">' +
                    '<a data-toggle="collapse" data-parent="#accordion" href="#collapse'+data.id+'" >'+data.number+'</a>' +
                '</h4>' +
            '</div>' +
            '<div id="collapse'+data.id+'" class="panel-collapse collapse" style="height: 0px;">' +
                '<div class="panel-body"' +
                '</div>' +
            '</div>' +
        '</div>');
    $node.find(".panel-body").append(col_lg1);
    $node.find(".panel-body").append(col_lg2);
    $('#accordion').prepend($node);
}

function fill_accordion(data) {
    for (var i = 0; i < data.length; i++) {
        create_accordion_node(data[i]);
    }
}
