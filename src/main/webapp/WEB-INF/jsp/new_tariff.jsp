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
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Add new tariff</h1>
    </div>
</div>
<form class="form-horizontal" id="add_tariff_form" action='add_tariff' method="POST">
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">New tariff data</div>
                <div class="panel-body">
                    <fieldset>
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
                    </fieldset>
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

