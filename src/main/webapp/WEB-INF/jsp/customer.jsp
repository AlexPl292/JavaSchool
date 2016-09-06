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
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.6.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/accordioner.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/contract_validate_rules.js"></script>
    <script>
        function loadlist(selobj, url, nameattr, valattr) {
            $(selobj).empty();
            $.getJSON(url, {page:-1, updateCount:false, search:""}, function (data) {
                $.each(data.data, function (i, obj) {
                    $(selobj).append($("<option></option>").val(obj[valattr]).html(obj[nameattr]));
                });
                $(selobj).change();
            });
        }

        function create_boxes(selobj) {
            return function (data) {
                $(selobj).empty();
                var checkboxs_name = selobj.attr('id');
                $.each(data.data, function (i, obj) {
                    $(selobj).append($("<input />", {type:"checkbox", id:checkboxs_name+i, value:obj.id, name:checkboxs_name}));
                    $(selobj).append($("<label/>", {"for": checkboxs_name+i, text:obj.name}));
                    $(selobj).append($("<br/>"));
                })
            }
        }

        function handler(form, e) {
            e.preventDefault();
            $.post($(form).attr("action"), $(form).serialize(), create_accordion_node);
        }

        $(function() {
            var $tariff = $("#tariff");

            $tariff.change(function (e) {
                e.preventDefault();
                $.getJSON("/load_options", {loadtype: "possibleOfTariff", tariff_id:$(this).val()}, create_boxes($('#options')));
            });
            loadlist($tariff, "/load_tariffs", "name", "id");

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
        <h1><small>${customer.getEmail()}</small></h1>
        <div class="row">
            <div class="col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">Passport</div>
                    <div class="panel-body">
                            <i>${customer.getPassportNumber()}</i> <hr>
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
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#collapse${contract.getId()}" >${contract.getNumber()}</a>
                                        </h4>
                                    </div>
                                    <div id="collapse${contract.getId()}" class="panel-collapse collapse" style="height: 0px;">
                                        <div class="panel-body">
                                            <div class="col-lg-6">
                                                <h3>${contract.getTariff().getName()}</h3>
                                                <hr>
                                                ${contract.getTariff().getDescription()}
                                            </div>
                                            <div class="col-lg-6">
                                                <div class="well">
                                                    <h4><small>Used options</small></h4>
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
                                        <a data-toggle="collapse" data-parent="#accordion" href="#newContract" >Add new contract</a>
                                    </h4>
                                </div>
                                <div id="newContract" class="panel-collapse collapse" style="height: 0px;">
                                    <div class="panel-body">
                                        <div class="col-lg-12">
                                            <form class="form-horizontal" id="add_contract_form" action='add_contract' method="POST">
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