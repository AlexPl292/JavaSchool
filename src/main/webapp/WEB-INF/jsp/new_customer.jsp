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
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles.css">
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
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
            loadlist($("#tariff"), "/show_tariffs", "name", "id");
            $('#add_customer_form').submit(function (event) {
                $form = $(this);
                if (!valid_inputs($(this))) {
                    event.preventDefault();
                    return false;
                }

                $.post($form.attr("action"), $form.serialize(), function(response) {

                });
                return false;
            });
        });
    </script>
</head>
<body>
<form class="form-horizontal" id="add_customer_form" action='add_customer' method="POST">
    <fieldset>
        <div id="legend">
            <legend class="">Add new customer</legend>
        </div>
        <div class="control-group">
            <label class="control-label" for="name">Name</label>
            <div class="controls">
                <input type="text" id="name" name="name" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="surname">Surname</label>
            <div class="controls">
                <input type="text" id="surname" name="surname" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="birthday">Day of birth</label>
            <div class="controls">
                <input type="date" id="birthday" name="birthday" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="passport">Passport data</label>
            <div class="controls">
                <input type="text" id="passport" name="passport" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="address">Address</label>
            <div class="controls">
                <input type="text" id="address" name="address" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <!-- E-mail -->
            <label class="control-label" for="email">E-mail</label>
            <div class="controls">
                <input type="email" id="email" name="email" placeholder="" class="form-control input-xlarge required">
                <span id="validEmail"></span>
                <p id="email_help" class="help-block">Please provide your E-mail</p>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="tariff">tariff</label>
            <div class="controls">
                <select id="tariff" name="tariff" class="form-control">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="number">Number</label>
            <div class="controls">
                <input type="text" id="number" name="number" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <!-- Button -->
            <div class="controls">
                <input type="submit" class="btn btn-success"/>
            </div>
        </div>
    </fieldset>
</form>
</body>
</html>
