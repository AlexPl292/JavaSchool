package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityGraph;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 26.08.16.
 * Add new Option
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/option")
public class OptionRest {

    private final OptionService service;
    private static final Logger logger = Logger.getLogger(OptionRest.class);

    @Autowired
    public OptionRest(OptionService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addOption(@Valid @RequestBody OptionDto optionDto, BindingResult bindingResult) {
        StatusResponse response = new StatusResponse();

        if (!bindingResult.hasErrors()) {
            service.addNew(optionDto);
        } else {
            response.addBindingResult(bindingResult);
        }

        return response;
    }

    @GetMapping
    public List<OptionDto> loadAdd() {
        return service.loadAll();
    }

    @GetMapping("/{optionId}")
    public OptionDto loadOption(@PathVariable Integer optionId) {
        EntityGraph<Option> graph = service.getEntityGraph();

        graph.addAttributeNodes("required", "forbidden");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        return service.loadByKey(optionId, hints);
    }

    @RequestMapping("/forTariffs")
    public List<OptionDto> getForTariffs(@RequestParam(value = "possibleTariffsOfOption[][id]", required = false) List<Integer> forTariffs) {
        return service.loadOptionsByTariffs(forTariffs);
    }
}
