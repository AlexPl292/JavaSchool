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
<%--    <script src="<%=application.getContextPath() %>/resources/js/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>--%>
    <script src="<%=application.getContextPath() %>/resources/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/notify.min.js"></script>
    <title>New customer</title>
    <script>
        function loadlist(selobj, url, nameattr, valattr) {
            $(selobj).empty();
            $.getJSON(url, {page:-1, updateCount:false, search:""}, function (data) {
                    $.each(data.data, function (i, obj) {
                       $(selobj).append($("<option></option>").val(obj[valattr]).html(obj[nameattr]));
                    })
                });
        }

        $(document).ready(function() {
            loadlist($("#tariff"), "/load_tariffs", "name", "id");
            $('#add_customer_form').submit(function (e) {
                e.preventDefault();
                var $form = $(this);

                $.post($form.attr("action"), $form.serialize(), function(response) {
                    if (response.success) {
                        $('input[type=submit]').notify("success", {position:"right", className:"success"});
                        $form[0].reset();
                    } else {
                        $('input[type=submit]').notify("Errors! See above", {position:"right", className:"error"});
                        $.each(response.errors, function(prop, val) {
                            $form.find('[name='+prop+']').notify("Error:"+val, {position:"top center", className:"error"});
                        });
                    }
                }, 'json');
                return false;
            });
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
                                        <input type="text" id="name" name="name" placeholder="" class="form-control input-xlarge">
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="surname">Surname</label>
                                    <div class="controls">
                                        <input type="text" id="surname" name="surname" placeholder="" class="form-control input-xlarge">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="birthday">Day of birth</label>
                            <div class="controls input-group">
                                <span class="input-group-addon"><i class="fa fa-calendar"></i> </span>
                                <input type="date" id="birthday" name="birthday" placeholder="" class="form-control input-xlarge">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-6">
                                <div class="control-group">
                                    <label class="control-label" for="passport">Passport data</label>
                                    <div class="controls">
                                        <textarea id="passport" name="passport" placeholder="" class="form-control textarea-xlarge"></textarea>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
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
                        <label class="control-label" for="tariff">tariff</label>
                        <div class="controls">
                            <select id="tariff" name="tariff" class="form-control">
                            </select>
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
