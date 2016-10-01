package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.exceptions.UniqueFieldDuplicateException;
import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

/**
 * Created by alex on 21.08.16.
 * Add new tariff
 * Returns json with either success:true, or success:false and object with errors
 */
@RestController
@RequestMapping("/rest/tariffs")
public class TariffRest {

    private final TariffService service;

    @Autowired
    public TariffRest(TariffService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addNewTariff(@Valid @RequestBody TariffDto tariff) {
        List<TariffDto> existings = service.findByName(tariff.getName());
        if (existings.size() > 0) {
            throw new UniqueFieldDuplicateException("Name", tariff.getName(), "/rest/tariffs/"+existings.get(0).getId());
        }
        TariffDto newTariff = service.addNew(tariff);
        return ResponseEntity.created(URI.create("/rest/tariffs/"+newTariff.getId())).body(newTariff);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TariffDto> loadAll() {
        return service.loadAll();
    }

    @GetMapping("/{tariffId}")
    @ResponseStatus(HttpStatus.OK)
    public TariffDto loadTariff(@PathVariable Integer tariffId) {
        TariffDto entity = service.loadByKey(tariffId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("tariff", tariffId);
        }
        return entity;
    }

    @GetMapping("/{tariffId}/options")
    @ResponseStatus(HttpStatus.OK)
    public Set<OptionDto> loadOptions(@PathVariable Integer tariffId)  {
        TariffDto entity = service.loadByKey(tariffId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("tariff", tariffId);
        }
        return entity.getPossibleOptions();
    }
}

