package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.util.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 19.08.16.
 */
@WebServlet("/add_customer")
public class AddCustomerController extends HttpServlet {

    private CustomerService service = new CustomerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> errors = new HashMap<>();
        JsonObject json = new JsonObject();

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String birthdayStr = request.getParameter("birthday");
        String passportNumber = request.getParameter("passport_number");
        String passport = request.getParameter("passport");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String number = request.getParameter("number");

        // Validation
        String tmpError;
        if ((tmpError = Validator.name(name)) != null)
            errors.put("name", tmpError);
        if ((tmpError = Validator.name(surname)) != null)
            errors.put("surname", tmpError);
        if ((tmpError = Validator.dateOlderThen18(birthdayStr)) != null)
            errors.put("birthday", tmpError);
        if (address == null)
            errors.put("address", "Address is null");
        if (passport == null)
            errors.put("passport", "Passport is null");
        if ((tmpError = Validator.email(email)) != null)
            errors.put("email", tmpError);
        if ((tmpError = Validator.phone(number)) != null)
            errors.put("number", tmpError);
        if ((tmpError = Validator.noSpaces(passportNumber)) != null)
            errors.put("passport_number", tmpError);

        if (errors.isEmpty()) {

            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            Date birthday;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                birthday = sdf.parse(birthdayStr);
            } catch (ParseException e) {
                birthday = new Date(0);
            }
            newCustomer.setDateOfBirth(birthday);
            newCustomer.setPassportNumber(passportNumber);
            newCustomer.setPassportData(passport);
            newCustomer.setAddress(address);
            newCustomer.setEmail(email);
            newCustomer.setIsBlocked(0);

            Tariff tariff = new TariffServiceImpl().loadByKey(Integer.parseInt(request.getParameter("tariff")));

            Contract contract = new Contract();
            contract.setCustomer(newCustomer);
            contract.setNumber(number);
            contract.setTariff(tariff);
            contract.setIsBlocked(0);

            List<Integer> options = Arrays.stream(request.getParameterValues("options")).map(Integer::parseInt).collect(Collectors.toList());
            try {
                service.createCustomerAndContract(newCustomer, contract, options);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
