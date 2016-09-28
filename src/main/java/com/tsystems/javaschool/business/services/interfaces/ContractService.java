package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.db.entities.Contract;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 24.08.16.
 *
 * Interface for contract service
 */
public interface ContractService extends GenericService<ContractDto, Integer>{
/*    *//**
     * Set block level to contract
     * 0 - unblocked
     * 1 - blocked by customer
     * 2 - blocked by staff
     * @param id id of contract
     * @param blockLevel new block level
     *//*
    void setBlock(Integer id, Integer blockLevel);

    *//**
     * Update contract by id.
     * @param contractId id of contract
     * @param tariffId id of new tariff
     * @param optionIds ids of new options
     * @return updated contract
     *//*
    ContractDto updateContract(Integer contractId, Integer tariffId, List<Integer> optionIds);*/
}
