/**
 * Created by alex on 21.08.16.
 */

function get_data(url, search_input) {
    return function (page_number, callback, updateCount) {
        $.getJSON(url, {page:page_number, updateCount:updateCount, search:search_input.val()}, callback);
    }
}

function paginationSet(pages_count, current, $pagination) {

    $pagination.find("li:not(.last,.first)").remove();
    var $first = $pagination.find(".first");
    var $last = $pagination.find(".last");
    $first.removeClass("disabled");
    $last.removeClass("disabled");
    var pages = pages_count > 3 ? 3 : pages_count;

    var $shown_pages = [];
    if (current === 1) {
        $shown_pages.push(1);
        for (var i = 1; i<pages; i++) {
            $shown_pages.push(i+1);
        }
    } else if (current === pages_count) {
        for (var i = 0; i<pages; i++) {
            $shown_pages.push(pages_count-(pages-i)+1);
        }
    } else {
        $shown_pages.push(current-1);
        $shown_pages.push(current);
        $shown_pages.push(current+1);
    }

    for (var i = 0; i < $shown_pages.length; i++) {
        $("<li class='page-item'><a class='page-link' href='#' name='" + $shown_pages[i] + "'>" + $shown_pages[i] + "</a></li>")
            .insertBefore($last);
    }

    if (current === 1) {
        $first.addClass("disabled");
    }
    if (current === pages_count || pages_count === 0) {
        $last.addClass("disabled");
    }
    $pagination.find("li a:contains('"+current+"')").parent().addClass("active");
}

function init_table($table) {

    var fields_count = $table.find("thead th").length;
    var table_body = $table.find("tbody");

    for (var i = 0; i < 10; i++) {
        var $tr = $('<tr>');
        for (var j = 0; j < fields_count; j++) {
            $tr.append('<td>&nbsp;</td>');
        }
        table_body.append($tr);
    }
    $(table_body).find('tr:first-child td:first-child').text("loading table...");
}

function fill_table($table, $pagination, link) {
    var recordsTotal, pages;
    return function (response) {
        var properties = $table.find("thead th").map(function () {
            return this.abbr;
        }).get();

        if (response.recordsTotal !== undefined) {
            recordsTotal = response.recordsTotal;
            pages = Math.ceil(recordsTotal / 10);
        }
        paginationSet(pages, response.draw, $pagination);

        for (var i = 0; i < 10; i++) {
            if (response.data[i] !== undefined) {
                $row = $table.find("tbody tr:nth-child(" + (i + 1) + ")");
                if (link) {
                    $row.addClass("clickableRow").attr("data-href", link).attr("data-val", response.data[i].id);
                }
                $.each(properties, function (j, item) {
                    if (item.indexOf(".") === -1) {
                        $row.find("td:nth-child(" + (j + 1) + ")").text(response.data[i][item]);
                    } else {
                        var items = item.split(".");
                        $row.find("td:nth-child(" + (j + 1) + ")").text(response.data[i][items[0]][items[1]]);
                    }
                });
/*                if (response.hasPopover) {
                    var content = $('<ul>');
                    for (var i = 0; i < response.popoverData.length; i++) {
                        content.append($('<li type="disc"></li>').text(response.popoverData[response.data[i].id]))
                    }
                    // $row.popover({content:content, title:response.popoverHeader, placement:"top", trigger:"hover"});
                }*/
            } else {
                $row = $table.find("tbody tr:nth-child(" + (i + 1) + ")");
                if (link){
                    $row.removeClass("clickableRow").removeAttr("data-href").removeAttr("data-val");
                }
                $.each(properties, function (j, item) {
                    $row.find("td:nth-child(" + (j + 1) + ")").html('&nbsp;');
                })
            }
        }
    }
}


function table_creator($context, url, row_link) {
    var $pagination = $context.find(".pagination");
    var $table = $context.find("table");
    var fill_wrapper = fill_table($table, $pagination, row_link);
    var get_data_wrapper = get_data(url, $context.find('#search_query'));
    var $search_input = $context.find("#search-form input");
    var $search_submit = $context.find("#search-form button");

    init_table($table);
    get_data_wrapper(1, fill_wrapper, true);

    $pagination.on('click', $pagination.find(".page-link"), function (e) {
        e.preventDefault();
        get_data_wrapper(e.target.name, fill_wrapper, false);
    });

    $search_input.keypress(function (e) {
        if (e.which === 13) {
            $search_submit.click();
        }
    });

    $search_submit.click(function (e) {
        e.preventDefault();
        get_data_wrapper(1, fill_wrapper, true);
    });
}
