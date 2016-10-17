package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 21.08.16.
 *
 * Rest controller for tariffs
 */
@RestController
@RequestMapping("/rest/tariffs")
public class TariffRest {

    private final TariffService service;

    @Autowired
    public TariffRest(TariffService service) {
        this.service = service;
    }

    /**
     * Create new tariff
     * @param tariff new tariff data
     * @return response entity
     * @throws JSException validation fail
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewTariff(@Valid @RequestBody TariffDto tariff) throws JSException {
        TariffDto newTariff = service.addNew(tariff);

        // Create new response entity wity location header
        return ResponseEntity.created(URI.create("/rest/tariffs/" + newTariff.getId())).body(newTariff);
    }

    /**
     * Load oll tariffs
     * @return load of tariff data
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public List<TariffDto> loadAll() {
        return service.loadAll();
    }

    /**
     * Load one tariff
     * @param tariffId tariff id
     * @return tariff DTo
     * @throws ResourceNotFoundException resource not found
     */
    @GetMapping("/{tariffId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public TariffDto loadTariff(@PathVariable Integer tariffId) throws ResourceNotFoundException {
        TariffDto entity = service.loadByKey(tariffId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("tariff", tariffId);
        }
        return entity;
    }

    /**
     * Load all options of tariff
     * @param tariffId tariff id
     * @return list of opions DTO
     * @throws ResourceNotFoundException resource not found
     */
    @GetMapping("/{tariffId}/options")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public Set<OptionDto> loadOptions(@PathVariable Integer tariffId) throws ResourceNotFoundException {
        TariffDto entity = service.loadByKey(tariffId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("tariff", tariffId);
        }
        return entity.getPossibleOptions();
    }

    /**
     * Delete tariff by id
     * @param tariffId id of tariff
     * @throws ResourceNotFoundException resource not found
     */
    @DeleteMapping("/{tariffId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void deleteTariff(@PathVariable Integer tariffId) throws ResourceNotFoundException {
        TariffDto entity = service.loadByKey(tariffId);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("tariff", tariffId);
        }
        service.remove(tariffId);
    }
}

