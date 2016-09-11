<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 02.09.16
  Time: 0:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!-- Navigation -->
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
                            <a href="/admin/add_customer">Add new customer</a>
                        </li>
                        <li>
                            <a href="/admin/customers">Show all customers</a>
                        </li>
                    </ul>
                </li>

                <li>
                    <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                    <a href="#"><i class="fa fa-cog fa-fw"></i> Tariffs<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="/admin/add_tariff">Add new tariff</a>
                        </li>
                        <li>
                            <a href="/admin/tariffs">Show all tariffs</a>
                        </li>
                    </ul>
                </li>

                <li>
                    <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                    <a href="#"><i class="fa fa-cogs fa-fw"></i> Options<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="/admin/add_option">Add new option</a>
                        </li>
                        <li>
                            <a href="/admin/options">Show all options</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <%--<a href="/index"><i class="fa fa-fax fa-fw"></i> Users</a>--%>
                    <a href="#"><i class="fa fa-credit-card fa-fw"></i> Contracts<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="/admin/contracts">Show all contracts</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
</nav>

