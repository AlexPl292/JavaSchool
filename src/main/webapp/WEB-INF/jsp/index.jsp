<!DOCTYPE html>
<html lang="en">
<head>
    <title>SO question 4112686</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script>
        $(document).on("submit", "#addCustomerForm", function(event) {
            var $form = $(this);

            $.post($form.attr("action"), $form.serialize(), function(response) {
                // ...
            });

            event.preventDefault(); // Important! Prevents submitting the form.
        });
    </script>
</head>
<body>
<form id="addCustomerForm" action="addCustomer" method="post">
    Name: <input type="text" name="name" /><br>
    Surname: <input type="text" name="surname" /><br>
    Birthday: <input type="date" name="birthday" /><br>
    Passport data: <input type="text" name="passport" /><br>
    Address: <input type="text" name="address" /><br>
    Email: <input type="text" name="email" /><br>
    Password: <input type="text" name="passA" /><br>
    <input type="submit" name="submit" value="Add new customer" />
</form>
</body>
</html>

