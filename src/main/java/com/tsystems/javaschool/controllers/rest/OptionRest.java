package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ROLE_ADMIN")
    public ResponseEntity addOption(@Valid @RequestBody OptionDto optionDto) throws JSException {
        OptionDto newOption = service.addNew(optionDto);
        return ResponseEntity.created(URI.create("/rest/options/" + newOption.getId())).body(newOption);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public List<OptionDto> loadAdd() {
        return service.loadAll();
    }

    @GetMapping("/{optionId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public OptionDto loadOption(@PathVariable Integer optionId) throws ResourceNotFoundException {
        OptionDto entity = service.loadByKey(optionId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("options", optionId);
        }
        return entity;
    }

    @DeleteMapping("/{optionId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void deleteOption(@PathVariable Integer optionId) throws ResourceNotFoundException {
        OptionDto entity = service.loadByKey(optionId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("option", optionId);
        }
        service.remove(optionId);
    }
}
