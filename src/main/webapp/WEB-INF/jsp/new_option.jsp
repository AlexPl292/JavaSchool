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
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.1.css">
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <script>
        $(document).ready(function() {
            prepare();
            $('#add_option_form').submit(function (event) {
                var $form = $(this);
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
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Add new option</h1>
    </div>
</div>
<form class="form-horizontal" id="add_option_form" action="add_option" method="POST">
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">New option data</div>
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
                        </fieldset>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">Available for there tariffs</div>
                <div class="panel-body boxes">
                        <div id="forTariffs"> </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">Required from</div>
                <div class="panel-body boxes">
                    <div id="requiredFrom"> </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">Incompatible with</div>
                <div class="panel-body boxes">
                    <div id="forbiddenWith"> </div>
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
