<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 23.08.16
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/table_creator.js"></script>
    <script>
        $(document).ready(function () {
            table_creator($('#tariffs'), '/load_tariffs')
        })
    </script>
    <title>Show tariffs</title>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Tariffs</h1>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Show all tariffs</div>
            <div class="panel-body">
                <div id="tariffs">
                    <div id="search-form" class="form-group input-group">
                        <input id="search_query" type="text" class="form-control" placeholder="Search by tariff name" >
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit"><i class="fa fa-search"></i>
                            </button>
                        </span>
                    </div>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th abbr="name">Tariff name</th>
                            <th abbr="cost">cost</th>
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
</body>
</html>

