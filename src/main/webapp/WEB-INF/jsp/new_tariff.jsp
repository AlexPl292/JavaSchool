<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 21.08.16
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles.css">
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/jquery-3.1.0.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <title>New tariff</title>

    <script>
        $(document).ready(function() {
            $('#add_tariff_form').submit(function (event) {
                var $form = $(this);
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
<form class="form-horizontal" id="add_tariff_form" action='add_tariff' method="POST">
    <fieldset>
        <div id="legend">
            <legend class="">Add new tariff</legend>
        </div>
        <div class="control-group">
            <label class="control-label" for="name">Name</label>
            <div class="controls">
                <input type="text" id="name" name="name" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="cost">Cost</label>
            <div class="controls">
                <input type="text" id="cost" name="cost" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="description">Description</label>
            <div class="controls">
                <textarea id="description" name="description" placeholder="" class="form-control textarea-xlarge required"></textarea>
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

