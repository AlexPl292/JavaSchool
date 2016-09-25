package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
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

    @GetMapping("/{tariffId}")
    public TariffDto loadTariff(@PathVariable Integer tariffId)  {
        return service.loadByKey(tariffId);
    }

    @GetMapping("/{tariffId}/option")
    public Set<OptionDto> loadOptions(@PathVariable Integer tariffId)  {
        EntityGraph<Tariff> graph = service.getEntityGraph();

        graph.addAttributeNodes("possibleOptions");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return service.loadByKey(tariffId, hints).getPossibleOptions();
    }
}

