package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.controllers.rest.CustomerRest;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * Created by alex on 27.09.16.
 */
@RestController
@RequestMapping("/rest/contracts")
public class ContractRest {

    private final transient ContractService service;

    @Autowired
    public ContractRest(ContractService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContractDto> loadAll() {
        return service.loadAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ContractDto entity = service.loadByKey(id);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", id);
        }
        service.remove(id);
    }

    @PostMapping("/{id}/block")
    public void block(@PathVariable Integer id, HttpServletRequest request) {
        int blockLevel;
        if (request.isUserInRole("ROLE_ADMIN")) {
            blockLevel = 2;
        } else {
            blockLevel = 1;
        }
        ContractDto entity = service.setBlock(id, blockLevel);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", id);
        }
    }

    @PostMapping("/{id}/unblock")
    public void unblock(@PathVariable Integer id, HttpServletRequest request) {
        ContractDto entity = service.setBlock(id, 0);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("customer", id);
        }
    }
}
