/**
 * Created by alex on 21.08.16.
 */

function get_cust(callback) {
    $.ajax({
        type:'POST',
        url: "/show_customers",
        success: callback
    });
}

function paginationSet(pages_count, current) {
    var pages = pages_count > 3 ? 3 : pages_count;
    var $pagination = $('.pagination');

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
        $("<li class='page-item'><a class='page-link' href='#'></a></li>")
            .insertBefore($pagination.find(".next"))
            .find('a').text($shown_pages[i]);
    }

    if (current === 1) {
        $pagination.find(".previous").addClass("disabled");
    }
    if (current === pages_count) {
        $pagination.find(".next").addClass("disabled");
    }
    $pagination.find("li a:contains('"+current+"')").parent().addClass("active");
}

$(document).ready(function() {
    get_cust(function (response) {
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
    })
} );
