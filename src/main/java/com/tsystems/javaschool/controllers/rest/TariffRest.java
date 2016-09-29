package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/rest/tariffs")
public class TariffRest {

    private final transient TariffService service;

    @Autowired
    public TariffRest(TariffService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addNewTariff(@Valid @RequestBody TariffDto tariff, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new StatusResponse(bindingResult);
        }

        service.addNew(tariff);
        return new StatusResponse();
    }

    @GetMapping
    public List<TariffDto> loadAll() {
        return service.loadAll();
    }

    @GetMapping("/{tariffId}")
    public TariffDto loadTariff(@PathVariable Integer tariffId)  {
        return service.loadByKey(tariffId);
    }

    @GetMapping("/{tariffId}/option")
    public Set<OptionDto> loadOptions(@PathVariable Integer tariffId)  {
        return service.loadByKey(tariffId).getPossibleOptions();
    }
}

