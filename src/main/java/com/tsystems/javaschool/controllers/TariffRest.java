package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by alex on 21.08.16.
 * Add new tariff
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/tariff")
public class TariffRest {

    private final transient TariffService service;
    private static final Logger logger = Logger.getLogger(TariffRest.class);

    @Autowired
    public TariffRest(TariffService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addNewTariff(@Valid @RequestBody TariffDto tariff, BindingResult bindingResult) {
        StatusResponse response = new StatusResponse();

        if (!bindingResult.hasErrors()) {
            service.addNew(tariff);
        } else {
            response.addBindingResult(bindingResult);
        }

        return response;
    }

    @GetMapping
    public List<TariffDto> loadAll() {
        return service.loadAll();
    }
}

