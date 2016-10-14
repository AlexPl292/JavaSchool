package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity addNewCustomer(@Valid @RequestBody CustomerDto customer) throws UniqueFieldDuplicateException {

        CustomerDto newCustomer = service.addNew(customer);
        return ResponseEntity.created(URI.create("/rest/customers/" + newCustomer.getId())).body(newCustomer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> loadAll() {
        return service.loadAll();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto loadCustomer(@PathVariable Integer customerId, Principal principal, HttpServletRequest request) throws ResourceNotFoundException {
        if (!request.isUserInRole("ROLE_ADMIN") &&
                !Objects.equals(((User)((UsernamePasswordAuthenticationToken)principal).getPrincipal()).getId(), customerId)) {
            return null;
        }
        CustomerDto entity = service.loadByKey(customerId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", customerId);
        }
        return entity;
    }
}
