package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.Contract;

import java.util.List;

/**
 * Created by alex on 24.08.16.
 */
public interface ContractService extends GenericService<Contract, Integer>{
    Contract addNew(Contract contract, List<Integer> optionIds);
}
