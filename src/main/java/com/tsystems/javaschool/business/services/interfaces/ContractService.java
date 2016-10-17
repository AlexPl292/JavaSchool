package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.exceptions.JSException;

import java.util.List;

/**
 * Created by alex on 24.08.16.
 * <p>
 * Interface for contract service
 */
public interface ContractService extends GenericService<ContractDto, Integer> {
    /**
     * Set block level to contract
     * 0 - unblocked
     * 1 - blocked by customer
     * 2 - blocked by eCare
     *
     * @param id         id of contract
     * @param blockLevel new block level
     */
    ContractDto setBlock(Integer id, Integer blockLevel);

    /**
     * Update contract by id with new tariff and options
     *
     * @param contractId id of contract
     * @param tariffId   id of new tariff
     * @param optionIds  ids of new options
     * @return updated contract
     */
    ContractDto updateContract(Integer contractId, Integer tariffId, List<Integer> optionIds) throws JSException;

    /**
     * Finding contract by number
     * @param number number to search
     * @return found contract DTO object
     */
    ContractDto findByNumber(String number);

    /**
     * Finding contracts by tariff
     * @param name tariff name to search
     * @return list of contracts with tariff
     */
    List<ContractDto> findByTariffName(String name);
}
