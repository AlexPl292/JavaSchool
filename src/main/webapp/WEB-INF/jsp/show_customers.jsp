<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 21.08.16
  Time: 20:27
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
//            table_creator($('.pagination-customers'), $('#customers'), '/show_customers')
            table_creator($('#customers'), '/show_customers')
        })
    </script>
    <title>Show customers</title>
</head>
<body>
<div id="customers">
    <table class="table table-striped">
        <thead>
        <tr>
            <th abbr="name">First name</th>
            <th abbr="surname">Last name</th>
            <th abbr="email">Email</th>
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
</body>
</html>
