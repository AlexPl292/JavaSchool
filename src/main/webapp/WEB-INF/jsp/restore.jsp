<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 10.10.16
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Restore password</title>
    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/jquery/jquery-3.1.0.min.js"></script>
    <script type="text/javascript"
            src="<%=application.getContextPath() %>/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script src="<%=application.getContextPath() %>/resources/vendor/notify/notify.min.js"></script>

    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/styles_v0.2.2.css">
    <script>
        $(function () {
            var form = $('#reset-form');
            form.submit(function (e) {
                e.preventDefault();
                var action = form.attr("action");
                var loginButton = $('#toLogin');
                var submit = $('#submit');
                $(loginButton).addClass("disabled");
                $(submit).addClass("disabled");
                $.ajax({
                    url: action,
                    type: "POST",
                    data: form.serialize(),
                    success: function (data) {
                        if (action === "<%=application.getContextPath() %>/rest/users/reset") {
                            $(".pass-change").show();
                            $("#submit").val("Change password");
                            form.attr("action", "<%=application.getContextPath() %>/rest/users/password")
                        } else {
                            $.notify("Success!", {position: "top right", className: "success"});
                            $('#toLoginDiv').show();
                            $('#toLogin').click(function (e) {
                                e.preventDefault();
                                window.location = "login";
                            });
                        }

                        $(loginButton).removeClass("disabled");
                        $(submit).removeClass("disabled");
                    },
                    error: function (jqXHR) {
                        $.each(jqXHR.responseJSON.errors, function (prop, val) {
                            $.notify("Error:" + prop + "\n" + val, {position: "top right", className: "error"});
                        });
                        $(loginButton).removeClass("disabled");
                        $(submit).removeClass("disabled");
                    }
                });
            });
        })
    </script>
</head>
<body>
<div class="form-gap"></div>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="text-center">
                        <h3><i class="fa fa-lock fa-4x"></i></h3>
                        <h2 class="text-center">Have no password?</h2>
                        <p>You can get your password here.</p>
                        <div class="panel-body">

                            <form id="reset-form" role="form" autocomplete="off" class="form" method="post"
                                  action="<%=application.getContextPath() %>/rest/users/reset">

                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i
                                                class="glyphicon glyphicon-envelope color-blue"></i></span>
                                        <input id="email" name="email" placeholder="email address" class="form-control"
                                               type="email">
                                    </div>

                                </div>

                                <div class="pass-change" style="display: none;">
                                    <div class="form-group">
                                        <input id="code" name="code" placeholder="Code" class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <input type="password" id="newPassword" name="newPassword"
                                               placeholder="Password" class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <input type="password" id="repeatPassword" name="repeatPassword"
                                               placeholder="Repeat password" class="form-control">
                                    </div>
                                </div>


                                <div class="form-group">
                                    <input id="submit" name="recover-submit" class="btn btn-lg btn-primary btn-block"
                                           value="Get code" type="submit">
                                </div>
                            </form>

                            <div class="form-group" id="toLoginDiv" style="display: none;">
                                <input id="toLogin" class="btn btn-lg btn-success btn-block" value="Go to login page"
                                       type="button">
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
