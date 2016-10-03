package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<CustomerDto> existings = service.findByPassportNumberOrEmail(customer.getPassportNumber(), customer.getEmail());
        if (existings.size() > 0) { // TODO провека существования номера контракта
            if (existings.get(0).getEmail().equalsIgnoreCase(customer.getEmail()))
                throw new UniqueFieldDuplicateException("Email", customer.getEmail(), "/rest/options/"+existings.get(0).getId());
            else
                throw new UniqueFieldDuplicateException("PassportNumber", customer.getPassportNumber(), "/rest/options/"+existings.get(0).getId());
        }
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
