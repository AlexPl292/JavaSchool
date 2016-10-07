package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity addOption(@Valid @RequestBody OptionDto optionDto) {
        DataBaseValidator.checkUnique(optionDto);

        OptionDto newOption = service.addNew(optionDto);
        return ResponseEntity.created(URI.create("/rest/options/" + newOption.getId())).body(newOption);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OptionDto> loadAdd() {
        return service.loadAll();
    }

    @GetMapping("/{optionId}")
    @ResponseStatus(HttpStatus.OK)
    public OptionDto loadOption(@PathVariable Integer optionId) {
        OptionDto entity = service.loadByKey(optionId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("options", optionId);
        }
        return entity;
    }
}
