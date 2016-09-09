<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLine", "\r\n"); %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 06.09.16
  Time: 0:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Customer</title>
    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.6.css">

    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/accordioner.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/contract_validate_rules.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <script>
        function handler(form, e) {
            e.preventDefault();
            $(form).find("input[type=checkbox]").prop("disabled", false);
            $.post($(form).attr("action"), $(form).serialize(), create_accordion_node);
            $(form).find(":input").prop("disabled", true);
        }

        function edit_tariff(panel, obj) {
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
            $(panel).find('.panel-heading .btn-group').html('<button type="button" class="btn btn-outline btn-danger btn-xs">Exit editing</button>');
            $(panel).find('button:contains("Exit editing")').click(function (e) {
                e.preventDefault();
                $(panel).html(panel_backup);
            });
            $(panel).find('.panel-body').append('<div class="col-lg-12"><div class="controls"><input type="submit" class="btn btn-success"/></div></div>');
            $(panel).find('.panel-body').wrapInner('<form class="form-horizontal" action="editContract" method="POST"></form>');
            $(panel).find('form').submit({form:$(panel).find('form')}, edit_handler);
        }

        function edit_handler(e) {
            e.preventDefault();
            console.log("x");
            var form = e.data.form;
            $(form).find("input[type=checkbox]").prop("disabled", false);
            $.post($(form).attr("action"), $(form).serialize(), function (e) {
                console.log("submit");
            });
            $(form).find(":input").prop("disabled", true);
        }

        $(function () {
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

                if (href === "/deleteContract") {
                    $.post(href, {id: id}, function (e) {
                        $panel.remove();
                    })
                } else if (href === "/blockContract") {
                    $.post(href, {id: id}, function (e) {
                        $panel.removeClass("panel-default").addClass("panel-red");
                        $obj.attr("href", "/unblockContract").text("Unblock");
                        $panel.find(":contains('Edit')").addClass("text-muted");
                    });
                } else if (href === "/unblockContract") {
                    $.post(href, {id: id}, function (e) {
                        $panel.removeClass("panel-red").addClass("panel-default");
                        $obj.attr("href", "/blockContract").text("Block");
                        $panel.find(":contains('Edit')").removeClass("text-muted");
                    });
                } else if (href === "/editTariff") {
                    edit_tariff($panel, $obj)
                }
            })
        });

    </script>
</head>
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/template.jsp"></c:import>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">${customer.getSurname()} ${customer.getName()}</h1>
            </div>
        </div>
        <h1>
            <small>${customer.getEmail()}</small>
        </h1>
        <div class="row">
            <div class="col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">Passport</div>
                    <div class="panel-body">
                        <i>${customer.getPassportNumber()}</i>
                        <hr>
                        ${fn:replace(customer.getPassportData(), newLine, "<br/>")}
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">Address</div>
                    <div class="panel-body">
                        ${fn:replace(customer.getAddress(), newLine, "<br/>")}
                    </div>
                </div>
            </div>
            <div class="col-lg-6"></div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Contracts
                    </div>
                    <!-- .panel-heading -->
                    <div class="panel-body">
                        <div class="panel-group" id="accordion">
                            <c:forEach items="${customer.getContracts()}" var="contract">
                                <c:set var="blocked" value="${contract.getIsBlocked()}"/>
                                <div class="panel ${blocked == 0 ? "panel-default" : "panel-red"}">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#accordion"
                                               href="#collapse${contract.getId()}">${contract.getNumber()}</a>
                                            <div class="pull-right">
                                                <div class="btn-group">
                                                    <button type="button" class="btn btn-default btn-xs dropdown-toggle"
                                                            data-toggle="dropdown" aria-expanded="false">
                                                        Actions
                                                        <span class="caret"></span>
                                                    </button>
                                                    <ul class="dropdown-menu pull-right" role="menu">
                                                        <li><a href="/editTariff"><p ${blocked != 0 ? "class=\"text-muted\"":""}>Edit</p></a>
                                                        </li>
                                                        <li>${blocked == 0 ? "<a href=\"/blockContract\"><p>Block</p></a>":"<a href=\"/unblockContract\"><p>Unblock</p></a>"}
                                                        </li>
                                                        <li class="divider"></li>
                                                        <li><a href="/deleteContract"><p class="text-danger">Delete</p></a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </h4>
                                    </div>
                                    <div id="collapse${contract.getId()}" class="panel-collapse collapse"
                                         style="height: 0px;">
                                        <div class="panel-body">
                                            <input type="hidden" name="contract_id" value="${contract.getId()}"/>
                                            <div class="col-lg-6">
                                                <h3>${contract.getTariff().getName()}</h3>
                                                <hr>
                                                    ${contract.getTariff().getDescription()}
                                            </div>
                                            <div class="col-lg-6">
                                                <div class="well">
                                                    <h4>
                                                        <small>Used options</small>
                                                    </h4>
                                                    <hr>
                                                    <ul>
                                                        <c:forEach items="${contract.getUsedOptions()}" var="option">
                                                            <li>${option.getName()}</li>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#newContract">Add new
                                            contract</a>
                                    </h4>
                                </div>
                                <div id="newContract" class="panel-collapse collapse" style="height: 0px;">
                                    <div class="panel-body">
                                        <div class="col-lg-12">
                                            <form class="form-horizontal" id="add_contract_form" action='add_contract'
                                                  method="POST">
                                                <input type="hidden" name="customer_id" value="${customer.getId()}">
                                                <c:import url="template_new_contract.jsp"/>
                                                <div class="row">
                                                    <div class="panel-body">
                                                        <!-- Button -->
                                                        <div class="controls">
                                                            <input type="submit" class="btn btn-success"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- .panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            <!-- /.col-lg-12 -->
        </div>
    </div>
</div>

</body>
</html>
