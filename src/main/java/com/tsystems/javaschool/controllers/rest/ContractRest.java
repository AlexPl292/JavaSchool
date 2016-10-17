package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Created by alex on 27.09.16.
 */
@RestController
@RequestMapping("/rest/contracts")
public class ContractRest {

    private final transient ContractService service;

    @Autowired
    public ContractRest(ContractService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public List<ContractDto> loadAll(@RequestParam(value = "tariff", required = false) String tariff) {
        if (tariff != null)
            return service.findByTariffName(tariff);
        return service.loadAll();
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity addNew(@Valid @RequestBody ContractDto contract) throws JSException {
        contract = service.addNew(contract);
        ContractDto newContract = service.loadByKey(contract.getId());
        return ResponseEntity.created(URI.create("/rest/contracts/" + newContract.getId())).body(newContract);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable Integer id) throws ResourceNotFoundException {
        ContractDto entity = service.loadByKey(id);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
        service.remove(id);
    }

    @PostMapping("/{id}/block")
    @Secured("ROLE_USER")
    public void block(@PathVariable Integer id, HttpServletRequest request) throws ResourceNotFoundException {
        int blockLevel;
        if (request.isUserInRole("ROLE_ADMIN")) {
            blockLevel = 2;
        } else {
            blockLevel = 1;
        }
        ContractDto entity = service.setBlock(id, blockLevel);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
    }

    @PostMapping("/{id}/unblock")
    @Secured("ROLE_USER")
    public void unblock(@PathVariable Integer id, HttpServletRequest request) throws ResourceNotFoundException {
        // Check if user can unblock contract
        ContractDto contractDto = service.loadByKey(id);
        if (contractDto == null) {
            throw new ResourceNotFoundException("contract", id);
        }

        if (!request.isUserInRole("ROLE_ADMIN") && contractDto.getIsBlocked() == 2)
            return;

        ContractDto entity = service.setBlock(id, 0);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public ContractDto modify(@RequestParam("tariff") Integer tariffId,
                              @RequestParam(value = "usedOptions", required = false) List<Integer> options,
                              @PathVariable Integer id) throws JSException {
        ContractDto entity = service.updateContract(id, tariffId, options);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
        return service.loadByKey(id);
    }
}
