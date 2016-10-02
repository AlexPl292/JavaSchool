<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 20.08.16
  Time: 12:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <link rel="import" href="${pageContext.request.contextPath}/resources/public/pieces.html">

    <link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAA8oQPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREREQAAAAABAQEBAAAAAAEREREAAAAAAQEBAQAAAAABERERAAAAAAEBAQEAAAAAAQEBAQAAAAABERERAAAAAAEREREAAAAAAQAAAQAAAAABAAABAAAAAAEAAAEAAAAAAREREQAAAAABERERAAAAAAAAARAAAAAAAAABEAAADwHwAA9V8AAPAfAAD1XwAA8B8AAPVfAAD1XwAA8B8AAPAfAAD33wAA998AAPffAADwHwAA8B8AAP8/AAD/PwAA" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/datatable/css/datatables.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.8.1.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery.serialize-object.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/js.cookie.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/datatable/js/datatables.min.js"></script>

    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/formhelpers/js/bootstrap-formhelpers-phone.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/form_validation.js"></script>
    <script src="<%=application.getContextPath() %>/resources/js/options.js"></script>
    <title>New customer</title>
</head>
<body>
    <div class="wrapper">
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">eCare</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="/change_password"><i class="fa fa-gear fa-fw"></i> Change </a>
                        </li>
                        <%--                <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                                        </li>--%>
                        <li class="divider"></li>
                        <li><a href="/sign_out"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
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
                                    <a id="new_customer_menu" onclick="loadpage('new_customer')">Add new customer</a>
                                </li>
                                <li>
                                    <a id="customer_menu" onclick="loadpage('customer')">Show all customers</a>
                                </li>
                            </ul>
                        </li>

                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-cog fa-fw"></i> Tariffs<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="new_tariff_menu" onclick="loadpage('new_tariff')">Add new tariff</a>
                                </li>
                                <li>
                                    <a id="tariff_menu" onclick="loadpage('tariff')">Show all tariffs</a>
                                </li>
                            </ul>
                        </li>

                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-cogs fa-fw"></i> Options<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="new_option_menu" onclick="loadpage('new_option')">Add new option</a>
                                </li>
                                <li>
                                    <a id="option_menu" onclick="loadpage('option')">Show all options</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                            <a href="#"><i class="fa fa-credit-card fa-fw"></i> Contracts<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="contract_menu" onclick="loadpage('contract')">Show all contracts</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
        <div id="page-wrapper">
        </div>
    </div>

<script>

    $(function () {
        $('#side-menu').metisMenu();
        var currentPage = Cookies.get("currentPage");
        if (currentPage !== undefined) {
            loadpage(currentPage);
        }
    });

</script>

</body>
</html>
