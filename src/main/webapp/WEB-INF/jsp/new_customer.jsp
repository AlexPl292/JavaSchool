<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 20.08.16
  Time: 12:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/customer_validate_rules.js"></script>
    <title>New customer</title>
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
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Add new customer</h1>
    </div>
</div>
<form class="form-horizontal" id="add_customer_form" action='add_customer' method="POST">
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">New customer data</div>
                <div class="panel-body">
                    <fieldset>
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="name">Name</label>
                                    <div class="controls">
                                        <input type="text" id="name" name="name" placeholder="" class="form-control input-xlarge startsUppercase">
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="surname">Surname</label>
                                    <div class="controls">
                                        <input type="text" id="surname" name="surname" placeholder="" class="form-control input-xlarge startsUppercase">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="passport_number">Passport number</label>
                                    <div class="controls">
                                        <input type="text" id="passport_number" name="passport_number" placeholder="" class="form-control input-xlarge">
                                        <span class="help-block">Enter number without spaces</span>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="passport">Passport data</label>
                                    <div class="controls">
                                        <textarea id="passport" name="passport" placeholder="" class="form-control textarea-xlarge"></textarea>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="birthday">Day of birth</label>
                                    <div class="controls input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i> </span>
                                        <input type="date" id="birthday" name="birthday" placeholder="" class="form-control input-xlarge">
                                    </div>
                                    <span class="help-block">New customer should be 18 years of age or older</span>
                                </div>

                                <div class="control-group">
                                    <label class="control-label" for="address">Address</label>
                                    <div class="controls">
                                        <textarea id="address" name="address" placeholder="" class="form-control textarea-xlarge"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="control-group">
                            <!-- E-mail -->
                            <label class="control-label" for="email">E-mail</label>
                            <div class="controls input-group">
                                <span class="input-group-addon"><i class="fa fa-at"></i> </span>
                                <input type="email" id="email" name="email" placeholder="" class="form-control input-xlarge">
                                <span id="validEmail"></span>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">New contract data</div>
                <div class="panel-body">
                    <div class="control-group">
                        <label class="control-label" for="tariff">Tariff</label>
                        <div class="controls">
                            <select id="tariff" name="tariff" class="form-control">
                            </select>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="options">Options</label>
                        <div class="controls">
                            <div id="options" class="boxes"></div>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="number">Number</label>
                        <div class="controls input-group">
                            <span class="input-group-addon"><i class="fa fa-phone"></i> </span>
                            <input type="text" id="number" name="number" placeholder="" class="form-control input-xlarge bfh-phone" data-format="+7 (ddd) ddd-dddd">
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="panel-body">
                <div class="form-group">
                    <!-- Button -->
                    <div class="controls">
                        <input type="submit" class="btn btn-success"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>
