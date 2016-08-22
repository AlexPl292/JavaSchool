/**
 * Created by alex on 21.08.16.
 */

function get_cust(page_number, callback) {
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
    $pagination.find(".first").removeClass("disabled");
    $pagination.find(".last").removeClass("disabled");

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
        $("<li class='page-item'><a class='page-link customers' href='#'></a></li>")
            .insertBefore($pagination.find(".last"))
            .find('a').text($shown_pages[i]).attr("name", $shown_pages[i]);
    }

    if (current === 1) {
        $pagination.find(".first").addClass("disabled");
    }
    if (current === pages_count) {
        $pagination.find(".last").addClass("disabled");
    }
    $pagination.find("li a:contains('"+current+"')").parent().addClass("active");
}

function create_table(response) {
    $("table#customers").find("tbody").children().remove();

    recordsTotal = response.recordsTotal;
    pages = Math.ceil(recordsTotal/10);
    paginationSet(pages, response.draw);
    $(function () {
        $.each(response.data, function (i, item) {
            var $tr = $('<tr>').append(
                $('<td>').text(item[0]),
                $('<td>').text(item[1])
            );
            $("#customers").find("tbody").append($tr);
        });
    })
}

$(document).ready(function() {
    get_cust(1, create_table);

    $(".pagination").on('click', '.page-link.customers', function (e) {
        e.preventDefault();
        get_cust($(this).attr("name"), create_table);
    });
} );
