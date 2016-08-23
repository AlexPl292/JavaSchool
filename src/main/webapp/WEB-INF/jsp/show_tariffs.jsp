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

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/table_creator.js"></script>
    <script>
        $(document).ready(function () {
            table_creator($('.pagination-tariffs'), $('#tariffs'), '/show_tariffs')
        })
    </script>
    <title>Show tariffs</title>
</head>
<body>
<table id="tariffs" class="table table-striped">
    <thead>
    <tr>
        <th abbr="name">Tariff name</th>
        <th abbr="cost">cost</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
<ul class="pagination pagination-tariffs">
    <li class='page-item first'>
        <a class='page-link tariffs' href='#' name="first">
            <span aria-hidden='true'>&laquo;</span>
            <span class='sr-only'>First</span>
        </a>
    </li>
    <li class="page-item last">
        <a class="page-link tariffs" href="#" aria-label="Last" name="last">
            <span aria-hidden="true">&raquo;</span>
            <span class="sr-only">Last</span>
        </a>
    </li>
</ul>
</body>
</html>

