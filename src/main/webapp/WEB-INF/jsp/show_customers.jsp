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
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/table_creator.js"></script>

    <title>Title</title>
</head>
<body>
<table id="customers" class="table table-striped">
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
        <a class='page-link customers' href='#' name="first">
            <span aria-hidden='true'>&laquo;</span>
            <span class='sr-only'>First</span>
        </a>
    </li>
    <li class="page-item last">
        <a class="page-link customers" href="#" aria-label="Last" name="last">
            <span aria-hidden="true">&raquo;</span>
            <span class="sr-only">Last</span>
        </a>
    </li>
</ul>
</body>
</html>
