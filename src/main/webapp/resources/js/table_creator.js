/**
 * Created by alex on 21.08.16.
 */

function get_customers(page_number, callback) {
    $.ajax({
        type:'POST',
        url: "/show_customers",
        data: {"page":page_number},
        success: callback
    });
}

function paginationSet(pages_count, current) {
    var pages = pages_count > 3 ? 3 : pages_count;
    var $pagination = $('.pagination');
    $pagination.find("li:not(.last,.first)").remove();
    var $first = $pagination.find(".first");
    var $last = $pagination.find(".last");
    $first.removeClass("disabled");
    $last.removeClass("disabled");

    var $shown_pages = [];
    if (current === 1) {
        for (var i = 0; i<pages; i++) {
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

    for (var i = 0; i < pages; i++) {
        $("<li class='page-item'><a class='page-link customers' href='#' name='"+$shown_pages[i]+"'>"+$shown_pages[i]+"</a></li>")
            .insertBefore($last);
    }

    if (current === 1) {
        $first.addClass("disabled");
    }
    if (current === pages_count) {
        $last.addClass("disabled");
    }
    $pagination.find("li a:contains('"+current+"')").parent().addClass("active");
}

function init_table() {
    var $customers = $("table#customers");
    var fields_count = $customers.find("thead th").length;
    for (var i = 0; i < 10; i++) {
        var $tr = $('<tr> s');
        for (var j = 0; j < fields_count; j++) {
            $tr.append('<td>&nbsp;</td>');
        }
        $customers.find("tbody").append($tr);
    }
}

function create_table(response) {
    var $customers = $("table#customers");

    var properties = $customers.find("thead th").map(function () {
        return this.abbr;
    }).get();

    recordsTotal = response.recordsTotal;
    pages = Math.ceil(recordsTotal/10);
    paginationSet(pages, response.draw);

    $(function () {
        for (var i = 0; i < 10; i++) {
            if (response.data[i] !== undefined) {
                $.each(properties, function (j, item) {
                    $customers.find("tbody tr:nth-child("+(i+1)+") td:nth-child("+(j+1)+")").text(response.data[i][item]);
                })
            } else {
                $.each(properties, function (j, item) {
                    $customers.find("tbody tr:nth-child("+(i+1)+") td:nth-child("+(j+1)+")").html('&nbsp;');
                })
            }
        }
    })
}

$(document).ready(function() {
    init_table();
    get_customers(1, create_table);

    $(".pagination").on('click', '.page-link.customers', function (e) {
        e.preventDefault();
        get_customers($(this).attr("name"), create_table);
    });
} );
