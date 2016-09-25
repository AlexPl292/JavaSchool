<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 20.08.16
  Time: 12:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAA8oQPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREREQAAAAABAQEBAAAAAAEREREAAAAAAQEBAQAAAAABERERAAAAAAEBAQEAAAAAAQEBAQAAAAABERERAAAAAAEREREAAAAAAQAAAQAAAAABAAABAAAAAAEAAAEAAAAAAREREQAAAAABERERAAAAAAAAARAAAAAAAAABEAAADwHwAA9V8AAPAfAAD1XwAA8B8AAPVfAAD1XwAA8B8AAPAfAAD33wAA998AAPffAADwHwAA8B8AAP8/AAD/PwAA" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/datatable/datatables.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.7.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery.serialize-object.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/js.cookie.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/datatable/datatables.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/customer_validate_rules.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <title>New customer</title>
</head>
<body>
    <div class="wrapper">
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">eCare</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="/change_password"><i class="fa fa-gear fa-fw"></i> Change </a>
                        </li>
                        <%--                <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                                        </li>--%>
                        <li class="divider"></li>
                        <li><a href="/sign_out"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-group fa-fw"></i> Customers<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="new_customer_menu" onclick="loadpage('new_customer')">Add new customer</a>
                                </li>
                                <li>
                                    <a href="/admin/customers">Show all customers</a>
                                </li>
                            </ul>
                        </li>

                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-cog fa-fw"></i> Tariffs<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="new_tariff_menu" onclick="loadpage('new_tariff')">Add new tariff</a>
                                </li>
                                <li>
                                    <a id="tariff_menu" onclick="loadpage('tariff')">Show all tariffs</a>
                                </li>
                            </ul>
                        </li>

                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-cogs fa-fw"></i> Options<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="new_option_menu" onclick="loadpage('new_option')">Add new option</a>
                                </li>
                                <li>
                                    <a id="option_menu" onclick="loadpage('option')">Show all options</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-credit-card fa-fw"></i> Contracts<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="/admin/contracts">Show all contracts</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
        <div id="page-wrapper">
        </div>
    </div>

<script>

    $(function () {
        $('#side-menu').metisMenu();
        var currentPage = Cookies.get("currentPage");
        if (currentPage !== undefined) {
            loadpage(currentPage);
        }
    });

    function loadpage(page) {
        var element = $('#side-menu').find('#'+page+"_menu");
        $(element).closest('ul').addClass('in').attr('aria-expanded', true);
        $(element).addClass("active");
        Cookies.set("currentPage", page, {expires: 1});
        var $page_wrapper = $('#page-wrapper');

        $.get("/resources/public/" + page + ".html", {}, function (res) {
            $page_wrapper.html(res);
            prepare[page]();
        })
    }

    var prepare = {
        "option" : function() {
            $('#content_table').DataTable({
                ajax:{
                    url:"/rest/option",
                    dataSrc: ''
                },
                order: [[0, 'asc']],
                columns: [
                    {title:"Name", data:"name"},
                    {title:"Cost", data:"cost"},
                    {title:"Connection cost", data:"connectCost"},
                    {title:"Description", data:"description"},
                ]
            })
        },
        "tariff" : function() {
            $('#content_table').DataTable({
                ajax:{
                    url:"/rest/tariff",
                    dataSrc: ''
                },
                order: [[0, 'asc']],
                columns: [
                    {title:"Name", data:"name"},
                    {title:"Cost", data:"cost"},
                    {title:"Description", data:"description"},
                ]
            })
        },
        "/customer" : function () {
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
                    $.post("/customer"+href, {id: id}, function () {
                        $panel.remove();
                    })
                } else if (href === "/blockContract") {
                    $.post("/customer"+href, {id: id}, function () {
                        $panel.removeClass("panel-default").addClass("panel-red");
                        $obj.attr("href", "/unblockContract").text("Unblock");
                        $panel.find(":contains('Edit')").addClass("text-muted");
                    });
                } else if (href === "/unblockContract") {
                    $.post("/customer"+href, {id: id}, function () {
                        $panel.removeClass("panel-red").addClass("panel-default");
                        $obj.attr("href", "/blockContract").text("Block");
                        $panel.find(":contains('Edit')").removeClass("text-muted");
                    });
                } else if (href === "/editTariff") {
                    edit_tariff($panel)
                }
            })
        },
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

                if (href === "/delete_contract") {
                    $.post("/admin"+href, {id: id}, function () {
                        $panel.remove();
                    })
                } else if (href === "/blockContract") {
                    $.post("/admin"+href, {id: id}, function () {
                        $panel.removeClass("panel-default").addClass("panel-red");
                        $obj.attr("href", "/admin/unblockContract").text("Unblock");
                        $panel.find(":contains('Edit')").addClass("text-muted");
                    });
                } else if (href === "/unblockContract") {
                    $.post("/admin"+href, {id: id}, function () {
                        $panel.removeClass("panel-red").addClass("panel-default");
                        $obj.attr("href", "/admin/blockContract").text("Block");
                        $panel.find(":contains('Edit')").removeClass("text-muted");
                    });
                } else if (href === "/editTariff") {
                    edit_tariff($panel)
                }
            })
        },
        "new_customer" : function() {
            prepare_tariff_list($('#tariff'), $('#options'));
        },
        "new_option" : function () {
            var requiredFrom = $("#requiredFrom");
            var forbiddenWith = $("#forbiddenWith");
            var forTariffs = $('#forTariffs');

            forTariffs.empty();
            $.getJSON("/rest/tariff", {}, create_boxes(forTariffs, "possibleTariffsOfOption[][id]"));

            $(forTariffs).on('change', 'input[type=checkbox]',  function (e) {
                e.preventDefault();
                requiredFrom.empty();
                forbiddenWith.empty();
                var data = $("#forTariffs").find("input").serializeArray();
                $.getJSON("/rest/option/forTariffs", $.param(data), function(data) {
                    create_boxes(requiredFrom, "requiredFrom[][id]")(data);
                    create_boxes(forbiddenWith, "forbiddenWith[][id]")(data);
                });
            });

            $(requiredFrom).on('change', 'input[type=checkbox]', check_item("requiredFrom"));
            $(forbiddenWith).on('change', 'input[type=checkbox]', check_item("forbidden"));
        },
        "new_tariff" : function () {
            $('#possibleOptions').on('change', 'input[type=checkbox]', optionCheckedNewTariff);
            $.getJSON("/rest/option", {}, create_boxes($('#possibleOptions'), "possibleOptions[][id]"));
        }
    };
</script>

</body>
</html>
