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
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/tariff_validate_rules.js"></script>
    <title>New tariff</title>
    <script>
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
            $.getJSON("/load_options_table", {}, create_boxes($('#options')));
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
                                <input type="text" id="name" name="name" placeholder="" class="form-control input-xlarge">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="cost">Cost</label>
                            <div class="controls input-group">
                                <span class="input-group-addon"><i class="fa fa-rub"></i> </span>
                                <input type="text" id="cost" name="cost" placeholder="" class="form-control input-xlarge">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="description">Description</label>
                            <div class="controls">
                                <textarea id="description" name="description" placeholder="" class="form-control textarea-xlarge"></textarea>
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
                <div class="panel-heading">New tariff data</div>
                <div class="panel-body">
                    <div class="control-group">
                        <label class="control-label" for="options">Options</label>
                        <div class="controls">
                            <div id="options" class="boxes"></div>
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
                    <div class="panel-body">
                    <!-- Button -->
                        <div class="controls">
                            <input type="submit" class="btn btn-success"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>

