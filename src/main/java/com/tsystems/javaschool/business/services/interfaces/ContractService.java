package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Contract;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 24.08.16.
 */
public interface ContractService extends GenericService<Contract, Integer>{
    /**
     * Add new contract with options, that are defined by ids
     * @param contract contract to add
     * @param optionIds list of ids of options
     * @return added contract
     */
    Contract addNew(Contract contract, List<Integer> optionIds);
    Contract loadByKey(Integer key, Map<String, Object> hints);
    void setBlock(Integer id, Integer blockLevel);
    Contract updateContract(Integer contract_id, Integer tariff_id, List<Integer> optionIds);
}
