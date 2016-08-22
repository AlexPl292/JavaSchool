<!DOCTYPE html>
<html lang="en">
<head>
    <title>Index</title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/resources/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1>Hello</h1>
    <jsp:include page="/WEB-INF/jsp/new_customer.jsp"/>
    <jsp:include page="/WEB-INF/jsp/new_tariff.jsp"/>
    <jsp:include page="/WEB-INF/jsp/show_customers.jsp"/>
</div>
</body>
</html>

