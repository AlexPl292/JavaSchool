package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by alex on 19.08.16.
 * Add new customer
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/customer")
public class CustomerRest {

    private final transient CustomerService service;

    @Autowired
    public CustomerRest(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addNewCustomer(@Valid @RequestBody CustomerDto customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new StatusResponse(bindingResult);
        }

        service.addNew(customer);
        return new StatusResponse();
    }

    @GetMapping
    public List<CustomerDto> loadAll() {
        return service.loadAll();
    }
}
