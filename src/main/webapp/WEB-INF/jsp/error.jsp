<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 11.09.16
  Time: 1:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAA8oQPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAREREQAAAAABAQEBAAAAAAEREREAAAAAAQEBAQAAAAABERERAAAAAAEBAQEAAAAAAQEBAQAAAAABERERAAAAAAEREREAAAAAAQAAAQAAAAABAAABAAAAAAEAAAEAAAAAAREREQAAAAABERERAAAAAAAAARAAAAAAAAABEAAADwHwAA9V8AAPAfAAD1XwAA8B8AAPVfAAD1XwAA8B8AAPAfAAD33wAA998AAPffAADwHwAA8B8AAP8/AAD/PwAA"
          rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css"
          href="<%=application.getContextPath() %>/resources/vendor/bootstrap/css/bootstrap.min.css">
    <title>Error</title>
    <style>
        .center {
            text-align: center;
            margin: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="span12">
            <div class="hero-unit center">
                <h1>Page Not Found
                    <small><font face="Tahoma" color="red">Error 404</font></small>
                </h1>
                <br/>
                <p>The page you requested could not be found, either contact your webmaster or try again. Use your
                    browsers <b>Back</b> button to navigate to the page you have prevously come from</p>
                <p><b>Or you could just press this neat little button:</b></p>
                <a href="/" class="btn btn-large btn-info"> Take Me Home</a>
            </div>
            <br/>
        </div>
    </div>
</div>

</body>
</html>
