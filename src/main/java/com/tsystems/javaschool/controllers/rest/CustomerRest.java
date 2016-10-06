package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Created by alex on 19.08.16.
 * Add new customer
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/customers")
public class CustomerRest {

    private final CustomerService service;

    @Autowired
    public CustomerRest(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addNewCustomer(@Valid @RequestBody CustomerDto customer) {
        DataBaseValidator.checkUnique(customer);

        CustomerDto newCustomer = service.addNew(customer);
        return ResponseEntity.created(URI.create("/rest/customers/"+newCustomer.getId())).body(newCustomer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> loadAll() {
        return service.loadAll();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto loadCustomer(@PathVariable Integer customerId) {
        CustomerDto entity = service.loadByKey(customerId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", customerId);
        }
        return entity;
    }
}
