<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 02.09.16
  Time: 0:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/font-awesome/css/font-awesome.min.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/js/sb-admin-2.min.js"></script>

    <script>
        $(function () {
            $.get('/load_content', {"path":window.location.pathname}, function (data) {
                $('#content').html(data);
            });
        });
    </script>
</head>
<body>

<div id="wrapper">

    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.html">eCare</a>
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                    </li>
                    <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="login.html"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>
                </ul>
                <!-- /.dropdown-user -->
            </li>
        </ul>
        <!-- /.navbar-top-links -->

        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                        <a href="#"><i class="fa fa-group fa-fw"></i> Customers<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="/add_customer">Add new customer</a>
                            </li>
                            <li>
                                <a href="/show_customers">Show all customers</a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                        <a href="#"><i class="fa fa-cog fa-fw"></i> Tariffs<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="/add_tariff">Add new tariff</a>
                            </li>
                            <li>
                                <a href="/show_tariffs">Show all tariffs</a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                        <a href="#"><i class="fa fa-cogs fa-fw"></i> Options<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="/add_option">Add new option</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>

    <!-- Page Content -->
    <div id="page-wrapper">
        <div id="content">
        </div>
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

</body>
</html>
