package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.util.StatusResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by alex on 26.08.16.
 * Add new Option
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/options")
public class OptionRest {

    private final OptionService service;

    @Autowired
    public OptionRest(OptionService service) {
        this.service = service;
    }

    @PostMapping
    public StatusResponse addOption(@Valid @RequestBody OptionDto optionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new StatusResponse(bindingResult);
        }

        service.addNew(optionDto);
        return new StatusResponse();
    }

    @GetMapping
    public List<OptionDto> loadAdd() {
        return service.loadAll();
    }

    @GetMapping("/{optionId}")
    public OptionDto loadOption(@PathVariable Integer optionId) {
        return service.loadByKey(optionId);
    }
}
