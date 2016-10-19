package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.CustomerDto;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
 *
 * Rest controller for customers
 */
@RestController
@RequestMapping("/rest/customers")
public class CustomerRest {

    private final CustomerService service;

    @Autowired
    public CustomerRest(CustomerService service) {
        this.service = service;
    }

    /**
     * Create new customer
     * @param customer new customer DTO
     * @return response entity
     * @throws JSException validation fail
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewCustomer(@Valid @RequestBody CustomerDto customer) throws JSException {
        CustomerDto newCustomer = service.addNew(customer);

        // Response entity with Located header
        return ResponseEntity.created(URI.create("/rest/customers/" + newCustomer.getId())).body(newCustomer);
    }

    /**
     * load oll customers
     * @return all customers as DTO
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<CustomerDto> loadAll() {
        return service.loadAll();
    }

    /**
     * Get customer by id
     * @param customerId customer id
     * @param principal principal to detect user
     * @param request principal to detect user
     * @return customer DTO
     * @throws ResourceNotFoundException resource not found
     */
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public CustomerDto loadCustomer(@PathVariable Integer customerId, Principal principal, HttpServletRequest request) throws ResourceNotFoundException {

        // CHeck if user CAN get this user
        if (!request.isUserInRole("ROLE_ADMIN") &&
                !Objects.equals(((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId(), customerId)) {
            return null;
        }
        // Load user by id
        CustomerDto entity = service.loadByKey(customerId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", customerId);
        }
        return entity;
    }

    /**
     * Delete user resource by id
     * @param id id of user
     * @throws ResourceNotFoundException resource not found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void deleteCustomer(@PathVariable Integer id) throws ResourceNotFoundException {
        CustomerDto entity = service.loadByKey(id);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", id);
        }
        service.remove(id);
    }

    /**
     * Delete contract of user
     * @param id customer id
     * @param contractId contract id
     */
    @DeleteMapping("/{id}/contracts/{contractId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public void deleteContract(@PathVariable Integer id, @PathVariable Integer contractId) {
        service.removeContract(id, contractId);
    }
}
