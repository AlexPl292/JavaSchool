package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.controllers.rest.CustomerRest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by alex on 27.09.16.
 */
@RestController
@RequestMapping("/rest/contract")
public class ContractRest {

    private final transient ContractService service;
    private static final Logger logger = Logger.getLogger(CustomerRest.class);

    @Autowired
    public ContractRest(ContractService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContractDto> loadAll() {
        return service.loadAll();
    }
}
