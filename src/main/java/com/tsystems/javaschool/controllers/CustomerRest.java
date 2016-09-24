package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by alex on 19.08.16.
 * Add new customer
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
public class CustomerRest {

    private final transient CustomerService service;
    private static final Logger logger = Logger.getLogger(CustomerRest.class);

    @Autowired
    public CustomerRest(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addNewCustomer(@Valid @RequestBody CustomerDto customer, BindingResult bindingResult) {
        StatusResponse response = new StatusResponse();

        if (!bindingResult.hasErrors()) {
            service.addNew(customer);
        } else {
            response.addBindingResult(bindingResult);
        }

        return response;
    }

    @GetMapping
    public List<CustomerDto> loadAll() {
        return service.loadAll();
    }

/*    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/WEB-INF/jsp/new_customer.jsp").forward(request, response);
        } catch (IOException e) {
            logger.error("Forward exception", e);
        } catch (ServletException e) {
            logger.error("Servlet exception", e);
        }
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
        String tariffIdStr = request.getParameter("tariff");

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
        if (tariffIdStr == null) {
            errors.put("tariff", "Choose tariff");
        }

        Integer tariffId = 0;
        try {
            tariffId = Integer.parseInt(tariffIdStr);
        } catch (NumberFormatException e) {
            errors.put("General", "Tariff id wrong format");
            logger.error("Exception while id converting", e);
        }

        Date birthday = new Date(0);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            birthday = sdf.parse(birthdayStr);
        } catch (ParseException e) {
            errors.put("General", "Date wrong format");
            logger.error("Exception while data converting", e);
        }

        if (errors.isEmpty()) {

            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            newCustomer.setDateOfBirth(birthday);
            newCustomer.setPassportNumber(passportNumber);
            newCustomer.setPassportData(passport);
            newCustomer.setAddress(address);
            newCustomer.setEmail(email);
            newCustomer.setIsBlocked(0);

//            Tariff tariff = TariffServiceImpl.getInstance().loadByKey(tariffId);
            Tariff tariff = new Tariff();

            Contract contract = new Contract();
            contract.setNumber(number);
            contract.setTariff(tariff);
            contract.setIsBlocked(0);
            contract.setBalance(new BigDecimal(100));

            // Get list of option ids from parameter
            String[] optionsIdStr = request.getParameterValues("options");
            List<Integer> options;
            if (optionsIdStr != null) {
                options = Arrays.stream(optionsIdStr).map(Integer::parseInt).collect(Collectors.toList());
            } else {
                options = new ArrayList<>();
            }
            ContractService contractService = ContractServiceImpl.getInstance();
            try {
                service.addNew(newCustomer);
                contract.setCustomer(newCustomer);
                contractService.addNew(contract, options);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
                logger.error("Exception while customer or contract creating", th);
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
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        } catch (IOException e){
            logger.error("Get writer exception!", e);
        }
    }*/
}
