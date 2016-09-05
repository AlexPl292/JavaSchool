<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 05.09.16
  Time: 0:59
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

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.5.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/table_creator.js"></script>
    <script>
        $(document).ready(function () {
            table_creator($('#contracts'), '/load_contracts')
        })
    </script>
    <title>Show contracts</title>
</head>
<body>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/template.jsp"></c:import>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Contracts</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">Show all contracts</div>
                    <div class="panel-body">
                        <div id="contracts">
                            <div id="search-form" class="form-group input-group">
                                <span class="input-group-addon"><i class="fa fa-phone"></i> </span>
                                <input id="search_query" type="text" class="form-control bfh-phone" data-format="+7 (ddd) ddd-dddd" placeholder="Search by number" >
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="submit"><i class="fa fa-search"></i>
                                    </button>
                                </span>
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th abbr="number">Contract number</th>
                                    <th abbr="tariff.name">Tariff</th>
                                    <th abbr="customer.name">Customer name</th>
                                    <th abbr="customer.surname">Customer surname</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                            <ul class="pagination">
                                <li class='page-item first'>
                                    <a class='page-link' href='#' name="first">
                                        <span aria-hidden='true'>&laquo;</span>
                                        <span class='sr-only'>First</span>
                                    </a>
                                </li>
                                <li class="page-item last">
                                    <a class="page-link" href="#" aria-label="Last" name="last">
                                        <span aria-hidden="true">&raquo;</span>
                                        <span class="sr-only">Last</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

