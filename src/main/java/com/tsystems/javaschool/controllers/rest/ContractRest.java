package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.exceptions.ResourceNotFoundException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ContractDto> loadAll() {
        return service.loadAll();
    }

    @PostMapping
    public ResponseEntity addNew(@Valid @RequestBody ContractDto contract) {
        DataBaseValidator.checkUnique(contract);

        contract = service.addNew(contract);
        ContractDto newContract = service.loadByKey(contract.getId());
        return ResponseEntity.created(URI.create("/rest/contracts/" + newContract.getId())).body(newContract);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ContractDto entity = service.loadByKey(id);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
        service.remove(id);
    }

    @PostMapping("/{id}/block")
    public void block(@PathVariable Integer id, HttpServletRequest request) {
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
    public void unblock(@PathVariable Integer id, HttpServletRequest request) {
        ContractDto entity = service.setBlock(id, 0);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
    }

    @PutMapping("/{id}")
    public ContractDto modify(@RequestParam("tariff") Integer tariffId,
                              @RequestParam(value = "usedOptions", required = false) List<Integer> options,
                              @PathVariable Integer id) {
        ContractDto entity = service.updateContract(id, tariffId, options);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
        return service.loadByKey(id);
    }
}
