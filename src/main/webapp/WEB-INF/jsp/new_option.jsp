<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 26.08.16
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles.css">
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <script>
        $(document).ready(function() {
            prepare();
            $('#add_option_form').submit(function (event) {
                $form = $(this);
                if (!valid_inputs($(this))) {
                    event.preventDefault();
                    return false;
                }

                $.post($form.attr("action"), $form.serialize(), function(response) { //TODO должен быть выбран хотя бы один тариф
                    // TODO Заполнить все возвраты!
                });
                return false;
            });
        });
    </script>
    <title>New Option</title>
</head>
<body>
<form class="form-horizontal" id="add_option_form" action="add_option" method="POST">
    <fieldset>
        <div id="legend">
            <legend class="">Add new option</legend>
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
            <label class="control-label" for="connect_cost">Connect cost</label>
            <div class="controls">
                <input type="text" id="connect_cost" name="connect_cost" placeholder="" class="form-control input-xlarge required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="description">Description</label>
            <div class="controls">
                <textarea id="description" name="description" placeholder="" class="form-control textarea-xlarge required"></textarea>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="forTariffs">Available for there tariffs:</label>
            <div id="forTariffs" class="boxes">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="requiredFrom">Required from</label>
            <div id="requiredFrom" class="boxes">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="forbiddenWith">Forbidden with</label>
            <div id="forbiddenWith" class="boxes">
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
