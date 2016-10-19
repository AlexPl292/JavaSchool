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
 *
 *  Rest controller for contract
 */
@RestController
@RequestMapping("/rest/contracts")
public class ContractRest {

    private final transient ContractService service;

    @Autowired
    public ContractRest(ContractService service) {
        this.service = service;
    }

    /**
     * Load all contracts or all contracts with tariff
     * @param tariff load all contracts with this tariff. Not required
     * @return tariffs DTO
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public List<ContractDto> loadAll(@RequestParam(value = "tariff", required = false) String tariff) {
        if (tariff != null)
            return service.findByTariffName(tariff);
        return service.loadAll();
    }

    /**
     * Creating new contract
     * @param contract new contract DTO object
     * @return response entity
     * @throws JSException exception by validating
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity addNew(@Valid @RequestBody ContractDto contract) throws JSException {
        contract = service.addNew(contract);
        ContractDto newContract = service.loadByKey(contract.getId());

        // Create new response entity with located header
        return ResponseEntity.created(URI.create("/rest/contracts/" + newContract.getId())).body(newContract);
    }

    /**
     * Delete contract
     * ATTENTION. Contracts must be deleted through customer rest
     * @param id id of contract
     * @throws ResourceNotFoundException resource not found
     */
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable Integer id) throws ResourceNotFoundException {
        ContractDto entity = service.loadByKey(id);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
        service.remove(id);
    }

    /**
     * Block customer by id
     * @param id  id of contract
     * @param request request to detect user
     * @throws ResourceNotFoundException contract not found
     */
    @PostMapping("/{id}/block")
    @Secured("ROLE_USER")
    public void block(@PathVariable Integer id, HttpServletRequest request) throws ResourceNotFoundException {
        int blockLevel;
        if (request.isUserInRole("ROLE_ADMIN")) { // Check with block level must be set
            blockLevel = 2; // Blocked by eCare
        } else {
            blockLevel = 1; // Blocked by user
        }
        ContractDto entity = service.setBlock(id, blockLevel);
        if (entity.getId() == null) {
            throw new ResourceNotFoundException("contract", id);
        }
    }

    /**
     * Unblock contract
     * @param id id of contract
     * @param request request to detect user
     * @throws ResourceNotFoundException contract not found
     */
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

    /**
     * Change contract tariff and options
     * @param tariffId new tariff id
     * @param options new options ids
     * @param id  id of contract to be changed
     * @return updated contract DTo
     * @throws JSException validation fail
     */
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
