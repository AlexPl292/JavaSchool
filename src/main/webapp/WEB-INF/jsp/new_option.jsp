<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 26.08.16
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.6.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/option_validate_rules.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <title>New Option</title>
</head>
<body>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/template.jsp"></c:import>
    <div id="page-wrapper">
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
                                        <label class="control-label" for="connect_cost">Connection cost</label>
                                        <div class="controls input-group">
                                            <span class="input-group-addon"><i class="fa fa-rub"></i> </span>
                                            <input type="text" id="connect_cost" name="connect_cost" placeholder="" class="form-control input-xlarge">
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
                    <input type="hidden" name="tariff_list"/>
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
    </div>
</div>
</body>
</html>
