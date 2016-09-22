<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 21.08.16
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAA8oQPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREREQAAAAABAQEBAAAAAAEREREAAAAAAQEBAQAAAAABERERAAAAAAEBAQEAAAAAAQEBAQAAAAABERERAAAAAAEREREAAAAAAQAAAQAAAAABAAABAAAAAAEAAAEAAAAAAREREQAAAAABERERAAAAAAAAARAAAAAAAAABEAAADwHwAA9V8AAPAfAAD1XwAA8B8AAPVfAAD1XwAA8B8AAPAfAAD33wAA998AAPffAADwHwAA8B8AAP8/AAD/PwAA" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.6.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/table_creator.js"></script>
    <script>
        $(function () {
            table_creator($('#customers'), '/admin/load_customers', "/admin/customer");
            $('table').on("click", '.clickableRow', function() {
                window.location = $(this).data("href")+"?id="+$(this).data("val");
            });
        })
    </script>
    <title>Show customers</title>
</head>
<body>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/template.jsp"/>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Customers</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">Show all customers</div>
                    <div class="panel-body">
                        <div id="customers">
                            <div id="search-form" class="form-group input-group">
                                <input id="search_query" type="text" class="form-control" placeholder="Search by name/surname" >
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="submit"><i class="fa fa-search"></i>
                                    </button>
                                </span>
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th abbr="name">First name</th>
                                    <th abbr="surname">Last name</th>
                                    <th abbr="email">Email</th>
                                    <th abbr="passportNumber">Passport number</th>
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
