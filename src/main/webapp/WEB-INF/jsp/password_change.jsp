<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 12.09.16
  Time: 21:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Login</title>
    <link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAA8oQPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREREQAAAAABAQEBAAAAAAEREREAAAAAAQEBAQAAAAABERERAAAAAAEBAQEAAAAAAQEBAQAAAAABERERAAAAAAEREREAAAAAAQAAAQAAAAABAAABAAAAAAEAAAEAAAAAAREREQAAAAABERERAAAAAAAAARAAAAAAAAABEAAADwHwAA9V8AAPAfAAD1XwAA8B8AAPVfAAD1XwAA8B8AAPAfAAD33wAA998AAPffAADwHwAA8B8AAP8/AAD/PwAA" rel="icon" type="image/x-icon" />

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/metisMenu/css/metisMenu.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/sb-admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/vendor/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.1.7.css">

    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/metisMenu/js/metisMenu.min.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath() %>/resources/vendor/sb-admin/js/sb-admin-2.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/jquery.validate/jquery.validate.min.js"></script>

    <script>
        $(function () {
            var $form = $('#changePassword');
            $form.validate({
                rules: {
                    oldPassword: {
                        required:true,
                    },
                    newPassword:{
                        required:true,
                        minlength: 8
                    },
                    repeatNewPassword: {
                        required: true,
                        equalTo: '#newPassword'
                    }
                },
                messages: {
                    oldPassword: {
                        required: "Please, enter old password"
                    },
                    newPassword:{
                        required: "Please, enter new password",
                        minlength: "New password should contain at least 8 characters!"
                    },
                    repeatNewPassword: {
                        required: "Please, repeat new password",
                        equalTo: "Passwords are not equal!"
                    }
                },
                submitHandler: function (form, e) {
                    e.preventDefault();
                    $.post($(form).attr("action"), $(form).serialize(), function (res) {
                        if (res.success) {
                            $.notify("Success!", {position: "top right", className: "success"});
                        } else {
                            $.each(res.errors, function (prop, val) {
                                $.notify("Error: in " + prop + "\n" + val, {position: "top right", className: "error"});
                            });
                        }
                    })
                }
            });
        })
    </script>
</head>

<body>

<div class="wrapper">
    <c:set var="isCustomer" value="${\"customer\".equals(sessionScope.user)}"/>
    <div id="page-wrapper" style="min-height: 939px;">
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="login-panel panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Change password</h3>
                        </div>
                        <div class="panel-body">
                            <form role="form" action="/change_password" method="POST" id="changePassword">
                                <fieldset>
                                    <div class="form-group">
                                        <input class="form-control" placeholder="Old password" name="oldPassword" type="password" autofocus>
                                    </div>
                                    <div class="form-group">
                                        <input id="newPassword" class="form-control" placeholder="New password" name="newPassword" type="password" value="">
                                    </div>
                                    <div class="form-group">
                                        <input class="form-control" placeholder="Repeat new password" name="repeatNewPassword" type="password" value="">
                                    </div>
                                    <!-- Change this to a button or input when using this as a form -->
                                    <input type="submit" class="btn btn-lg btn-success btn-block" title="Login"/>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
